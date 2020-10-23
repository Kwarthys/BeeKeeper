using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class My3DGraph : MonoBehaviour
{
    public GameObject pointPrefab;
    public Vector3 dimension;
    public Vector3 maxValues;

    [Range(0,1)]
    public float alphaValue = 0.5f;

    private List<GameObject> allPoints = new List<GameObject>();

    private Color[] colors = { Color.white, Color.red, Color.black, Color.blue, Color.green, Color.yellow, Color.cyan};

    public void Start()
    {
        for(int i = 0; i < colors.Length; ++i)
        {
            colors[i].a = alphaValue;
        }
    }

    public void movePoint(int pointID, Vector3 point)
    {
        Vector3 transformedPos = transformPoint(point);

        allPoints[pointID].transform.position = transformedPos;
    }

    public void createPoint(Vector3 point)
    {
        createPoint(point, 0);
    }

    public void createPoint(Vector3 point, int colorIndex)
    {
        Vector3 transformedPos = transformPoint(point);

        GameObject gameObject = Instantiate(pointPrefab, transformedPos, Quaternion.identity, transform);

        gameObject.GetComponent<MeshRenderer>().material.color = colors[colorIndex % colors.Length];

        allPoints.Add(gameObject);
    }

    public Vector3 transformPoint(Vector3 point)
    {
        Vector3 transformedPos = new Vector3();

        transformedPos.x = (point.x / maxValues.x) * dimension.x + transform.position.x;
        transformedPos.y = (point.y / maxValues.y) * dimension.y + transform.position.y;
        transformedPos.z = (point.z / maxValues.z) * dimension.z + transform.position.z;

        return transformedPos;
    }
}
