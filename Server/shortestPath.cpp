/*
    Class for shortest path algorithm
    Below an example of the dijkstra algorithm:
*/

/*
#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <vector>
#include <algorithm>
#include "shortestPath.h"

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
	}
}

//gives the shortest route
pair<int, string> KortstePad::route(string naam1, string naam2){
	string route;
	Plaats *plaats1,*plaats2,*huidige,*temp;
	int shortestDistance;
	//validate placenames
	if((plaats1 = getPlaats(naam1)) == nullptr || (plaats2 = getPlaats(naam2)) == nullptr)
		throw string("One or both of the placenames are incorrect, please enter the valid placenames");
	huidige = plaats1;
	//From place = 0
	plaats1->afstand = 0;
	plaats1->klaar = true;
	while(!plaats2->klaar){
		shortestDistance = verweg;
		for(pair<Plaats*, int> &road : huidige->wegen){
			if(!road.first->klaar && road.first->afstand > road.second + huidige->afstand){
				road.first->afstand = huidige->afstand + road.second;
				road.first->voorganger = huidige;
			}
		}
		for(pair<const string, Plaats*> naamEnPlaats : plaatsen){
			Plaats* p = naamEnPlaats.second;
			if(!p->klaar && p->afstand <= shortestDistance){
				shortestDistance = p->afstand;
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
	return pair<int,string>(shortestDistance,route);
}

//checks whether a road exists
bool KortstePad::roadExists(Plaats* fromPlace, Plaats* toPlace, int distance){
	return (std::find(fromPlace->wegen.begin(), fromPlace->wegen.end(),pair<Plaats*, int>(toPlace,distance)) != fromPlace->wegen.end());
}
//gets the place pointer of a given placename
KortstePad::Plaats* KortstePad::getPlaats(string naam){
	return (plaatsen.find(naam) != plaatsen.end()) ? plaatsen[naam] : nullptr;
}

//adds a road to a place
void KortstePad::add(Plaats* fromPlace, Plaats* toPlace, int distance){
	fromPlace->wegen.push_back(pair<Plaats*, int>(toPlace,distance));
}

void KortstePad::reset(){
	for(pair<const string, Plaats*> &naamEnPlaats : plaatsen){
		naamEnPlaats.second->klaar = false;
		naamEnPlaats.second->afstand = verweg;
		naamEnPlaats.second->voorganger = nullptr;
	}
}
*/
