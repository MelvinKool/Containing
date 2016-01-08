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
        vector3f currentLocation;
    private:
        int loaded_Speed, unloaded_Speed;
};

#endif //TRANSPORT_H
