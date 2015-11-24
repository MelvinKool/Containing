#include "server.h"

#include <iostream>

Server::Server()
{
    db = new database();
	xmlParser = new xmlparser();
	xmlParser->read_XML();
	this->connections = new Connections();
}

Server::~Server()
{
	delete this->connections;
	delete xmlParser;
    delete db;
}

void Server::writeToSim(string message)
{
	connections->writeToSim(message);
}
