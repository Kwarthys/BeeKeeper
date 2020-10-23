using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class IDManager
{
    private List<int> freedIds = new List<int>();

    private int currentMaxIndex = 0;

    public void freeIndex(int index)
    {
        freedIds.Add(index);
    }

    public int getNextFreeIndex()
    {
        if(freedIds.Count > 0)
        {
            int id = freedIds[0];
            freedIds.RemoveAt(0);
            return id;
        }

        return currentMaxIndex++;
    }

    public void reinit()
    {
        currentMaxIndex = 0;
        freedIds.Clear();
    }
}
