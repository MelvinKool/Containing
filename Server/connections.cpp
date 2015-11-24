#include "connections.h"

Connections::Connections()
{
	this->socket = new ServerSocket(1337);
	acceptClients();
}

Connections::~Connections()
{
	cout << "Closing..." << endl;
	this->stop = true;
	this->acceptThread->join();
	delete this->acceptThread;

	for(int i = 0; i < clients.size(); i++)
	{
		clients[i].socket->write("disconnect");
		clients[i].worker->join();
		delete clients[i].worker;
	}

	delete this->socket;
}

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

int Connections::getFreeClientNumber()
{
	int number = 0;
	while(number < clients.size() && clients[number].used) number++;
	if(number == clients.size())
		clients.push_back(Client());
	else
	{
		clients[number].worker->join();
		delete clients[number].worker;
	}
	return number;
}

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

		while(!this->stop)
		{
			string input;
			try{
				input = this->clients[number].socket->read();
			}
			catch(...){
				input = "disconnect";
			}

			if(input == "disconnect")
			{
				cout << clients[number].type + " disconneced." << endl;
				break;
			}
			else
			{
				cout << input << endl;	//do something with the input!?
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

void Connections::writeToSim(string message)
{
	if(simulator != nullptr)
	{
		try{
			simulator->socket->write(message);
		}
		catch(...){
			cout << "Could not write to Simulator." << endl;
		}
	}
}
