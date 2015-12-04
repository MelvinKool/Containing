#include <string>
#include <iostream>

#include "server.h"

using namespace std;
int main(int argc, char* argv[])
{
    cout << endl << "Containing Server." << endl;
    cout << "Type 'exit' to close the application." << endl << endl;

    Server* server = new Server();

    while(true)
    {
        string input;
        cin >> input;
        if(input == "exit") break;
        JSONGenerator jsongenerator;
        string jsonMoveToData = jsongenerator.moveTo();
        cout << jsonMoveToData << endl;
        server->writeToSim(jsonMoveToData);
    }

    delete server;
    return 0;
}
