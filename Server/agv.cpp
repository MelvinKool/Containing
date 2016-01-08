#include "agv.h"
#include "server.h"
using namespace std;

AGV::AGV(int id,int x,int y,int z,Server* ser)
{
    currentLocation = vector3f(x,y,z);
    ID = id;
    server = ser;
    char* path = "path to file";
    pathFinder(path);
    delete []path;
}

void AGV::goTo(vector3f destination)
{
    vector<vector3f> route;
    route = pathFinder.route(currentLocation.toString(),destination.toString()).second;
    string jsonCommand = JGen.moveTo(ID,route);
    server->writeToSim(jsonCommand);
}
