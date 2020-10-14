using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerMouseLook : MonoBehaviour
{
    public float mouseSensitivity = 100f;

    public Transform body;

    float xRotation = 0f;
    float yRotation = 0f;

    public bool limitYRotation;
    public Vector2 limitations;

    private bool canRotate = true;


    void Start()
    {
        Cursor.lockState = CursorLockMode.Locked;
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.LeftControl))
        {
            Cursor.lockState = CursorLockMode.None;
            canRotate = false;
        }

        if (Input.GetKeyUp(KeyCode.LeftControl))
        {
            Cursor.lockState = CursorLockMode.Locked;
            canRotate = true;
        }

        if(!canRotate)
        {
            return;
        }

        float mouseX = Input.GetAxis("Mouse X") * mouseSensitivity * Time.deltaTime;
        float mouseY = Input.GetAxis("Mouse Y") * mouseSensitivity * Time.deltaTime;

        xRotation -= mouseY;
        xRotation = Mathf.Clamp(xRotation, -70f, 70f);

        yRotation += mouseX;

        if(limitYRotation)
        {
            yRotation = Mathf.Clamp(yRotation, limitations.x, limitations.y);
        }

        transform.localRotation = Quaternion.Euler(xRotation, 0, 0);
        body.localRotation = Quaternion.Euler(0, yRotation, 0);
    }
}
