#ifndef SERVER_H
#define SERVER_H

#include <string>

#include "connections.h"

class Server
{
public:
	Server();
	~Server();
	void writeToSim(string message);
private:
    Connections* connections;
};

#endif //SERVER_H