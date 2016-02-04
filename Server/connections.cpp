#include "connections.h"
#include "JSONReader.h"
#include <iostream>
#include "allObjects.h"

using namespace std;


bool Connections::freeAgvAnswer = false;

void Connections::initConnections(Server* server)
{
    this->socket = new ServerSocket(1337);
    this->server = server;
}

// Stop all the running threads and delete them, then delete the socket.
Connections::~Connections()
{
    this->stop = true;
    this->acceptThread->join();
    delete this->acceptThread;

    for(uint i = 0; i < clients.size(); i++)
    {
        //clients[i].socket->write("disconnect");
        clients[i].worker->join();
        delete clients[i].worker;
    }

    delete this->socket;
}

bool Connections::freeAgvAvailable()
{
    return freeAgvAnswer;
}

// The thread that accepts new clients.
void Connections::acceptClients()
{
    this->acceptThread = new thread([this](){
        cout << "Accepting clients..." << endl;
        while(!this->stop)
        {
            int sock = this->socket->accept();
            if(sock != -1)
            {
                int number = getFreeClientNumber();
                this->clients[number].used = true;
                this->clients[number].socket = new ClientSocket(sock);
                this->clients[number].worker = newClientThread(number);
            }
        }
    });
}

// Looks for a unused client to reuse, else makes a new client.
int Connections::getFreeClientNumber()
{
    uint number = 0;
    while(number < clients.size() && clients[number].used) number++;
    if(number == clients.size())
    {
        clients.push_back(Client());
    }
    else
    {
        clients[number].worker->join();
        delete clients[number].worker;
    }
    return number;
}

// The readthread of the client.
thread* Connections::newClientThread(int number)
{
    return new thread([number, this]()
    {
        this->clients[number].type = this->clients[number].socket->read();
        cout << this->clients[number].type + " connected." << endl;

        bool isSim = false;
        if(this->clients[number].type == "Simulator" && this->simulator == nullptr)
        {
            this->simulator = &(this->clients[number]);
            isSim = true;
        }
        //send all simulation objects to the simulator
        if(isSim){
            cout << "sending initialization json to simulator..." << endl;
            JSONReader jsonReader("Files/ObjectsJSON/ObjectLocations.json", server);
            jsonReader.loadTransport(server->allObjects);
            server->startRunning();
        }
        //load vehicles
        while(!this->stop)
        {
            string input;
            try
            {
                input = this->clients[number].socket->read();
            }
            catch(...)
            {
                input = "disconnect";
            }

            if(input == "disconnect")
            {
                cout << clients[number].type + " disconneced." << endl;
                server->stopRunning();
                break;
            }
            else if(input == "connection_check")
            {
                //dont do anything.
            }
            else if(input.substr(0, 11) == "dataforapp/")
            {
                std::string result = input.erase(0, 11);
                dataForApp = result;
            }
            else if(input.substr(0, 7) == "freeAgv")
            {
                //cout << input << endl;
                std::unique_lock<std::mutex> lck(mtx);
                std::string data = input.substr(7);

                int id;
                std::istringstream iss(data);
                iss >> id;
                this->newFreeAgv = id;

                freeAgvAnswer = true;
                cv.notify_one();
            }
            else
            {
                //what to do with the input?
                cout << input << endl;
            }

            if(this->stop)
            {
                break;
            }
        }
        if(isSim)
        {
            this->simulator = nullptr;
        }
        delete this->clients[number].socket;
        this->clients[number].used = false;
    });
}

// Writes message to the first simulator.
void Connections::writeToSim(string message)
{
    if(simulator != nullptr)
    {
        try
        {
            simulator->socket->write(message);
        }
        catch(...)
        {
            cout << "Could not write to Simulator." << endl;
        }
    }
}

std::string Connections::getDataForApp()
{
    return dataForApp;
}

int Connections::requestFreeAgv()
{
    int freeAgv;
    std::unique_lock<std::mutex> lck(mtx);
    writeToSim("freeAgv");

    while (!freeAgvAnswer) {
        cv.wait(lck);
    }

    freeAgv = newFreeAgv;

    freeAgvAnswer = false;
    return freeAgv;
}


