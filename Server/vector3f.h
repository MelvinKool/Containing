#ifndef VECTOR3F_H
#define VECTOR3F_H

class vector3f
{
    public:
        vector3f(int x,int y, int z);
        int getX();
        int getY();
        int getZ();
    private:
        int x,y,z;
};
#endif
