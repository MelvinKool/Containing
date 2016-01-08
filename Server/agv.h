#ifndef AGV_H
#define AGV_H

#include "transport.h"
#include "shortestPathDijkstra.h"
#include "JSONGenerator.h"
#include "vector3f.h"
//typedef class Server Server;

//#include "server.h"
class Server;

class AGV : Transport
{
    public:
        AGV(){};

        void goTo(vector3f destination);
    private:
        ShortestPathDijkstra pathFinder;
        JSONGenerator JGen;
        Server* server;
        int ID;
    public:
        AGV(int id,int x,int y,int z,Server* ser);
};

#endif
