using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class VRGetInput : MonoBehaviour
{
    /*** Registering input on either of the two controllers, and comparing them to the Profile system before sending the results ***/

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


    private bool gripLastState = false;

    public bool getGripButttonDown(int profile = -1)
    {
        bool grip = Input.GetAxis("ViveGrip") != 0 || Input.GetAxis("ViveGrip2") != 0;

        bool value = grip && checkProfile(profile) && !gripLastState;

        gripLastState = grip;

        return value;
    }

    public bool getMenuButtonDown(int profile = -1)
    {
        return (Input.GetKeyDown(KeyCode.JoystickButton0) || Input.GetKeyDown(KeyCode.JoystickButton2)) && checkProfile(profile);
    }

    public float getHorizontalPad(int profile = -1)
    {
        float horizontal = Input.GetAxis("ViveHorizontalPad");

        if (horizontal == 0)
        {
            horizontal = Input.GetAxis("ViveHorizontalPad2");
        }
        return horizontal * (checkProfile(profile) ? 1 : 0);
    }

    public float getVerticalPad(int profile = -1)
    {
        float vertical = Input.GetAxis("ViveVerticalPad");

        if (vertical == 0)
        {
            vertical = Input.GetAxis("ViveVerticalPad2");
        }
        return vertical * (checkProfile(profile) ? 1 : 0);
    }



    private bool checkProfile(int profile)
    {
        return profile == -1 || profile == profileManager.activeProfile.index;
    }


    public enum PadPress { Top, Bot, Left, Right, None };

    public PadPress getPadPressPos(int profile = -1)
    {
        if(!getPadPress(profile))
        {
            return PadPress.None;
        }

        float padV = getVerticalPad(profile);
        float padH = getHorizontalPad(profile);
        //Debug.Log(myInputs.getHorizontalPad() + " " + myInputs.getVerticalPad());

        if (Mathf.Abs(padV) > Mathf.Abs(padH))
        {
            if (padV > 0)
            {
                return PadPress.Bot;
            }
            else
            {
                return PadPress.Top;
            }
        }
        else
        {
            if (padH > 0)
            {
                return PadPress.Right;
            }
            else
            {
                return PadPress.Left;
            }
        }
    }
}
