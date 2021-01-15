using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PlayerRayCastExecute : MonoBehaviour
{
    public Image knob;

    public LayerMask layers;

    private bool knobState = false;

    private Transform grabbed;
    private FrameBehaviour frameGrabbed; //if handling a frame, shortcut to avoid getcomponents

    public FramePositionner positionner;
    public FramePositionner falseHivePositionner;

    // Update is called once per frame
    void Update()
    {
        if(grabbed == null)
        {
            RaycastHit hit;

            if (Physics.Raycast(transform.position, transform.forward, out hit, 3f, layers))
            {
                highlight(true);

                if (Input.GetMouseButtonDown(0))
                {
                    Interactible item = hit.collider.transform.GetComponentInParent<Interactible>();
                    if (item != null)
                    {
                        item.interact(this);
                    }
                    else
                    {
                        Debug.Log("Did not find any interactible in " + hit.collider.transform.name);
                    }
                }

            }
            else
            {
                highlight(false);
            }
        }
        else
        {
            //DROP
            if (Input.GetMouseButtonDown(0))
            {
                if (frameGrabbed != null)
                {
                    positionner.notifyFrameDropped(frameGrabbed);
                    if (falseHivePositionner.notifyFrameDropped(frameGrabbed))
                    {
                        grabbed.SetParent(positionner.transform);
                    }
                    else
                    {
                        grabbed.SetParent(positionner.transform);
                    }
                }
                else
                {
                    grabbed.SetParent(null);
                }

                grabbed = null;
                frameGrabbed = null;
            }
            else
            {
                if(frameGrabbed!=null)
                {
                    positionner.notifyFrameHandled(frameGrabbed);
                    falseHivePositionner.notifyFrameHandled(frameGrabbed);
                }
                //frameHandling

                if(Input.GetKey(KeyCode.A))
                {
                    grabbed.Rotate(Vector3.up*2);
                }
                else if (Input.GetKey(KeyCode.E))
                {
                    grabbed.Rotate(Vector3.down*2);
                }
                else if (Input.GetKey(KeyCode.R))
                {
                    grabbed.Rotate(Vector3.left*2);
                }
                else if (Input.GetKey(KeyCode.F))
                {
                    grabbed.Rotate(Vector3.right * 2);
                }
                else if (Input.GetKey(KeyCode.T) && frameGrabbed != null)
                {
                    //start shaknig anim

                    //send message through positionner command sender
                    positionner.sender.hitFrame(frameGrabbed.id);
                }

                if (Input.mouseScrollDelta.y != 0)
                {
                    grabbed.Translate(-transform.forward * 0.1f * Input.mouseScrollDelta.y, Space.World);
                }
            }
        }

              
    }

    void highlight(bool state)
    {
        if(knobState != state)
        {
            knobState = state;
            knob.color = knobState ? Color.red : Color.white;
        }
    }

    public void grab(Transform tograb)
    {
        tograb.SetParent(transform);
        grabbed = tograb;

        FrameBehaviour f = tograb.GetComponent<FrameBehaviour>();
        if(f!=null)
        {
            frameGrabbed = f;
        }
    }
}
