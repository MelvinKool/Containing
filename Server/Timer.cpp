#include "Timer.h"
#include <unistd.h>
#include <mutex>
#include <sstream>

#include <iostream>//remove

Timer::Timer()
{
    this->year   = 2004;
    this->month  = 12;
    this->day    = 1;
    this->hour   = 0;
    this->minute = 0;

    this->stop = false;
}

Timer::~Timer()
{
    this->stop = true;
    this->t_tick->join();
}

void Timer::start()
{
    this->t_tick = new std::thread([this] {tick();});
}

std::string Timer::getDate()
{
    std::stringstream ss;
    ss << this->year << "-";
    if(this->month < 10)
    {
        ss << 0;
    }
    ss << this->month << "-";
    if(this->day < 10)
    {
        ss << 0;
    }
    ss << this->day;
    return ss.str();
}
std::string Timer::getTime()
{
    std::stringstream ss;
    if(this->hour < 10)
    {
        ss << 0;
    }
    ss << this->hour << ":";
    if(this->minute < 10)
    {
        ss << 0;
    }
    ss << this->minute;
    return ss.str();
}

void Timer::tick()
{
    std::mutex mtx;
    while(!this->stop)
    {
        mtx.lock();
        this->minute++;
        if(this->minute == 60)
        {
            this->minute = 0;
            this->hour++;
            if(this->hour == 24)
            {
                this->hour = 0;
                this->day++;
                if(this->day == 32)
                {
                    this->day = 1;
                    this->month++;
                    if(this->month == 13)
                    {
                        this->month = 1;
                        this->year++;
                    }
                }
            }
        }
        mtx.unlock();
        sleep(1);
    }
}