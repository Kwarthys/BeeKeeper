using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FrameManager : MonoBehaviour
{
    public GameObject framePrefab;
    public CommandSender sender;

    private List<FrameBehaviour> frames = new List<FrameBehaviour>();

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
            int fid = combId / 2;
            FrameBehaviour f = Instantiate(framePrefab, new Vector3(0, 0, -fid * 0.1f), Quaternion.identity).GetComponent<FrameBehaviour>();
            f.id = fid;

            cpc = f.getCPC(combId);

            frames.Add(f);
        }
        return cpc;
    }

    public Vector3 getAbsolutePosOf(Vector3 relativePos)
    {
        Transform combStarter = getStartTransformForComb((int)relativePos.z);

        if(combStarter == null)
        {
            //Debug.Log("Couldn't find comb of id " + relativePos.z);
            return relativePos;
        }

        Vector3 absolute = new Vector3();
        absolute = combStarter.position;
        absolute += -combStarter.right * (relativePos.x + (relativePos.y%2==0 ? 0 : 0.5f)) * 0.02f;
        absolute += -combStarter.up * relativePos.y * 0.02f;

        return absolute;
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
