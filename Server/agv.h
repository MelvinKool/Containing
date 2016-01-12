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
        std::string goTo(vector3f destination);
        vector3f getCurrentLocation();
        AGV(int id,float x,float y,float z,Server* ser);
    private:
        Server* server;
        int ID;
        bool loaded = false;
};

#endif
