#ifndef CRANE_H
#define CRANE_H

#include "transport.h"

class Crane : Transport
{
    public:
    private:
        double secureTime, unsecureTime, liftTime, lowerTime, transferSpeed;
};

#endif //CRANE_H
