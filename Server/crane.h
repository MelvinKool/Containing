#ifndef CRANE_H
#define CRANE_H

#include "transport.h"
#include "vector3f.h"

class Server;

class Crane : Transport
{
    public:
        Crane(){};
        std::string transfer(int containerID,int craneID,std::string craneType,int destID);
    private:
        double secureTime, unsecureTime, liftTime, lowerTime, transferSpeed;
        int ID;
        Server* server;
    public:
        Crane(int id,float x,float y,float z,Server* ser);
};

#endif
