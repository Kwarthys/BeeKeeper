using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class OptiTrackReferencer : MonoBehaviour
{
    [Header("Tracked gameObjects which need disable rigidbody for tracking")]
    [SerializeField]
    private GameObject[] trackedObjects;

    void OnEnable()
    {
        updateTrackedObjectState(true);
    }

    void OnDisable()
    {
        updateTrackedObjectState(false);
    }

    private void updateTrackedObjectState(bool active)
    {
        foreach (GameObject g in trackedObjects)
        {
            //Enable or disable physics on tracked object depending on active OptiTrack
            Rigidbody r = g.GetComponent<Rigidbody>();
            if (r != null)
            {
                r.isKinematic = active;
            }
            //Enable or disable tracking script on tracked object depending on active OptiTrack
            Debug.Log("Removed this");
            /*
            OptitrackRigidBody o = g.GetComponent<OptitrackRigidBody>();
            if (o != null)
            {
                o.enabled = active;
            }
            */
        }
    }
}
