#ifndef KORTSTEPAD_H_
#define KORTSTEPAD_H_
#include <string>
#include <vector>
#include <map>

using namespace std;

class KortstePad
{
public:
	KortstePad(string fPath);
	void initRoutes(string fPath);
	~KortstePad();
	pair<double, string> route(string naam1, std::string naam2);
	void reset();
	double distance(string coordinate1, string coordinate2);
private:
	struct Plaats
	{
		Plaats(string naam) : naam(naam) {}
		string naam;
		vector<pair<Plaats*, double> > wegen;
		bool klaar;
		double shortestDistance = verweg;
		Plaats* voorganger;
	};
	//31.25,0,-681.50
	//1600.25,0,-675.25
	static const int verweg = 10000;
	map<string, Plaats*> plaatsen;
	Plaats* getPlaats(string naam);
	void add(Plaats* fromPlace, Plaats* toPlace, double distance);
	bool roadExists(Plaats* fromPlace, Plaats* toPlace, double distance);
	//double distance(string coordinate1, string coordinate2);
	std::vector<std::string> &split(const std::string &s, char delim, std::vector<std::string> &elems);
	std::vector<std::string> split(const std::string &s, char delim);
};
#endif
