#ifndef TIMER_H
#define TIMER_H

#include <string>
#include <thread>

class Timer
{
public:
    Timer();
    ~Timer();
    void start();
    std::string getDate();
    std::string getTime();
private:
    //yyyy-mm-dd
    //hh:mm
    int year;
    int month;
    int day;
    int hour;
    int minute;
    std::thread* t_tick;
    bool stop;

    void tick();
};

#endif //TIMER_H