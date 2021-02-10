using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using UnityEngine;

public class CommandInterpreter : MonoBehaviour
{
    public HiveModel model;

    public FrameManager frameManager;

    public void postOrder(NetCommand command)
    {
        List<int> ids;
        List<Vector3> ts;
        string[] data = command.data.Split(' ');

        switch (command.command)
        {
            case "Restarted":
            case "Started":
                int[] parsedArray = new int[data.Length];
                for (int i = 0; i < data.Length; ++i)
                {
                    parsedArray[i] = int.Parse(data[i]);
                }
                frameManager.registerNewInit(parsedArray);
                break;

            case "AGENTS":
                ts = new List<Vector3>();
                ids = new List<int>();

                for(int i = 0; i < data.Length; i+=3)
                {                    
                    ts.Add(new Vector3(int.Parse(data[i + 1]), int.Parse(data[i + 2]), int.Parse(command.param)));
                    ids.Add(int.Parse(data[i]));
                }
                model.registerOrder(new UpdateOrder(ts, ids));
                //Debug.Log("recieved agent update");
                break;

            case "FORAGERS":
                ids = new List<int>();
                ts = new List<Vector3>();
                for (int i = 0; i < data.Length; ++i)
                {
                    ts.Add(new Vector3(0,0, -1));//foragerCode
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

            case "STATES":
                ids = new List<int>();
                List<int> ages = new List<int>();
                List<float> jhAmounts = new List<float>();
                List<string> taskNames = new List<string>();

                for (int i = 0; i < data.Length; i+=4)
                {
                    ids.Add(int.Parse(data[i]));
                    ages.Add(int.Parse(data[i + 1]));
                    jhAmounts.Add(float.Parse(data[i + 2], CultureInfo.InvariantCulture));
                    taskNames.Add(data[i + 3]);
                }
                model.registerStatusUpdate(new UpdateStatus(int.Parse(command.param),  ids, jhAmounts, taskNames, ages));
                break;

            case "CONTACTS":
                ids = new List<int>();
                List<float> amounts = new List<float>();

                for (int i = 0; i < data.Length; i+=2)
                {
                    ids.Add(int.Parse(data[i]));
                    amounts.Add(float.Parse(data[i+1]));
                    //if (i%200 == 0) Debug.Log(float.Parse(data[i + 1]));
                }

                model.registerContactUpdate(new UpdateContactOrder(ids, amounts));

                break;

            case "DEATHS":
                ids = new List<int>();

                if(int.Parse(command.param) != 0)
                {
                    for (int i = 0; i < data.Length; i ++)
                    {
                        ids.Add(int.Parse(data[i]));
                    }

                    model.registerDeathList(ids);
                }

                break;

            default:
                Debug.Log("NaC::SERVER MESSAGE : " + command.command + " " + command.param + " " + command.data);
                break;
        }
    }
}
