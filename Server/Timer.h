#ifndef TIMER_H
#define TIMER_H

#include <string>
#include <thread>
#include <mutex>

class Timer
{
public:
    Timer();
    ~Timer();
    void start();
    std::string getDate();
    std::string getTime();
private:
    int year;
    int month;
    int day;
    int hour;
    int minute;
    std::thread* t_tick;
    bool stop;
    std::mutex mtx;

    void tick();
};

#endif //TIMER_H