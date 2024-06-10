#ifndef HELPER_H
#define HELPER_H

#include <ctime>
#include <string>
#include <sstream> //converting various type to string
#include <fstream>

namespace Helper
{
    // https://www.geeksforgeeks.org/templates-cpp/
    template <class T>

    std::string ToString(const T &);

    struct DateTime
    {
        DateTime()
        {
            time_t ms; //located in ctime to store time
            time(&ms); //function which takes system time and place it in ms

            struct tm *info = localtime(&ms);

            D = info->tm_mday; //represent Day
            m = info->tm_mon + 1;
            y = 1900 + info->tm_year; // 1900 is for refference
            M = info->tm_min;
            H = info->tm_hour;
            S = info->tm_sec;
        }

        DateTime(int D, int m, int y, int H, int M, int S) : D(D), m(m), y(y), H(H), M(M), S(S) {}
        DateTime(int D, int m, int y) : D(D), m(m), y(y), H(0), M(0), S(0) {}

        DateTime Now() const
        {
            return DateTime();
        }
        int D, m, y, H, M, S;

        std::string GetDateString() const
        {
            return std::string(D < 10 ? "0" : "") + ToString(D) + std::string(m < 10 ? ".0" : ".") + ToString(m) + "." + ToString(y);
        }

        std::string GetTimeString(const std::string &sep = ":") const
        {
            return std::string(H < 10 ? "0" : "") + ToString(H) + sep +
                   std::string(M < 10 ? "0" : "") + ToString(M) + sep +
                   std::string(S < 10 ? sep : "") + ToString(S);
        }

        std::string GetDateTimeString(const std::string &sep = ":") const
        {
            return GetDateString() + " " + GetTimeString(sep);
        }
    };

    template <class T>

    std::string ToString(const T &e)
    {
        std::ostringstream s;
        s << e;
        return s.str();
    }

    //For debuging --mail
    void WriteAppLog(const std::string &s)
    {

        std::ofstream file("AppLog.txt", std::ios::app); //app stands for append

        file << "[" << Helper::DateTime().GetDateTimeString() << "]"
             << "\n"
             << s << std::endl
             << "\n";
        file.close();
    }
}

#endif //HELPER_H