#ifndef ShortestPathDijkstra_H_
#define ShortestPathDijkstra_H_
#include <string>
#include <vector>
#include <map>
#include <limits>
using namespace std;

class ShortestPathDijkstra
{
    public:
    	ShortestPathDijkstra(char* fPath);
    	void initRoutes(char* fPath);
    	~ShortestPathDijkstra();
    	//pair<double, string> route(string name1, std::string name2);
    	pair<double, vector<string> > route(string name1, string name2);
    	void reset();
    private:
    	struct Place
    	{
    		Place(string name) : name(name) {}
    		string name;
    		vector<pair<Place*, double> > roads;
    		bool done = false;
    		double distance = faraway;
    		Place* previous;
    	};
    	//31.25,0,-681.50
    	//1600.25,0,-675.25
    	static constexpr double faraway = std::numeric_limits<double>::max();
    	map<string, Place*> places;
    	Place* getPlace(string name);
    	void add(Place* fromPlace, Place* toPlace, double distance);
    	bool roadExists(Place* fromPlace, Place* toPlace, double distance);
    	double distance(string coordinate1, string coordinate2);
    	vector<string> &split(const string &s, char delim, vector<string> &elems);
    	vector<string> split(const string &s, char delim);
};
#endif
