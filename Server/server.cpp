#include "server.h"

#include <iostream>

Server::Server()
{
    if(db.isConnected())
    {
        xmlParser.readXML(db);
    }
    connections.acceptClients();
    httpserver.init("4000");
}

void Server::writeToSim(string message)
{
    connections.writeToSim(message);
}

void Server::checkContainers()
{
    while (!stop)
    {
        cout<<"container check"<<endl;
        this_thread::sleep_for(chrono::seconds(3));
        /*
        //Logics outline and stuffs to send and let everything move
        //TODO getting the right time into querries!!
        string arrivals = "SELECT cont.containerID,ship.sort, arr.date, arr.timeFrom FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID AND arr.date = "+ currentDate +" AND arr.timeFrom = "+ currentTime;
        string departures = "SELECT cont.containerID, ship.sort, dep.date, dep.timeFrom FROM Departure as dep,Container as cont, ShippingType as ship WHERE cont.departureInfo = dep.shipmentID AND dep.shippingType = ship.shippingTypeID AND dep.date = "+ currentDate +" AND dep.timeFrom = "+ currentTime;

        //process leaving containers
        MYSQL_RES* res = db.select(departures);
        MYSQL_ROW row;
        string vehicle;
        while((row = mysql_fetch_row(res)) != NULL)
        {
            processLeavingContainers(row);
        }
        mysql_free_result(res);

        //process arriving containers
        MYSQL_RES* res = db.select(arrivals);
        MYSQL_ROW row;
        string vehicle;
        while((row = mysql_fetch_row(res)) != NULL)
        {
            processArrivingContainers(row);
        }
        mysql_free_result(res);
        */
    }
}

void Server::processLeavingContainer(MYSQL_ROW &row)
{
    /*
    string vehicle = row[1];

    agvID = getFreeAGV(); //TODO
    crane.goTo(positionX,positionY,positionZ); //crane at container dump //TODO
    agvs[agvID].goTo(vector3f(x,y,z)); // send agv to dump row //TODO
    crane.transfer(container,dump,agv); //transfer container from dump to agv //TODO

    if(vehicle=="trein") //TODO
    {
        //TODO spawn train
        agvs[agvID].goTo(vector3f(x,y,z)); //send agv with current container to unloading position
        crane.goTo(positionX,positionY,positionZ); //move crane to load location
        crane.transfer(container,agv,train); //transfer container from agv to train
    }

    if(vehicle=="vrachtauto") //TRUCK YEAH!! //TODO
    {
        //TODO spawn truck
        agvs[agvID].goTo(vector3f(x,y,z)); //send agv with current container to unloading position
        crane.goTo(positionX,positionY,positionZ); //move crane to load location
        crane.transfer(container,agv,truck); //transfer container from agv to truck
    }

    if(vehicle=="zeeschip") //TODO
    {
        //TODO spawn seaship
        crane.goTo(positionX,positionY,positionZ); //move crane to load location
        agvs[agvID].goTo(vector3f(x,y,z)); //send agv with current container to unloading position
        crane.transfer(container,agv,ship); //transfer container from agv to ship
    }

    if vehicle=="binnenschip") //TODO
    {
        //TODO spawn ship
        crane.goTo(positionX,positionY,positionZ); //move crane to load location
        agvs[agvID].goTo(vector3f(x,y,z)); //send agv with current container to unloading position
        crane.transfer(container,agv,ship); //transfer container from agv to ship
    }
    */
}

void Server::processArrivingContainer(MYSQL_ROW &row)
{
    /*
    string vehicle = row[1];

    agvID = getFreeAGV(); //TODO
    if(vehicle=="vrachtauto") //TODO
    {
        //TODO spawn truck
        crane.goTo(positionX,positionY,positionZ); //move crane to unloading location
        agvs[agvID].goTo(vector3f(x,y,z));
        crane.transfer(container,lorry,agv); //get container from truck to agv
    }

    if(vehicle=="trein") //TODO
    {
        //TODO spawn train
        crane.goTo(positionX,positionY,positionZ);
        agvs[agvID].goTo(vector3f(x,y,z));
        crane.transfer(container,train,agv);
    }

    if(vehicle=="zeeschip") //TODO
    {
        //TODO spawn seaship
        crane.goTo(positionX,positionY,positionZ);
        agvs[agvID].goTo(vector3f(x,y,z));
        crane.transfer(container,ship,agv);

        if(crane.currentRowContainerCount()==0)
        {
            //crane to new row?
            //or do this at first line of code
        }
    }

    if(vehicle=="binnenschip") //TODO
    {
        //TODO spawn ship
        crane.goTo(positionX,positionY,positionZ);
        agvs[agvID].goTo(vector3f(x,y,z));
        crane.transfer(container,ship,agv);

        if(crane.currentRowContainerCount()==0)
        {
            //crane to new row?
            //or do this at first line of code
        }
    }

    agvs[agvID].goTo(vector3f(x,y,z)); //move to dump row //TODO
    crane.goTo(positionX,positionY,positionZ); //move crane to agv parking //TODO
    crane.getBestDumpPosition(); //get best spot to place container in dumping row //TODO
    crane.transfer(container,agv,dump); //TODO
    crane.goTo(positionX,positionY,positionZ); //TODO
    */
}

void Server::stopRunning()
{
    stop = true;
}
