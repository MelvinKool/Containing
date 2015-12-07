#ifndef HTTPSERVER_H
#define HTTPSERVER_H

//include's
#include <netdb.h>
#include <iostream>
#include <sstream>
#include <fstream>
#include <regex>
#include <memory>
#include <cstdlib>
#include <unistd.h>
//#include <thread>

constexpr int BACKLOG = 10;

class HttpServer
{
public:
    HttpServer();
    ~HttpServer();
private:
    int sockfd; //listen on sock_fd
    struct addrinfo hints, *serverinfo;//, *p;
    struct sockaddr_storage their_addr; //client's address information
    socklen_t sin_size;
    int yes = 1;
    int rv;
    //thread *handleConnectionsThread;
    
    bool init(char* port);
    void handleConnections();
};

#endif //HTTPSERVER_H