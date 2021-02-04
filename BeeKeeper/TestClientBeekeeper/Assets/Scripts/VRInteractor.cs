using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR;

public class VRInteractor : MonoBehaviour
{
    public VRGetInput myVRInput;

    private void Update()
    {
        if (myVRInput.getPadPress())
        {
            Debug.Log("PadPress");
        }
        if (myVRInput.getTriggerPress())
        {
            Debug.Log("TriggerPress");
        }

        float trigger = myVRInput.getTriggerAxis();

        if(trigger!=0)Debug.Log("Trigger: " + trigger);
    }
}
