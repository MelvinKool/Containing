#include <string>
#include <iostream>

#include "server.h"
//testdata
#include "vector3f.h"
#include <vector>
#include "JSONGenerator.h"
#include "shortestpathdijkstra.h"
//////////
using namespace std;

int main(int argc, char* argv[])
{
    cout << endl << "Containing Server." << endl;
    cout << "Type 'exit' to close the application." << endl << endl;
    ShortestPathDijkstra shortestPath("Files/ObjectsJSON/test.txt");
    string goal = "100,0.0,-49.50";
    string loc1 = "54.75,0.0,-73.5";//, loc2 = "79.75,0.0,-73.5", loc3 = "113.75,0.0,-73.5";
    cout << "test" << endl;
    pair<double,vector<vector3f>> route = shortestPath.route(loc1, goal);
    cout << "routetest" << endl;
    /*pair<double,vector<vector3f>> route2 = shortestPath.route(loc2, goal);
    cout << "routetest" << endl;
    pair<double,vector<vector3f>> route3 = shortestPath.route(loc3, goal);*/
    JSONGenerator generator;
    cout << "test2" << endl;
    string message = generator.moveTo(2, route.second);
    cout << message << endl;
    /*string message2 = generator.moveTo(6, route2.second);
    string message3 = generator.moveTo(12, route3.second);*/
    Server server;
    cout << "test3" << endl;
    /*server.writeToSim(message2);
    server.writeToSim(message3);*/
    while(true)
    {
        string input;
        cin >> input;
        server.writeToSim(message);
        if(input == "exit") break;
        //server.writeToSim(input);
    }
    cout << "Closing..." << endl;
    return 0;
}
