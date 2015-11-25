#ifndef SERVER_H
#define SERVER_H

#include <string>

#include "connections.h"
#include "xmlparser.h"
#include "JSONGenerator.h"

class Server
{
public:
	Server();
	~Server();
	void writeToSim(string message);
private:
  Connections* connections;
	xmlparser parser;
	JSONGenerator jsonGenerator;
};

#endif //SERVER_H
