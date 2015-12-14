#ifndef SHORTESTPATH_H
#define SHORTESTPATH_H

/*
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
    pair<int, string> route(string naam1, std::string naam2);
    void reset();
private:
    struct Plaats
    {
        Plaats(string naam) : naam(naam) {}
        string naam;
        vector<pair<Plaats*, int> > wegen;
        bool klaar;
        int afstand = verweg;
        Plaats* voorganger;
    };
    static const int verweg = 1000000;
    map<string, Plaats*> plaatsen;
    Plaats* getPlaats(string naam);
    void add(Plaats* fromPlace, Plaats* toPlace, int distance);
    bool roadExists(Plaats* fromPlace, Plaats* toPlace, int distance);
};
*/

#endif //SHORTESTPATH_H