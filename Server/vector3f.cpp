#include "vector3f.h"

vector3f::vector3f(int x, int y, int z)
{
    this->x = x;
    this->y = y;
    this->z = z;
}
int vector3f::getX()
{
    return x;
}

int vector3f::getY()
{
    return y;
}

int vector3f::getZ()
{
    return z;
}
