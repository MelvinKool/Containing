#include "agv.h"
#include "server.h"
using namespace std;

AGV::AGV(int id,float x,float y,float z,Server* ser)
{
    currentLocation = vector3f(x,y,z);
    ID = id;
    server = ser;
}

void AGV::goTo(vector3f destination)
{
    vector<vector3f> route;
    if (loaded)
    {
        route = server->pathFinderLoaded.route(currentLocation.toString(),destination.toString()).second;
    }
    else
    {
        route = server->pathFinderUnloaded.route(currentLocation.toString(),destination.toString()).second;
    }
    string jsonCommand = server->JGen.moveTo(ID,route);
    server->writeToSim(jsonCommand);
}
