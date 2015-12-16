#include "shortestPath.h"

#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <vector>
#include <algorithm>

using namespace std;

ShortestPath::ShortestPath(string fPath){
    initRoutes(fPath);
}

ShortestPath::~ShortestPath(){
    //delete every item in plaatsen
    for(pair<const vector3f,Place*> &place : places){
        //Plaats *p = place.second;
        delete place.second;
        place.second = nullptr;
    }
}

void ShortestPath::initRoutes(Database db){
    //load routes from db
    /*ifstream loadRoutes(fPath.c_str());
    if(loadRoutes.is_open()){
        //init the routes
        string from, to;
        int distance;
        while(loadRoutes >> from >> to >> distance){
            //cout << "from: " << from << " to: " << to << " distance: " << distance << endl;
            Plaats * fromPlace = new Plaats(from);
            Plaats * toPlace = new Plaats(to);
            auto insertedFrom = plaatsen.insert(pair<string,Plaats*>(from,fromPlace));
            auto insertedTo = plaatsen.insert(pair<string,Plaats*>(to,toPlace));
            //delete from or to if they are not inserted
            if(!insertedFrom.second){
                //fromPlace already exists
                delete fromPlace;
                fromPlace = nullptr;
            }
            if(!insertedTo.second){
                //toPlace already exists
                delete toPlace;
                toPlace = nullptr;
            }
            fromPlace = getPlaats(from);
            toPlace = getPlaats(to);
            if(!roadExists(fromPlace,toPlace,distance)){
                add(fromPlace,toPlace,distance);
            }
            if(!roadExists(toPlace,fromPlace,distance)){
                add(toPlace,fromPlace,distance);
            }
        }
    }*/
}

//gives the shortest route
pair<int, vector<vector3f>> ShortestPath::route(vector3f location1, vector3f location2){
    vector<vector3f> route;
    Place *place1,*place2,*current,*temp;
    int shortestDistance;
    //validate placenames
    if((place1 = getPlace(place1)) == nullptr || (place2 = getPlace(place2)) == nullptr)
        throw string("One or both of the placenames are incorrect, please enter the valid placenames");
    current = place1;
    //From place = 0
    place1->distance = 0;
    place1->done = true;
    while(!place2->done){
        shortestDistance = faraway;
        for(pair<Place2*, int> &road : current->roads){
            if(!road.first->done && road.first->distance > road.second + current->distance){
                road.first->distance = current->distance + road.second;
                road.first->previous = current;
            }
        }
        for(pair<const string, Place*> nameAndPlace : places){
            Place* p = nameAndPlace.second;
            if(!p->done && p->distance <= shortestDistance){
                shortestDistance = p->distance;
                temp = p;
            }
        }
        temp->done = true;
        current = temp;
    }
    //trace back the route
    while(current != place1){
        current = current->previous;
        route = current->location + ", " + route;
    }
    route.push_back(plaats2->location);
    return pair<int,vector<vector3f>>(shortestDistance,route);
}

//checks whether a road exists
bool ShortestPath::roadExists(Place* fromPlace, Place* toPlace, int distance){
    return (std::find(fromPlace->roads.begin(), fromPlace->roads.end(),pair<Place*, int>(toPlace,distance)) != fromPlace->roads.end());
}
//gets the place pointer of a given placename
KortstePad::Place* ShortestPath::getPlaats(vector3f location){
    return (places.find(location) != places.end()) ? places[location] : nullptr;
}

//adds a road to a place
void ShortestPath::add(Place* fromPlace, Place* toPlace, int distance){
    fromPlace->roads.push_back(pair<Place*, int>(toPlace,distance));
}

void ShortestPath::reset(){
    for(pair<const string, Place*> &nameAndPlace : places){
        nameAndPlace.second->done = false;
        nameAndPlace.second->distance = faraway;
        nameAndPlace.second->previous = nullptr;
    }
}
