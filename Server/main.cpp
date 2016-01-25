#include <string>
#include <iostream>
#include "vector3f.h"
#include "server.h"
#include <vector>
#include <thread>

using namespace std;

void containers(Server* ser)
{
    ser->checkContainers();
}

int main(int argc, char* argv[])
{
    cout << endl << "Containing Server." << endl;
    cout << "Type 'exit' to close the application." << endl << endl;
    Server* server = new Server();
    for (uint i = 0; i < 100; i++)
    {
        server->agvs[i] = AGV(i+1,0.0,0.0,0.0,server);
    }
    //server->crane = Crane(12,0.0,0.0,0.0,server);
    thread t1(containers,server);

    while(true)
    {
        string input;
        getline(cin, input);
        if(input == "exit") break;
        server->writeToSim(input);
    }

    cout << "Closing..." << endl;
    server->stopRunning();
    t1.join();
    delete server;
    return 0;
}
