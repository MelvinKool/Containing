#ifndef ShortestPathDijkstra_H_
#define ShortestPathDijkstra_H_

#include <string>
#include <vector>
#include <map>
#include <limits>

#include "vector3f.h"

class ShortestPathDijkstra
{
public:
    ShortestPathDijkstra(){};
	ShortestPathDijkstra(char* fPath);
	void initRoutes(char* fPath);
	~ShortestPathDijkstra();
	std::pair<double,std::vector<vector3f>> route(std::string name1, std::string name2);
	void reset();
	double distance(std::string coordinate1, std::string coordinate2);
private:
	struct Place
	{
		Place(std::string name) : name(name) {}
		std::string name;
		std::vector<std::pair<Place*, double> > roads;
		bool done = false;
		double distance = faraway;
		Place* previous = nullptr;
	};
    int amountCalls = 0;
    char* loadedPath = "";
	static constexpr double faraway = std::numeric_limits<double>::max();
	std::map<std::string, Place*> places;
	Place* getPlace(std::string name);
	void add(Place* fromPlace, Place* toPlace, double distance);
	bool roadExists(Place* fromPlace, Place* toPlace, double distance);
	std::vector<std::string> &split(const std::string &s, char delim, std::vector<std::string> &elems);
	std::vector<std::string> split(const std::string &s, char delim);
	std::vector<vector3f> vectorStringToVectorVector3f(std::vector<std::string> stringVector);
};

#endif