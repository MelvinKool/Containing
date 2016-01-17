#ifndef VECTOR3F_H
#define VECTOR3F_H
#include <string>
#include <iostream>

using namespace std;

class vector3f
{
    public:
        vector3f(){}
        vector3f(float x,float y, float z);
        float getX();
        float getY();
        float getZ();
        string toString();
    private:
        float x,y,z;
};

/*std::ostream& operator<<(std::ostream& o,const vector3f& p)
{
    return o << p.toString();
}*/
#endif
