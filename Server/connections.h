#ifndef CONNECTIONS_H
#define CONNECTIONS_H

#include <vector>
#include <string>
#include <thread>
#include <iostream>

#include "socket.h"

using namespace std;

class Connections
{
public:
	Connections();
	~Connections();
	void acceptClients();
private:
	struct Client
	{
		string type;
		bool used;
		ClientSocket* socket;
		thread* worker;
	};

	ServerSocket* socket;
	Client* simulator = nullptr;
	bool stop = false;
	vector<Client> clients;
	thread* acceptThread;

	int getFreeClientNumber();
	thread* newClientThread(int number);
};

#endif // CONNECTIONS_H