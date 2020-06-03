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

    private List<BeeAgent> agents = new List<BeeAgent>();

    private List<UpdateOrder> incOrders = new List<UpdateOrder>();
    private List<UpdateContentOrder> incContentOrders = new List<UpdateContentOrder>();

    private bool listLock = false;
    private bool listContentLock = false;

    void Update()
    {
        float updateStartTime = Time.realtimeSinceStartup;

        if (!listLock)
        {
            listLock = true;
            int initCount = incOrders.Count;
            while (incOrders.Count > 0)
            {

                UpdateOrder updateOrder = incOrders[0];
                incOrders.RemoveAt(0);

                int newAgents = 0;

                float loopUpdate = Time.realtimeSinceStartup;

                List<Vector3> targets = new List<Vector3>();
                List<int> ids = new List<int>();

                for (int i = 0; i < updateOrder.targetsIDs.Count; ++i)
                {
                    BeeAgent a = getAgentOfId(updateOrder.targetsIDs[i]);
                    if (a == null)
                    {
                        a = new BeeAgent();
                        a.id = updateOrder.targetsIDs[i];
                        a.pos = updateOrder.newTargets[i];
                        a.pointID = agents.Count;
                        agents.Add(a);

                        newAgents++;
                    }
                    else
                    {
                        a.pos = updateOrder.newTargets[i];
                    }

                    if(a.pos.z == -1)
                    {
                        targets.Add(a.pos); //Forager
                    }
                    else
                    {
                        targets.Add(frameManager.getAbsolutePosOf(a.pos));
                    }
                    ids.Add(a.pointID);
                }
                Debug.Log("AgentLoop" + updateOrder.targetsIDs.Count + "(" + newAgents + ") update took " + (Time.realtimeSinceStartup - loopUpdate) * 1000 + "ms.");
                pointCloud.updatePoints(new UpdateOrder(targets, ids));
            }
            listLock = false;
            if (initCount != 0) Debug.Log("Agent update took " + (Time.realtimeSinceStartup - updateStartTime) * 1000 + "ms. From " + initCount + " to " + incOrders.Count);
        }

        updateStartTime = Time.realtimeSinceStartup;

        if(!listContentLock)
        {
            listContentLock = true;
            int initCount = incContentOrders.Count;

            int jobCount = 0;

            while (incContentOrders.Count > 0 && jobCount < 10)//System.Environment.ProcessorCount-1)
            {
                float loopStart = Time.realtimeSinceStartup;
                jobCount++;
                UpdateContentOrder order = incContentOrders[0];
                incContentOrders.RemoveAt(0);

                Dictionary<int, Color> colorsOfCellsIds = new Dictionary<int, Color>();
                
                for(int i = 0; i < order.cellIDs.Count; ++i)
                {
                    Color c = getColorFor(order.cellCodes[i], order.cellQuantities[i]);
                    colorsOfCellsIds.Add(order.cellIDs[i], c);
                }                

                UpdateContent up = new UpdateContent(order.combID, colorsOfCellsIds);
                frameManager.treatOrder(up);                
            }

            listContentLock = false;

            if (initCount != 0) Debug.Log("Content update took " + (Time.realtimeSinceStartup - updateStartTime) * 1000 + "ms. From " + initCount + " to " + incContentOrders.Count);
        }
    }

    public void registerContentUpdate(UpdateContentOrder order)
    {
        while (listContentLock) { }

        listContentLock = true;
        incContentOrders.Add(order);
        listContentLock = false;
    }
    
    public void registerOrder(UpdateOrder updateOrder)
    {
        while(listLock){}

        listLock = true;
        incOrders.Add(updateOrder);
        listLock = false;
    }

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

    private Color getColorFor(int contentCode, int quantity)
    {
        Color c = Color.black;

        if (contentCode == 0) c = Color.green;
        else if (contentCode == 1) c = Color.white;

        c.a = quantity / 255.0f;
        //Debug.Log(contentCode + " * " + quantity + " " + c);
        return c;
    }
}

public class BeeAgent
{
    public int id;
    public int pointID;

    public Vector3 pos;
}
