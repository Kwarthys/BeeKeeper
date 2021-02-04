using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class VRGraphGripper : MonoBehaviour
{
    public VRGetInput myInputs;

    public Transform graph;

    public float distanceTeleportTreshold = .5f;

    private bool gripped = false;

    // Update is called once per frame
    void Update()
    {
        if (myInputs.getTriggerDown(VRInteractionProfileManager.GRAPH_PROFILE))
        {
            //Debug.Log("GRIP");

            gripped = true;

            if((graph.position - transform.position).magnitude > distanceTeleportTreshold)
            {
                graph.position = transform.position;
            }

            graph.SetParent(transform);
            //graph.rotation = transform.rotation;
        }
        else if(gripped)
        {
            //Debug.Log("DROP");
            graph.SetParent(null);
        }
    }
}
