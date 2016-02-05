#ifndef HTTPSERVER_H
#define HTTPSERVER_H

#include <netdb.h>
#include <thread>
#include "connections.h"

constexpr int BACKLOG = 10;

class HttpServer
{
public:
    // Do not remove this default constructor.
    HttpServer(){}
    ~HttpServer();
    void init(Connections& connections);
private:
    int sockfd; //listen on sock_fd
    struct addrinfo hints, *serverinfo;
    struct sockaddr_storage their_addr; //client's address information
    socklen_t sin_size;
    int yes = 1;
    int rv;
    bool stop = false;
    std::thread *handleConnectionsThread;

    bool initSocket();
    void handleConnections(Connections& connections);
    int acceptClient();
};

#endif //HTTPSERVER_H