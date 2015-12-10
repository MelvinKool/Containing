#ifndef CONNECTIONS_H
#define CONNECTIONS_H

#include <vector>
#include <string>
#include <thread>

#include "socket.h"


// Handles all connections with simulators and mobile app's.
class Connections
{
public:
    Connections();
    ~Connections();
    void acceptClients();
    void writeToSim(std::string message);
private:
    // Holds som information about the client.
    //   type - either simulator or mobile app
    //   used - is this client used or not
    //   socket - the socket of this client
    //   worker - the thread that reads from this client
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