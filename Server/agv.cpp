#include "agv.h"
#include "server.h"
using namespace std;

AGV::AGV(int id,float x,float y,float z,Server* ser)
{
    currentLocation = vector3f(x,y,z);
    ID = id;
    server = ser;
}

string AGV::goTo(vector3f destination,vector3f lastKnowPointShortestPath,bool loaded)
{
    pair<double,vector<vector3f>> route;
    if (loaded)
    {
        route = server->pathFinderLoaded.route(currentLocation.toString(),lastKnowPointShortestPath.toString());
    }
    else
    {
         route = server->pathFinderUnloaded.route(currentLocation.toString(),lastKnowPointShortestPath.toString());
    }
    route.second.push_back(destination);
    return server->JGen.moveTo(ID,route.second,route.first);
}

vector3f AGV::getCurrentLocation()
{
    return currentLocation;
}
