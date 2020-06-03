using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using UnityEngine;

public class CommandInterpreter : MonoBehaviour
{
    public HiveModel model;

    public void postOrder(NetCommand command)
    {
        List<int> ids;
        List<Vector3> ts;
        string[] data = command.data.Split(' ');

        switch (command.command)
        {
            case "AGENTS":
                ts = new List<Vector3>();
                ids = new List<int>();

                for(int i = 0; i < data.Length; i+=3)
                {                    
                    ts.Add(new Vector3(int.Parse(data[i + 1]), int.Parse(data[i + 2]), int.Parse(command.param)));
                    ids.Add(int.Parse(data[i]));
                }
                model.registerOrder(new UpdateOrder(ts, ids));
                break;

            case "FORAGERS":
                ids = new List<int>();
                ts = new List<Vector3>();
                for (int i = 0; i < data.Length; ++i)
                {
                    ts.Add(new Vector3(-1, -1, -1));//foragerCode
                    ids.Add(int.Parse(data[i]));
                }
                model.registerOrder(new UpdateOrder(ts, ids));
                break;

            case "CONTENT":

                ids = new List<int>();
                List<int> codes = new List<int>();
                List<int> quantities = new List<int>();

                for (int i = 0; i < data.Length; i+=3)
                {
                    ids.Add(int.Parse(data[i]));
                    codes.Add(int.Parse(data[i + 1]));
                    quantities.Add(int.Parse(data[i + 2]));
                }
                model.registerContentUpdate(new UpdateContentOrder(int.Parse(command.param), ids, quantities, codes));
                break;
        }
    }
}
