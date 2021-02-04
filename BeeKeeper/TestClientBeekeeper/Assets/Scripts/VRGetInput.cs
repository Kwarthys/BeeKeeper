using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class VRGetInput : MonoBehaviour
{
    public VRInteractionProfileManager profileManager;

    public bool getPadPress(int profile = -1)
    {
        return (Input.GetKeyDown(KeyCode.JoystickButton8) || Input.GetKeyDown(KeyCode.JoystickButton9)) && checkProfile(profile);    
    }

    public float getTriggerAxis(int profile = -1)
    {
        return Input.GetAxis("ViveTrigger") * (checkProfile(profile) ? 1 : 0);
    }

    public bool getTriggerPress(int profile = -1)
    {
        return (Input.GetKeyDown(KeyCode.JoystickButton15) || Input.GetKeyDown(KeyCode.JoystickButton14)) && checkProfile(profile);
    }

    public bool getTriggerDown(int profile = -1)
    {
        return (Input.GetKey(KeyCode.JoystickButton15) || Input.GetKey(KeyCode.JoystickButton14)) && checkProfile(profile);
    }

    public bool getGripButttonDown(int profile = -1)
    {
        return Input.GetAxis("ViveGrip") != 0 && checkProfile(profile);
    }

    public bool getMenuButtonDown(int profile = -1)
    {
        return (Input.GetKeyDown(KeyCode.JoystickButton0) || Input.GetKeyDown(KeyCode.JoystickButton2)) && checkProfile(profile);
    }

    public float getHorizontalPad(int profile = -1)
    {
        return Input.GetAxis("ViveHorizontalPad") * (checkProfile(profile) ? 1 : 0);
    }

    public float getVerticalPad(int profile = -1)
    {
        return Input.GetAxis("ViveVerticalPad") * (checkProfile(profile) ? 1 : 0);
    }



    private bool checkProfile(int profile)
    {
        return profile == -1 || profile == profileManager.activeProfile.index; 
    }
}
