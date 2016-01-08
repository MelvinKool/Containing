#include "crane.h"
#include "server.h"

Crane::Crane(int id,float x,float y,float z,Server* ser)
{
    currentLocation = vector3f(x,y,z);
    ID = id;
    server = ser;
}
void Crane::goTo(vector3f destination)
{
    vector<vector3f> dest;
    dest.push_back(destination);
    string jsonCommand = server->JGen.moveTo(ID,dest);
    server->writeToSim(jsonCommand);
}

void Crane::transfer(int containerID,int originID,int destID)
{
    string jsonCommand = server->JGen.transferContainer(containerID,originID,destID);
    server->writeToSim(jsonCommand);
}
