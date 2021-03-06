#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <vector>
#include <algorithm>
#include "shortestPathDijkstra.h"

using namespace std;

ShortestPathDijkstra::ShortestPathDijkstra(char* fPath)
{
	loadedPath = fPath;
	initRoutes(fPath);
}

ShortestPathDijkstra::~ShortestPathDijkstra()
{
	amountCalls++;
	//delete every item in places
	for(pair<const string,Place*> &place : places)
	{
		delete place.second;
		place.second = nullptr;
	}
}


void ShortestPathDijkstra::initRoutes(char* fPath)
{
	ifstream loadRoutes(fPath);
	if(loadRoutes.is_open())
    {
		//init the routes
		string from, to, laden, teleport;
		double distanceBetween;
		int speed;
		while(loadRoutes >> from >> to >> laden >> speed)
		{
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
		}
	}
}

//gives the shortest route
pair<double,vector<vector3f>> ShortestPathDijkstra::route(string name1, string name2)
{
	//make sure the paths are empty
	reset();
	Place *place1,*place2,*current,*temp;
	double shortestDistance;
	//validate placenames
	if((place1 = getPlace(name1)) == nullptr || (place2 = getPlace(name2)) == nullptr)
    {
        string error = "One or both of the placenames are incorrect: " + name1 + " ; " + name2;
		throw string(error);
    }
	current = place1;
	//From place = 0
	place1->distance = 0;
	place1->done = true;
	while(!place2->done)
	{
		shortestDistance = faraway;
		//update distances
		for(pair<Place*, double> &road : current->roads)
		{
			if(!road.first->done && road.first->distance > road.second + current->distance)
			{
				road.first->distance = current->distance + road.second;
				road.first->previous = current;
			}
		}
		//search the node with the smallest distance
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
	}
	vector<string> route;
	//trace back the route
	while(current != place1)
	{
 		route.insert(route.begin(),current->name);
		Place* previous = nullptr;
		previous = current->previous;
		if(previous != nullptr)
        {
			current = previous;
		}
		else
        {
			throw string("Could not trace back path");
			break;
		}
	}
	vector<vector3f> vector3fRoute = vectorStringToVectorVector3f(route);
	return pair<double,vector<vector3f>>(shortestDistance,vector3fRoute);
}

vector<vector3f> ShortestPathDijkstra::vectorStringToVectorVector3f(vector<string> stringVector)
{
	vector<vector3f> vector3fVector;
	for(string s : stringVector)
    {
		vector<string> subStringVector = split(s, ',');
		float x = atof(subStringVector.at(0).c_str());
		float y = atof(subStringVector.at(1).c_str());
	    float z = atof(subStringVector.at(2).c_str());
		vector3f tempVector3f = vector3f(x,y,z);
		vector3fVector.push_back(tempVector3f);
	}
	return vector3fVector;
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
	double x1,z1,x2,z2;
	x1 = atof(coordVect1.at(0).c_str());
	x2 = atof(coordVect2.at(0).c_str());
	z1 = atof(coordVect1.at(2).c_str());
	z2 = atof(coordVect2.at(2).c_str());

	double distSideAC = abs(abs(x2) - abs(x1));
	double distSideBC = abs(abs(z2) - abs(z1));
	double distanceC1ToC2 = sqrt(pow(distSideAC,2) + pow(distSideBC,2));
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