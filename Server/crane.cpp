#include "crane.h"

void Crane::goTo(vector3f destination)
{
    JSONGenerator.moveTo(ID,currentLocation,destination);
}

void Crane::transfer(int containerID,int originID,int destID)
{

}
