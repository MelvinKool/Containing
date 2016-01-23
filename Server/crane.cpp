#include "crane.h"
#include "server.h"

using namespace std;

Crane::Crane(int id,float x,float y,float z,Server* ser)
{
    //set movement speeds?
    currentLocation = vector3f(x,y,z);
    ID = id;
    server = ser;
}

string Crane::transfer(int containerID,int destID)
{
    return "";
    //!!!!!!!!!!!!
    //return server->JGen.craneTransferContainer(containerID,ID,destID);
}
