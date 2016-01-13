#ifndef TRANSPORT_H
#define TRANSPORT_H
/*
    Transport:
    class where all transport classes are deriving from.
*/
#include "vector3f.h"

class Transport
{
    public:
        Transport();
    protected:
        vector3f currentLocation = vector3f(0,0,0);
    private:
        int loaded_Speed, unloaded_Speed;
};

#endif //TRANSPORT_H
