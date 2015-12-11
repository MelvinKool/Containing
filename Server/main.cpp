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
        JSONGenerator jsongenerator;
        vector<vector3f> coords;
        vector3f coordinate1(200,0,600);
        coords.push_back(coordinate1);
        vector3f coordinate2(0,0,0);
        coords.push_back(coordinate2);
        vector3f coordinate3(-500,0,1500);
        coords.push_back(coordinate3);
        string jsontest = jsongenerator.moveTo(2,coords);
        server.writeToSim(jsontest);
    }

    return 0;
}
