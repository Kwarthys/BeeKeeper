using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FrameManager : MonoBehaviour
{
    public GameObject framePrefab;
    public CommandSender sender;

    private List<FrameBehaviour> frames = new List<FrameBehaviour>();

    private volatile bool registeredInit = false;
    private volatile int[] registeredInitParams;

    public Transform hiveContainer;

    public PointCloudReferencer foragerCloud;

    public FramePositionner positionner;

    public void treatOrder(UpdateContent order)
    {
        CombPointCloud cpc = getCloudOfId(order.combID);
        cpc.setColors(getFullArray(order.colorsOfCellIDs, cpc.rows * cpc.columns));
    }

    private CombPointCloud getCloudOfId(int combId)
    {
        CombPointCloud cpc = null;

        for(int i = 0; i < frames.Count && cpc==null; ++i)
        {
            cpc = frames[i].getCPC(combId);
        }

        if(cpc == null)
        {
            //Spawn it
            //Debug.LogError("Shouldn't have to create a frame"); this can happen at start, but is quickly overriden by the correct stuff
            int fid = combId / 2;
            FrameBehaviour f = spawnAFrame(fid);

            cpc = f.getCPC(combId);

            frames.Add(f);
        }
        return cpc;
    }


    public void updateBeePoints(UpdateOrder o)
    {
        List<UpdateOrder> sortedOrders = new List<UpdateOrder>();
        UpdateOrder foragersUpdate = new UpdateOrder();

        for (int i = 0; i < o.targetsIDs.Count; ++i)
        {
            Vector3 target = o.newTargets[i];

            //if(UnityEngine.Random.value > 0.99f)Debug.Log("target: " + target + " -> " + getPosOnFrame(target));

            if(target.z == -1)
            {
                //forager
                foragersUpdate.add(target, o.targetsIDs[i]);
            }
            else
            {
                int frameTargetIDInList = (int)(target.z / 2);

                while (sortedOrders.Count <= frameTargetIDInList)
                {
                    sortedOrders.Add(new UpdateOrder());
                }

                if(target.x < 0)
                {
                    //sending two request for bees changing frames to prevent them to freeze for a timestep. Basically telerpoting them to the first target en route to the second
                    sortedOrders[frameTargetIDInList].add(getPosOnFrame(target), o.targetsIDs[i]);
                    target.x = -target.x;
                }


                sortedOrders[frameTargetIDInList].add(getPosOnFrame(target), o.targetsIDs[i]);
                

            }            
        }

        for(int i = 0; i < sortedOrders.Count; ++i)
        {
            //if (Random.value > 0.99f) Debug.Log("Order " + i + " populated with " + sortedOrders[i].targetsIDs.Count);
            frames[i].pointCloud.updatePoints(sortedOrders[i]);
        }

        //Debug.Log("FUpdate: " + foragersUpdate.targetsIDs.Count);
        foragerCloud.updatePoints(foragersUpdate);
    }

    private bool tryGetCloudAtPos(Vector3 pos, out PointCloudReferencer cloud)
    {
        if(pos.z == -1)
        {
            cloud = foragerCloud;
            return true;
        }

        int frameID = (int)(pos.z / 2);

        if (frames.Count > frameID)
        {
            cloud = frames[frameID].pointCloud;
            return true;
        }

        cloud = null;
        return false;
    }

    public void freeFrameIDForPos(Vector3 pos, int pointID)
    {
        if(tryGetCloudAtPos(pos, out PointCloudReferencer cloud))
        {
            cloud.freeIndex(pointID);
        }
    }

    public int getPointIDForPos(Vector3 pos)
    {
        if(tryGetCloudAtPos(pos, out PointCloudReferencer cloud))
        {
            return cloud.idManager.getNextFreeIndex();
        }

        return -1;
    }

    public void registerNewInit(int[] initParams)
    {
        registeredInit = true;
        registeredInitParams = initParams;
    }

    private void Update()
    {
        if(registeredInit)
        {
            registeredInit = false;
            initialiseWith(registeredInitParams);
        }
    }

    private void initialiseWith(int[] combIDs)
    {
        foreach(FrameBehaviour f in frames)
        {
            Destroy(f.gameObject);
        }

        frames.Clear();

        for(int i = 0; i < combIDs.Length; i+=2)
        {
            FrameBehaviour f = spawnAFrame(combIDs[i] / 2);

            if(combIDs[i] > combIDs[i+1])
            {
                f.transform.Rotate(Vector3.up, 180, Space.World);
            }

            //Debug.Log("Spanwed frame" + combIDs[i] / 2 + " at pos " + (frames.Count));

            frames.Add(f);
        }
    }

    
    private FrameBehaviour spawnAFrame(int id)
    {
        FrameBehaviour f = Instantiate(framePrefab, Vector3.zero, Quaternion.identity, hiveContainer).GetComponent<FrameBehaviour>();
        f.id = id;
        Vector3 framePos = positionner.registerAndPlaceNewFrame(f);
        f.transform.position = framePos;

        return f;
    }
    

    public Vector3 getPosOnFrame(Vector3 relativePos)
    {
        Transform combStarter = getStartTransformForComb((int)relativePos.z);

        bool changedOfFrame = false;
        if(relativePos.x < 0)
        {
            //changed detected
            changedOfFrame = true;
            relativePos.x = -relativePos.x;
        }

        if(combStarter == null)
        {
            Debug.Log("Couldn't find comb of id " + relativePos.z);
            return relativePos;
        }

        Vector3 absolute = combStarter.localPosition;
        absolute.x -= relativePos.x + (relativePos.y % 2 == 0 ? 0 : 0.5f); //hexagonal grid, offseting one row each two
        absolute.y -= relativePos.y;

        if(changedOfFrame)
        {
            absolute.z += (relativePos.z%2==0 ? 1 : -1) * 0.1f / 0.02f;
        }

        //absolute += -combStarter.right * (relativePos.x + (relativePos.y % 2 == 0 ? 0 : 0.5f)) * 0.02f;
        //absolute += -combStarter.up * relativePos.y * 0.02f;

        return absolute * 0.02f;
    }

    private Transform getStartTransformForComb(int combID)
    {
        Transform starter = null;

        for (int i = 0; i < frames.Count && starter == null; ++i)
        {
            starter = frames[i].getStarterOfId(combID);
        }

        return starter;
    }

    private Color[] getFullArray(Dictionary<int, Color> colorsOfCellIDs, int length)
    {
        Color[] colorsArray = new Color[length];

        for (int i = 0; i < length; ++i)
        {
            if (colorsOfCellIDs.ContainsKey(i))
            {
                colorsArray[i] = colorsOfCellIDs[i];
            }
            else
            {
                colorsArray[i] = Color.gray;
            }
        }
        return colorsArray;
    }

    private FrameBehaviour getFrameOfIndex(int index)
    {
        foreach(FrameBehaviour f in frames)
        {
            if(f.id == index)
            {
                return f;
            }
        }

        return null;
    }

    public void setFrameSelected(int frameIndex, bool selected)
    {
        frames[frameIndex].setVisualSelectedStatus(selected);
    }

    public void toggleFrameState(int frameIndex)
    {
        if(frameIndex >= frames.Count)
        {
            return;
        }

        FrameBehaviour frame = frames[frameIndex];

        if (frame.isUp)
        {
            //Put it down
            frame.transform.position = frame.initPos;
            frame.transform.rotation = frame.initRot;
            sender.putFrame(frameIndex, frameIndex, false);
        }
        else
        {
            //lift it up
            frame.transform.Translate(frame.transform.up * 50f);
            sender.liftFrame(frameIndex);
        }
        frames[frameIndex].isUp = !frame.isUp;
    }
}
