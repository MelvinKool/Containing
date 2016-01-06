#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <vector>
#include <algorithm>
#include "shortestpathdijkstra.h"

using namespace std;

ShortestPathDijkstra::ShortestPathDijkstra(string fPath)
{
	initRoutes(fPath);
}

ShortestPathDijkstra::~ShortestPathDijkstra()
{
	//delete every item in places
	for(pair<const string,Place*> &place : places)
	{
		//Place *p = place.second;
		delete place.second;
		place.second = nullptr;
	}
}


void ShortestPathDijkstra::initRoutes(string fPath)
{
	ifstream loadRoutes(fPath.c_str());
	if(loadRoutes.is_open())
    {
		//init the routes
		string from, to, laden, teleport;
		double distanceBetween;
		while(loadRoutes >> from >> to >> laden >> teleport)
		{
			//cout << "from: " << from << " to: " << to << " distance: " << distance << endl;
			distanceBetween = distance(from,to);
			Place * fromPlace = new Place(from);
			Place * toPlace = new Place(to);
			auto insertedFrom = places.insert(pair<string,Place*>(from,fromPlace));
			auto insertedTo = places.insert(pair<string,Place*>(to,toPlace));
			//delete from or to if they are not inserted
			if(!insertedFrom.second)
			{
				//fromPlace already exists
				delete fromPlace;
				fromPlace = nullptr;
			}
			if(!insertedTo.second)
			{
				//toPlace already exists
				delete toPlace;
				toPlace = nullptr;
			}
			fromPlace = getPlace(from);
			toPlace = getPlace(to);
			//only one way roads
			if(!roadExists(fromPlace,toPlace,distanceBetween))
			{
				add(fromPlace,toPlace,distanceBetween);
			}
			/*if(!roadExists(toPlace,fromPlace,distanceBetween))
			{
				add(toPlace,fromPlace,distanceBetween);
			}*/
		}
	}
}

//gives the shortest route
pair<double, vector<string>> ShortestPathDijkstra::route(string name1, string name2)
{
	//make sure the paths are empty
	reset();
	Place *place1,*place2,*current,*temp;
	double shortestDistance;
	//validate placenames
	if((place1 = getPlace(name1)) == nullptr || (place2 = getPlace(name2)) == nullptr)
		throw string("One or both of the placenames are incorrect, please enter the valid placenames");
	current = place1;
	//From place = 0
	place1->distance = 0;
	place1->done = true;
	while(!place2->done)
	{
		shortestDistance = faraway;
		for(pair<Place*, double> &road : current->roads)
		{
			if(!road.first->done && road.first->distance > road.second + current->distance)
			{
				road.first->distance = current->distance + road.second;
				road.first->previous = current;
			}
		}
		for(pair<const string, Place*> nameAndPlace : places)
		{
			Place* p = nameAndPlace.second;
			if(!p->done && p->distance <= shortestDistance)
			{
				shortestDistance = p->distance;
				temp = p;
			}
		}
		temp->done = true;
		current = temp;
		//cout << " going to next one.... " << endl;
	}
	vector<string> route;
	//trace back the route
	while(current != place1)
	{
		route.insert(route.begin(),current->name);
		current = current->previous;
		//route = current->name + ", " + route;
	}
	//route += place2->name;
	return pair<double,vector<string>>(shortestDistance,route);
}

//checks whether a road exists
bool ShortestPathDijkstra::roadExists(Place* fromPlace, Place* toPlace, double distance)
{
	return (std::find(fromPlace->roads.begin(), fromPlace->roads.end(),pair<Place*, double>(toPlace,distance)) != fromPlace->roads.end());
}
//gets the place pointer of a given placename
ShortestPathDijkstra::Place* ShortestPathDijkstra::getPlace(string name)
{
	return (places.find(name) != places.end()) ? places[name] : nullptr;
}

//adds a road to a place
void ShortestPathDijkstra::add(Place* fromPlace, Place* toPlace, double distance)
{
	fromPlace->roads.push_back(pair<Place*, double>(toPlace,distance));
}

void ShortestPathDijkstra::reset()
{
	for(pair<const string, Place*> &nameAndPlace : places)
	{
		nameAndPlace.second->done = false;
		nameAndPlace.second->distance = faraway;
		nameAndPlace.second->previous = nullptr;
	}
}

/**
*Calculates the distance between two coordinates: only x and z from 3d coordinates
*/
double ShortestPathDijkstra::distance(string coordinate1, string coordinate2)
{
	vector<string> coordVect1 = split(coordinate1,',');
	vector<string> coordVect2 = split(coordinate2,',');
	double x1,y1,z1,x2,y2,z2;
	x1 = atof(coordVect1.at(0).c_str());
	x2 = atof(coordVect2.at(0).c_str());
	z1 = atof(coordVect1.at(2).c_str());
	z2 = atof(coordVect2.at(2).c_str());
	//only x and z distance
	//distance between coordinate 1 and 2 = side c
	//find out point ab of pythagoras
	//int pointAB_X = abs(x2 - abs(x1));
	//int pointAB_Z = abs(z2 - abs(z1));
	double distSideAC = abs(abs(x2) - abs(x1));
	double distSideBC = abs(abs(z2) - abs(z1));
	double distanceC1ToC2 = sqrt(pow(distSideAC,2) + pow(distSideBC,2));
	//cout << "done : distance = " << distanceC1ToC2 << " from: "<< coordinate1 << " to: " << coordinate2 << endl;
	return distanceC1ToC2;
}

vector<string> &ShortestPathDijkstra::split(const string &s, char delim, vector<string> &elems)
{
    stringstream ss(s);
    string item;
    while (getline(ss, item, delim))
	{
        elems.push_back(item);
    }
    return elems;
}

vector<string> ShortestPathDijkstra::split(const string &s, char delim)
{
    vector<string> elems;
    split(s, delim, elems);
    return elems;
}
