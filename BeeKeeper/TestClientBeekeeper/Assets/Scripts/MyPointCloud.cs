using System;
using System.Collections;
using System.Collections.Generic;
using Unity.Jobs;
using UnityEngine;
using UnityEngine.Profiling;

public class MyPointCloud : MonoBehaviour
{
    public class MyPoint
    {
        public Vector3 pos;
        public Vector3 target;
        public Vector3 origin;
        public float timeAtLastUpdate;
        public bool enabled = true;

        public Color color = Color.black;

        public MyPoint(Vector3 pos)
        {
            this.pos = pos;
            this.origin = pos;
            this.target = pos;
            timeAtLastUpdate = -1;
        }

        /*
        public static Vector3[] getAllPos(MyPoint[] list)
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

        public static Color[] getAllColors(MyPoint[] list)
        {
            Color[] array = new Color[list.Length];

            for (int i = 0; i < list.Length; ++i)
            {
                if (list[i] != null)
                {
                    array[i] = list[i].color;
                }
            }
            return array;
        }
        */
    }

    public static int ID = 0;

    private int id;

    public float pointsSize = 1;

    private Mesh mesh;

    //Moving points
    //private MyPoint[] points = new MyPoint[PointCloudReferencer.cloudMaxSize];

    private Dictionary<int, MyPoint> points = new Dictionary<int, MyPoint>();

    public int serverRefreshRate = 1000;

    private List<int> indecies = new List<int>();

    public int startingIndex = -1;

    void Start()
    {
        mesh = new Mesh();

        //mesh.SetIndices(new int[0], MeshTopology.Points, 0);

        GetComponent<MeshFilter>().mesh = mesh;
        MeshRenderer m = GetComponent<MeshRenderer>();
        m.material = new Material(Shader.Find("Custom/BeePointCloudShader"));
        m.material.SetFloat("_Radius", pointsSize);

        id = ID++;
    }

    public void disablePointID(int pointID)
    {
        //points[pointID] = null;
        points.Remove(pointID);
    }

    public void UpdatePointTarget(int pointId, Vector3 newTarget, Color? color = null)
    {
        Color c = color ?? Color.black;

        //MyPoint thePoint = points[pointId];        

        if(points.TryGetValue(pointId, out MyPoint thePoint))
        {
            thePoint.target = newTarget;
            thePoint.timeAtLastUpdate = Time.time * 1000f;
            thePoint.origin = thePoint.pos;
        }
        else
        {
            thePoint = new MyPoint(newTarget);
            //points[pointId] = thePoint;
            points.Add(pointId, thePoint);

            if (newTarget.z == -1)
            {
                thePoint.pos = getRandomPosForForaging(pointId);
            }
        }

        thePoint.color = c;

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
        return (UnityEngine.Random.onUnitSphere * 50f + 5f * Vector3.up - points[id].pos).normalized * 2f + points[id].pos;
    }

    //Profiler.BeginSample("PCU meshupdate");
    //Profiler.EndSample();

    private void Update()
    {
        float time = Time.time;

        List<MyPoint> thePoints = new List<MyPoint>(points.Values); //Play with a copy to avoid multithread shenanigans

        int numberOfPoints = thePoints.Count;

        Vector3[] pointsPos = new Vector3[numberOfPoints];
        Color[] colors = new Color[numberOfPoints];

        int pi = 0;
        foreach (MyPoint p in thePoints)
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

            colors[pi] = p.color;
            pointsPos[pi] = p.pos;

            pi++;
            //a for loop could have been used
        }

        mesh.Clear();

        mesh.vertices = pointsPos;
        mesh.colors = colors;

        for (int i = indecies.Count; i < numberOfPoints; ++i) // adding indecies if needed
        {
            indecies.Add(i);
        }

        while (indecies.Count > numberOfPoints) // removing indecies if needed
        {
            indecies.RemoveAt(indecies.Count - 1);
        }
        mesh.SetIndices(indecies, MeshTopology.Points, 0);

        //Debug.Log(numberOfPoints + " " + pointsPos.Length + " " + colors.Length + " " + indecies.Count);
        //Debug.Log(id + ":" + mesh.vertices.Length + " " + mesh.colors.Length + " " + pointsPos.Length + " " + colors.Length + " " + indecies.Count + " / " + pi + " " + numberOfPoints);
    }
}
