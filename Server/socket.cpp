#include "socket.h"

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
	if(count > 0 && buffer[count-1] == '\n') count--;
	buffer[count] = '\0';
	return string(buffer);
}

void ClientSocket::write(string message)
{
	int count = message.size();
	if(count <= bufsize-1)// throw runtime_error("ClientSocket::write - argument too large");
	{
		char buffer[bufsize];
		strcpy(buffer, message.c_str());
		buffer[count++] = '\n';
		send(sock, buffer, count, 0);
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
	return ::accept(this->sock, NULL, NULL);
}