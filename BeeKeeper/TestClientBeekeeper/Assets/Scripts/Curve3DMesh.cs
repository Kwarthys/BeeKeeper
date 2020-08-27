using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Curve3DMesh : MonoBehaviour
{
    public Color color = Color.black;

    public float width = 0.05f;
    
    public GameObject label;

    public float labelXOffset;

    private List<Vector3> allPoints = new List<Vector3>();

    private List<Vector3> vertices = new List<Vector3>();
    private List<int> triangles = new List<int>();

    private MeshFilter meshFilter;
    private MeshRenderer meshRenderer;

    private Mesh mesh;

    private Dictionary<int, int> pointIndexRemap = new Dictionary<int, int>();

    public void Start()
    {
        //initialize();
    }

    public void initialize()
    {
        meshFilter = transform.GetComponent<MeshFilter>();
        meshRenderer = transform.GetComponent<MeshRenderer>();

        mesh = new Mesh();
        meshFilter.mesh = mesh;
        meshRenderer.material.color = color;
        
        /*
        createPoint(0,new Vector3(0, .2f, 0));
        createPoint(1,new Vector3(.25f, .5f, 0));
        createPoint(2,new Vector3(.75f, 0.2f, 0));
        createPoint(3,new Vector3(1, .4f, 0));

        movePoint(3, new Vector3(1, 0, 0));
        //mesh.RecalculateNormals();
        */
    }

    public void createPoint(int pointIndex, Vector3 point)
    {
        pointIndexRemap[pointIndex] = allPoints.Count;

        allPoints.Add(point);

        addLastToMesh();
    }

    public void movePoint(int pointIndex, Vector3 newPos)
    {
        allPoints[pointIndexRemap[pointIndex]] = newPos;
        updateMesh(pointIndexRemap[pointIndex]);
    }

    private void addLastToMesh()
    {
        //Debug.Log(transform.name + " has " + allPoints.Count + " dataPoints.");

        Vector3 basis = allPoints[allPoints.Count - 1];

        label.transform.position = basis + Vector3.right * labelXOffset + transform.position;

        //Debug.Log(label.transform.position);
        //Debug.Log(basis);

        vertices.Add(basis + Vector3.up * width + Vector3.forward * width);
        vertices.Add(basis + Vector3.up * width - Vector3.forward * width);
        vertices.Add(basis - Vector3.up * width - Vector3.forward * width);
        vertices.Add(basis - Vector3.up * width + Vector3.forward * width);

        mesh.SetVertices(vertices);

        int currentLastVertex = vertices.Count - 1;
        /*
        triangles.Add(currentLastVertex - 0);
        triangles.Add(currentLastVertex - 2);
        triangles.Add(currentLastVertex - 3);

        triangles.Add(currentLastVertex - 0);
        triangles.Add(currentLastVertex - 1);
        triangles.Add(currentLastVertex - 2);

        triangles.Add(currentLastVertex - 0);
        triangles.Add(currentLastVertex - 3);
        triangles.Add(currentLastVertex - 2);

        triangles.Add(currentLastVertex - 0);
        triangles.Add(currentLastVertex - 2);
        triangles.Add(currentLastVertex - 1);
        */
        if (allPoints.Count > 1)
        {
            triangles.Add(currentLastVertex - 3);
            triangles.Add(currentLastVertex - 6);
            triangles.Add(currentLastVertex - 7);

            triangles.Add(currentLastVertex - 3);
            triangles.Add(currentLastVertex - 2);
            triangles.Add(currentLastVertex - 6);

            triangles.Add(currentLastVertex - 2);
            triangles.Add(currentLastVertex - 5);
            triangles.Add(currentLastVertex - 6);

            triangles.Add(currentLastVertex - 2);
            triangles.Add(currentLastVertex - 1);
            triangles.Add(currentLastVertex - 5);

            triangles.Add(currentLastVertex - 1);
            triangles.Add(currentLastVertex - 4);
            triangles.Add(currentLastVertex - 5);

            triangles.Add(currentLastVertex - 1);
            triangles.Add(currentLastVertex - 0);
            triangles.Add(currentLastVertex - 4);

            triangles.Add(currentLastVertex - 0);
            triangles.Add(currentLastVertex - 7);
            triangles.Add(currentLastVertex - 4);

            triangles.Add(currentLastVertex - 0);
            triangles.Add(currentLastVertex - 3);
            triangles.Add(currentLastVertex - 7);

            mesh.SetTriangles(triangles.ToArray(),0);
        }
    }

    private void updateMesh(int index)
    {
        Vector3 basis = allPoints[index];

        vertices[index*4] = (basis + Vector3.up * width + Vector3.forward * width);
        vertices[index*4+1] = (basis + Vector3.up * width - Vector3.forward * width);
        vertices[index*4+2] = (basis - Vector3.up * width - Vector3.forward * width);
        vertices[index*4+3] = (basis - Vector3.up * width + Vector3.forward * width);

        mesh.SetVertices(vertices);
    }
}
