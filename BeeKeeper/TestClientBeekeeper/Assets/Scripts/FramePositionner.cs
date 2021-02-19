using System;
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

    private int[] framesSlotsIndex;

    public CommandSender sender;

    public bool authorizeAllDrop = false;

    private Dictionary<TextureBasedFrameBehaviour, int> framePosIndexes = new Dictionary<TextureBasedFrameBehaviour, int>();

    private void Start()
    {
        framesSlotsIndex = new int[maxNumberOfFrames];
        if(authorizeAllDrop)
        {
            for(int i = 0; i < maxNumberOfFrames; ++i)
            {
                framesSlotsIndex[i] = -1;
            }
        }

        instanciatedPlaceHolder = Instantiate(framePlaceHolderPrefab, new Vector3(0, -5, 0), Quaternion.identity, transform).transform;

        instanciatedFrame = 0;
        framePosIndexes = new Dictionary<TextureBasedFrameBehaviour, int>();
    }

    public void reset()
    {
        Start();        
    }

    public Vector3 registerAndPlaceNewFrame(TextureBasedFrameBehaviour f)
    {
        if(instanciatedFrame > maxNumberOfFrames)
        {
            Debug.LogError("Max number of 8 frames reached");
            return Vector3.zero;
        }

        //Debug.Log("instanciatedFrame " + instanciatedFrame);

        framePosIndexes.Add(f, instanciatedFrame);
        framesSlotsIndex[instanciatedFrame] = f.frameID;

        return getPosForIndex(instanciatedFrame++);
    }

    private Vector3 getPosForIndex(int index)
    {
        //Vector3 pos = transform.position;
        Vector3 pos = Vector3.zero;
        pos.z += frameIntervals * index;
        return pos;
    }

    public void notifyFrameLift(TextureBasedFrameBehaviour lifted)
    {
        if(framePosIndexes.ContainsKey(lifted))
        {
            int prevPos = framePosIndexes[lifted];
            framesSlotsIndex[prevPos] = -1; //lifting it up

            Debug.Log("lifted F" + lifted.frameID + " from " + prevPos);
            sender?.liftFrame(lifted.frameID);

            framePosIndexes[lifted] = -1;
        }

        lifted.exterior.SetActive(false);
    }

    public void notifyFrameHandled(TextureBasedFrameBehaviour lifted)
    {
        if(framePosIndexes.ContainsKey(lifted))
        {
            if(framePosIndexes[lifted] != -1)
            {
                notifyFrameLift(lifted);
            }
        }

        if(tryGetClosestEmptyPos(lifted.transform.position, out int index))
        {
            //highlight pos
            //Debug.DrawRay(new Vector3(0, 0, index * frameIntervals), Vector3.up * 2, Color.red);
            //Debug.Log("Found " + index);
            //move a ghost or smthg
            instanciatedPlaceHolder.localPosition = getPosForIndex(index);
        }
        else
        {
            resetPlaceHolderPos();
        }
    }

    private bool tryGetClosestEmptyPos(Vector3 framePos, out int closestIndex)
    {
        Vector3 frameLocalPos = transform.InverseTransformPoint(framePos);

        float sqrdDistance = -1;
        closestIndex = -1;

        for(int i = 0; i < framesSlotsIndex.Length; ++i)
        {
            if(framesSlotsIndex[i] == -1) //emptyPos
            {
                float d = (new Vector3(0, 0, i * frameIntervals) - frameLocalPos).sqrMagnitude;
                if (d < sqrdDistance || sqrdDistance == -1)
                {
                    sqrdDistance = d;
                    closestIndex = i;
                }
            }
        }

        return sqrdDistance != -1 && sqrdDistance < scoutDistance*scoutDistance; //squared scoutdistance
    }

    public bool notifyFrameDropped(TextureBasedFrameBehaviour f, bool replaceFrame = true)
    {
        f.exterior.SetActive(true);

        if (tryGetClosestEmptyPos(f.transform.position, out int index))
        {
            framesSlotsIndex[index] = f.frameID;
            framePosIndexes[f] = index;

            float angle = Vector3.Angle(f.transform.forward, transform.forward);

            //Debug.Log("Angle " + angle);

            //Debug.DrawRay(f.transform.position, -f.transform.forward, Color.red, 1);
            //Debug.DrawRay(f.transform.position, transform.forward, Color.blue, 1);

            if (replaceFrame)
            {
                f.transform.SetParent(transform);

                f.transform.localRotation = Quaternion.identity;
                f.transform.localPosition = getPosForIndex(index);

                if (angle > 90)
                {
                    f.transform.Rotate(transform.up, 180);
                }
            }

            bool reversed = angle > 90;

            bool wasReversed = f.isReversed;

            f.isReversed = reversed;

            Debug.Log("Dropped F" + f.frameID + " to " + index);
            sender?.putFrame(f.frameID, index, reversed != wasReversed);

            resetPlaceHolderPos();

            return true;
        }

        return false;
    }

    private void resetPlaceHolderPos()
    {
        instanciatedPlaceHolder.position = new Vector3(0, -5, 0);
    }

    public static bool FRAMESTATE_OUT = false;
    public static bool FRAMESTATE_IN = true;

    public Dictionary<int, bool> getFrameStates()
    {
        Dictionary<int, bool> states = new Dictionary<int, bool>();

        foreach(TextureBasedFrameBehaviour f in framePosIndexes.Keys)
        {
            states[f.frameID] = framePosIndexes[f] != -1;
        }

        return states;
    }
}
