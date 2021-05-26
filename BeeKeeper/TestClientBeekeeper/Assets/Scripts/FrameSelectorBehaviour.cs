using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Valve.VR.InteractionSystem;

public class FrameSelectorBehaviour : MonoBehaviour
{
    public VRGetInput myInput;

    private TextureBasedFrameBehaviour pickedUp = null;

    public FrameManager frameManager;

    public float distanceGrab = .5f;

    public FramePositionner positionner;

    public CommandSender sender;

    public VelocityEstimator velocityEstimator;
    public float maxVelocity = 0;

    public float trackGlitchLowerLimit = 1500;
    public float realHitLowerLimit = 100;

    public bool detectFrameHit = true;

    private bool checkInput()
    {
        return myInput.getTriggerPress(VRInteractionProfileManager.FRAME_PROFILE);
        //return myInput.getGripButttonDown(VRInteractionProfileManager.FRAME_PROFILE);
        //return myInput.getPadPress(VRInteractionProfileManager.FRAME_PROFILE);
    }

    private bool checkIfHit()
    {
        float vel = velocityEstimator.GetAccelerationEstimate().magnitude;

        if (vel > trackGlitchLowerLimit)
        {
            vel = 0;
        }

        //Debug.Log(maxVelocity + " " + vel);
        maxVelocity = Mathf.Max(maxVelocity, vel);

        if (vel > realHitLowerLimit)
        {
            Debug.Log("hit " + vel);
            return true;
        }
        return false;
    }

    private void Update()
    {

        //velocityEstimator.FinishEstimatingVelocity();

        if(checkInput())
        {
            Debug.Log("Triggered");
            //Select or Deselect
            if(pickedUp == null)
            {
                if(tryGetNearestFrame(out TextureBasedFrameBehaviour f))
                {
                    pickedUp = f;

                    f.transform.SetParent(transform);
                    f.transform.localPosition = Vector3.zero;
                    f.transform.localRotation = Quaternion.identity;

                    positionner.notifyFrameLift(f);

                    velocityEstimator.BeginEstimatingVelocity();
                }
            }
            else
            {
                pickedUp.transform.SetParent(null);
                positionner.notifyFrameDropped(pickedUp);
                pickedUp = null;
                velocityEstimator.FinishEstimatingVelocity();
            }
        }

        if(pickedUp != null)
        {
            positionner.notifyFrameHandled(pickedUp);

            if(checkIfHit() && detectFrameHit)
            {
                //Hit frame
                sender?.hitFrame(pickedUp.frameID);
            }
        }

        //if(myInput.getTriggerPress(VRInteractionProfileManager.FRAME_PROFILE) && pickedUp != null)
        //{
        //}
    }

    private bool tryGetNearestFrame(out TextureBasedFrameBehaviour nearest)
    {
        nearest = null;
        float closestDistance = distanceGrab;

        foreach(TextureBasedFrameBehaviour f in frameManager.getFrames())
        {
            float distance = (f.transform.position - transform.position).magnitude;

            if (distance < closestDistance)
            {
                closestDistance = distance;
                nearest = f;
            }
        }

        return nearest != null;
    }
}
