using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FrameBehaviour : Interactible
{
    public int id;

    public CombPointCloud faceA;
    public CombPointCloud faceB;

    public Transform faceAStart;
    public Transform faceBStart;

    public bool isUp = false;

    private Renderer depthRenderer;
    private Color baseDepthColor;

    public Vector3 initPos;
    public Quaternion initRot;

    private void Start()
    {
        depthRenderer = transform.Find("Depth").GetComponent<Renderer>();
        baseDepthColor = depthRenderer.material.color;

        initPos = transform.position;
        initRot = transform.rotation;
    }

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

    public void setVisualSelectedStatus(bool isSelected)
    {
        if(isSelected)
        {
            depthRenderer.material.color = Color.red;
        }
        else
        {
            depthRenderer.material.color = baseDepthColor;
        }
    }

    public override void interact(PlayerRayCastExecute executor)
    {
        executor.grab(transform);
    }
}
