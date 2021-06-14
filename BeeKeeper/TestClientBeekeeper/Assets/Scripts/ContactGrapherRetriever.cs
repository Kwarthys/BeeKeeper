using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ContactGrapherRetriever : MonoBehaviour
{
    public HiveModel model;

    public Vector3 dimension;
    public Vector3 maxValues;

    public bool xRelative = false;
    public bool yRelative = false;
    public bool zRelative = false;

    public bool zLogarithmicScale = false;

    public PointCloudReferencer pointCloud;

    public float refreshRate = 1;

    private float lastRefresh = -10;

    private Dictionary<int, int> idToPointID = new Dictionary<int, int>();

    private void Start()
    {
        if(zLogarithmicScale)
        {
            maxValues.z = Mathf.Log10(maxValues.z + 1);
        }
    }

    /**** EXPERIMENTAL ****/
    private void clearGraph()
    {
        foreach(KeyValuePair<int, int> entry in idToPointID)
        {
            pointCloud.freeIndex(entry.Value);
        }
        idToPointID.Clear();
    }
    /*********************/

    void Update()
    {
        if(Time.realtimeSinceStartup - lastRefresh > refreshRate)
        {
            List<Vector3> targets = new List<Vector3>();
            List<int> ids = new List<int>();
            List<Color> colors = new List<Color>();

            //Debug.Log("ContactGrapher AgentsSize - " + model.theAgents.Count);

            //update graph
            int size = model.theAgents.Count;
            foreach (BeeAgent b in model.theAgents.Values)
            {                
                Vector3 point = transformPoint(new Vector3(b.age, b.JH, b.amountExchanged));
                targets.Add(point);
                colors.Add(b.JH > 0.5f ? Color.yellow : Color.red);
                //Debug.Log(b.JH > 0.5f ? Color.yellow : Color.red);

                int pointID;

                if(!idToPointID.ContainsKey(b.id))
                {
                    pointID = pointCloud.idManager.getNextFreeIndex();
                    idToPointID.Add(b.id, pointID);

                }
                else
                {
                    pointID = idToPointID[b.id];
                }

                ids.Add(pointID);
            }

            pointCloud.updatePoints(new UpdateOrder(targets, ids, colors));

            lastRefresh = Time.realtimeSinceStartup;
        }        
    }


    public Vector3 transformPoint(Vector3 point)
    {
        if(point.z > 0)
            Debug.Log(point.z);

        if (zLogarithmicScale)
        {
            point.z = Mathf.Log10(point.z + 1);
        }

        if (xRelative)
            maxValues.x = Mathf.Max(maxValues.x, point.x);
        if(yRelative)
            maxValues.y = Mathf.Max(maxValues.y, point.y);
        if(zRelative)
            maxValues.z = Mathf.Max(maxValues.z, point.z);


        Vector3 transformedPos = new Vector3();
        transformedPos.x = (point.x / maxValues.x) * dimension.x/* - transform.position.x*/;
        transformedPos.y = (point.y / maxValues.y) * dimension.y/* - transform.position.y*/;
        transformedPos.z = (point.z / maxValues.z) * dimension.z/* - transform.position.z*/;

        return transformedPos;
    }

    private void OnDrawGizmos()
    {
        Gizmos.color = Color.green;
        Gizmos.DrawLine(transform.position, transform.position + Vector3.right * dimension.x);
        Gizmos.DrawLine(transform.position, transform.position + Vector3.up * dimension.y);
        Gizmos.DrawLine(transform.position, transform.position + Vector3.forward * dimension.z);
    }
}
