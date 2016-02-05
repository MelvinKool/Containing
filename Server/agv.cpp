#include "agv.h"
#include "server.h"
using namespace std;

AGV::AGV(int id,float x,float y,float z,Server* ser)
{
    loaded_Speed = 20;
    unloaded_Speed = 40;
    vehicleType = "AGV";
    currentLocation = vector3f(x,y,z);
    Id = id;
    server = ser;
}

//Generate JSON-string telling an AGV how to get to its destination
/*
    destination:    Vector of coordinates of destination
    loaded:	        Tell if AGV has a container or not, while riding this route
    contID:         ID of container to pick up(-1 if not picking up container)
*/
string AGV::goTo(vector3f destination,bool loaded,int containerId)
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
    currentLocation = destination;
    return server->JGen.moveTo(Id,route.second,route.first,containerId);
}

//returns AGV current location
vector3f AGV::getCurrentLocation()
{
    return currentLocation;
}
