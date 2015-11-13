#include <string>
#include "server.h"

using namespace std;

int main(int argc, char** argv)
{
	cout << "Type 'exit' to close the application." << endl;
	Server* server = new Server();

	string input;
	while(true)
	{
		cin >> input;
		if(input == "exit") break;
	}

	delete server;
	return 0;
}
//http://cboard.cprogramming.com/networking-device-communication/128469-non-blocking-socket-timeout.html