#ifndef SOCKET_H
#define SOCKET_H

#include <string>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <iostream>

using namespace std;

#define bufsize 4096

class ClientSocket
{
    public:
        ClientSocket(string ip, int port);
        ClientSocket(int sock);
        ~ClientSocket();
        string read();
        void write(string message);
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
