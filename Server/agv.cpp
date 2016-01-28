#include "agv.h"
#include "server.h"
using namespace std;

AGV::AGV(int id,float x,float y,float z,Server* ser)
{
    loaded_Speed = 20;
    unloaded_Speed = 40;
    vehicleType = "AGV";
    currentLocation = vector3f(x,y,z);
    ID = id;
    server = ser;
}

string AGV::goTo(vector3f destination,bool loaded)
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
    cout << route.second.size() << endl;
    return server->JGen.moveTo(ID,route.second,route.first);
}

vector3f AGV::getCurrentLocation()
{
    return currentLocation;
}
