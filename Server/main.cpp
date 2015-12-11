#include <string>
#include <iostream>

#include "server.h"
//testdata
#include "vector3f.h"
#include <vector>
//////////
using namespace std;
int main(int argc, char* argv[])
{
    cout << endl << "Containing Server." << endl;
    cout << "Type 'exit' to close the application." << endl << endl;

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
