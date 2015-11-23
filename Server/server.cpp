#include "server.h"

#include <iostream>

Server::Server()
{
	parser = new xmlparser();
	parser->read_XML();
	db = new database();
	this->connections = new Connections();
}

Server::~Server()
{
	delete this->connections;
	delete db;
	delete parser;
}

void Server::writeToSim(string message)
{
	connections->writeToSim(message);
}
