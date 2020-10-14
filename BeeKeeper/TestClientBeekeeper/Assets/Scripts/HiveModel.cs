using System;
using System.Collections;
using System.Collections.Generic;
using Unity.Collections;
using Unity.Jobs;
using UnityEngine;

public class HiveModel : MonoBehaviour
{
    public PointCloudReferencer pointCloud;

    public FrameManager frameManager;

    public IDManager idManager;

    public CommandSender commandSender;

    //private List<BeeAgent> agents = new List<BeeAgent>();

    public Dictionary<int, BeeAgent> theAgents = new Dictionary<int, BeeAgent>();

    private MyLockedList<UpdateOrder> agentOrders = new MyLockedList<UpdateOrder>();
    private MyLockedList<UpdateContentOrder> incContentOrders = new MyLockedList<UpdateContentOrder>();
    private MyLockedList<UpdateContactOrder> contactOrders = new MyLockedList<UpdateContactOrder>();
    private MyLockedList<UpdateStatus> statusOrders = new MyLockedList<UpdateStatus>();

    public TaskGrapher taskGrapher;

    private int initAskedForUpdate = 0;

    void Update()
    {
        float updateStartTime = Time.realtimeSinceStartup;

        updateAgents();
        updateContent();
        updateContacts();
        updateStatus();

        if (initAskedForUpdate > 0) initAskedForUpdate--;
    }

    public void updateAgents()
    {
        while (agentOrders.tryReadFifo(out UpdateOrder updateOrder) && initAskedForUpdate == 0)
        {
            int newAgents = 0;

            float loopUpdate = Time.realtimeSinceStartup;

            List<Vector3> targets = new List<Vector3>();
            List<int> ids = new List<int>();

            for (int i = 0; i < updateOrder.targetsIDs.Count; ++i)
            {
                BeeAgent a;
                if (!theAgents.ContainsKey(updateOrder.targetsIDs[i]))
                {
                    a = new BeeAgent();
                    a.id = updateOrder.targetsIDs[i];
                    a.pos = updateOrder.newTargets[i];
                    a.pointID = idManager.getNextFreeIndex();
                    theAgents.Add(updateOrder.targetsIDs[i], a);

                    newAgents++;
                }
                else
                {
                    a = theAgents[updateOrder.targetsIDs[i]];
                    a.pos = updateOrder.newTargets[i];
                }

                if (a.pos.z == -1)
                {
                    targets.Add(a.pos); //Forager
                }
                else
                {
                    targets.Add(frameManager.getAbsolutePosOf(a.pos));
                }
                ids.Add(a.pointID);
            }
            //Debug.Log("AgentLoop" + updateOrder.targetsIDs.Count + "(" + newAgents + ") update took " + (Time.realtimeSinceStartup - loopUpdate) * 1000 + "ms.");
            pointCloud.updatePoints(new UpdateOrder(targets, ids));

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
                } // Ignoring the agents that have still not been recieved via position
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

        initAskedForUpdate = 60;
    }
}

public class BeeAgent
{
    public int id;
    public int pointID;
    public int age;
    public float JH;
    public int amountExchanged;
    public Vector3 pos;
}
