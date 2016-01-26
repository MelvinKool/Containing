#include <string>
#include <iostream>
#include "vector3f.h"
#include "server.h"
#include <vector>
#include <thread>

using namespace std;

int main(int argc, char* argv[])
{
    cout << endl << "Containing Server." << endl;
    cout << "Type 'exit' to close the application." << endl << endl;
    Server* server = new Server();
    //server->crane = Crane(12,0.0,0.0,0.0,server);

    while(true)
    {
        string input;
        getline(cin, input);
        if(input == "exit") break;
        server->writeToSim(input);
    }

    cout << "Closing..." << endl;
    server->stopRunning();
    delete server;
    return 0;
}
