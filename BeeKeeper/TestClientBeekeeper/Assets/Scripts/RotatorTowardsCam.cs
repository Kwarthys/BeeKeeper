using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RotatorTowardsCam : MonoBehaviour
{
    public List<Transform> toRotate = new List<Transform>();

    public Camera cam;

    void FixedUpdate()
    {
        foreach(Transform t in toRotate)
        {
            t.rotation = Quaternion.LookRotation(- cam.transform.position + t.position);
        }
    }
}
