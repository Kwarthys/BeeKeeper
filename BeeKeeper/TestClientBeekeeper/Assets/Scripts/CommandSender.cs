using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CommandSender : MonoBehaviour
{
    public NetworkClient sender;

    public void liftFrame(int frameIndex)
    {
        string request = "FrUP " + frameIndex;
        sender.sendTCP(request);
    }

    public void putFrame(int frameIndex, int newPos, bool isReverse)
    {
        string request = "FrDOWN " + frameIndex + " " + newPos + " " + isReverse;
        sender.sendTCP(request);
    }

    public void hitFrame(int frameIndex)
    {
        string request = "FrHIT " + frameIndex;
        sender.sendTCP(request);
    }

    public void sendString(string s)
    {
        sender.sendTCP(s);
    }

    public void sendAskFFW(int seconds)
    {
        string request = "FFWD " + seconds.ToString();
        sender.sendTCP(request);
    }

    public void sendAskRebase(int[] frameIds, bool keepFrameInside)
    {
        string request = "REBASE " + keepFrameInside;
        foreach(int i in frameIds)
        {
            request += " " + i;
        }

        Debug.Log("sending " + request);
        sender.sendTCP(request);
    }

    public void askForTheDead()
    {
        sender.sendTCP("Deaths");
    }
}
