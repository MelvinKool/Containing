#include <string>
#include <iostream>

#include "server.h"
#include "vector3f.h"
#include <vector>
#include <thread>

using namespace std;
int main(int argc, char* argv[])
{
    cout << endl << "Containing Server." << endl;
    cout << "Type 'exit' to close the application." << endl << endl;

    Server server;

    thread t1(server.checkContainers());

    while(true)
    {
        string input;
        getline(cin, input);
        if(input == "exit") break;
        server.writeToSim(input);

    }
    cout << "Closing..." << endl;
    server.stopRunning();
    t1.join();
    return 0;
}
