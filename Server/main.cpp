#include <string>
#include <iostream>

#include "server.h"
//testdata
#include "vector3f.h"
#include <vector>
//////////
using namespace std;
int main(int argc, char* argv[])
{
    cout << endl << "Containing Server." << endl;
    cout << "Type 'exit' to close the application." << endl << endl;

    Server server;

    while(true)
    {
        string input;
        getline(cin, input);
        if(input == "exit") break;
        server.writeToSim(input);
        /*
        Logics outline and stuffs to send let everything move

        resultSet newContainers = Database.select("current arrival time order by shippingType");
        resultSet leavingContainers = Database.selsect("current departure time order by shippingType");

        //TODO maybe per 5 containers? just like a buffer?
        while(leavingContainers.hasNext())
        {
            crane.goto(positionX,positionY,positionZ); //crane at container dump
            agv.goto(positionX,positionY,positionZ); // send agv to dump row
            crane.transfer(container, agv); //transfer container from dump to agv

            if(vehicle=="train")
            {
                agv.goto(positionX,positionY,positionZ); //send agv with current container to unloading position
                crane.goto(positionX,positionY,positionZ); //move crane to load location
                crane.transfer(container,train); //transfer container from agv to train
            }

            if(vehicle=="lorry") //TRUCK YEAH!!
            {
                agv.goto(positionX,positionY,positionZ); //send agv with current container to unloading position
                crane.goto(positionX,positionY,positionZ); //move crane to load location
                crane.transfer(container,lorry); //transfer container from agv to truck
            }

            if(vehicle=="ship")
            {
                crane.goto(positionX,positionY,positionZ); //move crane to load location
                agv.goto(positionX,positionY,positionZ); //send agv with current container to unloading position
                crane.transfer(container,ship); //transfer container from agv to ship
            }
        }

        while(newContainers.hasNext())
        {
            if(vehicle=="")
        }



        */
    }
    cout << "Closing..." << endl;
    return 0;
}
