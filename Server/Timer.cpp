#include "Timer.h"
#include <unistd.h>
#include <sstream>
#include <iostream>
#include <string>

Timer::Timer()
{
    std::cout << "Set time multiplier: ";
    std::string temp;
    getline(std::cin, temp);
    this->multiplier = atoi(temp.c_str());
}

Timer::~Timer()
{
    this->stop();
}

void Timer::start()
{
    this->year   = 2004;
    this->month  = 12;
    this->day    = 0;
    this->hour   = 0;
    this->minute = 0;

    this->shouldStop = false;
    this->t_tick = new std::thread([this] {tick();});
}

void Timer::stop()
{
    if(this->shouldStop == false)
    {
        this->shouldStop = true;
        this->t_tick->join();
    }
}

std::string Timer::getDate()
{
    std::stringstream ss;
    this->mtx.lock();
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
    this->mtx.unlock();
    return ss.str();
}
std::string Timer::getTime()
{
    std::stringstream ss;
    this->mtx.lock();
    if(this->hour < 10)
    {
        ss << 0;
    }
    ss << this->hour << ".";
    if(this->minute < 10)
    {
        ss << 0;
    }
    ss << this->minute;
    this->mtx.unlock();
    return ss.str();
}

void Timer::tick()
{
    while(!this->shouldStop)
    {
        this->mtx.lock();
        this->minute += 1 * this->multiplier;
        if(this->minute >= 60)
        {
            this->minute = 0;
            this->hour++;
            if(this->hour >= 24)
            {
                this->hour = 0;
                this->day++;
                if(this->day >= 32)
                {
                    this->day = 1;
                    this->month++;
                    if(this->month >= 13)
                    {
                        this->month = 1;
                        this->year++;
                    }
                }
            }
        }
        this->mtx.unlock();
        std::cout << this->getDate()+" "+this->getTime() << '\r' << std::flush;
        sleep(1);
    }
}
