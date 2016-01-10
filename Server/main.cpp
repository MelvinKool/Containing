#include <string>
#include <iostream>
#include <map>
#include "server.h"
#include "JSONReader.h"
#include "transport.h"
using namespace std;

map<int,Transport> transportMap;
int main(int argc, char* argv[])
{
    cout << endl << "Containing Server." << endl;
    cout << "Type 'exit' to close the application." << endl << endl;
    cout << "begin" << endl;
    JSONReader jsonReader("Files/ObjectsJSON/ObjectLocations.json");
    jsonReader.loadTransport(transportMap);
    cout << "end" << endl;
    Server server;
    while(true)
    {
        string input;
        cin >> input;
        if(input == "exit") break;
        server.writeToSim(input);
    }
    cout << "Closing..." << endl;
    return 0;
}
