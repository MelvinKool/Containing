#ifndef AGV_H
#define AGV_H

#include "transport.h"
#include "shortestPathDijkstra.h"
#include "vector3f.h"

class Server;

class AGV : Transport
{
    public:
        AGV(){};
        void goTo(vector3f destination);
        vector3f getCurrentLocation();
        bool getWorkingState();
        void arrived();
    private:
        Server* server;
        int ID;
        bool loaded = false,bizzy = false;
    public:
        AGV(int id,float x,float y,float z,Server* ser);
};

#endif
