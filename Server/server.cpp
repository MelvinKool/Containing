#include "server.h"

#include <iostream>

Server::Server()
{
	this->connections = new Connections();
}

Server::~Server()
{
	delete this->connections;
}