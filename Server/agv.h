#ifndef AGV_H
#define AGV_H

/*
    AGV: Automatic Guided Vehicle
    All AGV's are controlled in this class.
    The AGV gets a replace call, but the AGV is driving itself.
*/
#include "transport.h"

class agv : transport
{
    private:
    public:
        void Transfer(int x, int y);
};

#endif // AGV_H
