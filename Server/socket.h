#ifndef SOCKET_H
#define SOCKET_H

#include <string>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <iostream>

#define bufsize 65536

class ClientSocket
{
    public:
        ClientSocket(std::string ip, int port);
        ClientSocket(int sock);
        ~ClientSocket();
        std::string read();
        void write(std::string message);
    private:
        int sock;
};

class ServerSocket
{
    public:
        ServerSocket(int port);
        ~ServerSocket();
        int accept();
    private:
        int sock;
};

#endif //SOCKET_H
