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
}
