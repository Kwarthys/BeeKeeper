using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UIManager : MonoBehaviour
{
    public GameObject FFWDScreen;

    // Start is called before the first frame update
    void Start()
    {
        FFWDScreen.SetActive(false);
    }

    // Update is called once per frame
    void Update()
    {
        if(Input.GetKeyDown(KeyCode.LeftControl))
        {
            FFWDScreen.SetActive(true);
        }
        else if(Input.GetKeyUp(KeyCode.LeftControl))
        {
            FFWDScreen.SetActive(false);
        }
    }
}
