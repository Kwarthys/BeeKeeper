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
        if(myInputs.getPadPress(VRInteractionProfileManager.TIME_PROFILE))
        {
            float padV = myInputs.getVerticalPad();
            float padH = myInputs.getHorizontalPad();
            //Debug.Log(myInputs.getHorizontalPad() + " " + myInputs.getVerticalPad());

            if(Mathf.Abs(padV) > Mathf.Abs(padH))
            {
                if(padV > 0)
                {
                    Debug.Log("Skipping " + BOT_SECONDSKIP + "s.");
                    model.askTimeAcceleration(BOT_SECONDSKIP);
                }
                else
                {
                    Debug.Log("Skipping " + TOP_SECONDSKIP + "s.");
                    model.askTimeAcceleration(TOP_SECONDSKIP);
                }
            }
            else
            {
                if (padH > 0)
                {
                    Debug.Log("Skipping " + RIGHT_SECONDSKIP + "s.");
                    model.askTimeAcceleration(RIGHT_SECONDSKIP);
                }
                else
                {
                    Debug.Log("Skipping " + LEFT_SECONDSKIP + "s.");
                    model.askTimeAcceleration(LEFT_SECONDSKIP);
                }
            }
        }
    }
}
