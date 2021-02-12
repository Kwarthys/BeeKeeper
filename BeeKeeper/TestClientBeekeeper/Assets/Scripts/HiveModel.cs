using System;
using System.Collections;
using System.Collections.Generic;
using Unity.Collections;
using Unity.Jobs;
using UnityEngine;

public class HiveModel : MonoBehaviour
{
    //public PointCloudReferencer pointCloud;

    public FrameManager frameManager;

    private IDManager idManager = new IDManager();

    public CommandSender commandSender;

    public Dictionary<int, BeeAgent> theAgents = new Dictionary<int, BeeAgent>();

    //public PointCloudReferencer pointCloud;

    private MyLockedList<UpdateOrder> agentOrders = new MyLockedList<UpdateOrder>();
    private MyLockedList<UpdateContentOrder> incContentOrders = new MyLockedList<UpdateContentOrder>();
    private MyLockedList<UpdateContactOrder> contactOrders = new MyLockedList<UpdateContactOrder>();
    private MyLockedList<UpdateStatus> statusOrders = new MyLockedList<UpdateStatus>();
    private MyLockedList<List<int>> deadOrder = new MyLockedList<List<int>>();

    public TaskGrapher taskGrapher;

    private int initAskedForUpdate = 0;

    public float deathUpdateTime = 10;
    private float lastDeathUpdate = 0;

    public int lastRegisteredTimeStep = -1;

    public TimeWarpFeedbackManager warpFeedback;

    void Update()
    {
        float updateStartTime = Time.realtimeSinceStartup;

        updateAgents();
        updateContent();
        updateContacts();
        updateStatus();
        updateDeaths();

        if (initAskedForUpdate > 0) initAskedForUpdate--;

        if(Time.realtimeSinceStartup - lastDeathUpdate > deathUpdateTime)
        {
            lastDeathUpdate = Time.realtimeSinceStartup;
            commandSender.askForTheDead();
        }
    }

    public void updateAgents()
    {
        while (agentOrders.tryReadFifo(out UpdateOrder updateOrder) && initAskedForUpdate == 0)
        {
            int newAgents = 0;

            float loopUpdate = Time.realtimeSinceStartup;

            List<Vector3> targets = new List<Vector3>();
            List<int> ids = new List<int>();
            List<Color> colors = new List<Color>();

            for (int i = 0; i < updateOrder.targetsIDs.Count; ++i)
            {
                BeeAgent a;
                if (!theAgents.ContainsKey(updateOrder.targetsIDs[i]))
                {
                    a = new BeeAgent();
                    a.id = updateOrder.targetsIDs[i];
                    a.pos = updateOrder.newTargets[i];

                    //a.pointID = idManager.getNextFreeIndex();
                    a.pointID = frameManager.getPointIDForPos(a.pos);

                    theAgents.Add(updateOrder.targetsIDs[i], a);

                    newAgents++;
                }
                else
                {
                    a = theAgents[updateOrder.targetsIDs[i]];

                    bool changed = false;

                    if (a.pointID == -2)//Uncomplete agent code
                    {
                        a.pos = updateOrder.newTargets[i];
                        a.pointID = frameManager.getPointIDForPos(a.pos);
                        changed = true;
                    }
                    else
                    {
                        if (a.pos.z != updateOrder.newTargets[i].z)
                        {
                            //Change of Frame
                            frameManager.freeFrameIDForPos(a.pos, a.pointID);
                            a.pointID = frameManager.getPointIDForPos(updateOrder.newTargets[i]);

                            //do something to animate that change, not mandatory tho
                            changed = true;
                        }
                    }

                    a.pos = updateOrder.newTargets[i];
                    if(changed)
                    {
                        a.pos.x = -a.pos.x; //Encoding the change to be detected by the manager
                    }
                }

                if (a.pos.z == -1)
                {
                    targets.Add(a.pos); //Forager
                }
                else
                {
                    //targets.Add(frameManager.getPosOnFrame(a.pos));
                    targets.Add(a.pos);
                }
                ids.Add(a.pointID);
                colors.Add(a.JH > 0.5f ? Color.yellow : Color.red);
            }
            //Debug.Log("AgentLoop" + updateOrder.targetsIDs.Count + "(" + newAgents + ") update took " + (Time.realtimeSinceStartup - loopUpdate) * 1000 + "ms.");
            //pointCloud.updatePoints(new UpdateOrder(targets, ids));
            frameManager.updateBeePoints(new UpdateOrder(targets, ids, colors));

            //if (newAgents != 0) Debug.Log("created " + newAgents + " newAgents / total: " + theAgents.Count);
        }
        //if (initCount != 0) Debug.Log("Agent update took " + (Time.realtimeSinceStartup - updateStartTime) * 1000 + "ms. From " + initCount + " to " + incOrders.Count);

    }

    public void updateContent()
    {
        int initCount = incContentOrders.Count;

        int jobCount = 0;

        while (incContentOrders.tryReadFifo(out UpdateContentOrder order) && initAskedForUpdate == 0)
        {
            float loopStart = Time.realtimeSinceStartup;
            jobCount++;

            Dictionary<int, Color> colorsOfCellsIds = new Dictionary<int, Color>();

            for (int i = 0; i < order.cellIDs.Count; ++i)
            {
                Color c = getColorFor(order.cellCodes[i], order.cellQuantities[i]);
                colorsOfCellsIds.Add(order.cellIDs[i], c);
            }

            UpdateContent up = new UpdateContent(order.combID, colorsOfCellsIds);
            frameManager.treatOrder(up);
        }

        //if (initCount != 0) Debug.Log("Content update took " + (Time.realtimeSinceStartup - updateStartTime) * 1000 + "ms. From " + initCount + " to " + incContentOrders.Count);

    }

    public void updateStatus()
    {
        while (statusOrders.tryReadFifo(out UpdateStatus order) && initAskedForUpdate == 0)
        {
            for (int i = 0; i < order.ids.Count; ++i)
            {
                if(theAgents.ContainsKey(order.ids[i]))
                {
                    theAgents[order.ids[i]].age = order.ages[i];
                    theAgents[order.ids[i]].JH = order.jhAmounts[i];

                    if (order.taskNames[i] == "Queen Task")
                    {
                        theAgents[order.ids[i]].isQueen = true;
                    }
                }
            }
        }
    }

    public void updateContacts()
    {
        while(contactOrders.tryReadFifo(out UpdateContactOrder order) && initAskedForUpdate == 0)
        {
            for(int i = 0; i < order.ids.Count; ++i)
            {
                if(theAgents.ContainsKey(order.ids[i]))
                {
                    theAgents[order.ids[i]].amountExchanged = order.amounts[i];
                } // Ignoring NO MORE the agents that have still not been recieved via position
                else
                {
                    BeeAgent a = new BeeAgent();
                    a.id = order.ids[i];
                    a.pos = Vector3.zero;

                    //a.pointID = idManager.getNextFreeIndex();
                    //a.pointID = frameManager.getPointIDForPos(a.pos);
                    a.pointID = -2;
                    //Code to notify this is an uncomplete agent

                    theAgents.Add(a.id, a);
                }
            }
        }
    }

    public void updateDeaths()
    {
        while(deadOrder.tryReadFifo(out List<int> deadIds) && initAskedForUpdate == 0)
        {
            foreach(int id in deadIds)
            {
                BeeAgent agent = theAgents[id];
                //Delete the BeeAgents and free their ids in the different clouds
                frameManager.freeFrameIDForPos(agent.pos, agent.pointID);

                theAgents.Remove(id);
            }
        }
    }

    public void registerContactUpdate(UpdateContactOrder order)
    {
        contactOrders.waitAndPost(order);
    }

    public void registerStatusUpdate(UpdateStatus update)
    {
        taskGrapher.postData(update);
        statusOrders.waitAndPost(update);
        //Debug.Log("lockedlist state: " + statusOrders.tryPost(update));
    }

    public void registerContentUpdate(UpdateContentOrder order)
    {
        incContentOrders.waitAndPost(order);
    }
    
    public void registerOrder(UpdateOrder updateOrder)
    {
        agentOrders.waitAndPost(updateOrder);
    }

    public void registerDeathList(List<int> deadIds)
    {
        deadOrder.waitAndPost(deadIds);
    }


    /*
    private BeeAgent getAgentOfId(int id)
    {
        foreach(BeeAgent a in agents)
        {
            if(a.id == id)
            {
                return a;
            }
        }
        return null;
    }
    */
    private Color getColorFor(int contentCode, int quantity)
    {
        Color c = Color.black;

        if (contentCode == 0) c = Color.green;
        else if (contentCode == 1) c = Color.white;

        c.a = quantity / 255.0f;
        //Debug.Log(contentCode + " * " + quantity + " " + c);
        return c;
    }

    public void askTimeAcceleration(int seconds)
    {
        commandSender.sendAskFFW(seconds);
        warpFeedback.warpstarted(lastRegisteredTimeStep, lastRegisteredTimeStep + seconds * 1);
    }

    public void restartSimulation()
    {
        //Debug.Log("Restarting");
        //Debug.Log("theAgents.Count BEFORE " + theAgents.Count);
        theAgents.Clear();
        //Debug.Log("theAgents.Count " + theAgents.Count);
        statusOrders.Clear();
        contactOrders.Clear();
        agentOrders.Clear();
        incContentOrders.Clear();

        idManager.reinit();

        taskGrapher.reinit();

        commandSender.sendString("RESTART");

        initAskedForUpdate = 10;

        lastRegisteredTimeStep = -1;
    }

    public void askRebase(bool keepFrameInside)
    {
        List<int> framesToKeepIds = new List<int>();

        Dictionary<int, bool> states = frameManager.positionner.getFrameStates();

        string debugString = "";

        foreach(int fID in states.Keys)
        {
            Debug.Log("evaluating " + fID);

            bool state = states[fID];

            if(state == FramePositionner.FRAMESTATE_IN && keepFrameInside || state == FramePositionner.FRAMESTATE_OUT && !keepFrameInside)
            {
                framesToKeepIds.Add(fID);
                debugString += " " + fID;
            }
        }

        Debug.Log("Asking rebase for " + debugString);

        commandSender.sendAskRebase(framesToKeepIds.ToArray(), keepFrameInside);
    }

    public void registerNewTimeStep(int receivedTS)
    {
        if(receivedTS > lastRegisteredTimeStep && initAskedForUpdate == 0)
        {
            lastRegisteredTimeStep = receivedTS;
        }
    }
}

public class BeeAgent
{
    public int id;
    public int pointID;
    public int age;
    public float JH;
    public float amountExchanged;
    public Vector3 pos;

    public bool isQueen = false;
}
