#include <string>
#include <iostream>
#include "vector3f.h"
#include "server.h"
#include <vector>
#include <thread>

using namespace std;

int main(int argc, char* argv[])
{
    system("clear");
    cout << endl;
    cout << " ██████╗ ██████╗ ███╗   ██╗████████╗ █████╗ ██╗███╗   ██╗██╗███╗   ██╗ ██████╗ " << endl;
    cout << "██╔════╝██╔═══██╗████╗  ██║╚══██╔══╝██╔══██╗██║████╗  ██║██║████╗  ██║██╔════╝ " << endl;
    cout << "██║     ██║   ██║██╔██╗ ██║   ██║   ███████║██║██╔██╗ ██║██║██╔██╗ ██║██║  ███╗" << endl;
    cout << "██║     ██║   ██║██║╚██╗██║   ██║   ██╔══██║██║██║╚██╗██║██║██║╚██╗██║██║   ██║" << endl;
    cout << "╚██████╗╚██████╔╝██║ ╚████║   ██║   ██║  ██║██║██║ ╚████║██║██║ ╚████║╚██████╔╝" << endl;
    cout << " ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝   ╚═╝   ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝╚═╝  ╚═══╝ ╚═════╝ " << endl;
    cout << endl;
    cout << "Type 'exit' to close the application." << endl << endl;

    Server* server = new Server();

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