#include <string>
#include <iostream>

#include "server.h"

using namespace std;

int main(int argc, char* argv[])
{
	cout << "Type 'exit' to close the application." << endl;
	Server* server = new Server();

	string input;
	while(true)
	{
		cin >> input;
		if(input == "exit") break;
		else server->writeToSim(input);
	}

	cout << "Closing..." << endl;
	delete server;
	return 0;
}
