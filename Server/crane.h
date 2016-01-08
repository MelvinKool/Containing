#ifndef CRANE_H
#define CRANE_H

#include "transport.h"
#include "vector3f.h"

class Crane : Transport
{
    public:
        void goTo(vector3f destination);
        void transfer(int containerID,int originID,int destID);
    private:
        double secureTime, unsecureTime, liftTime, lowerTime, transferSpeed;
};

#endif
