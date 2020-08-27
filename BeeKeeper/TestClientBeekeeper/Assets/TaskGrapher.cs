using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TaskGrapher : MonoBehaviour
{
    private Dictionary<int, TaskGraphData> data = new Dictionary<int, TaskGraphData>();

    private Dictionary<int, int> dataTStoIndexRemap = new Dictionary<int, int>();

    private int nextIndex = 0;

    public MyMesh3DGraph grapher;

    private bool lockToken = false;

    private List<string> allKnownTaskNames = new List<string>();

    public void Update()
    {
        if (lockToken) return;
        lockToken = true;

        int index = 0;

        foreach (TaskGraphData tgd in data.Values)
        {
            if (tgd.newPoint)
            {
                //string[] taskNames = getTaskNames(tgd);
                Vector3[] points = buildPointsWithData(index, tgd);
                for(int i = 0; i < allKnownTaskNames.Count; ++i)
                {
                    //MOVE/SPAWN LEGEND
                    grapher.createPoint(points[i], allKnownTaskNames[i]);
                }

                //updateLegend(index, tgd);

                tgd.newPoint = false;
                tgd.updated = false;
            }
            else if(tgd.updated)
            {
                foreach (Vector3 point in buildPointsWithData(index, tgd))
                {
                    grapher.movePoint(index, point);
                }
                tgd.updated = false;
            }

            index += tgd.tasks.Values.Count;
        }


        lockToken = false;
    }

    private Vector3[] buildPointsWithData(int pointIndex, TaskGraphData taskData)
    {
        List<Vector3> pointList = new List<Vector3>();

        foreach(string taskName in taskData.tasks.Keys)
        {
            if(!allKnownTaskNames.Contains(taskName))
            {
                //Building knowledge about all the tasks to graph
                allKnownTaskNames.Add(taskName);
            }
        }

        foreach(string taskName in allKnownTaskNames)
        {
            int value = 0;
            if(taskData.tasks.ContainsKey(taskName))
            {
                value = taskData.tasks[taskName];
            }
            //Debug.Log(taskData.remappedIndex + " : " + taskName + " <- " + value);
            pointList.Add(new Vector3(taskData.remappedIndex, value, 0));
        }

        return pointList.ToArray();
    }

    public void postData(UpdateStatus status)
    {
        while (lockToken) { }
        lockToken = true;


        TaskGraphData graphData;

        if(!data.ContainsKey(status.timeStep))
        {
            graphData = new TaskGraphData(status.timeStep, nextIndex++);
        }
        else
        {
            graphData = data[status.timeStep];
            graphData.updated = true;
        }


        //TREAT DATA
        for(int i = 0; i < status.ids.Count; ++i)
        {
            if(!graphData.tasks.ContainsKey(status.taskNames[i]))
            {
                graphData.tasks[status.taskNames[i]] = 0;
            }

            graphData.tasks[status.taskNames[i]]++;
        }

        data[status.timeStep] = graphData;

        lockToken = false;
    }
}


public class TaskGraphData
{
    public int timestep;
    public int remappedIndex;
    public Dictionary<string, int> tasks = new Dictionary<string, int>();
    public bool updated = false;
    public bool newPoint = true;

    public TaskGraphData(int timestep, int remappedIndex)
    {
        this.timestep = timestep;
        this.remappedIndex = remappedIndex;
    }
}
