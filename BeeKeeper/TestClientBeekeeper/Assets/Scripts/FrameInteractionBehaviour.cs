using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FrameInteractionBehaviour : MonoBehaviour
{
    public FrameBehaviour frame;

    public int getFrameID()
    {
        return frame.id;
    }
}
