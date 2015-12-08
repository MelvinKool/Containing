#include "server.h"

#include <iostream>

Server::Server()
{
    if(db.isConnected()){
        xmlParser.readXML(db);
    }
    connections.acceptClients();
    httpserver.init("4000");
}

void Server::writeToSim(string message)
{
    connections.writeToSim(message);
}