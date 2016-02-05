#include "vector3f.h"
#include <string>
#include <iostream>
#include <vector>
#include <sstream>

using namespace std;

vector3f::vector3f(float x, float y, float z)
{
    this->x = x;
    this->y = y;
    this->z = z;
}

vector3f::vector3f(string s)
{
    //split string to x,y,z and call this constructor
    stringstream sSplitStream(s);
    string temp;
    string coords[3];
    for(int i = 0; i < 3; i++)
    {
        getline(sSplitStream,temp,',');
        coords[i] = temp;
    }
    string::size_type sz;
    float x = stof(coords[0],&sz);
    float y = stof(coords[1],&sz);
    float z = stof(coords[2],&sz);
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