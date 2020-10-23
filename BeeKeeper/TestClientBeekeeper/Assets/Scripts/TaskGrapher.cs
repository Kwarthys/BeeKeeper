using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TaskGrapher : MonoBehaviour
{
    private List<TaskGraphData> data = new List<TaskGraphData>();
    private MyLockedList<UpdateStatus> asyncDataList = new MyLockedList<UpdateStatus>();

    private int nextIndex = 0;

    public MyMesh3DGraph grapher;

    private List<string> allKnownTaskNames = new List<string>();

    private bool initAsked = false;

    public void Update()
    {
        //float startTreat = Time.realtimeSinceStartup;
        while(asyncDataList.tryReadFifo(out UpdateStatus status))
        {
            treatData(status);
        }
        //Debug.Log("TGD treatement took " + (Time.realtimeSinceStartup - startTreat) +"s");

        int index = 0;

        foreach (TaskGraphData tgd in data)
        {
            if (tgd.ready)
            {
                //string[] taskNames = getTaskNames(tgd);
                Vector3[] points = buildPointsWithData(index, tgd);
                for(int i = 0; i < allKnownTaskNames.Count; ++i)
                {
                    //MOVE/SPAWN LEGEND
                    grapher.createPoint(points[i], allKnownTaskNames[i]);
                }

                //updateLegend(index, tgd);

                tgd.ready = false;
            }

            index += tgd.tasks.Values.Count;
        }

        //Debug.Log("TGD update took " + (Time.realtimeSinceStartup - startTreat) + "s");
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

    private void treatData(UpdateStatus status)
    {
        if (initAsked)
        {
            if(status.timeStep > 10)
            {
                Debug.Log("Ejecting T" + status.timeStep);
                return;
            }
            else
            {
                initAsked = false;
            }
        }

        //Debug.Log("TGD recieved: " + status.timeStep);
        
        //Retrieving correct data to update
        TaskGraphData graphData;
        if (data.Count == 0)
        {
            graphData = new TaskGraphData(status.timeStep, nextIndex++);
        }
        else if (data[data.Count - 1].timestep == status.timeStep)
        {
            graphData = data[data.Count - 1];
        }
        else if (data[data.Count - 1].timestep < status.timeStep)
        {
            graphData = new TaskGraphData(status.timeStep, nextIndex++);
            data[data.Count - 1].ready = true;
            fillTheGaps(data[data.Count - 1]);
        }
        else
        {
            Debug.Log("Ignored TS " + status.timeStep + ". Last is " + data[data.Count - 1].timestep + ".");
            return;
        }


        //TREAT DATA
        for (int i = 0; i < status.ids.Count; ++i)
        {
            if (!graphData.tasks.ContainsKey(status.taskNames[i]))
            {
                graphData.tasks[status.taskNames[i]] = 0;
            }

            graphData.tasks[status.taskNames[i]]++;
        }

        data.Add(graphData);
    }

    public void postData(UpdateStatus status)
    {
        asyncDataList.waitAndPost(status);
    }

    private void fillTheGaps(TaskGraphData tgd)
    {
        foreach(string taskName in allKnownTaskNames)
        {
            if(!tgd.tasks.ContainsKey(taskName))
            {
                tgd.tasks[taskName] = 0;
            }
        }
    }

    internal void reinit()
    {
        grapher.reinit();
        nextIndex = 0;
        data.Clear();
        asyncDataList.Clear();
        initAsked = true;
    }
}


public class TaskGraphData
{
    public int timestep;
    public int remappedIndex;
    public Dictionary<string, int> tasks = new Dictionary<string, int>();

    public bool ready = false;

    public TaskGraphData(int timestep, int remappedIndex)
    {
        this.timestep = timestep;
        this.remappedIndex = remappedIndex;
    }
}
