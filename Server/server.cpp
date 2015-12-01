#include "server.h"

#include <iostream>

Server::Server()
{
    if(db.isConnected()){
        xmlParser.readXML(db);
    }
    connections.acceptClients();
}

void Server::writeToSim(string message)
{
    connections.writeToSim(message);
}