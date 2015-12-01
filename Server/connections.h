#ifndef CONNECTIONS_H
#define CONNECTIONS_H

#include <vector>
#include <string>
#include <thread>
#include <iostream>

#include "socket.h"

class Connections
{
public:
    Connections();
    ~Connections();
    void acceptClients();
    void writeToSim(std::string message);
private:
    struct Client
    {
        std::string type;
        bool used;
        ClientSocket* socket;
        thread* worker;
    };

    ServerSocket* socket;
    Client* simulator = nullptr;
    bool stop = false;
    std::vector<Client> clients;
    thread* acceptThread;

    int getFreeClientNumber();
    thread* newClientThread(int number);
};

#endif //CONNECTIONS_H