#ifndef TIMER_H
#define TIMER_H

#include <functional>
#include <thread>
#include <chrono> //Chrono library is used to deal with date and time

class Timer
{
    std::thread Thread; //creates a variable Thread of type std::thread for asyn execution
    bool Alive = false;
    long CallNumber = -1L;                                                 //-1 is the default value, how many times we want to call
    long repeat_count = -1L;                                               //count the number of time a certain function has been already called
    std::chrono::milliseconds interval = std::chrono::milliseconds(10000); //it will determin the default interval between function call and default is zero
    std::function<void(void)> funct = nullptr;                             //it can contain function, currently it doesnt point anywhere

    void SleepAndRun()
    {
        //sleep_for will block the thread for certain interval
        std::this_thread::sleep_for(interval); //this_thread indicates current thread which created the timer object
        if (Alive)
            Function()(); //the first parenthesis call the function Function and second one will hold the function that Function return
    }

    void ThreadFunc()
    {
        while (Alive)
            SleepAndRun();
    }

public:
    static const long Infinite = -1L;

    Timer() {}

    Timer(const std::function<void(void)> &f) : funct(f) {}

    //   Timer(const std::function<void(void)> &f, const unsigned long &i, const long repeat = Timer::Infinite) : funct(f), interval(std::chrono::milliseconds(i)), CallNumber(repeat) {}

    void Start(bool Async = true)
    {
        if (IsAlive())
            return;
        Alive = true;
        //       repeat_count = CallNumber;
        if (Async)
            Thread = std::thread(ThreadFunc, this);
        else
            this->ThreadFunc();
    }

    void Stop()
    {
        Alive = false;
        Thread.join();
    }

    void SetFunction(const std::function<void(void)> &f)
    {
        funct = f;
    }

    bool IsAlive() const { return Alive; }

    void RepeatCount(const long r)
    {
        if (Alive)
            return;
        CallNumber = r;
    }

    long GetLeftCount() const { return repeat_count; } //repeat_count is used for iteration, RepeatCount just return the current status of repeat_count

    long RepeatCount() const { return CallNumber; } //fetch the total number of instenses

    void SetInterval(const unsigned long &i)
    {
        if (Alive)
            return;
        interval = std::chrono::milliseconds(i);
    }

    unsigned long Interval() const { return interval.count(); } //return the number of interval; .count is used to get the number because by default its not a number

    const std::function<void(void)> &Function() const
    {
        return funct;
    }
};

#endif //TIMER_H