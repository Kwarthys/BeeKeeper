using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class VRDivisionHandManager : MonoBehaviour
{
    public VRGetInput myInputs;

    public HiveModel hive;

    // Update is called once per frame
    void Update()
    {
        VRGetInput.PadPress padPress = myInputs.getPadPressPos(VRInteractionProfileManager.FRAME_PROFILE);

        switch(padPress)
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
                break;
        }
    }
}
