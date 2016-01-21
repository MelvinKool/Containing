#include "crane.h"
#include "server.h"

using namespace std;

Crane::Crane(int id,float x,float y,float z,Server* ser)
{
    currentLocation = vector3f(x,y,z);
    ID = id;
    server = ser;
}

string Crane::transfer(int containerID,int craneID,int destID)
{
    return server->JGen.craneTransferContainer(ID,containerID,vector3f(0,0,0));
}
