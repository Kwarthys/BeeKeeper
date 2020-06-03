using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UpdateOrder
{
    public List<Vector3> newTargets;
    public List<int> targetsIDs;

    public UpdateOrder(List<Vector3> newTargets, List<int> targetsIDs)
    {
        this.newTargets = newTargets;
        this.targetsIDs = targetsIDs;
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
