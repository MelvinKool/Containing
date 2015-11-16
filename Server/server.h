#ifndef SERVER_H
#define SERVER_H

#include "connections.h"

class Server
{
public:
	Server();
	~Server();
private:
    Connections* connections;
};

#endif //SERVER_H