#include "httpserver.h"

#include <iostream>
#include <sstream>
#include <fstream>
#include <regex>
#include <memory>
#include <cstdlib>
#include <unistd.h>

HttpServer::~HttpServer()
{
    stop = true;
    handleConnectionsThread->join();
    delete handleConnectionsThread;
}

void HttpServer::init(Connections& connections)
{
    if(initSocket())
    {
        std::cout << "HTTP Server is up, waiting for connections..." << std::endl;
        handleConnections(connections);
    }
}

bool HttpServer::initSocket()
{
    memset(&hints, 0, sizeof hints);
    hints.ai_family   = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags    = AI_PASSIVE; //use current ip

    if((rv = getaddrinfo(nullptr, "4000", &hints, &serverinfo)) != 0)
    {
        std::cout << stderr << "getaddrinfo: " << gai_strerror(rv) << std::endl;
    }

    //loop through all the results and bind to the first wee can
    for(struct addrinfo *p = serverinfo; p != nullptr; p = p->ai_next)
    {
        if((sockfd = socket(p->ai_family, p->ai_socktype, p->ai_protocol)) == -1)
        {
            //error
            perror("httpserver: server socket");
            continue;
        }
        if(setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int)) == -1)
        {
            perror("httpserver: setsockopt");
            return false;
        }
        if(bind(sockfd, p->ai_addr, p->ai_addrlen) == -1)
        {
            close(sockfd);
            perror("httpserver: socket bind");
            continue;
        }
        break;
    }

    freeaddrinfo(serverinfo);

    if(listen(sockfd, BACKLOG) == -1)
    {
        perror("httpserver: listen");
        return false;
    }

    return true;
}

void HttpServer::handleConnections(Connections& connections)
{
    handleConnectionsThread = new std::thread([this, &connections]()
    {
        while(!stop)
        {
            int connection = acceptClient();

            if(connection != -1)
            {
                char buf [1024];
                int bytes = read(connection, buf, sizeof(buf));
                buf[bytes]=0;
                std::string msg(buf);

                std::ostringstream oss;
                oss << " HTTP/1.1 200 OK\r\n \r\nContent-Type: text/html\r\nContent-Length: ";

                std::smatch m;
                std::regex e ("([^ ]*)(.css|.html|.json|.js)");
                if(std::regex_search (msg,m,e))
                {
                    std::ostringstream location;
                    location << "./Files/MobileApp" << m[0];

                    if(location.str() == "./Files/MobileApp/DATA.json")
                    {
                        std::string response = connections.getDataForApp();
                        oss << response.size() << "\r\n\r\n";
                        oss << response;
                    }
                    else
                    {
                        std::ifstream str (location.str());
                        std::string result = "";
                        if(str.good())
                        {
                            std::string line;
                            while(!str.eof())
                            {
                                getline(str,line);
                                result+= line + '\n';
                            }
                            oss << result.size()  <<  "\r\n\r\n";
                            oss << result;
                        }
                        else //Load the default page
                        {
                            std::string defaultPage = "";
                            std::ifstream str ("./Files/MobileApp/Default.html");
                            if(str.good())
                            {
                                std::string line;
                                while(!str.eof())
                                {
                                    getline(str,line);
                                    defaultPage += line + '\n';
                                }
                            }
                            str.close();
                            oss << defaultPage.size() << "\r\n\r\n";
                            oss << defaultPage;
                        }
                    }
                }
                std::string reply = oss.str();
                send(connection, reply.c_str(), strlen(reply.c_str()), 0);
                close(connection);
            }
        }
    });
}

int HttpServer::acceptClient()
{
    int iResult;
    struct timeval tv;
    fd_set rfds;
    FD_ZERO(&rfds);
    FD_SET(sockfd, &rfds);

    tv.tv_sec = (long)5;
    tv.tv_usec = 0;

    iResult = select(sockfd + 1, &rfds, (fd_set *) 0, (fd_set *) 0, &tv);
    if(iResult > 0)
    {
        return ::accept(sockfd, NULL, NULL);
    }
    else
    {
        return -1;
    }
}
