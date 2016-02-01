#include "server.h"
#include "connections.h"
#include <iostream>
#include <fstream>
using namespace std;

Server::Server()
{
    if(db.isConnected())
    {
        xmlParser.readXML(db);
    }
    connections.initConnections(this);
    connections.acceptClients();
    httpserver.init(connections);
    for (int stops = 0; stops < 20; stops++)
    {
        truckStops.push_back(vector3f(835.25+(7.5*stops),0.0,10));
    }
    loadParkingLots();
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
        this_thread::sleep_for(chrono::milliseconds(10));

        //Logics outline and stuffs to send and let everything move
        previousDate = currentDate;
        previousTime = currentTime;
        currentDate = timer.getDate();
        currentTime = timer.getTime();
        //SQL for current containers
        string arrivals = "SELECT cont.containerID,ship.sort, arr.timeTill, arr.positionX, arr.positionY, arr.positionZ FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID AND arr.date <= \""+currentDate+"\" AND arr.date >= \""+previousDate+"\" AND arr.timeFrom <= \""+currentTime+"\" AND arr.timeFrom >= \""+previousTime+"\" ORDER BY ship.sort ASC,arr.positionZ ASC,arr.positionX ASC,arr.positionY ASC;";
        //string arrivals = "SELECT cont.containerID,ship.sort, arr.timeTill, arr.positionX, arr.positionY, arr.positionZ FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID;";
        string departures = "SELECT cont.containerID, ship.sort, dep.timeTill FROM Departure as dep,Container as cont, ShippingType as ship WHERE cont.departureInfo = dep.shipmentID AND dep.shippingType = ship.shippingTypeID AND dep.date <= \""+currentDate+"\" AND dep.date >= \""+previousDate+"\" AND dep.timeFrom <= \""+currentTime+"\" AND dep.timeFrom >= \""+previousTime+"\" ORDER BY ship.sort;";

        //cout << arrivals << endl; //TODO:remove()
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
        int truckLoc = getTruckStop();
        vector3f truckLocation = truckStops.at(truckLoc);
        writeToSim(JGen.spawnTruck(truckLocation,containerId,transportId));
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(truckLocation.getX(),0.0000,-25.000),false,containerId));
        commands.push_back(allObjects.truckCranes.at(truckLoc).transfer(containerId,agvID)); //get container from truck to agv
        commands.push_back(JGen.agvAttachContainer(agvID,containerId));
        commands.push_back(JGen.despawnObject(transportId, "truck",containerId));
    }

    if(vehicle=="trein")
    {
        if (!trainSpawned)
        {
            string treinContainers = "SELECT cont.containerID FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID AND ship.sort = \"trein\" AND arr.date <= \""+currentDate+"\" AND arr.date >= \""+previousDate+"\" AND arr.timeFrom <= \""+currentTime+"\" AND arr.timeFrom >= \""+previousTime+"\";";

            MYSQL_RES* resContainerList = db.select(treinContainers);
            MYSQL_ROW row;
            while((row = mysql_fetch_row(resContainerList)) != NULL)
            {
                containers.push_back(atoi(row[0]));
            }
            mysql_free_result(resContainerList);

            containersPerCrane = containers.size()/4;

            writeToSim(JGen.spawnTrain(containers,-1));
            lastTreinContainer = containers.at(containers.size()-1);
            trainSpawned = true;
        }
        containerCount++;
        if (containerCount>containersPerCrane)
        {
            containerCount = 0;
            CraneId++;
            if (trainCraneId >= 4)
            {
                CraneId = 0;
            }
        }
        if (containerId==lastTrainContainer)
        {
            commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(58.00,0.0,-720.0),false,containerId));
            commands.push_back(allObjects.trainCranes.at(3).transfer(containerId,agvID));
            commands.push_back(JGen.agvAttachContainer(agvID,containerId));
            commands.push_back(JGen.despawnObject(-1, "train",containerId));
            trainSpawned = false;
            CraneId = 0;
            containerCount = 0;
        }
        else
        {
            commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(58.00,0.0,-720.0),false,containerId));
            commands.push_back(allObjects.trainCranes.at(trainCraneId).transfer(containerId,agvID));
            commands.push_back(JGen.agvAttachContainer(agvID,containerId));
        }
    }

    if(vehicle=="zeeschip") //TODO
    {
        if (!seaShipSpawned)
        {
            string Containers = "SELECT cont.containerID FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID AND ship.sort = \"zeeschip\" AND arr.date <= \""+currentDate+"\" AND arr.date >= \""+previousDate+"\" AND arr.timeFrom <= \""+currentTime+"\" AND arr.timeFrom >= \""+previousTime+"\";";

            MYSQL_RES* resContainerList = db.select(Containers);
            MYSQL_ROW row;
            while((row = mysql_fetch_row(resContainerList)) != NULL)
            {
                containers.push_back(atoi(row[0]));
            }
            mysql_free_result(resContainerList);

            containersPerCrane = containers.size()/10;

            writeToSim(JGen.spawnSeaShip(containers,-1));
            lastSeaShipContainer = containers.at(containers.size()-1);
            seaShipSpawned = true;
        }
        containerCount++;
        if (containerCount>containersPerCrane)
        {
            containerCount = 0;
            shipCraneId++;
            if (shipCraneId >= 4)
            {
                shipCraneId = 0;
            }
        }
        if (containerId==lastSeashipContainer)
        {
            commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(6.0,0.0,-100.0),false,containerId));
            commands.push_back(allObjects.trainCranes.at(0).transfer(containerId,agvID));
            commands.push_back(JGen.agvAttachContainer(agvID,containerId));
            commands.push_back(JGen.despawnObject(-1, "seaship",containerId));
            seaShipSpawned = false;
            shipCraneId = 0;
            containerCount = 0;
        }
        else
        {
            commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(6.0,0.0,-100.0),false,containerId));
            commands.push_back(allObjects.trainCranes.at(shipCraneId).transfer(containerId,agvID));
            commands.push_back(JGen.agvAttachContainer(agvID,containerId));
        }

    if(vehicle=="binnenschip") //TODO
    {
        return;
        /*
        //TODO spawn ship
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),false));
        commands.push_back(crane.transfer(containerId,agvID));
        */
    }

    vector<int> storageLane = getStorageLaneSpot();
    int storageLaneID = storageLane.at(1);
    commands.push_back(allObjects.agvs.at(agvID).goTo(allObjects.parkingSpots[6*storageLaneID],true,-1)); //move to dump row
    commands.push_back(allObjects.storageCranes.at(storageLaneID).transfer(containerId,storageLaneID,vector3f(storageLane.at(0),0,storageLane.at(2))));
    writeToSim(JGen.generateCommandList(containerId,commands));
}

void Server::processLeavingContainer(MYSQL_ROW &row)
{
    /*
    containerId = atoi(row[0]);
    vehicle = row[1];
    agvID = getFreeAGV();
    vector<string> commands;

    //crane.goTo(vector3f(x,y,z)); //crane at container dump
    commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),false)); // send agv to dump row
    commands.push_back(crane.transfer(containerId,agvID)); //transfer container from dump to agv

    if(vehicle=="vrachtauto") //TRUCK YEAH!! //TODO
    {
        //TODO spawn truck
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(crane.transfer(containerId,truck)); //transfer container from agv to truck
    }
    /*
    if(vehicle=="trein") //TODO
    {
        //TODO spawn train //-1 if no container present
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(crane.transfer(containerId,train)); //transfer container from agv to train
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
        i = 0;
    else
        i++;

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
        i = 0;
    else
        i++;

    return i;
}

int Server::getTransportID()
{
    static int i = -1;
    i++;
    return i;
}

vector<int> Server::getStorageLaneSpot()
{
    static int x=0,y=0,z=0;
    if (y>43)
    {
        y=0;

        if (x>4)
        {
            x=0;

            if (z>40)
                z=0;
            else
                z++;
        }
        else
            x++;
    }
    else
        y++;

    vector<int> result;
    result.push_back(x);
    result.push_back(y);
    result.push_back(z);
    return result;
}

void Server::loadParkingLots()
{
    ifstream agvStream("./Files/ObjectsJSON/AllAGVlocations.txt");
    string parkingLot = "";
    for(int i = 0; i < sizeof(allObjects.parkingSpots); i++)
    {
        if(agvStream >> parkingLot)
        {
            vector3f tempVector3f(parkingLot);
            allObjects.parkingSpots[i] = tempVector3f;
        }
    }
}
