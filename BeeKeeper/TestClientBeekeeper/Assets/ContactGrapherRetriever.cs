using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ContactGrapherRetriever : MonoBehaviour
{
    public CommandSender sender;

    public float coolDown = 5f;
    private float lastTime;
    // Start is called before the first frame update
    void Start()
    {
        lastTime = -coolDown;
    }

    // Update is called once per frame
    void Update()
    {
        if(Time.realtimeSinceStartup - lastTime > coolDown)
        {
            sender.askForContacts();
            lastTime = Time.realtimeSinceStartup;
            Debug.Log("ASking contacts");
        }
    }
}
