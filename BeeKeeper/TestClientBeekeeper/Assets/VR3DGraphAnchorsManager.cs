using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class VR3DGraphAnchorsManager : MonoBehaviour
{
    public List<Transform> anchors;

    public Transform playerAnchor;

    public float timeToCome = 1f;

    public float closePos = 1;
    public float dismissPos = 25;

    public VRGetInput myInputs;

    private Transform selectedAnchor = null;
    private Quaternion anchorStartRot;
    private Quaternion rotAtStart;
    private float timeAtStart = -1;
    private bool moving = false;

    void Update()
    {
        int keyPadPressed = -1;

        if (myInputs.getTriggerDown(VRInteractionProfileManager.GRAPH_PROFILE)) return;

        switch(myInputs.getPadPressPos(VRInteractionProfileManager.GRAPH_PROFILE))
        {
            case VRGetInput.PadPress.Top:
                keyPadPressed = 1;
                break;

            case VRGetInput.PadPress.Bot:
                keyPadPressed = 0;
                break;

            case VRGetInput.PadPress.Left:
                keyPadPressed = 2;
                break;

            case VRGetInput.PadPress.Right:
                keyPadPressed = 3;
                break;

            case VRGetInput.PadPress.None:
                break;
        }

        if (keyPadPressed == 0)
        {
            if (selectedAnchor == null) selectedAnchor = anchors[0];
            rotAtStart = selectedAnchor.root.rotation;
            anchorStartRot = selectedAnchor.rotation;
            timeAtStart = Time.realtimeSinceStartup;

            playerAnchor.localPosition = new Vector3(0, 0, dismissPos);

            moving = true;
        }
        else if (keyPadPressed != -1 && anchors.Count >= keyPadPressed)
        {
            selectedAnchor = anchors[keyPadPressed - 1];
            rotAtStart = selectedAnchor.root.rotation;
            anchorStartRot = selectedAnchor.rotation;
            timeAtStart = Time.realtimeSinceStartup;

            playerAnchor.localPosition = new Vector3(0, 0, closePos);

            moving = true;
        }


        if (moving)
        {
            float t = (Time.realtimeSinceStartup - timeAtStart) / timeToCome;

            Quaternion lookRot = playerAnchor.rotation * Quaternion.Inverse(anchorStartRot) * rotAtStart;

            selectedAnchor.root.rotation = Quaternion.Slerp(rotAtStart, lookRot, t);

            //selectedAnchor.root.rotation = Quaternion.LookRotation(anchorStartForward, playerAnchor.up) * Quaternion.FromToRotation(rootStartForward, anchorStartForward);

            selectedAnchor.root.position += (playerAnchor.position - selectedAnchor.position) * t;

            if (t >= 1)
            {
                moving = false;
            }
        }
    }
}
