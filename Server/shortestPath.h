#ifndef SHORTESTPATH_H
#define SHORTESTPATH_H

#include <string>
#include <vector>
#include <map>
#include "vector3f.h"

using namespace std;

class ShortestPath
{
public:
    ShortestPath();
    void initRoutes();
    ~ShortestPath();
    pair<int, vector<vector3f>> route(vector3f location1, vector3f location2);
    void reset();
private:
    struct Place
    {
        Place(vector3f location) : location(location) {}
        vector3f location;
        vector<pair<Place*, int> > roads;
        bool done;
        int distance = faraway;
        Place* previous;
    };
    static const int faraway = 1000000;
    map<vector3f, Place*> places;
    Place* getPlace(vector3f location);
    void add(Place* fromPlace, Place* toPlace, int distance);
    bool roadExists(Place* fromPlace, Place* toPlace, int distance);
};

#endif //SHORTESTPATH_H
