#ifndef CRANE_H
#define CRANE_H

#include "transport.h"
#include "vector3f.h"

class Server;

class Crane : public Transport
{
    public:
        Crane(){};
        std::string transfer(int containerID,int destID);
        std::string transfer(int containerID,int destID,vector3f location);
        struct grabber {
            //grabber() : () {}
            float holderSpeed = 0, speed = 0, y_offset = 0;
            bool has_holder = false;
            vector3f position = vector3f(0,0,0);
        } currentGrabber;
    private:
        double secureTime, unsecureTime, liftTime, lowerTime, transferSpeed;
        Server* server;
    public:
        Crane(const char* craneType, int id,float x,float y,float z, float maximumSpeed, Server* ser);
};

#endif
