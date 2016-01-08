#include "agv.h"

using namespace std;

AGV::AGV(int id,int x,int y,int z)
{
    currentLocation = vector3f(x,y,z);
    ID = id;
}

void AGV::goTo(vector3f destination)
{
    vector<vector3f> route;
    route = pathFinder.route(currentLocation.toString(),destination.toString());
    JGen.moveTo(ID,route);
}
