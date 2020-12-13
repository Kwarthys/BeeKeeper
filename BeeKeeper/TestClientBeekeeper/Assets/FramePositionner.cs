using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FramePositionner : MonoBehaviour
{
    public GameObject framePlaceHolderPrefab;
    private Transform instanciatedPlaceHolder;

    public float frameIntervals = -.1f;

    public int maxNumberOfFrames = 10;

    public float scoutDistance = 3;

    private int instanciatedFrame = 0;

    private int[] framesIndex;

    public CommandSender sender;

    private void Start()
    {
        framesIndex = new int[maxNumberOfFrames];
        instanciatedPlaceHolder = Instantiate(framePlaceHolderPrefab, new Vector3(0, -5, 0), Quaternion.identity).transform;
    }

    private Dictionary<FrameBehaviour, int> framePosIndexes = new Dictionary<FrameBehaviour, int>();

    public Vector3 registerAndPlaceNewFrame(FrameBehaviour f)
    {
        framePosIndexes.Add(f, instanciatedFrame);
        framesIndex[instanciatedFrame] = f.id;

        return getPosForIndex(instanciatedFrame++);
    }

    private Vector3 getPosForIndex(int index)
    {
        Vector3 pos = transform.position;
        pos.z += frameIntervals * index;
        return pos;
    }

    public void notifyFrameLift(FrameBehaviour lifted)
    {
        int prevPos = framePosIndexes[lifted];
        framesIndex[prevPos] = -1; //lifting it up

        Debug.Log("lifted F" + lifted.id + " from " + prevPos);
        sender.liftFrame(lifted.id);

        framePosIndexes[lifted] = -1;
    }

    public void notifyFrameHandled(FrameBehaviour lifted)
    {
        if(framePosIndexes[lifted] != -1)
        {
            notifyFrameLift(lifted);
        }

        if(tryGetClosestEmptyPos(lifted.transform.position, out int index))
        {
            //highlight pos
            //Debug.DrawRay(new Vector3(0, 0, index * frameIntervals), Vector3.up * 2, Color.red);
            //Debug.Log("Found " + index);
            //move a ghost or smthg
            instanciatedPlaceHolder.position = getPosForIndex(index);
        }
        else
        {
            resetPlaceHolderPos();
        }
    }

    private bool tryGetClosestEmptyPos(Vector3 pos, out int closestIndex)
    {
        float sqrdDistance = -1;
        closestIndex = -1;

        for(int i = 0; i < framesIndex.Length; ++i)
        {
            if(framesIndex[i] == -1) //emptyPos
            {
                float d = (new Vector3(0, 0, i * frameIntervals) - pos).sqrMagnitude;
                if (d < sqrdDistance || sqrdDistance == -1)
                {
                    sqrdDistance = d;
                    closestIndex = i;
                }
            }
        }

        return sqrdDistance != -1 && sqrdDistance < scoutDistance*scoutDistance; //squared scoutdistance
    }

    public void notifyFrameDropped(FrameBehaviour f)
    {
        if(tryGetClosestEmptyPos(f.transform.position, out int index))
        {
            framesIndex[index] = f.id;
            framePosIndexes[f] = index;

            float angle = Vector3.Angle(f.transform.forward, transform.forward);

            f.transform.rotation = Quaternion.identity;
            f.transform.position = getPosForIndex(index);

            if (angle > 90)
            {
                f.transform.Rotate(transform.up, 180);
            }

            Debug.Log("Dropped F" + f.id + " to " + index);
            sender.putFrame(f.id, index, angle > 90);

            resetPlaceHolderPos();
        }
    }

    private void resetPlaceHolderPos()
    {
        instanciatedPlaceHolder.position = new Vector3(0, -5, 0);
    }
}
