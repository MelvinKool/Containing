#ifndef ALLOBJECTS_H
#define ALLOBJECTS_H
#include "agv.h"
#include "crane.h"
#include <vector>
#include "vector3f.h"

class AllObjects
{
public:
    std::vector<Crane> freightShipCranes, storageCranes, seaShipCranes, trainCranes, truckCranes;
    //std::vector<Crane> allCranes;
    std::vector<AGV> agvs;
    vector3f parkingSpots[540];
private:
    //common object methods here
};
#endif
