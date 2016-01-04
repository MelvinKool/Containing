#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <vector>
#include <algorithm>
#include <math.h>
#include "kortstepad.h"

using namespace std;

KortstePad::KortstePad(string fPath){
	initRoutes(fPath);
}

KortstePad::~KortstePad(){
	//delete every item in plaatsen
	for(pair<const string,Plaats*> &place : plaatsen){
		//Plaats *p = place.second;
		delete place.second;
		place.second = nullptr;
	}
}


void KortstePad::initRoutes(string fPath){
	ifstream loadRoutes(fPath.c_str());
	if(loadRoutes.is_open()){
		//init the routes
		string from, to,laden ,teleport;
		double distanceBetween;
		while(loadRoutes >> from >> to >> laden >> teleport){
			//cout << "from: " << from << " to: " << to << " distance: " << distance << endl;
			distanceBetween = distance(from,to);
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
			if(!roadExists(fromPlace,toPlace,distanceBetween)){
				add(fromPlace,toPlace,distanceBetween);
			}
			if(!roadExists(toPlace,fromPlace,distanceBetween)){
				add(toPlace,fromPlace,distanceBetween);
			}
		}
	}
}

//gives the shortest route
pair<double, string> KortstePad::route(string naam1, string naam2){
	string route;
	Plaats *plaats1,*plaats2,*huidige,*temp;
	double shortestDistance;
	//validate placenames
	if((plaats1 = getPlaats(naam1)) == nullptr || (plaats2 = getPlaats(naam2)) == nullptr)
		throw string("One or both of the placenames are incorrect, please enter the valid placenames");
	huidige = plaats1;
	//From place = 0
	plaats1->shortestDistance = 0;
	plaats1->klaar = true;
	while(!plaats2->klaar){
		shortestDistance = verweg;
		for(pair<Plaats*, double> &road : huidige->wegen){
			if(!road.first->klaar && road.first->shortestDistance > road.second + huidige->shortestDistance){
				road.first->shortestDistance = huidige->shortestDistance + road.second;
				road.first->voorganger = huidige;
			}
		}
		for(pair<const string, Plaats*> naamEnPlaats : plaatsen){
			Plaats* p = naamEnPlaats.second;
			if(!p->klaar && p->shortestDistance <= shortestDistance){
				shortestDistance = p->shortestDistance;
				temp = p;
			}
		}
		temp->klaar = true;
		huidige = temp;
	}
	//trace back the route
	while(huidige != plaats1){
		huidige = huidige->voorganger;
		route = huidige->naam + ", " + route;
	}
	route += plaats2->naam;
	return pair<double,string>(shortestDistance,route);
}

//checks whether a road exists
bool KortstePad::roadExists(Plaats* fromPlace, Plaats* toPlace, double distance){
	return (std::find(fromPlace->wegen.begin(), fromPlace->wegen.end(),pair<Plaats*, double>(toPlace,distance)) != fromPlace->wegen.end());
}
//gets the place pointer of a given placename
KortstePad::Plaats* KortstePad::getPlaats(string naam){
	return (plaatsen.find(naam) != plaatsen.end()) ? plaatsen[naam] : nullptr;
}

//adds a road to a place
void KortstePad::add(Plaats* fromPlace, Plaats* toPlace, double distance){
	fromPlace->wegen.push_back(pair<Plaats*, double>(toPlace,distance));
}

void KortstePad::reset(){
	for(pair<const string, Plaats*> &naamEnPlaats : plaatsen){
		naamEnPlaats.second->klaar = false;
		naamEnPlaats.second->shortestDistance = verweg;
		naamEnPlaats.second->voorganger = nullptr;
	}
}

/**
*Calculates the distance between two coordinates: only x and z from 3d coordinates
*/
double KortstePad::distance(string coordinate1, string coordinate2){
	cout << coordinate1 << endl;
	cout << coordinate2 << endl;
	cout << "test1" << endl;
	vector<string> coordVect1 = split(coordinate1,',');
	vector<string> coordVect2 = split(coordinate2,',');
	double x1,y1,z1,x2,y2,z2;
	cout << "test " << endl;
	x1 = atof(coordVect1.at(0).c_str());
	x2 = atof(coordVect2.at(0).c_str());
	cout <<"test2" << endl;
	z1 = atof(coordVect1.at(2).c_str());
	z2 = atof(coordVect2.at(2).c_str());
	cout << "After" << endl;
	//only x and z distance
	//distance between coordinate 1 and 2 = side c
	//find out point ab of pythagoras
	//int pointAB_X = abs(x2 - abs(x1));
	//int pointAB_Z = abs(z2 - abs(z1));
	double distSideAC = abs(abs(x2) - abs(x1));
	double distSideBC = abs(abs(z2) - abs(z1));
	double distanceC1ToC2 = sqrt(pow(distSideAC,2) + pow(distSideBC,2));
	cout << "done" << endl;
	return distanceC1ToC2;
}

vector<string> &KortstePad::split(const string &s, char delim, vector<string> &elems) {
    stringstream ss(s);
    string item;
    while (getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}

vector<string> KortstePad::split(const string &s, char delim) {
    vector<string> elems;
    split(s, delim, elems);
    return elems;
}
