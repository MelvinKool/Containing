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
        truckStops.push_back(vector3f(835.25+(7.5*stops),0.0,0.0));
    }
    loadParkingLots();
}

//Writes message to Simulator
void Server::writeToSim(string message)
{
    connections.writeToSim(message);
}

//Starts Container processing thread
void Server::startRunning()
{
    stop = false;
    t1 = thread([this] { this->checkContainers(); } );
}

//stops and joins container processing thread
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

//Main brain of container processing
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
        //SQL for current containers, with the time from the timer this gets the containers corresponding containers
        string arrivals = "SELECT cont.containerID,ship.sort, arr.timeTill, arr.positionX, arr.positionY, arr.positionZ FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID AND arr.date <= \""+currentDate+"\" AND arr.date >= \""+previousDate+"\" AND arr.timeFrom <= \""+currentTime+"\" AND arr.timeFrom >= \""+previousTime+"\" ORDER BY ship.sort ASC,arr.positionZ ASC,arr.positionX ASC,arr.positionY ASC;";
        //string arrivals = "SELECT cont.containerID,ship.sort, arr.timeTill, arr.positionX, arr.positionY, arr.positionZ FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID;";
        string departures = "SELECT cont.containerID, ship.sort, dep.timeTill FROM Departure as dep,Container as cont, ShippingType as ship WHERE cont.departureInfo = dep.shipmentID AND dep.shippingType = ship.shippingTypeID AND dep.date <= \""+currentDate+"\" AND dep.date >= \""+previousDate+"\" AND dep.timeFrom <= \""+currentTime+"\" AND dep.timeFrom >= \""+previousTime+"\" ORDER BY ship.sort;";

        /* The SQL works, but the logics for leaving containers doesn't
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

//Processes 1 container the arrives
void Server::processArrivingContainer(MYSQL_ROW &row)
{
    int containerId = atoi(row[0]); //gets container Id
    std::string vehicle = row[1]; //gets vehicle containers arrives with
    int agvID = getFreeAGV();
    vector<string> commands; //vector to keep track of commands to run
    int containersPerCrane;

    if(vehicle=="vrachtauto")
    {
        int transportId = getTransportID();
        int truckLoc = getTruckStop();
        vector3f truckLocation = truckStops.at(truckLoc);
        writeToSim(JGen.spawnTruck(truckLocation,containerId,transportId)); //spawn a truck
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(truckLocation.getX(),0.0000,-25.000),false,containerId));
        commands.push_back(allObjects.truckCranes.at(truckLoc).transfer(containerId,agvID)); //move container from truck to agv
        commands.push_back(JGen.agvAttachContainer(agvID,containerId));
        commands.push_back(JGen.despawnObject(transportId, "truck",containerId));//when done with the truck, remove it
    }

    if(vehicle=="trein")
    {
        if (!trainSpawned) //when there is no train, it has to be spawned in
        {
            vector<int> containers;
            //SQL gets all container Id's that go on the current train
            string trainContainers = "SELECT cont.containerID FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID AND ship.sort = \"trein\" AND arr.date <= \""+currentDate+"\" AND arr.date >= \""+previousDate+"\" AND arr.timeFrom <= \""+currentTime+"\" AND arr.timeFrom >= \""+previousTime+"\";";

            MYSQL_RES* resContainerList = db.select(trainContainers);
            MYSQL_ROW row;
            while((row = mysql_fetch_row(resContainerList)) != NULL)
            {
                containers.push_back(atoi(row[0]));
            }
            mysql_free_result(resContainerList);

            containersPerCrane = containers.size()/4;

            writeToSim(JGen.spawnTrain(containers,-1));
            lastTrainContainer = containers.at(containers.size()-1);
            trainSpawned = true; //set this, to make sure there are not 2 trains being spawned at once
        }
        containerCount++;
        if (containerCount>containersPerCrane) //distribute containers evenly over available cranes
        {
            containerCount = 0;
            trainCraneId++;
            if (trainCraneId >= 4)
            {
                trainCraneId = 0;
            }
        }
        if (containerId==lastTrainContainer) //if at last container, also despawn train
        {
            commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(58.00,0.0,-720.0),false,containerId));
            commands.push_back(allObjects.trainCranes.at(3).transfer(containerId,agvID));
            commands.push_back(JGen.agvAttachContainer(agvID,containerId));
            commands.push_back(JGen.despawnObject(-1, "train",containerId));
            trainSpawned = false;
            trainCraneId = 0;
            containerCount = 0;
        }
        else
        {
            commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(58.00,0.0,-720.0),false,containerId));
            commands.push_back(allObjects.trainCranes.at(trainCraneId).transfer(containerId,agvID));
            commands.push_back(JGen.agvAttachContainer(agvID,containerId));
        }
    }

    if(vehicle=="zeeschip")
    {
        //return;
        if (!seaShipSpawned) //When there is not yet a ship, spawn it
        {
            vector<int> containers;
            //SQL gets containers that are on current ship
            string seaShipContainers = "SELECT cont.containerID FROM Arrival as arr,Container as cont, ShippingType as ship WHERE cont.arrivalInfo = arr.shipmentID AND arr.shippingType = ship.shippingTypeID AND ship.sort = \"zeeschip\" AND arr.date <= \""+currentDate+"\" AND arr.date >= \""+previousDate+"\" AND arr.timeFrom <= \""+currentTime+"\" AND arr.timeFrom >= \""+previousTime+"\";";

            MYSQL_RES* resContainerList = db.select(seaShipContainers);
            MYSQL_ROW row;
            while((row = mysql_fetch_row(resContainerList)) != NULL)
            {
                containers.push_back(atoi(row[0]));
            }
            mysql_free_result(resContainerList);

            containersPerCrane = containers.size()/10;

            seaShipId = getTransportID();
            writeToSim(JGen.spawnSeaShip(containers,seaShipId));
            lastSeaShipContainer = containers.at(containers.size()-1);
            seaShipSpawned = true;
        }

        int containerCount = 0;
        seaShipCraneId++;
        if (seaShipCraneId >= 10)
        {
            seaShipCraneId = 0;
        }

        if (containerId==lastSeaShipContainer) //If at last container, despawn ship
        {
            commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(6.0,0.0,-100.0),false,containerId));
            commands.push_back(allObjects.seaShipCranes.at(seaShipCraneId).transfer(containerId,agvID));
            commands.push_back(JGen.agvAttachContainer(agvID,containerId));
            commands.push_back(JGen.despawnObject(-1, "seaShip",containerId));
            seaShipSpawned = false;
            seaShipCraneId = 0;
            containerCount = 0;
        }
        else
        {
            commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(6.0,0.0,-100.0),false,containerId));
            commands.push_back(allObjects.seaShipCranes.at(seaShipCraneId).transfer(containerId,agvID));
            commands.push_back(JGen.agvAttachContainer(agvID,containerId));
        }
    }

    if(vehicle=="binnenschip")
    {
        return;
        /*
        //TODO spawn ship
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),false));
        commands.push_back(crane.transfer(containerId,agvID));
        */
    }

    //Same for aal containers, drive to storage lane and unload container
    vector<int> storageLane = getStorageLaneSpot();
    int storageLaneID = storageLane.at(3);
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

    if(vehicle=="vrachtauto") //TRUCK YEAH!!
    {
        //TODO spawn truck
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(crane.transfer(containerId,truck)); //transfer container from agv to truck
    }
    /*
    if(vehicle=="trein")
    {
        //spawn train //-1 if no container present
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(crane.transfer(containerId,train)); //transfer container from agv to train
    }

    if(vehicle=="zeeschip")
    {
        //spawn seaship
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        commands.push_back(crane.transfer(containerId,ship)); //transfer container from agv to ship
    }

    if (vehicle=="binnenschip")
    {
        //spawn ship
        //crane.goTo(vector3f(x,y,z)); //move crane to load location
        commands.push_back(allObjects.agvs.at(agvID).goTo(vector3f(x,y,z),vector3f(x,y,z),true)); //send agv with current container to unloading position
        commands.push_back(crane.transfer(containerId,ship)); //transfer container from agv to ship
    }
    */
}

//Returns a free AGV
int Server::getFreeAGV()
{
    int agvId = connections.requestFreeAgv();
    return agvId;

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
            if (agv.getWorkingState()) //if agv is busy, dont even try to give it a new order
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

//Distributes trucks over all truck stops
int Server::getTruckStop()
{
    if (lastTruckStop == 20)
        lastTruckStop = 0;

    return lastTruckStop++;
}

//Generates fresh cup of transport Id
int Server::getTransportID()
{
    return lastTransportID++;;
}

//Gives back spot in storage lane to place container
storageLaneSpot Server::getStorageLaneSpot()
{
    if (lastStorageLaneSpot.nr>43) //nr represents storagelane Id
    {
        lastStorageLaneSpot.nr=0;

        if (lastStorageLaneSpot.x>4)
        {
            lastStorageLaneSpot.x=0;

            if (lastStorageLaneSpot.z>40)
                lastStorageLaneSpot.z=0;
            else
                lastStorageLaneSpot.z++;
        }
        else
            lastStorageLaneSpot.x++;
    }
    else
        lastStorageLaneSpot.nr++;

    vector<int> result;
    result.push_back(lastStorageLaneSpot.x);
    result.push_back(lastStorageLaneSpot.y);
    result.push_back(lastStorageLaneSpot.z);
    result.push_back(lastStorageLaneSpot.nr);
    return result;
}

//Initially loads all AGV parking spots into a vector
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
