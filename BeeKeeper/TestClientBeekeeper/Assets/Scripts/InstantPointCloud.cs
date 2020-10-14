using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InstantPointCloud : MonoBehaviour
{
    private Vector3[] points;
    private List<int> indecies = new List<int>();

    private Mesh mesh;

    public void Start()
    {
        mesh = new Mesh();

        GetComponent<MeshFilter>().mesh = mesh;
        GetComponent<MeshRenderer>().material = new Material(Shader.Find("Custom/PointToSquareShader"));
    }

    public void registerPointCloud(Vector3[] points)
    {
        if (indecies.Count < points.Length)
        {
            for (int i = indecies.Count; i < points.Length; ++i)
            {
                indecies.Add(i);
            }
        }

        while (indecies.Count > points.Length)
        {
            indecies.RemoveAt(indecies.Count - 1);
        }

        //Debug.Log("Vertices: " + points.Length + " indecies: " + indecies.Count);

        mesh.vertices = points;
        mesh.SetIndices(indecies, MeshTopology.Points, 0);
    }
}
