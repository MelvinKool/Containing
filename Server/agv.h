#ifndef AGV_H
#define AGV_H

#include "transport.h"
#include "shortestPathDijkstra.h"
#include "JSONGenerator.h"
#include "vector3f.h"
using namespace std;

class AGV : Transport
{
    public:
        AGV(int id,int x,int y,int z);
        void goTo(vector3f destination);
    private:
        ShortestPathDijkstra pathFinder("path to file");
        JSONGenerator JGen;
        int ID;
};

#endif
