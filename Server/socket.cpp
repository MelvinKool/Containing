#include "socket.h"

using namespace std;

ClientSocket::ClientSocket(string ip, int port)
{
    sockaddr_in sa;
    memset(&sa, 0, sizeof(sa));
    sa.sin_family = AF_INET;
    sa.sin_port = htons(port);
    inet_pton(AF_INET, ip.c_str(), &sa.sin_addr);

    this->sock = socket(AF_INET, SOCK_STREAM, 0);
    connect(this->sock, (const sockaddr *)&sa, sizeof(sa));
}

ClientSocket::ClientSocket(int sock)
{
    this->sock = sock;
}

ClientSocket::~ClientSocket()
{
    close(this->sock);
}

string ClientSocket::read()
{
    char buffer[bufsize];
    int count = recv(sock, buffer, bufsize-1, 0);
    if(count == 0) return "disconnect";
    if(count > 0 && buffer[count-1] == '\n') count--;
    buffer[count] = '\0';
    return string(buffer);
}

void ClientSocket::write(string message)
{
    if(!message.empty())
    {
        int count = message.size();
        /* if you want to send a larger message than bufsize*/
        char buffer[count];
        strcpy(buffer, message.c_str());
        buffer[count++] = '\n';
        send(sock, buffer, count, 0);
        //cout << buffer << endl;
        /*if(count <= bufsize-1)
        {
            char buffer[bufsize];
            strcpy(buffer, message.c_str());
            buffer[count++] = '\n';
            send(sock, buffer, count, 0);
        }
        else
        {
            cout << "ClientSocket::write - message to large to send." << endl;
        }*/
    }

}

ServerSocket::ServerSocket(int port)
{
    sockaddr_in sa;
    memset(&sa, 0, sizeof(sa));
    sa.sin_family = AF_INET;
    sa.sin_port = htons(port);
    sa.sin_addr.s_addr = htonl(INADDR_ANY);

    int yes = 1;

    this->sock = socket(AF_INET, SOCK_STREAM, 0);
    setsockopt(this->sock, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int));
    bind(this->sock, (const sockaddr*)&sa, sizeof(sa));
    listen(this->sock, 1);
}

ServerSocket::~ServerSocket()
{
    close(this->sock);
}

int ServerSocket::accept()
{
    int iResult;
    struct timeval tv;
    fd_set rfds;
    FD_ZERO(&rfds);
    FD_SET(this->sock, &rfds);

    tv.tv_sec = (long)5;
    tv.tv_usec = 0;

    iResult = select(this->sock + 1, &rfds, (fd_set *) 0, (fd_set *) 0, &tv);
    if(iResult > 0)
    {
        return ::accept(this->sock, NULL, NULL);
    }
    else
    {
        return -1;
    }
}
