using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class VRInteractionProfileManager : MonoBehaviour
{
    public static int FRAME_PROFILE = 0;
    public static int GRAPH_PROFILE = 1;
    public static int TIME_PROFILE = 2;

    public List<GameObject> TimeProfileSpecific = new List<GameObject>();
    public List<GameObject> FrameProfileSpecific = new List<GameObject>();

    public TextMeshProUGUI displayText;

    private List<Profile> profiles = new List<Profile>();

    public VRGetInput myInput;

    public Profile activeProfile { get; private set; }

    // Start is called before the first frame update
    void Start()
    {
        profiles.Add(new Profile(FRAME_PROFILE, "Frame Manipulation"));
        profiles.Add(new Profile(GRAPH_PROFILE, "3D Graph Manipulation"));
        profiles.Add(new Profile(TIME_PROFILE, "Time Manipulation"));

        activeProfile = profiles[0];
        activateProfile(0);
        deactivateProfile(1);
        deactivateProfile(2);

        displayText.text = activeProfile.displayName;
    }

    // Update is called once per frame
    void Update()
    {
        if(myInput.getMenuButtonDown())
        {
            deactivateProfile(activeProfile.index);

            activeProfile = profiles[(activeProfile.index + 1) % profiles.Count];
            Debug.Log("Changing mode to " + activeProfile.displayName);

            activateProfile(activeProfile.index);

            displayText.text = activeProfile.displayName;
        }
    }

    private void activateProfile(int profileIndex)
    {
        if(profileIndex == TIME_PROFILE)
        {
            foreach(GameObject g in TimeProfileSpecific)
            {
                g.SetActive(true);
            }
        }
        else if (profileIndex == FRAME_PROFILE)
        {
            foreach (GameObject g in FrameProfileSpecific)
            {
                g.SetActive(true);
            }
        }
    }

    private void deactivateProfile(int profileIndex)
    {
        if (profileIndex == TIME_PROFILE)
        {
            foreach (GameObject g in TimeProfileSpecific)
            {
                g.SetActive(false);
            }
        }
        else if (profileIndex == FRAME_PROFILE)
        {
            foreach (GameObject g in FrameProfileSpecific)
            {
                g.SetActive(false);
            }
        }
    }
}

public struct Profile
{
    public int index;
    public string displayName;
    
    public Profile(int index, string displayName)
    {
        this.index = index;
        this.displayName = displayName;
    } 
}
