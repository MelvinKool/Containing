#ifndef ALLOBJECTS_H
#define ALLOBJECTS_H
#include "agv.h"
#include "crane.h"
class AllObject{
private:
public:
    vector<Crane> freightShipCranes, storageCranes, seaShipCranes, trainCranes, truckCranes;
    vector<AGV> agvs;
private:
    //common object methods here
};
#endif
