#include "server.h"

#include <iostream>

Server::Server()
{
    db = new database();
    xmlParser.read_XML(db);
    this->connections = new Connections();
}

Server::~Server()
{
    delete connections;
    delete db;
}

void Server::writeToSim(string message)
{
    connections->writeToSim(message);
}
