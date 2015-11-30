#include <iostream>
#include <fstream>

#include "xmlparser.h"
#include "Files/RapidXML/rapidxml.hpp"
#include "Files/RapidXML/rapidxml_utils.hpp"

using namespace std;
using namespace rapidxml;

void xmlparser::read_XML(database *db)
{
    cout << "Do you want to load the XML files ('y' or 'n')? ";
    string answer;
    cin >> answer;
    if(answer == "y"){
        cout << "Loading XML..." << endl;
        
        vector<string> xmlDocPaths;
        xmlDocPaths.push_back("../INFO/XML/xml1.xml");
        xmlDocPaths.push_back("../INFO/XML/xml2.xml");
        xmlDocPaths.push_back("../INFO/XML/xml3.xml");
        xmlDocPaths.push_back("../INFO/XML/xml4.xml");
        xmlDocPaths.push_back("../INFO/XML/xml5.xml");
        xmlDocPaths.push_back("../INFO/XML/xml6.xml");
        xmlDocPaths.push_back("../INFO/XML/xml7.xml");
        
        if(process_Data(xmlDocPaths, db))
            cout << "....Done!" << endl;
        else
            cout << "....Error while reading." << endl;
    }
}

//places data in the database
bool xmlparser::process_Data(vector<string> &xmlDocPaths, database *db)
{
    db->resetDatabase();
    for(string xmlDocPath : xmlDocPaths)
    {
        //create xml_document object
        xml_document<> doc;
        ifstream theFile (xmlDocPath.c_str());
        vector<char> buffer((istreambuf_iterator<char>(theFile)), istreambuf_iterator<char>());
        buffer.push_back('\0');
        
        //parse the contents of file
        doc.parse<0>(&buffer[0]);
        //find our root node
        xml_node<> * root = doc.first_node("recordset");
        
        for (xml_node<> *record = root->first_node(); record; record = record->next_sibling())
        {
            string iso = record->first_node("ISO")->value();

            //arrival
            /////////////////////////////////////////////////////////////////////
            xml_node<> * arrival = record->first_node("aankomst");
                string type_Transport_Arrival = arrival->first_node("soort_vervoer")->value();
                string company_Arrival = arrival->first_node("bedrijf")->value();
            
            //date
            xml_node<> * date_Arrival = arrival->first_node("datum");
                string day_Arrival = date_Arrival->first_node("d")->value();
                string month_Arrival = date_Arrival->first_node("m")->value();
                string year_Arrival = date_Arrival->first_node("j")->value();
                string arrival_Date = year_Arrival+"-"+month_Arrival+"-"+day_Arrival;

            //time
            xml_node<> * time_Arrival = arrival->first_node("tijd");
                string from_Arrival = time_Arrival->first_node("van")->value();
                if(from_Arrival.size() == 4){
                    from_Arrival.append("0");
                }
                from_Arrival[2] = ':';
                string till_Arrival = time_Arrival->first_node("tot")->value();
                if(till_Arrival.size() == 4){
                    till_Arrival.append("0");
                }
                till_Arrival[2] = ':';

            //position
            xml_node<> * position_Arrival = arrival->first_node("positie");
                string pos_X_Arrival = position_Arrival->first_node("x")->value();
                string pos_Y_Arrival = position_Arrival->first_node("y")->value();
                string pos_Z_Arrival = position_Arrival->first_node("z")->value();
            /////////////////////////////////////////////////////////////////////

            //owner
            /////////////////////////////////////////////////////////////////////
            xml_node<> * owner = record->first_node("eigenaar");
                string owner_Name = owner->first_node("naam")->value();
                string containerNr = owner->first_node("containernr")->value();
            /////////////////////////////////////////////////////////////////////

            //departure
            /////////////////////////////////////////////////////////////////////
            xml_node<> * departure = record->first_node("vertrek");
                string type_Transport_Departure = departure->first_node("soort_vervoer")->value();
                string company_Departure = departure->first_node("bedrijf")->value();

            //date
            xml_node<> * date_Departure = departure->first_node("datum");
                string day_Departure = date_Departure->first_node("d")->value();
                string month_Departure = date_Departure->first_node("m")->value();
                string year_Departure = date_Departure->first_node("j")->value();
                string departure_Date = year_Departure+"-"+month_Departure+"-"+day_Departure;

            //time
            xml_node<> * time_Departure = departure->first_node("tijd");
                string from_Departure = time_Departure->first_node("van")->value();
                if(from_Departure.size() == 4){
                    from_Departure.append("0");
                }
                from_Departure[2] = ':';
                string till_Departure = time_Departure->first_node("tot")->value();
                if(till_Departure.size() == 4){
                    till_Departure.append("0");
                }
                till_Departure[2] = ':';
            /////////////////////////////////////////////////////////////////////

            //dimensions
            /////////////////////////////////////////////////////////////////////
            xml_node<> * dimensions = record->first_node("afmetingen");
                string length = dimensions->first_node("l")->value();
                string width = dimensions->first_node("b")->value();
                string height = dimensions->first_node("h")->value();
            /////////////////////////////////////////////////////////////////////

            //weight
            /////////////////////////////////////////////////////////////////////
            xml_node<> * weight = record->first_node("gewicht");
                string empty_Weight = weight->first_node("leeg")->value();
                string content_Weight = weight->first_node("inhoud")->value();
            /////////////////////////////////////////////////////////////////////

            //content
            /////////////////////////////////////////////////////////////////////
            xml_node<> * content = record->first_node("inhoud");
                string content_Name =  content->first_node("naam")->value();
                string content_Type =  content->first_node("soort")->value();
                string content_Danger =  content->first_node("gevaar")->value();
            /////////////////////////////////////////////////////////////////////
            
            MYSQL_RES* res;
            MYSQL_ROW row;
            
            int ownerID                 = -1;
            int sizeID                  = -1;
            int contentID               = -1;
            int arrivalShippingTypeID   = -1;
            int arrivalCompanyID        = -1;
            int departureShippingTypeID = -1;
            int departureCompanyID      = -1;
            int arrivalShipmentID       = -1;
            int departureShipmentID     = -1;
            int containerID             = -1;
            
            string select_ownerID                 = "SELECT ownerID FROM Owner"
                                                    " WHERE name = '"+owner_Name+"';";
            string select_sizeID                  = "SELECT sizeID FROM Size"
                                                    " WHERE length = "+length+
                                                    " AND width = "+width+
                                                    " AND hight = "+height+";";
            string select_contentID               = "SELECT contentID FROM Content"
                                                    " WHERE name = '"+content_Name+
                                                    "' AND type = '"+content_Type+
                                                    "' AND danger = '"+content_Danger+"';";
            string select_arrivalShippingTypeID   = "SELECT shippingTypeID FROM ShippingType"
                                                    " WHERE sort = '"+type_Transport_Arrival+"';";
            string select_arrivalCompanyID        = "SELECT shippingCompanyID FROM ShippingCompany"
                                                    " WHERE name = '"+company_Arrival+"';";
            string select_departureShippingTypeID = "SELECT shippingTypeID FROM ShippingType"
                                                    " WHERE sort = '"+type_Transport_Departure+"';";
            string select_departureCompanyID      = "SELECT shippingCompanyID FROM ShippingCompany"
                                                    " WHERE name = '"+company_Departure+"';";
            string select_arrivalShipmentID       = "SELECT shipmentID FROM Arrival"
                                                    " WHERE date = '"+arrival_Date+
                                                    "' AND timeFrom = '"+from_Arrival+
                                                    "' AND timeTill = '"+till_Arrival+
                                                    "' AND positionX = "+pos_X_Arrival+
                                                    " AND positionY = "+pos_Y_Arrival+
                                                    " AND positionZ = "+pos_Z_Arrival+
                                                    " AND shippingType = "+to_string(arrivalShippingTypeID)+
                                                    " AND shippingCompany = "+to_string(arrivalCompanyID)+";";
            string select_departureShipmentID     = "SELECT shipmentID FROM Departure"
                                                    " WHERE date = '"+departure_Date+
                                                    "' AND timeFrom = '"+from_Departure+
                                                    "' AND timeTill = '"+till_Departure+
                                                    "' AND shippingType = "+to_string(departureShippingTypeID)+
                                                    " AND shippingCompany = "+to_string(departureCompanyID)+";";
            string select_containerID             = "SELECT containerID FROM Container"
                                                    " WHERE containerNr = "+containerNr+
                                                    " AND iso = '"+iso+
                                                    "' AND weightEmpty = "+empty_Weight+
                                                    " AND weightContents = "+content_Weight+
                                                    " AND owner = "+to_string(ownerID)+
                                                    " AND size = "+to_string(sizeID)+
                                                    " AND contents = "+to_string(contentID)+
                                                    " AND arrivalInfo = "+to_string(arrivalShipmentID)+
                                                    " AND departureInfo = "+to_string(departureShipmentID)+";";
            
            //=================================================================================================
            
            string insert_Owner           = "INSERT INTO Owner(name) VALUES('"+owner_Name+"')";
            string insert_Size            = "INSERT INTO Size(length, width, hight) VALUES()";
            string insert_Content         = "INSERT INTO (name, type, danger) VALUES()";
            string insert_ShippingType    = "INSERT INTO (sort) VALUES()";
            string insert_ShippingCompany = "INSERT INTO (name) VALUES()";
            string insert_Arrival         = "INSERT INTO (date, timeFrom, timeTill, positionX, positionY, positionZ, shippingType, shippingCompany) VALUES()";
            string insert_Departure       = "INSERT INTO (date, timeFrom, timeTill, shippingType, shippingCompany) VALUES()";
            string insert_Container       = "INSERT INTO (containerNr, iso, weightEmpty, weightContents, owner, size, contents, arrivalInfo, departureInfo) VALUES()";
            
            //Owner
            /*
            res = select("SELECT ownerID FROM Owner WHERE name = '"+owner_Name+"';");
            while((row = mysql_fetch_row(res)) != NULL){
                ownerID = row[0];
            }
            mysql_free_result(res);
            
            if(ownerID == -1){
                if(db->execute("INSERT INTO Owner(name) VALUES('"+owner_Name+"')")){
                    res = select("SELECT ownerID FROM Owner WHERE name = '"+owner_Name+"';");
                    while((row = mysql_fetch_row(res)) != NULL){
                        ownerID = row[0];
                    }
                    mysql_free_result(res);
                }
                else{
                    return false;
                }
            }
            */
        }
        theFile.close();
        cout << "xml done : " << xmlDocPath << endl;
    }
    return true;
}