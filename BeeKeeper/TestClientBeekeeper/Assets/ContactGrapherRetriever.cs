using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ContactGrapherRetriever : MonoBehaviour
{
    public HiveModel model;

    public Vector3 dimension;
    public Vector3 maxValues;

    public InstantPointCloud pointCloud;

    public float refreshRate = 10;

    private float lastRefresh = -10;

    void Update()
    {
        if(Time.realtimeSinceStartup - lastRefresh > refreshRate)
        {
            //update graph
            int size = model.theAgents.Count;
            Vector3[] points = new Vector3[size];
            int index = 0;
            foreach (BeeAgent b in model.theAgents.Values)
            {                
                Vector3 point = transformPoint(new Vector3(b.age, b.JH, b.amountExchanged));
                points[index++] = point;

                pointCloud.registerPointCloud(points);
            }

            lastRefresh = Time.realtimeSinceStartup;
        }        
    }


    public Vector3 transformPoint(Vector3 point)
    {
        maxValues.x = Mathf.Max(maxValues.x, point.x);
        maxValues.y = Mathf.Max(maxValues.y, point.y);
        maxValues.z = Mathf.Max(maxValues.z, point.z);

        Vector3 transformedPos = new Vector3();
        transformedPos.x = (point.x / maxValues.x) * dimension.x/* - transform.position.x*/;
        transformedPos.y = (point.y / maxValues.y) * dimension.y/* - transform.position.y*/;
        transformedPos.z = (point.z / maxValues.z) * dimension.z/* - transform.position.z*/;
        return transformedPos;
    }

    private void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.green;
        Gizmos.DrawLine(transform.position, transform.position + Vector3.right * dimension.x);
        Gizmos.DrawLine(transform.position, transform.position + Vector3.up * dimension.y);
        Gizmos.DrawLine(transform.position, transform.position + Vector3.forward * dimension.z);
    }
}
