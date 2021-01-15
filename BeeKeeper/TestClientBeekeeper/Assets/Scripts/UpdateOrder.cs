using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UpdateOrder
{
    public List<Vector3> newTargets;
    public List<int> targetsIDs;
    public List<Color> colors;

    public UpdateOrder(List<Vector3> newTargets, List<int> targetsIDs, List<Color> colors = null)
    {
        this.newTargets = newTargets;
        this.targetsIDs = targetsIDs;
        
        if (colors != null) this.colors = colors;
        else
        {
            colors = new List<Color>();
            for(int i = 0; i < newTargets.Count; ++i)
            {
                colors.Add(Color.black);
            }
        }
    }

    public UpdateOrder()
    {
        this.newTargets = new List<Vector3>();
        this.targetsIDs = new List<int>();
        this.colors = new List<Color>();
    }

    public void add(Vector3 target, int ID, Color? color = null)
    {
        newTargets.Add(target);
        targetsIDs.Add(ID);

        colors.Add(color ?? Color.black);        
    }
}

public class UpdateContactOrder
{
    public List<int> ids;
    public List<float> amounts;

    public UpdateContactOrder(List<int> ids, List<float> amounts)
    {
        this.ids = ids;
        this.amounts = amounts;
    }
}

public class UpdateContentOrder
{
    public int combID;

    public List<int> cellIDs;
    public List<int> cellQuantities;
    public List<int> cellCodes;

    public UpdateContentOrder(int combID, List<int> ids, List<int> quantities, List<int> codes)
    {
        this.combID = combID;
        cellIDs = ids;
        cellQuantities = quantities;
        cellCodes = codes;
    }
}

public class UpdateStatus
{
    public List<int> ids;
    public List<float> jhAmounts;
    public List<string> taskNames;
    public List<int> ages;

    public int timeStep;

    public UpdateStatus(int timeStep, List<int> ids, List<float> JHAmounts, List<string> taskName, List<int> ages)
    {
        this.ids = ids;
        this.jhAmounts = JHAmounts;
        this.taskNames = taskName;
        this.ages = ages;

        this.timeStep = timeStep;
    }
}

public class UpdateContent
{
    public int combID;

    //public List<int> cellIDs;
    //public List<Color> cellColors;

    public Dictionary<int, Color> colorsOfCellIDs;

    public UpdateContent(int combID, Dictionary<int, Color> colorsOfCellIDs)
    {
        this.combID = combID;
        this.colorsOfCellIDs = colorsOfCellIDs;
    }
}
