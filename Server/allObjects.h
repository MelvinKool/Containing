#ifndef ALLOBJECTS_H
#define ALLOBJECTS_H
#include "agv.h"
#include "crane.h"
#include <vector>

class AllObjects
{
private:
public:
    //std::vector<Crane> freightShipCranes, storageCranes, seaShipCranes, trainCranes, truckCranes;
    std::vector<Crane> allCranes;
    std::vector<AGV> agvs;
private:
    //common object methods here
};
#endif
