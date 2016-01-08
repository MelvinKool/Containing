#include "server.h"

#include <iostream>

Server::Server()
{
    if(db.isConnected())
    {
        xmlParser.readXML(db);
    }
    connections.acceptClients();
    httpserver.init();
    pathFinderLoaded = ShortestPathDijkstra("pathtoloaded");
    pathFinderUnloaded = ShortestPathDijkstra("pathtounloaded");
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

        //Logics outline and stuffs to send and let everything move
        //TODO getting the right time into querries!!
        string arrivals = "SELECT cont.containerID,ship.sort, arr.date, arr.timeFrom FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID";// AND arr.date = "+ currentDate +" AND arr.timeFrom = "+ currentTime;
        string departures = "SELECT cont.containerID, ship.sort, dep.date, dep.timeFrom FROM Departure as dep,Container as cont, ShippingType as ship WHERE cont.departureInfo = dep.shipmentID AND dep.shippingType = ship.shippingTypeID";// AND dep.date = "+ currentDate +" AND dep.timeFrom = "+ currentTime;

        //process leaving containers
        MYSQL_RES* res1 = db.select(departures);
        MYSQL_ROW row1;
        string vehicle1;
        while((row1 = mysql_fetch_row(res1)) != NULL)
        {
            try
            {
                processLeavingContainer(row1);
            }
            catch (string error)
            {
                cout<<error<<endl;
            }
        }
        mysql_free_result(res1);

        //process arriving containers
        MYSQL_RES* res2 = db.select(arrivals);
        MYSQL_ROW row2;
        string vehicle2;
        while((row2 = mysql_fetch_row(res2)) != NULL)
        {
            try
            {
            processArrivingContainer(row2);
            }
            catch (string error)
            {
                cout<<error<<endl;
            }
        }
        mysql_free_result(res2);

    }
}

void Server::processLeavingContainer(MYSQL_ROW &row)
{
    string vehicle = row[1];

    int agvID = getFreeAGV(); //TODO
    crane.goTo(vector3f(x,y,z)); //crane at container dump
    agvs[agvID].goTo(vector3f(x,y,z)); // send agv to dump row
    crane.transfer(container,dump,agvID); //transfer container from dump to agv

    if(vehicle=="trein") //TODO
    {
        //TODO spawn train
        agvs[agvID].goTo(vector3f(x,y,z)); //send agv with current container to unloading position
        crane.goTo(vector3f(x,y,z)); //move crane to load location
        crane.transfer(container,agvID,train); //transfer container from agv to train
    }

    if(vehicle=="vrachtauto") //TRUCK YEAH!! //TODO
    {
        //TODO spawn truck
        agvs[agvID].goTo(vector3f(x,y,z)); //send agv with current container to unloading position
        crane.goTo(vector3f(x,y,z)); //move crane to load location
        crane.transfer(container,agvID,truck); //transfer container from agv to truck
    }

    if(vehicle=="zeeschip") //TODO
    {
        //TODO spawn seaship
        crane.goTo(vector3f(x,y,z)); //move crane to load location
        agvs[agvID].goTo(vector3f(x,y,z)); //send agv with current container to unloading position
        crane.transfer(container,agvID,ship); //transfer container from agv to ship
    }

    if (vehicle=="binnenschip") //TODO
    {
        //TODO spawn ship
        crane.goTo(vector3f(x,y,z)); //move crane to load location
        agvs[agvID].goTo(vector3f(x,y,z)); //send agv with current container to unloading position
        crane.transfer(container,agvID,ship); //transfer container from agv to ship
    }

}

void Server::processArrivingContainer(MYSQL_ROW &row)
{

    string vehicle = row[1];

    int agvID = getFreeAGV();
    if(vehicle=="vrachtauto") //TODO
    {
        //TODO spawn truck
        crane.goTo(vector3f(x,y,z)); //move crane to unloading location
        agvs[agvID].goTo(vector3f(x,y,z));
        crane.transfer(container,truck,agvID); //get container from truck to agv
    }

    if(vehicle=="trein") //TODO
    {
        //TODO spawn train
        crane.goTo(vector3f(x,y,z));
        agvs[agvID].goTo(vector3f(x,y,z));
        crane.transfer(container,train,agvID);
    }

    if(vehicle=="zeeschip") //TODO
    {
        //TODO spawn seaship
        crane.goTo(vector3f(x,y,z));
        agvs[agvID].goTo(vector3f(x,y,z));
        crane.transfer(container,ship,agvID);
        /*
        if(crane.currentRowContainerCount()==0)
        {
            //crane to new row?
            //or do this at first line of code
        }*/
    }

    if(vehicle=="binnenschip") //TODO
    {
        //TODO spawn ship
        crane.goTo(vector3f(x,y,z));
        agvs[agvID].goTo(vector3f(x,y,z));
        crane.transfer(container,ship,agvID);
        /*
        if(crane.currentRowContainerCount()==0)
        {
            //crane to new row?
            //or do this at first line of code
        }*/
    }

    agvs[agvID].goTo(vector3f(x,y,z)); //move to dump row
    crane.goTo(vector3f(x,y,z)); //move crane to agv parking
    //crane.getBestDumpPosition(); //get best spot to place container in dumping row //TODO
    crane.transfer(container,agvID,dump);
    crane.goTo(vector3f(x,y,z));

}

void Server::stopRunning()
{
    stop = true;
}

int Server::getFreeAGV()
{
    return 0;
}
