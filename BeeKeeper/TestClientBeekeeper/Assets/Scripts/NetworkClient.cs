using System.Collections.Generic;
using System.Globalization;
using System.Threading;
using System.Net;
using System;
using System.Net.Sockets;
using System.Text;
using UnityEngine;
using Unity.Jobs;
using Unity.Collections;

public class NetworkClient : MonoBehaviour
{
    public CommandInterpreter commander;

    IPEndPoint ip;
    Socket s = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
    UdpClient udpC;

    private List<Vector3> targets = new List<Vector3>();

    /*
    NativeArray<bool> flag;
    NativeArray<bool> recieverRun;
    NativeList<float> targets;
    
    JobHandle recieverHandle;
    */

    void Start()
    {
        Debug.Log("Trying to connect TCP");

        s.Connect("127.0.0.1", 4241);

        Debug.Log("TCP Connected");

        s.Send(Encoding.Default.GetBytes("STARTUDP"));

        Debug.Log("Starting UDP reader thread");
        Thread t = new Thread(readUDP);
        t.Start();

        Debug.Log("Starting TCP reader thread");
        Thread ttcp = new Thread(readTCP);
        ttcp.Start();
    }

    public void sendTCP(string request)
    {
        s.Send(Encoding.Default.GetBytes(request));
    }

    private void parseModel(string rawData)
    {
        char[] separator = " ".ToCharArray();
        string[] data = rawData.Split(separator, 3);

        NetCommand c = new NetCommand();
        c.command = data[0];
        c.param = data[1];
        c.data = data[2];

        //Debug.Log("New Command : c:" + c.command + ".p:" + c.param + ".d:" + c.data);

        commander.postOrder(c);

        //Debug.Log("Parse took " + (DateTime.Now - t));

        //pointCloudReferencer.registerOrder(new UpdateOrder(startingIndex, targets));
    }


    public void OnApplicationQuit()
    {
        //recieverRun[0] = false;
        s.Send(Encoding.Default.GetBytes("CLOSE"));
        //targets.Dispose();
        //flag.Dispose();
    }

    private void readTCP()
    {
        while(true)
        {
            byte[] rawData = new byte[1024];
            s.Receive(rawData);
            string data = Encoding.Default.GetString(rawData);
            Debug.Log("fromTCP : " + data.Length + " : " + data);            
        }
    }

    private void readUDP()
    {
        Debug.Log("Starting job: Connceting UDP");

        ip = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 4244);
        udpC = new UdpClient(ip);

        Debug.Log("UDP Connected");

        while (true)
        {
            //Debug.Log("Waiting UDP");
            byte[] rawData = udpC.Receive(ref ip);
            string data = Encoding.Default.GetString(rawData);
            parseModel(data);
        }
    }
}
