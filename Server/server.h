#ifndef SERVER_H
#define SERVER_H

#include "socket.h"

using namespace std;

class Server
{
public:
	Server();
private:
	Socket *socket;
};

#endif //SERVER_H