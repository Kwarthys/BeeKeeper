using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TextureBasedFrameBehaviour : MonoBehaviour
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

    public PointCloudReferencer beePointCloud;

    // Start is called before the first frame update
    void Start()
    {
        renderA = faceA.GetComponent<MeshRenderer>();
        renderB = faceB.GetComponent<MeshRenderer>();

        setRandomColors();

        initPos = transform.position;
        initRot = transform.rotation;
    }

    public Transform getStarterOfFace(int combID)
    {
        int faceAID = frameID * 2;
        int faceBID = frameID * 2 + 1;

        if (combID == faceAID) return frameAStart;
        if (combID == faceBID) return frameBStart;
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

    private Texture2D buildTexture(Dictionary<int, Color> cellsColor, bool reverseDrawing = false)
    {
        Texture2D t = new Texture2D(frameSize.x * cellResolution + cellResolution/2, frameSize.y * cellResolution);

        Color background = new Color(0, 0, 0, 0);
        Color[] backgrounds = new Color[t.width * t.height];
        for (int a = 0; a < t.width * t.height; ++a)
        {
            backgrounds[a] = background;
        }

        t.SetPixels(backgrounds);

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

                drawCell(i * cellResolution + offset + cellResolution / 2, j * cellResolution + cellResolution / 2, c, t);
            }
        }

        t.Apply();

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

        renderA.material.mainTexture = buildTexture(cellsColor);
        renderB.material.mainTexture = buildTexture(cellsColor);
    }

    public void setColors(Dictionary<int, Color> colors, int combID)
    {
        getCombRenderer(combID).material.mainTexture = buildTexture(colors, combID%2==1);
    }

    private void drawCell(int centerX, int centerY, Color c, Texture2D t)
    {
        for(int j = 0; j < cellResolution; ++j)
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
}
