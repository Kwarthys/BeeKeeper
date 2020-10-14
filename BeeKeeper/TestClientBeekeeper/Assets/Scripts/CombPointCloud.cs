using System.Collections;
using System.Collections.Generic;
using Unity.Jobs;
using UnityEngine;

public class CombPointCloud : MonoBehaviour
{
    private Vector3[] points;
    public int rows = 20;
    public int columns = 20;

    private int numPoints;

    private int counter = 0;

    private Mesh mesh;

    private void OnValidate()
    {
        //init();
        //createMesh();
    }

    private void Start()
    {
        init();
        createMesh();
    }

    public void setColors(Color[] colors)
    {
        if(mesh!=null)
        {
            mesh.colors = colors;
        }
    }

    private void Update()
    {
        /*
        if(counter++ > 60)
        {
            counter = 0;
            
            Color[] colors = new Color[numPoints];
            for (int y = 0; y < numPoints; y++)
            {
                colors[y] = getRandColor();
            }

            mesh.colors = colors;
        }
        */
    }

    void init()
    {
        if (mesh == null)
        {
            mesh = new Mesh();

            GetComponent<MeshFilter>().mesh = mesh;
            GetComponent<MeshRenderer>().material = new Material(Shader.Find("Custom/HexPointCloudShader"));
        }

        numPoints = rows * columns;

    }

    private Color getRandColor()
    {
        Color c;

        if (Random.value > 0.5f)
        {
            c = Color.yellow;
        }
        else
        {
            c = new Color(.25f, .25f, .25f);
        }
        /*
        if (Random.value > 0.75f)
        {
            c = Color.yellow;
        }
        else if (Random.value > 0.5f)
        {
            c = Color.blue;
        }
        else if (Random.value > 0.25f)
        {
            c = Color.red;
        }
        else
        {
            c = new Color(0,0.9f,0);
        }
        */
        return c;
    }

    void createMesh()
    {
        points = new Vector3[numPoints];
        Vector3[] normals = new Vector3[numPoints];
        int[] indecies = new int[numPoints];
        Color[] colors = new Color[numPoints];

        int max = 10;
        int min = -10;

        bool offset = false;

        for(int y = 0; y < rows; y++)
        {
            for(int x = 0 ; x < columns; x++)
            {
                int n = x + y * columns;
                points[n] = new Vector3(offset? x+0.5f:x, -y, 0f);
                indecies[n] = n;
                normals[n] = transform.forward;

                float value = (float)1.0 * (((float)y - (float)min) / ((float)max - (float)min));                

                colors[n] = getRandColor();
            }
            offset = !offset;
        }
        /*
        for (int i = 0; i < points.Length; ++i)
        {
            int x = Random.Range(min, max);
            int y = Random.Range(min, max);
            int z = Random.Range(min, max);
            points[i] = new Vector3(x, y, z);
            indecies[i] = i;

            float value = (float)1.0 * (((float)y - (float)min) / ((float)max - (float)min));

            colors[i] = new Color(value, value, value, 1.0f);
        }
        */
        mesh.vertices = points;
        mesh.colors = colors;
        mesh.SetIndices(indecies, MeshTopology.Points, 0);
        mesh.normals = normals;
    }
}
