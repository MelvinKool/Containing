#include "server.h"
#include "connections.h"
#include <iostream>

using namespace std;

/*
    OBJECT-ID's:
    0-7     FreightShip Crane's
    8-52    Storage Crane's
    53-62   SeaShip Crane's
    63-66   Train Crane's
    67-86   Truck Crane's
*/

Server::Server()
{
    if(db.isConnected())
    {
        xmlParser.readXML(db);
    }
    connections.initConnections(this);
    connections.acceptClients();
    httpserver.init(connections);
    pathFinderLoaded = ShortestPathDijkstra("./Files/RouteFiles/LoadedRoutes.csv");
    pathFinderUnloaded = ShortestPathDijkstra("./Files/RouteFiles/UnloadedRoutes.csv");
    for (int stops = 0; stops < 20; stops++)
    {
        truckStops.push_back(vector3f(835.25+(7.5*stops),0.0,10));
    }
}

void Server::writeToSim(string message)
{
    connections.writeToSim(message);
}

void Server::startRunning()
{
    stop = false;
    t1 = thread([this] { this->checkContainers(); } );
}

void Server::stopRunning()
{
    if (stop)
    {
        //if it already stopped, don't stop it again
    }
    else
    {
        stop = true;
        timer.stop();
        t1.join();
    }
}

void Server::checkContainers()
{
    timer.start();
    currentDate = timer.getDate();
    currentTime = timer.getTime();

    while (!stop)
    {
        //cout<<"container check"<<endl;
        this_thread::sleep_for(chrono::seconds(1));

        //Logics outline and stuffs to send and let everything move
        previousDate = currentDate;
        previousTime = currentTime;
        currentDate = timer.getDate();
        currentTime = timer.getTime();
        //SQL for current containers
        //string arrivals = "SELECT cont.containerID,ship.sort, arr.timeTill, arr.positionX, arr.positionY, arr.positionZ FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID AND arr.date <= \""+currentDate+"\" AND arr.date > \""+previousDate+"\" AND arr.timeFrom <= \""+currentTime+"\" AND arr.timeFrom > \""+previousTime+"\" ORDER BY ship.sort ASC,arr.positionZ ASC,arr.positionX ASC,arr.positionY ASC;";
        string arrivals = "SELECT cont.containerID,ship.sort, arr.timeTill, arr.positionX, arr.positionY, arr.positionZ FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID;";
        string departures = "SELECT cont.containerID, ship.sort, dep.timeTill FROM Departure as dep,Container as cont, ShippingType as ship WHERE cont.departureInfo = dep.shipmentID AND dep.shippingType = ship.shippingTypeID ORDER BY ship.sort;";// AND dep.date = "+ currentDate +" AND dep.timeFrom = "+ currentTime;

        /*
        //process leaving containers
        MYSQL_RES* res1 = db.select(departures);
        MYSQL_ROW row1;
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
        while((row2 = mysql_fetch_row(res2)) != NULL)
        {
            if (stop)
            {
                break;
            }
            else
            {
                try
                {
                    processArrivingContainer(row2);
                }
                catch (string error)
                {
                    cout<<error<<endl;
                }
                catch (...)
                {}
            }
        }
        mysql_free_result(res2);
        if (trainSpawned)
        {
            writeToSim(JGen.despawnObject(-1));
        }
        break;
    }
}

void Server::processArrivingContainer(MYSQL_ROW &row)
{
    containerId = atoi(row[0]);
    vehicle = row[1];
    agvID = getFreeAGV();
    int transportId = getTransportID();
    vector<int> containers;
    vector<string> commands;

    if(vehicle=="vrachtauto") //TODO
    {
        //crane ids: 67-86;
        int truckLoc = getTruckStop();
        vector3f truckLocation = truckStops[truckLoc];
        //TODO void expression?!?
        writeToSim(JGen.spawnTruck(truckLocation,containerId,transportId));
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(truckLocation.getX(),0.0000,-25.000),false,containerId));
        commands.push_back(allObjects.truckCranes.at(truckLoc).transfer(containerId,agvID)); //get container from truck to agv
        commands.push_back(JGen.agvAttachContainer(agvID,containerId));
        commands.push_back(JGen.despawnObject(transportId));
    }

    if(vehicle=="trein")
    {
        //crane ids: 63-66;
        if (!trainSpawned)
        {
            string treinContainers = "SELECT cont.containerID FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID AND ship.sort = \"trein\" AND arr.date <= \""+currentDate+"\" AND arr.date > \""+previousDate+"\" AND arr.timeFrom <= \""+currentTime+"\" AND arr.timeFrom > \""+previousTime+"\";";

            MYSQL_RES* resContainerList = db.select(treinContainers);
            MYSQL_ROW row;
            while((row = mysql_fetch_row(resContainerList)) != NULL)
            {
                containers.push_back(atoi(row[0]));
            }
            mysql_free_result(resContainerList);

            writeToSim(JGen.spawnTrain(containers,-1));
        }
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(250.0,0.0,-723.0),false,containerId));
        commands.push_back(allObjects.trainCranes.at(0).transfer(containerId,agvID));
        commands.push_back(JGen.agvAttachContainer(agvID,containerId));
    }
    /*
    if(vehicle=="zeeschip") //TODO
    {
        //crane ids: 53-62;
        //TODO spawn seaship
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),false));
        commands.push_back(crane.transfer(containerId,agvID));
        /*
        if(crane.currentRowContainerCount()==0)
        {
            //crane to new row?
            //or do this at first line of code
        }
    }

    if(vehicle=="binnenschip") //TODO
    {
        //crane ids: 0-7;
        //TODO spawn ship
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),false));
        commands.push_back(crane.transfer(containerId,agvID));
        /*
        if(crane.currentRowContainerCount()==0)
        {
            //crane to new row?
            //or do this at first line of code
        }
    }
    */
    int storageLane = 41;
    commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(875.25,0.0,-73.5),true,-1)); //move to dump row
    commands.push_back(allObjects.storageCranes.at(storageLane).transfer(containerId,storageLane,vector3f(0,0,0)));
    writeToSim(JGen.generateCommandList(containerId,commands));
}

void Server::processLeavingContainer(MYSQL_ROW &row)
{
    /*
    containerId = atoi(row[0]);
    vehicle = row[1];
    agvID = getFreeAGV();

    //crane.goTo(vector3f(x,y,z)); //crane at container dump
    commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),false)); // send agv to dump row
    commands.push_back(crane.transfer(containerId,agvID)); //transfer container from dump to agv
    if(vehicle=="trein") //TODO
    {
        //TODO spawn train //-1 if no container present
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(crane.transfer(containerId,train)); //transfer container from agv to train
    }

    if(vehicle=="vrachtauto") //TRUCK YEAH!! //TODO
    {
        //TODO spawn truck
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(crane.transfer(containerId,truck)); //transfer container from agv to truck
    }

    if(vehicle=="zeeschip") //TODO
    {
        //TODO spawn seaship
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        commands.push_back(crane.transfer(containerId,ship)); //transfer container from agv to ship
    }

    if (vehicle=="binnenschip") //TODO
    {
        //TODO spawn ship
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        commands.push_back(crane.transfer(containerId,ship)); //transfer container from agv to ship
    }
    */
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
            AGV agv = allObjects.agvs[e];
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

int Server::getTruckStop()
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

    return i;
}

int Server::getTransportID()
{
    static int i = -1;
    i++;
    return i;
}
