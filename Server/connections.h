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
        std::string getDataForApp();
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
            std::thread* worker;
        };
        ServerSocket* socket;
        Client* simulator = nullptr;
        bool stop = false;
        std::vector<Client> clients;
        std::thread* acceptThread;

        int getFreeClientNumber();
        std::thread* newClientThread(int number);
        //std::string dataForApp = "0,0,0,0,0,0,0";
        std::string dataForApp = "1,1,1,1,1,1,1";
};

#endif //CONNECTIONS_H
