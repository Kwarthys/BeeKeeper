using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FrameManager : MonoBehaviour
{
    public GameObject framePrefab;
    public CommandSender sender;

    private List<TextureBasedFrameBehaviour> frames = new List<TextureBasedFrameBehaviour>();

    private volatile bool registeredInit = false;
    private volatile int[] registeredInitParams;

    public Transform hiveContainer;

    public PointCloudReferencer foragerCloud;

    public FramePositionner positionner;

    public List<TextureBasedFrameBehaviour> getFrames() { return frames; }

    public void treatOrder(UpdateContent order)
    {
        //CombPointCloud cpc = getCloudOfId(order.combID);
        //cpc.setColors(getFullArray(order.colorsOfCellIDs, cpc.rows * cpc.columns));

        int combID = order.combID;

        getFrameWithCombID(combID).setColors(order.colorsOfCellIDs, combID);
    }

    private TextureBasedFrameBehaviour getFrameWithCombID(int combID)
    {
        int frameID = combID / 2;

        TextureBasedFrameBehaviour f = null;

        for (int i = 0; i < frames.Count && f == null; ++i)
        {
            if(frames[i].frameID == frameID)
            {
                return frames[i];
            }
        }

        return spawnAFrame(frameID);
    }

  /*  
    private CombPointCloud getCloudOfId(int combId)
    {
        CombPointCloud cpc = null;

        for(int i = 0; i < frames.Count && cpc==null; ++i)
        {
            //cpc = frames[i].getCPC(combId);
        }

        if(cpc == null)
        {
            //Spawn it
            //Debug.LogError("Shouldn't have to create a frame"); this can happen at start, but is quickly overriden by the correct stuff
            int fid = combId / 2;
            TextureBasedFrameBehaviour f = spawnAFrame(fid);

            //cpc = f.getCPC(combId);

            //frames.Add(f);
        }
        return cpc;
    }
*/

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
                foragersUpdate.add(target, o.targetsIDs[i], o.colors[i]);
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


                sortedOrders[frameTargetIDInList].add(getPosOnFrame(target), o.targetsIDs[i], o.colors[i]);
                

            }            
        }

        for(int i = 0; i < sortedOrders.Count; ++i)
        {
            //if (Random.value > 0.99f) Debug.Log("Order " + i + " populated with " + sortedOrders[i].targetsIDs.Count);
            frames[i].beePointCloud.updatePoints(sortedOrders[i]);
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
            cloud = frames[frameID].beePointCloud;
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
        foreach(TextureBasedFrameBehaviour f in frames)
        {
            Destroy(f.gameObject);
        }

        frames.Clear();

        for(int i = 0; i < combIDs.Length; i+=2)
        {
            TextureBasedFrameBehaviour f = spawnAFrame(combIDs[i] / 2);

            if(combIDs[i] > combIDs[i+1])
            {
                f.transform.Rotate(Vector3.up, 180, Space.World);
            }

            //Debug.Log("Spanwed frame" + combIDs[i] / 2 + " at pos " + (frames.Count));
        }
    }

    
    private TextureBasedFrameBehaviour spawnAFrame(int id)
    {
        TextureBasedFrameBehaviour f = Instantiate(framePrefab, Vector3.zero, Quaternion.identity, hiveContainer).GetComponent<TextureBasedFrameBehaviour>();
        f.frameID = id;
        Vector3 framePos = positionner.registerAndPlaceNewFrame(f);
        f.transform.localPosition = framePos;

        frames.Add(f);

        return f;
    }

    /*
    private void Start()
    {
        Vector3 test = new Vector3(0, 0, 0);
        Debug.Log(test + " " + getPosOnFrame(test, true));
        test = new Vector3(78, 50, 0);
        Debug.Log(test + " " + getPosOnFrame(test, true));
    }
    */

    public Vector3 getPosOnFrame(Vector3 relativePos, bool debug = false)
    {
        float sizeCoef = 0.0055f;

        Transform combStarter = getStartTransformForComb((int)relativePos.z);

        if (debug) Debug.Log(combStarter.position);

        float combFace = relativePos.z;

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

        relativePos.x += (relativePos.y % 2 == 0 ? 0 : 0.5f); //hexagonal grid, offseting one row each two
        relativePos *= sizeCoef;

        Vector3 absolute = combStarter.localPosition;
        absolute.x += relativePos.x;
        absolute.y -= relativePos.y;

        if(changedOfFrame)
        {
            absolute.z += (combFace % 2 == 1 ? 1 : -1) * 0.02f;// * sizeCoef;
        }

        //absolute += -combStarter.right * (relativePos.x + (relativePos.y % 2 == 0 ? 0 : 0.5f)) * 0.02f;
        //absolute += -combStarter.up * relativePos.y * 0.02f;

        return absolute;
    }

    private Transform getStartTransformForComb(int combID)
    {
        return getFrameWithCombID(combID).getStarterOfFace(combID);
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

    private TextureBasedFrameBehaviour getFrameOfIndex(int index)
    {
        foreach(TextureBasedFrameBehaviour f in frames)
        {
            if(f.frameID == index)
            {
                return f;
            }
        }

        return null;
    }

    public void setFrameSelected(int frameIndex, bool selected)
    {
        //frames[frameIndex].setVisualSelectedStatus(selected);
    }
    
    public void toggleFrameState(int frameIndex)
    {
        if(frameIndex >= frames.Count)
        {
            return;
        }

        TextureBasedFrameBehaviour frame = frames[frameIndex];

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
