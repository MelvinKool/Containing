#include "crane.h"
#include "server.h"

using namespace std;
Crane::Crane(const char* craneType, int id,float x,float y,float z, float maximumSpeed, Server* ser)
{
    //set movement speeds and secure time?
    currentLocation = vector3f(x,y,z);
    ID = id;
    server = ser;
    vehicleType = string(craneType);
    loaded_Speed = maximumSpeed;
    unloaded_Speed = maximumSpeed;
}

string Crane::transfer(int containerID,int destID)
{
    return server->JGen.craneTransferContainer(ID,containerID,destID);
}

string Crane::transfer(int containerID,int destID,vector3f location)
{
    return server->JGen.craneTransferContainer(ID,containerID,destID,location);
}
