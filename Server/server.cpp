#include "server.h"

#include <iostream>

Server::Server()
{
    if(db.isConnected()){
        xmlParser.readXML(db);
    }
    connections.acceptClients();
    httpserver.init("4000");
    stop = false;
}

void Server::writeToSim(string message)
{
    connections.writeToSim(message);
}

void Server::checkContainers()
{
    while (!stop)
    {
        //Logics outline and stuffs to send let everything move
        string arrivals = "SELECT * FROM Arrival as arr,Container as cont WHERE cont.containerID = arr.shipmentID AND arr.date = "+ currentDate +" AND arr.timeFrom = "+ currentTime;
        string departures = "SELECT * FROM Departure as dep,Container as cont WHERE cont.containerID = dep.shipmentID AND dep.date = "+ currentDate +" AND dep.timeFrom = "+ currentTime;
        MYSQL_RES* arrivingContainers = db.select(arrivals);
        MYSQL_RES* leavingContainers = db.select(departures);

        while(leavingContainers.hasNext())
        {
            agv = getFreeAGV(); //TODO
            crane.goto(positionX,positionY,positionZ); //crane at container dump //TODO
            agv.goto(positionX,positionY,positionZ); // send agv to dump row //TODO
            crane.transfer(container,dump,agv); //transfer container from dump to agv //TODO

            if(vehicle=="train") //TODO
            {
                agv.goto(positionX,positionY,positionZ); //send agv with current container to unloading position
                crane.goto(positionX,positionY,positionZ); //move crane to load location
                crane.transfer(container,agv,train); //transfer container from agv to train
            }

            if(vehicle=="truck") //TRUCK YEAH!! //TODO
            {
                agv.goto(positionX,positionY,positionZ); //send agv with current container to unloading position
                crane.goto(positionX,positionY,positionZ); //move crane to load location
                crane.transfer(container,agv,truck); //transfer container from agv to truck
            }

            if(vehicle=="ship") //TODO
            {
                crane.goto(positionX,positionY,positionZ); //move crane to load location
                agv.goto(positionX,positionY,positionZ); //send agv with current container to unloading position
                crane.transfer(container,agv,ship); //transfer container from agv to ship
            }
        }

        while(arrivingContainers.hasNext())
        {
            agv = getFreeAGV(); //TODO
            if(vehicle=="truck") //TODO
            {
                crane.goto(positionX,positionY,positionZ); //move crane to unloading location
                crane.transfer(container,lorry,agv); //get container from truck to agv
            }

            if(vehicle=="train") //TODO
            {
                crane.goto(positionX,positionY,positionZ);
                agv.goto(positionX,positionY,positionZ);
                crane.transfer(container,train,agv);
            }

            if(vehicle=="ship") //TODO
            {
                crane.goto(positionX,positionY,positionZ);
                agv.goto(positionX,positionY,positionZ);
                crane.transfer(container,ship,agv);

                if(crane.currentRowContainerCount()==0)
                {
                    //crane to new row?
                    //or do this at first line of code
                }
            }

            agv.goto(positionX,positionY,positionZ); //move to dump row //TODO
            crane.goto(positionX,positionY,positionZ); //move crane to agv parking //TODO
            crane.getBestDumpPosition(); //get best spot to place container in dumping row //TODO
            crane.transfer(container,agv,dump); //TODO
            crane.goto(positionX,positionY,positionZ); //TODO
        }

    }
}

void Server::stopRunning()
{
    stop = true;
}

void Server::processLeavingContainers(MYSQL_RES* res)
{
    MYSQL_ROW row;
    while((row = mysql_fetch_row(res)) != NULL){
        ID = atoi(row[0]);
    }
    mysql_free_result(res);
}
