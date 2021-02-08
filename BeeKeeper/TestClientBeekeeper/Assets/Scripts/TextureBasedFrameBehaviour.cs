using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Profiling;

public class TextureBasedFrameBehaviour : Interactible
{
    public GameObject faceA;
    public GameObject faceB;

    private MeshRenderer renderA;
    private MeshRenderer renderB;

    public Transform frameAStart;
    public Transform frameBStart;

    public int frameID = 0;

    public int cellResolution = 10;

    public Vector3 initPos;
    public Quaternion initRot;

    public Vector2Int frameSize = new Vector2Int(78, 50);

    public bool isUp = false;

    public bool isReversed = false;

    public PointCloudReferencer beePointCloud;

    private Dictionary<int, Color> cellsColorMemoryA = new Dictionary<int, Color>();
    private Dictionary<int, Color> cellsColorMemoryB = new Dictionary<int, Color>();

    // Start is called before the first frame update
    void Start()
    {
        renderA = faceA.GetComponent<MeshRenderer>();
        renderB = faceB.GetComponent<MeshRenderer>();

        createTextures();

        setRandomColors();

        initPos = transform.position;
        initRot = transform.rotation;
    }

    private void createTextures()
    {
        Texture2D t = new Texture2D(frameSize.x * cellResolution + cellResolution / 2, frameSize.y * cellResolution);

        Color background = new Color(0, 0, 0, 0);
        Color[] backgrounds = new Color[t.width * t.height];
        for (int a = 0; a < t.width * t.height; ++a)
        {
            backgrounds[a] = background;
        }

        t.SetPixels(backgrounds);
        renderA.material.mainTexture = t;

        t = new Texture2D(frameSize.x * cellResolution + cellResolution / 2, frameSize.y * cellResolution);
        t.SetPixels(backgrounds);
        renderB.material.mainTexture = t;
    }

    public Transform getStarterOfFace(int combID)
    {
        int faceAID = frameID * 2;
        int faceBID = frameID * 2 + 1;

        if (combID == faceAID) return frameAStart;
        if (combID == faceBID) return frameBStart;
        else return null;
    }

    public Dictionary<int, Color> getCombColorsMemory(int combID)
    {
        int faceAID = frameID * 2;
        int faceBID = frameID * 2 + 1;

        if (combID == faceAID) return cellsColorMemoryA;
        if (combID == faceBID) return cellsColorMemoryB;
        else return null;
    }

    private MeshRenderer getCombRenderer(int combID)
    {
        int faceAID = frameID * 2;
        int faceBID = frameID * 2 + 1;

        if (combID == faceAID) return renderA;
        if (combID == faceBID) return renderB;
        else return null;
    }

    private Texture2D updateTexture(Dictionary<int, Color> cellsColor, Texture2D t, Dictionary<int, Color> combColorsMemory = null, bool reverseDrawing = false)
    {
        bool changed = false;

        for (int j = 0; j < frameSize.y; ++j)
        {
            for (int i = 0; i < frameSize.x; ++i)
            {
                int offset = j % 2 == 0 ? cellResolution / 2 : 0;

                if (reverseDrawing) offset = j % 2 == 0 ? 0 : cellResolution / 2;

                int cellx = reverseDrawing ? frameSize.x - 1 - i: i;
                int celly = frameSize.y - 1 - j;

                int cellIndex = celly * frameSize.x + cellx;

                Color c = Color.gray;

                if (cellsColor.ContainsKey(cellIndex))
                {
                    c = cellsColor[cellIndex];
                }

                bool redrawNeeded = true;

                if(combColorsMemory != null)
                {
                    if(combColorsMemory.ContainsKey(cellIndex))
                    {
                        if(c == combColorsMemory[cellIndex])
                        {
                            redrawNeeded = false;
                        }
                    }
                }

                if(redrawNeeded)
                {
                    drawCell(i * cellResolution + offset + cellResolution / 2, j * cellResolution + cellResolution / 2, c, t);

                    if(combColorsMemory != null)
                        combColorsMemory[cellIndex] = c;

                    changed = true;
                }
            }
        }

        if (changed)
        {
            t.Apply();
        }

        return t;
    }

    private void setRandomColors()
    {
        Dictionary<int, Color> cellsColor = new Dictionary<int, Color>();

        for (int j = 0; j < frameSize.y; ++j)
        {
            for (int i = 0; i < frameSize.x; ++i)
            {
                cellsColor.Add(j * frameSize.x + i, Random.value > 0.5f ? Color.yellow : Color.gray);
            }
        }

        updateTexture(cellsColor, (Texture2D)renderA.material.mainTexture);
        updateTexture(cellsColor, (Texture2D)renderB.material.mainTexture, null, true);
    }

    public void setColors(Dictionary<int, Color> colors, int combID)
    {
        Profiler.BeginSample("TextureBasedFrameRecolor");
        updateTexture(colors, (Texture2D)getCombRenderer(combID).material.mainTexture, getCombColorsMemory(combID), combID % 2==1);
        Profiler.EndSample();
    }

    private void drawCell(int centerX, int centerY, Color c, Texture2D t)
    {
        for (int j = 0; j < cellResolution; ++j)
        {
            for (int i = 0; i < cellResolution; ++i)
            {
                int x = centerX - cellResolution / 2 + i;
                int y = centerY - cellResolution / 2 + j;

                if(Mathf.Sqrt((centerX - x)* (centerX - x) + (centerY - y)* (centerY - y)) < cellResolution / 2)
                {
                    //INSIDE CELL
                    t.SetPixel(x, y, c);
                }
                else
                {
                    //OUTSIDE CELL
                    t.SetPixel(x, y, new Color(0, 0, 0, 0));
                }
            }
        }
    }

    public override void interact(PlayerRayCastExecute executor)
    {
        executor.grab(transform);
    }
}
