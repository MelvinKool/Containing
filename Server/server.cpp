#include "server.h"

#include <iostream>

Server::Server()
{
	parser.read_XML();
	this->connections = new Connections();
}

Server::~Server()
{
	delete this->connections;
}

void Server::writeToSim(string message)
{
	connections->writeToSim(message);
}
