using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SimpleFramePositionner : MonoBehaviour
{
    public GameObject framePlaceHolderPrefab;
    private Transform instanciatedPlaceHolder;

    public float frameIntervals = -.1f;

    public int maxNumberOfFrames = 10;

    public float scoutDistance = 3;

    private int instanciatedFrame = 0;

    private int[] framesIndex;
}
