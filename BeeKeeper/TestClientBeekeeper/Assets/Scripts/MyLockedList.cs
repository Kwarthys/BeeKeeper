using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MyLockedList<T> : List<T>
{
    private volatile bool token = false;

    public bool rejectAll = false;
    
    public void waitAndPost(T item)
    {
        while (token)
        {
            Debug.Log("post Thread Locked");
        }
        token = true;
        Add(item);
        token = false;
    }

    public bool tryPost(T item)
    {
        if(token)
        {
            return false;
        }
        else
        {
            token = true;
            Add(item);
            token = false;
            return true;
        }
    }

    public bool tryReadFifo(out T t)
    {
        if(token || Count == 0)
        {
            t = default;
            return false;
        }
        else
        {
            token = true;
            t = this[0];
            Remove(t);
            token = false;
            return true;
        }
    }
}
