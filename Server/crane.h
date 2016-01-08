#ifndef CRANE_H
#define CRANE_H

#include "transport.h"
#include "vector3f.h"

class Server;

class Crane : Transport
{
    public:
        Crane(){};
        void goTo(vector3f destination);
        void transfer(int containerID,int originID,int destID);
    private:
        double secureTime, unsecureTime, liftTime, lowerTime, transferSpeed;
        int ID;
        Server* server;
    public:
        Crane(int id,float x,float y,float z,Server* ser);
};

#endif
