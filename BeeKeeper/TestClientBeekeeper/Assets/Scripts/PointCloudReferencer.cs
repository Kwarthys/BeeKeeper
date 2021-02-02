using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PointCloudReferencer : MonoBehaviour
{
    private List<UpdateOrder> orders = new List<UpdateOrder>();

    public GameObject pointCloudPrefab;

    public static int cloudMaxSize = 60000;

    public static int pointUpdateTime = 0;

    public int lerpTime = 500;

    public IDManager idManager = new IDManager();

    private List<MyPointCloud> clouds = new List<MyPointCloud>();

    public float pointsSize = 1;

    public void updatePoints(UpdateOrder o)
    {
        for(int i = 0; i < o.targetsIDs.Count; ++i)
        {
            MyPointCloud c = getCloudFor(o.targetsIDs[i]);
            if (c != null) c.UpdatePointTarget(o.targetsIDs[i], o.newTargets[i], o.colors[i]);
        }
    }

    public void freeIndex(int pointID)
    {
        idManager.freeIndex(pointID);
        getCloudFor(pointID).disablePointID(pointID);
    }

    private MyPointCloud getCloudFor(int pointId)
    {
        int cloudID = pointId / cloudMaxSize;

        while(clouds.Count-1 < cloudID)
        {
            //Create
            createANewCloud(clouds.Count * cloudMaxSize);
        }

        return clouds[cloudID];
    }

    private void createANewCloud(int starting)
    {
        GameObject c = Instantiate(pointCloudPrefab, transform);
        MyPointCloud newCloud = c.GetComponent<MyPointCloud>();
        newCloud.pointsSize = pointsSize;
        clouds.Add(newCloud);
        newCloud.startingIndex = starting;
        newCloud.serverRefreshRate = lerpTime;
    }
}
