using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class TimeWarpFeedbackManager : MonoBehaviour
{
    //public HiveModel model;

    private int firstStepWarp = -1;
    private float timeAtStartWarp = -1;
    private int targetStepWarp = -1;

    private bool warping = false;

    public float refreshRate = 2;
    private float lastRefresh = 0;

    public Image visualFeedback;
    public TextMeshProUGUI text;
    public GameObject visualFeedbackHolder;

    private void Start()
    {
        text.text = "";
        visualFeedbackHolder.SetActive(false);
    }

    public void warpstarted(int step, int targetStep)
    {
        firstStepWarp = step;
        timeAtStartWarp = Time.realtimeSinceStartup;
        lastRefresh = Time.realtimeSinceStartup;

        targetStepWarp = targetStep;

        warping = true;

        visualFeedbackHolder.SetActive(true);
    }

    public void notifyNewTSRecieved(int currentTS)
    {
        if(warping)
        {
            int warped = currentTS - firstStepWarp;
            int warpTotal = targetStepWarp - firstStepWarp;

            float advance = warped * 1.0f / warpTotal;

            visualFeedback.fillAmount = 1 - advance;

            if (Time.realtimeSinceStartup - lastRefresh > refreshRate)
            {
                lastRefresh = Time.realtimeSinceStartup;

                float timeTakenPerTS = (Time.realtimeSinceStartup - timeAtStartWarp) / warped;
                float timeEstimate = (warpTotal - warped) * timeTakenPerTS;

                text.text = (int)(timeEstimate) + " s.";
            }

            if(advance > 1)
            {
                warping = false;
                text.text = "";
                visualFeedbackHolder.SetActive(false);
            }
        }
    }
}
