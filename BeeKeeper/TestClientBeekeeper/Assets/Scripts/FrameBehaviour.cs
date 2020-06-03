using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FrameBehaviour : MonoBehaviour
{
    public int id;

    public CombPointCloud faceA;
    public CombPointCloud faceB;

    public Transform faceAStart;
    public Transform faceBStart;

    public CombPointCloud getCPC(int combId)
    {
        int faceAID = id * 2;
        int faceBID = id * 2 + 1;

        if (combId == faceAID) return faceA;
        if (combId == faceBID) return faceB;
        else return null;
    }

    public Transform getStarterOfId(int combId)
    {
        int faceAID = id * 2;
        int faceBID = id * 2 + 1;

        if (combId == faceAID) return faceAStart;
        if (combId == faceBID) return faceBStart;
        else return null;
    }
}
