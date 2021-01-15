using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class MyMesh3DGraph : MonoBehaviour
{
    public Vector3 dimension;
    public Vector3 maxValues;

    public GameObject legendText;
    public Transform textParentItem;

    public float xTagsGraphOffset = .5f;

    public float lineWidth = .05f;

    public GameObject curve3DMeshPrefab;

    private Color[] colors = {Color.blue, Color.green, Color.magenta, Color.cyan };

    private Dictionary<string, Curve3DMesh> curves = new Dictionary<string, Curve3DMesh>();
    private Dictionary<string, Color> taskColors = new Dictionary<string, Color>();

    private List<string> pointTask = new List<string>();

    void Start()
    {
        /*
        createPoint(new Vector3(0, .2f, 0), 2);
        createPoint(new Vector3(.25f, .5f, 0), 2);
        createPoint(new Vector3(.75f, 0.2f, 0), 2);
        createPoint(new Vector3(1, .4f, 0), 2);

        createPoint(new Vector3(0, .2f, 1), 1);
        createPoint(new Vector3(.25f, .5f, 1), 1);
        createPoint(new Vector3(.75f, 0.2f, 1), 1);
        createPoint(new Vector3(1, .4f, 1), 1);
        */
    }

    public void reinit()
    {
        foreach(Transform t in transform)
        {
            Destroy(t.gameObject);
        }

        curves.Clear();
        pointTask.Clear();

        foreach(Transform t in textParentItem.transform)
        {
            Destroy(t.gameObject);
        }

    }

    public void movePoint(int pointID, Vector3 toPoint)
    {
        Vector3 transformedPos = transformPoint(toPoint);

        curves[pointTask[pointID]].movePoint(pointID, transformedPos);
    }

    public void createPoint(Vector3 point)
    {
        createPoint(point, "");
    }

    private Color getColorOfTask(string taskName)
    {
        if (!taskColors.ContainsKey(taskName))
        {
            taskColors[taskName] = colors[curves.Count-1];
        }

        return taskColors[taskName];
    }

    public void createPoint(Vector3 point, string taskName)
    {
        Vector3 transformedPos = transformPoint(point);

        if(!curves.ContainsKey(taskName))
        {
            Curve3DMesh curve = Instantiate(curve3DMeshPrefab, transform).GetComponent<Curve3DMesh>();
            curves[taskName] = curve;
            curve.gameObject.name = taskName + "Curve";
            curve.color = getColorOfTask(taskName);

            GameObject label = Instantiate(legendText, textParentItem);
            label.GetComponent<TextMeshProUGUI>().text = taskName;

            curve.label = label;
            curve.labelXOffset = xTagsGraphOffset;

            curve.width = lineWidth;

            curve.initialize();
        }

        curves[taskName].createPoint(pointTask.Count, transformedPos);
        pointTask.Add(taskName);
    }

    public Vector3 transformPoint(Vector3 point)
    {
        //maxValues.x = Mathf.Max(maxValues.x, point.x);
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
        Gizmos.color = Color.blue;
        Gizmos.DrawLine(transform.position, transform.position + Vector3.up * dimension.y);
        Gizmos.DrawLine(transform.position, transform.position + Vector3.right * dimension.x);
    }
}
