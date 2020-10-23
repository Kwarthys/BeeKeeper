using System.Collections;
using System.Collections.Generic;
using Unity.Jobs;
using UnityEngine;

public class MyPointCloud : MonoBehaviour
{
    public class MyPoint
    {
        public Vector3 pos;
        public Vector3 target;
        public Vector3 origin;
        public float timeAtLastUpdate;
        public bool enabled = true;

        public MyPoint(Vector3 pos)
        {
            this.pos = pos;
            this.origin = pos;
            this.target = pos;
            timeAtLastUpdate = -1;
        }

        public static Vector3[] getPos(MyPoint[] list)
        {
            Vector3[] array = new Vector3[list.Length];

            for(int i = 0; i < list.Length; ++i)
            {
                if(list[i] != null)
                {
                    array[i] = list[i].pos;
                }
            }
            return array;
        }
    }

    private Mesh mesh;

    //Moving points
    private MyPoint[] points = new MyPoint[PointCloudReferencer.cloudMaxSize];

    public int serverRefreshRate = 500;

    private List<int> indecies = new List<int>();

    private List<UpdateOrder> orders = new List<UpdateOrder>();

    public int startingIndex = -1;

    void Start()
    {
        mesh = new Mesh();

        GetComponent<MeshFilter>().mesh = mesh;
        GetComponent<MeshRenderer>().material = new Material(Shader.Find("Custom/PointToSquareShader"));        
    }

    public void disablePointID(int pointID)
    {
        points[pointID] = null;
    }

    public void UpdatePointTarget(int pointId, Vector3 newTarget)
    {
        MyPoint thePoint = points[pointId];

        if(thePoint == null)
        {
            thePoint = new MyPoint(newTarget);
            points[pointId] = thePoint;

            if (newTarget.z == -1)
            {
                thePoint.pos = getRandomPosForForaging(pointId);
            }
        }
        else
        {
            thePoint.target = newTarget;
            thePoint.timeAtLastUpdate = Time.time * 1000f;
            thePoint.origin = thePoint.pos;
        }

        if (newTarget.z == -1)
        {
            thePoint.target = getRandomPosForForaging(pointId);
            if (pointId % 100 == 0)
            {
                //Debug.Log("Forager" + pointId + " Target: " + points[pointId].target + " and pos: " + points[pointId].pos);
                //Debug.Log(Random.onUnitSphere * 200f + " " + (Random.onUnitSphere * 200f - points[pointId].pos).normalized);
            }
        }
        else
        {
            //Debug.DrawLine(points[pointId].origin, points[pointId].target, Color.red, 0.1f);
        }
    }

    private Vector3 getRandomPosForForaging(int id)
    {
        return ((Random.onUnitSphere) * 50f + 5f * Vector3.up - points[id].pos).normalized * 2f + points[id].pos;
    }

    private void Update()
    {
        float time = Time.time;

        /*** debug
        if(points[0] != null)
        {
            MyPoint p = points[0];
            Debug.Log(p.timeAtLastUpdate + " " + Time.time*1000f + " " + ((Time.time * 1000f - p.timeAtLastUpdate) / (serverRefreshRate)) + "||" + p.pos + " -> " + p.target);
        }
        */
        
        //Move 'em
        foreach (MyPoint p in points)
        {
            if(p != null)
            {
                if (p.timeAtLastUpdate != -1)
                {
                    float t = (Time.time * 1000f - p.timeAtLastUpdate) / (serverRefreshRate);

                    if (t < 0) t = 0;

                    if (t > 1)
                    {
                        p.timeAtLastUpdate = -1;
                        t = 1;
                    }
                    else
                    {
                        p.pos = new Vector3(
                            Mathf.Lerp(p.origin.x, p.target.x, t),
                            Mathf.Lerp(p.origin.y, p.target.y, t),
                            Mathf.Lerp(p.origin.z, p.target.z, t));
                    }
                }
            }
        }

        mesh.vertices = MyPoint.getPos(points);

        if(indecies.Count < mesh.vertices.Length)
        {
            for(int i = indecies.Count; i < points.Length; ++i)
            {
                indecies.Add(i);
            }
        }

        while(indecies.Count > mesh.vertices.Length)
        {
            indecies.RemoveAt(indecies.Count - 1);
        }

        mesh.SetIndices(indecies, MeshTopology.Points, 0);
    }
}
