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

string vector3f::toString()
{
    string x = to_string(getX());
    string y = to_string(getY());
    string z = to_string(getZ());
    string vector3fString = x.substr(0,(x.length()-4)) + "," + y.substr(0,(y.length()-4)) + ","+ z.substr(0,(z.length()-4));
    return vector3fString;
}
