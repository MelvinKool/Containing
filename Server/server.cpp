#include "server.h"

#include <iostream>

Server::Server()
{
	//setup db
	parser.read_XML(nullptr);
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
