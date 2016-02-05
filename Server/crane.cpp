#include "crane.h"
#include "server.h"

using namespace std;
Crane::Crane(const char* craneType, int id,float x,float y,float z, float maximumSpeed, Server* ser)
{
    //set movement speeds and secure time?
    currentLocation = vector3f(x,y,z);
    Id = id;
    server = ser;
    vehicleType = string(craneType);
    loaded_Speed = maximumSpeed;
    unloaded_Speed = maximumSpeed;
}

//Generate JSON-string telling a crane what container to transfer to an AGV
/*
    containerId:    Vector of coordinates of destination
    destId:         Id of destination AGV
*/
string Crane::transfer(int containerId,int destId)
{
    return server->JGen.craneTransferContainer(Id,containerId,destId);
}

//Generate JSON-string telling a crane what container to transfer to a location in storage
/*
    containerId:    Vector of coordinates of destination
    destId:         Id of destination storageLane
    location:       Realtive location in storageLane
*/
string Crane::transfer(int containerId,int destId,vector3f location)
{
    return server->JGen.craneTransferContainer(Id,containerId,destId,location);
}
