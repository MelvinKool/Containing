#include "server.h"

#include <iostream>

using namespace std;

Server::Server()
{
    if(db.isConnected())
    {
        xmlParser.readXML(db);
    }
    connections.acceptClients();
    httpserver.init();
    pathFinderLoaded = ShortestPathDijkstra("./Files/ObjectsJSON/pathsLoadedAGV.csv");
    pathFinderUnloaded = ShortestPathDijkstra("./Files/ObjectsJSON/pathsUnloadedAGV.csv");
    for (int stops = 1; stops < 21; stops++)
    {
        truckStops.push_back(vector3f(835.25+(7.5*stops),0.0,25.0));
    }
}

void Server::writeToSim(string message)
{
    connections.writeToSim(message);
}

void Server::checkContainers()
{
    while (!stop)
    {
        //cout<<"container check"<<endl;
        this_thread::sleep_for(chrono::seconds(1));

        //Logics outline and stuffs to send and let everything move
        //TODO getting the right time into querries!!
        string arrivals = "SELECT cont.containerID,ship.sort, arr.timeTill, arr.positionX, arr.positionY, arr.positionZ FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID ORDER BY arr.date ASC,arr.timeFrom,ship.sort ASC,arr.positionZ ASC,arr.positionX ASC,arr.positionY ASC;";// AND arr.date = "+ currentDate +" AND arr.timeFrom = "+ currentTime;
        string departures = "SELECT cont.containerID, ship.sort, dep.timeTill FROM Departure as dep,Container as cont, ShippingType as ship WHERE cont.departureInfo = dep.shipmentID AND dep.shippingType = ship.shippingTypeID ORDER BY ship.sort;";// AND dep.date = "+ currentDate +" AND dep.timeFrom = "+ currentTime;

        /*
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
                //cout<<error<<endl;
            }
        }
        mysql_free_result(res1);
        */

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
                //cout<<error<<endl;
            }
        }
        mysql_free_result(res2);

    }
}

void Server::processArrivingContainer(MYSQL_ROW &row)
{
    containerId = atoi(row[0]);
    vehicle = row[1];
    //x=atoi(row[3]);y=atoi(row[4]);z=atoi(row[5]);//these are relative container coordinates,relative to vehicle position
    agvID = getFreeAGV();//x,y,z are destination, so those have to be known by this point

    if(vehicle=="vrachtauto") //TODO
    {
        vector3f truckLocation = getTruckStop();
        spawnObject("truck",truckLocation);
        commands.push_back(agvs[agvID].goTo(truckLocation));
        commands.push_back(crane.transfer(containerId,agvID)); //get container from truck to agv
    }

    if(vehicle=="trein") //TODO
    {
        //TODO spawn train
        commands.push_back(agvs[agvID].goTo(vector3f(x,y,z)));
        commands.push_back(crane.transfer(containerId,agvID));
    }

    if(vehicle=="zeeschip") //TODO
    {
        //TODO spawn seaship
        commands.push_back(agvs[agvID].goTo(vector3f(x,y,z)));
        commands.push_back(crane.transfer(containerId,agvID));
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
        commands.push_back(agvs[agvID].goTo(vector3f(x,y,z)));
        commands.push_back(crane.transfer(containerId,agvID));
        /*
        if(crane.currentRowContainerCount()==0)
        {
            //crane to new row?
            //or do this at first line of code
        }*/
    }

    commands.push_back(agvs[agvID].goTo(vector3f(x,y,z))); //move to dump row
    //crane.getBestDumpPosition(); //get best spot to place container in dumping row //TODO
    commands.push_back(crane.transfer(containerId,dump));
    writeToSim(JGen.generateCommandList(containerId,commands));
}

void Server::processLeavingContainer(MYSQL_ROW &row)
{
    containerId = atoi(row[0]);
    vehicle = row[1];
    agvID = getFreeAGV();

    //crane.goTo(vector3f(x,y,z)); //crane at container dump
    commands.push_back(agvs[agvID].goTo(vector3f(x,y,z))); // send agv to dump row
    commands.push_back(crane.transfer(containerId,agvID)); //transfer container from dump to agv
    if(vehicle=="trein") //TODO
    {
        //TODO spawn train
        commands.push_back(agvs[agvID].goTo(vector3f(x,y,z))); //send agv with current container to unloading position
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(crane.transfer(containerId,train)); //transfer container from agv to train
    }

    if(vehicle=="vrachtauto") //TRUCK YEAH!! //TODO
    {
        //TODO spawn truck
        commands.push_back(agvs[agvID].goTo(vector3f(x,y,z))); //send agv with current container to unloading position
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(crane.transfer(containerId,truck)); //transfer container from agv to truck
    }

    if(vehicle=="zeeschip") //TODO
    {
        //TODO spawn seaship
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(agvs[agvID].goTo(vector3f(x,y,z))); //send agv with current container to unloading position
        commands.push_back(crane.transfer(containerId,ship)); //transfer container from agv to ship
    }

    if (vehicle=="binnenschip") //TODO
    {
        //TODO spawn ship
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(agvs[agvID].goTo(vector3f(x,y,z))); //send agv with current container to unloading position
        commands.push_back(crane.transfer(containerId,ship)); //transfer container from agv to ship
    }

}

void Server::stopRunning()
{
    stop = true;
}

int Server::getFreeAGV()
{
    static int i = -1;
    if (i > 98)
    {
        i = 0;
    }
    else
    {
        i++;
    }
    return i;

    /* this would work if server knows which AGV are idling
    static int i = 0;
    int e = i, idClosestAGV = 0;
    double distClosesestAGV = 999999999.9999;
    if (i > 99)
    {
        i = 0;
    }

    for (int j = 0; j < 5; j++)
    {
        double distance = -1;
        while (distance < 0)
        {
            AGV agv = agvs[e];
            if (agv.getWorkingState()) //if agv is bussy, dont even try to give it a new order
            {
                if (e > 98)
                {
                    e = 0;
                }
                else
                {
                    e++;
                }
                continue;
            }

            try
            {
                distance = pathFinderUnloaded.route(agv.getCurrentLocation().toString(),destination.toString()).first;

                if (distance < distClosesestAGV) //if there is a distance, see if it is shorter than previous
                {
                    distClosesestAGV = distance;
                    idClosestAGV = e;
                }
            }
            catch (string error)
            {
                distance = -1;

                //needed till agv have correct locations and correct destination
                distance = 291.6;
                idClosestAGV = e;
            }

            if (e > 98)
            {
                e = 0;
            }
            else
            {
                e++;
            }
        }
    }

    i = i + 2;
    return idClosestAGV;
    */
}

vector3f Server::getTruckStop()
{
    static int i = -1;

    if (i > 19)
    {
        i = 0;
    }
    else
    {
        i++;
    }

    return truckStops[i];
}

string Server::spawnObject(string type,vector3f location)
{
    return JGen.spawnObject(type,location);
}
