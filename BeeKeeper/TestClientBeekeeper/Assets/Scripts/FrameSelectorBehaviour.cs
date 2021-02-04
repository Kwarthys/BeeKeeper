using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FrameSelectorBehaviour : MonoBehaviour
{
    public VRGetInput myInput;

    private TextureBasedFrameBehaviour pickedUp = null;

    public FrameManager frameManager;

    public float distanceGrab = .5f;

    public FramePositionner positionner;

    public CommandSender sender;

    private void Update()
    {
        if(myInput.getPadPress(VRInteractionProfileManager.FRAME_PROFILE))
        {
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
                }
            }
            else
            {
                pickedUp.transform.SetParent(null);
                positionner.notifyFrameDropped(pickedUp);
                pickedUp = null;
            }
        }

        if(pickedUp != null)
        {
            positionner.notifyFrameHandled(pickedUp);
        }

        if(myInput.getTriggerPress(VRInteractionProfileManager.FRAME_PROFILE) && pickedUp != null)
        {
            //Hit frame
            sender?.hitFrame(pickedUp.frameID);
        }
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
