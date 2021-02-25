using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class VRDivisionHandManager : MonoBehaviour
{
    public VRGetInput myInputs;

    public HiveModel hive;

    public float longPressTime = 1;
    private float startPress = -1;

    private bool pressing = false;

    private bool longPressValidated = false;

    public Image longPressHint;

    // Update is called once per frame
    void Update()
    {
        if(myInputs.getPadDown(VRInteractionProfileManager.FRAME_PROFILE))
        {
            if(pressing)
            {
                float timePressed = Time.time - startPress;

                longPressValidated = timePressed > longPressTime;
                float amount = timePressed / longPressTime;
                longPressHint.fillAmount = amount;
            }
            else
            {
                //Just started
                startPress = Time.time;
            }

            pressing = true;
        }
        else
        {
            pressing = false;
            longPressHint.fillAmount = 0;
        }

        if(longPressValidated)
        {
            longPressValidated = false;
            startPress += 5000;

            VRGetInput.PadPress padPress = myInputs.getPadDownPos(VRInteractionProfileManager.FRAME_PROFILE);

            switch (padPress)
            {
                case VRGetInput.PadPress.Left:
                    //Division keeping inside frames
                    hive.askRebase(true);
                    break;
                case VRGetInput.PadPress.Right:
                    //Division keeping outside frame
                    hive.askRebase(false);
                    break;
                default:
                    //do nothing if top or bot
                    Debug.Log("Nothing");
                    break;
            }
        }
    }
}
