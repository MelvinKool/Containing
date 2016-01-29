#ifndef VECTOR3F_H
#define VECTOR3F_H

#include <string>
#include <iostream>

class vector3f
{
    public:
        vector3f(){}
        vector3f(float x,float y, float z);
        vector3f(std::string s);
        float getX();
        float getY();
        float getZ();
        std::string toString();
    private:
        float x,y,z;
};
#endif
