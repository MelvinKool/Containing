#ifndef TRANSPORT_H
#define TRANSPORT_H

#include <string>
#include "vector3f.h"

/*
    Transport:
    class where all transport classes are deriving from.
*/
class Transport
{
public:
    Transport();
    std::string vehicleType;
    vector3f currentLocation = vector3f(0,0,0);
    int loaded_Speed, unloaded_Speed,Id;
};

#endif //TRANSPORT_H