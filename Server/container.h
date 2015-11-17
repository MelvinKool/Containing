#ifndef CONTAINER_H
#define CONTAINER_H

#include <chrono>
#include <string>
using namespace std;
class container
{
    private:
        time_t date_arrival, date_departure;
        string type_Transport_Arrival, company_Arrival, owner, type_Transport_Departure, company_Departure, dimensions, position, content_Name, content_Type, content_Danger;
        double weight, content, time_Arrival_From, time_Arrival_Till,departure_From, departure_Till, weight_Empty;
        int container_Nr;
    public:
};

#endif // CONTAINER_H
