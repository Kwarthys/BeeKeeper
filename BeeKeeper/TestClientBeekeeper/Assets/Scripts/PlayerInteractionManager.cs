using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerInteractionManager : MonoBehaviour
{
    public FrameManager frameManager;

    public Camera cam;

    public LayerMask combMask;

    private int lastSelectedIndex = -1;

    private Transform tmpTransform;
    private Transform boundFrame;

    // Update is called once per frame
    void Update()
    {
        /**** MOUSE INTERACTION ****/

        RaycastHit hit;
        Ray ray = cam.ScreenPointToRay(Input.mousePosition);
        if (Physics.Raycast(ray, out hit, 200f, combMask))
        {
            int id = hit.transform.GetComponent<FrameInteractionBehaviour>().getFrameID();
            tmpTransform = hit.transform;
            if(id != lastSelectedIndex)
            {
                if (lastSelectedIndex != -1)
                {
                    frameManager.setFrameSelected(lastSelectedIndex, false);
                }

                frameManager.setFrameSelected(id, true);
                lastSelectedIndex = id;
            }
        }
        else
        {
            if (lastSelectedIndex != -1)
            {
                frameManager.setFrameSelected(lastSelectedIndex, false);
                lastSelectedIndex = -1;
                tmpTransform = null;
            }
        }

        if (Input.GetMouseButtonDown(0))
        {
            if (boundFrame == null && lastSelectedIndex != -1 && tmpTransform != null)
            {
                //BIND
                frameManager.toggleFrameState(lastSelectedIndex);
                boundFrame = tmpTransform;
                tmpTransform = null;
            }
            else if(boundFrame != null)
            {
                //SWITCH
                frameManager.toggleFrameState(lastSelectedIndex);
                boundFrame = null;
            }
            
        }

        if (boundFrame != null)
        {
            //MOVE
            /*
            Vector3 pos = Input.mousePosition;
            Debug.Log(pos);
            pos = cam.ScreenToWorldPoint(pos);
            pos += cam.transform.forward * 20f;
            boundFrame.position = pos;
            */
        }
    }
}
