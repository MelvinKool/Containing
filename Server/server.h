#ifndef SERVER_H
#define SERVER_H

#include <string>

#include "connections.h"
#include "xmlparser.h"

class Server
{
public:
	Server();
	~Server();
	void writeToSim(string message);
private:
    Connections* connections;
	xmlparser parser;
};

#endif //SERVER_H
