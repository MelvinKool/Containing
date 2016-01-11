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
    pair<double,vector<vector3f>> route;
    if (loaded)
    {
        route = server->pathFinderLoaded.route(currentLocation.toString(),destination.toString());
    }
    else
    {
        route = server->pathFinderUnloaded.route(currentLocation.toString(),destination.toString());
    }
    string jsonCommand = server->JGen.moveTo(ID,route.second);
    server->writeToSim(jsonCommand);
    bizzy = true;
}

void AGV::arrived()
{
    bizzy = false;
}

vector3f AGV::getCurrentLocation()
{
    return currentLocation;
}

bool AGV::getWorkingState()
{
    return bizzy;
}
