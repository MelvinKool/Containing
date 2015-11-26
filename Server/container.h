#ifndef CONTAINER_H
#define CONTAINER_H

#include <chrono>
#include <string>
using namespace std;

//Deze class is niet af, en er klopt nog geen ruk van
class container
{
    private:
		struct DateTime{
			int year;
			int month;
			int day;
			int hour;
			int minute;
		};

        DateTime date_arrival, date_departure, time_Arrival_From, time_Arrival_Till, departure_From, departure_Till;
        string type_Transport_Arrival, company_Arrival, owner, type_Transport_Departure, company_Departure, dimensions;
		string position, content_Name, content_Type, content_Danger;
        double weight, weight_Empty;
        int container_Nr;
    public:
};

#endif // CONTAINER_H
