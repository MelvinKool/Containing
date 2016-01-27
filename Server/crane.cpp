#include "crane.h"
#include "server.h"

using namespace std;
Crane::Crane(const char* craneType, int id,float x,float y,float z,Server* ser)
{
    //set movement speeds and secure time?
    currentLocation = vector3f(x,y,z);
    ID = id;
    server = ser;
    vehicleType = string(craneType);
}

string Crane::transfer(int containerID,int destID)
{
    return server->JGen.craneTransferContainer(ID,containerID,destID);
}

string Crane::transfer(int containerID,int destID,vector3f location)
{
    return server->JGen.craneTransferContainer(ID,containerID,destID,location);
}
