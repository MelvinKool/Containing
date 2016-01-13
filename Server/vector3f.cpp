#include "vector3f.h"
#include <string>
#include <iostream>

using namespace std;

vector3f::vector3f(float x, float y, float z)
{
    this->x = x;
    this->y = y;
    this->z = z;
}
float vector3f::getX()
{
    return x;
}

float vector3f::getY()
{
    return y;
}

float vector3f::getZ()
{
    return z;
}

string vector3f::toString(){
    string vector3fString = to_string(getX()) + "," + to_string(getY()) + ","+ to_string(getZ());
    return vector3fString;
}
