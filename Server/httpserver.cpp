#include "httpserver.h"

#include <netdb.h>
#include <iostream>
#include <sstream>
#include <fstream>
#include <regex>
#include <memory>
#include <cstdlib>
#include <unistd.h>

HttpServer::HttpServer()
{
    if(init("4000"))
    {
        std::cout << "HTTP Server is up, waiting for connoctions..." << std::endl;
        handleConnections();
    }
}

HttpServer::~HttpServer()
{
    //stop de httpserver thread
}

bool HttpServer::init(char* port)
{
    //time_t now = time(0);
    //tm* localtm = localtime(&now);
    
    memset(&hints, 0, sizeof hints);
    hints.ai_family   = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags    = AI_PASSIVE; //use current ip
    
    if((rv = getaddrinfo(nullptr, port, &hints, &serverinfo)) != 0)
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

void HttpServer::handleConnections()
{
    //handleConnectionsThread = new thread([this]()
    //{
        
        while(true) {  // main accept() loop
            sin_size = sizeof their_addr;
            int connection =accept(sockfd, (struct sockaddr *)&their_addr, &sin_size);
    
            char buf [1024];
            int bytes = read(connection, buf, sizeof(buf));
            buf[bytes]=0;
            std::string msg(buf);
    
            std::cout << msg << std::endl;
            std::ostringstream oss;
    
            //Dit is een protocol
            oss << " HTTP/1.1 200 OK\r\n \r\nContent-Type: text/html\r\nContent-Length: ";
    
            std::smatch m;
            std::regex e ("([^ ]*)(.css|.js|.html|.json)");
            if(std::regex_search (msg,m,e)) {
                std::cout << "\n" << m[0] << "\n";
                std::ostringstream location;
                location << "./Files/MobileApp/" << m[0];
    
                std::ifstream str (location.str());
                std::string result = "";
                if(str.good()){
                    std::string line;
                    while(!str.eof()){
                        getline(str,line);
                        result+= line + '\n';
                    }
                    oss << result.size()  <<  "\r\n\r\n";
                    oss << result;
                }else{
                    //Load the default html page
                    std::string defaultPage = "";
                    std::ifstream str ("./Files/MobileApp/Default.html");
                    if(str.good()){
                        std::string line;
                        while(!str.eof()){
                            getline(str,line);
                            defaultPage += line + '\n';
                        }
                    }
                    else{
                        str.close();
                        exit(EXIT_FAILURE);
                    }
    
                    str.close();
                    oss << defaultPage.size() << "\r\n\r\n";
                    oss << defaultPage;
                }
    
            }
            std::string reply = oss.str();
            send(connection, reply.c_str(), strlen(reply.c_str()), 0);
            close(connection);
        }
        
        
    //});
}