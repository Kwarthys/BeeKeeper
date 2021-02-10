using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class VRFFWTimeController : MonoBehaviour
{
    public VRGetInput myInputs;

    public HiveModel model;

    public int TOP_SECONDSKIP = 60 * 60 * 5; //5 Hours
    public int BOT_SECONDSKIP = 60 * 60 * 24 * 7; // 7 Days
    public int LEFT_SECONDSKIP = 0; // 0
    public int RIGHT_SECONDSKIP = 60 * 60 * 24; // 1 Day

    void Update()
    {
        VRGetInput.PadPress press = myInputs.getPadPressPos(VRInteractionProfileManager.TIME_PROFILE);

        switch(press)
        {
            case VRGetInput.PadPress.Top:
                Debug.Log("Skipping " + TOP_SECONDSKIP + "s.");
                model.askTimeAcceleration(TOP_SECONDSKIP);
                break;
            case VRGetInput.PadPress.Bot:
                Debug.Log("Skipping " + BOT_SECONDSKIP + "s.");
                model.askTimeAcceleration(BOT_SECONDSKIP);
                break;
            case VRGetInput.PadPress.Right:
                Debug.Log("Skipping " + RIGHT_SECONDSKIP + "s.");
                model.askTimeAcceleration(RIGHT_SECONDSKIP);
                break;
            case VRGetInput.PadPress.Left:
                Debug.Log("Skipping " + LEFT_SECONDSKIP + "s.");
                model.askTimeAcceleration(LEFT_SECONDSKIP);
                break;
            case VRGetInput.PadPress.None:
                //do Nothing
                break;
        }
    }
}
