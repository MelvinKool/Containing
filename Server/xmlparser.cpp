#include <stdlib.h>

#include "xmlparser.h"
#include "Files/RapidXML/rapidxml.hpp"
#include "Files/RapidXML/rapidxml_utils.hpp"

using namespace std;
using namespace rapidxml;

void XmlParser::readXML(Database &db)
{
    cout << "Do you want to load the XML files ('yes' or 'no')? ";
    string answer;
    getline(cin, answer);
    if(answer == "yes")
    {
        cout << "Loading XML..." << endl;

        vector<string> xmlDocPaths;
        xmlDocPaths.push_back("../docs/XML/xml1.xml");
       // xmlDocPaths.push_back("../docs/XML/xml2.xml");
       // xmlDocPaths.push_back("../docs/XML/xml3.xml");
       // xmlDocPaths.push_back("../docs/XML/xml4.xml");
        //xmlDocPaths.push_back("../docs/XML/xml5.xml");
        //xmlDocPaths.push_back("../docs/XML/xml6.xml");
        //xmlDocPaths.push_back("../docs/XML/xml7.xml");

        if(checkData(xmlDocPaths, db))
        {
            cout << "....Done!" << endl;
        }
        else
        {
            cout << "....Error while reading." << endl;
        }
    }
}

//places data in the database
bool XmlParser::processData(string &xmlDocPath, Database &db)
{
    cout<<"started loading xml into database"<<endl;
    //create xml_document object
    xml_document<> doc;
    ifstream theFile (xmlDocPath.c_str());
    vector<char> buffer((istreambuf_iterator<char>(theFile)), istreambuf_iterator<char>());
    buffer.push_back('\0');
    //parse the contents of fildde
    doc.parse<0>(&buffer[0]);

    //find the root node
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
            string arrival_Date = "20"+year_Arrival+"-"+month_Arrival+"-"+day_Arrival;

        //time
        xml_node<> * time_Arrival = arrival->first_node("tijd");
            string from_Arrival = time_Arrival->first_node("van")->value();
            if(from_Arrival.size() == 4)
            {
                from_Arrival = "0"+from_Arrival;
            }
            from_Arrival[2] = ':';
            string till_Arrival = time_Arrival->first_node("tot")->value();
            if(till_Arrival.size() == 4)
            {
                till_Arrival = "0"+till_Arrival;
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
            string departure_Date = "20"+year_Departure+"-"+month_Departure+"-"+day_Departure;

        //time
        xml_node<> * time_Departure = departure->first_node("tijd");
            string from_Departure = time_Departure->first_node("van")->value();
            if(from_Departure.size() == 4)
            {
                from_Departure = "0"+from_Departure;
            }
            from_Departure[2] = ':';
            string till_Departure = time_Departure->first_node("tot")->value();
            if(till_Departure.size() == 4)
            {
                till_Departure = "0"+till_Departure;
            }
            till_Departure[2] = ':';
        /////////////////////////////////////////////////////////////////////

        //dimensions
        /////////////////////////////////////////////////////////////////////
        xml_node<> * dimensions = record->first_node("afmetingen");
            string length = dimensions->first_node("l")->value();
                replace(length.begin(), length.end(), '\'' , ',');
            string width = dimensions->first_node("b")->value();
                replace(width.begin(), width.end(), '\'' , ',');
            string height = dimensions->first_node("h")->value();
                replace(height.begin(), height.end(), '\'' , ',');
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

        if (atoi(year_Arrival.c_str())>atoi(year_Departure.c_str()))
        {
            continue;
        }
        else if (atoi(year_Arrival.c_str())==atoi(year_Departure.c_str()))
        {
            if (atoi(month_Arrival.c_str())>atoi(month_Departure.c_str()))
            {
                continue;
            }
            else if (atoi(month_Arrival.c_str())==atoi(month_Departure.c_str()))
            {
                if (atoi(day_Arrival.c_str())>atoi(day_Departure.c_str()))
                {
                    continue;
                }
                else if (atoi(day_Arrival.c_str())==atoi(day_Departure.c_str()))
                {
                    if (atoi(from_Arrival.substr(0,2).c_str())>atoi(from_Departure.substr(0,2).c_str()))//substring is hours
                    {
                        continue;
                    }
                    else if (atoi(from_Arrival.substr(0,2).c_str())==atoi(from_Departure.substr(0,2).c_str()))
                    {
                        if (atoi(from_Arrival.substr(3,2).c_str())>atoi(from_Departure.substr(3,2).c_str()))//substring is minutes
                        {
                            continue;
                        }
                        else if (atoi(from_Arrival.substr(3,2).c_str())==atoi(from_Departure.substr(3,2).c_str()))
                        {
                            continue;//since a container can't be at 2 places at once skip container
                        }
                    }
                }
            }
        }




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

        auto getID = [&db](string query)
        {
            MYSQL_RES* res = db.select(query);
            MYSQL_ROW row;
            int ID = -1;
            while((row = mysql_fetch_row(res)) != NULL){
                ID = atoi(row[0]);
            }
            mysql_free_result(res);
            return ID;
        };

        //Owner
        string select_ownerID = "SELECT ownerID FROM Owner WHERE name = '"+owner_Name+"';";
        string insert_Owner = "INSERT INTO Owner(name) VALUES('"+owner_Name+"')";
        ownerID = getID(select_ownerID);
        if(ownerID == -1)
        {
            if(db.execute(insert_Owner))
            {
                ownerID = getID(select_ownerID);
            }
            else return false;
        }

        //Size
        string select_sizeID = "SELECT sizeID FROM Size WHERE length = '"+length+"' AND width = '"+width+"' AND height = '"+height+"';";
        string insert_Size = "INSERT INTO Size(length, width, height) VALUES('"+length+"', '"+width+"', '"+height+"')";
        sizeID = getID(select_sizeID);
        if(sizeID == -1)
        {
            if(db.execute(insert_Size))
            {
                sizeID = getID(select_sizeID);
            }
            else return false;
        }

        //Content
        string select_contentID = "SELECT contentID FROM Content WHERE name = '"+content_Name+"' AND type = '"+content_Type+"' AND danger = '"+content_Danger+"';";
        string insert_Content = "INSERT INTO Content(name, type, danger) VALUES('"+content_Name+"', '"+content_Type+"', '"+content_Danger+"')";
        contentID = getID(select_contentID);
        if(contentID == -1)
        {
            if(db.execute(insert_Content))
            {
                contentID = getID(select_contentID);
            }
            else return false;
        }

        //ArrivalShippingType
        string select_arrivalShippingTypeID = "SELECT shippingTypeID FROM ShippingType WHERE sort = '"+type_Transport_Arrival+"';";
        string insert_ArrivalShippingType = "INSERT INTO ShippingType(sort) VALUES('"+type_Transport_Arrival+"')";
        arrivalShippingTypeID = getID(select_arrivalShippingTypeID);
        if(arrivalShippingTypeID == -1)
        {
            if(db.execute(insert_ArrivalShippingType))
            {
                arrivalShippingTypeID = getID(select_arrivalShippingTypeID);
            }
            else return false;
        }

        //ArrivalCompany
        string select_arrivalCompanyID = "SELECT shippingCompanyID FROM ShippingCompany WHERE name = '"+company_Arrival+"';";
        string insert_ArrivalShippingCompany = "INSERT INTO ShippingCompany(name) VALUES('"+company_Arrival+"')";
        arrivalCompanyID = getID(select_arrivalCompanyID);
        if(arrivalCompanyID == -1)
        {
            if(db.execute(insert_ArrivalShippingCompany))
            {
                arrivalCompanyID = getID(select_arrivalCompanyID);
            }
            else return false;
        }

        //DepartureShippingType
        string select_departureShippingTypeID = "SELECT shippingTypeID FROM ShippingType WHERE sort = '"+type_Transport_Departure+"';";
        string insert_DepartureShippingType = "INSERT INTO ShippingType(sort) VALUES('"+type_Transport_Departure+"')";
        departureShippingTypeID = getID(select_departureShippingTypeID);
        if(departureShippingTypeID == -1)
        {
            if(db.execute(insert_DepartureShippingType))
            {
                departureShippingTypeID = getID(select_departureShippingTypeID);
            }
            else return false;
        }

        //DepartureCompany
        string select_departureCompanyID = "SELECT shippingCompanyID FROM ShippingCompany WHERE name = '"+company_Departure+"';";
        string insert_DepartureShippingCompany = "INSERT INTO ShippingCompany(name) VALUES('"+company_Departure+"')";
        departureCompanyID = getID(select_departureCompanyID);
        if(departureCompanyID == -1)
        {
            if(db.execute(insert_DepartureShippingCompany))
            {
                departureCompanyID = getID(select_departureCompanyID);
            }
            else return false;
        }

        //ArrivalShipment
        string select_arrivalShipmentID = "SELECT shipmentID FROM Arrival WHERE date = '"+arrival_Date+"' AND timeFrom = '"+from_Arrival+"' AND timeTill = '"+till_Arrival+"' AND positionX = "+pos_X_Arrival+" AND positionY = "+pos_Y_Arrival+" AND positionZ = "+pos_Z_Arrival+" AND shippingType = "+to_string(arrivalShippingTypeID)+" AND shippingCompany = "+to_string(arrivalCompanyID)+";";
        string insert_Arrival = "INSERT INTO Arrival(date, timeFrom, timeTill, positionX, positionY, positionZ, shippingType, shippingCompany) VALUES('"+arrival_Date+"', '"+from_Arrival+"', '"+till_Arrival+"', "+pos_X_Arrival+", "+pos_Y_Arrival+", "+pos_Z_Arrival+", "+to_string(arrivalShippingTypeID)+", "+to_string(arrivalCompanyID)+")";
        arrivalShipmentID = getID(select_arrivalShipmentID);
        if(arrivalShipmentID == -1)
        {
            if(db.execute(insert_Arrival))
            {
                arrivalShipmentID = getID(select_arrivalShipmentID);
            }
            else return false;
        }

        //DepartureShipment
        string select_departureShipmentID = "SELECT shipmentID FROM Departure WHERE date = '"+departure_Date+"' AND timeFrom = '"+from_Departure+"' AND timeTill = '"+till_Departure+"' AND shippingType = "+to_string(departureShippingTypeID)+" AND shippingCompany = "+to_string(departureCompanyID)+";";
        string insert_Departure = "INSERT INTO Departure(date, timeFrom, timeTill, shippingType, shippingCompany) VALUES('"+departure_Date+"', '"+from_Departure+"', '"+till_Departure+"', "+to_string(departureShippingTypeID)+", "+to_string(departureCompanyID)+")";
        departureShipmentID = getID(select_departureShipmentID);
        if(departureShipmentID == -1)
        {
            if(db.execute(insert_Departure))
            {
                departureShipmentID = getID(select_departureShipmentID);
            }
            else return false;
        }

        //Container
        string select_containerID = "SELECT containerID FROM Container WHERE containerNr = "+containerNr+" AND iso = '"+iso+"' AND weightEmpty = "+empty_Weight+" AND weightContents = "+content_Weight+" AND owner = "+to_string(ownerID)+" AND size = "+to_string(sizeID)+" AND contents = "+to_string(contentID)+" AND arrivalInfo = "+to_string(arrivalShipmentID)+" AND departureInfo = "+to_string(departureShipmentID)+";";
        string insert_Container = "INSERT INTO Container(containerNr, iso, weightEmpty, weightContents, owner, size, contents, arrivalInfo, departureInfo) VALUES("+containerNr+", '"+iso+"', "+empty_Weight+", "+content_Weight+", "+to_string(ownerID)+", "+to_string(sizeID)+", "+to_string(contentID)+", "+to_string(arrivalShipmentID)+", "+to_string(departureShipmentID)+")";
        containerID = getID(select_containerID);
        if(containerID == -1)
        {
            if(db.execute(insert_Container))
            {
                containerID = getID(select_containerID);
            }
            else return false;
        }
    }
    theFile.close();
    return true;
}

//checks validity of given xmlfiles
bool XmlParser::checkData(vector<string> &xmlPaths, Database &db)
{
    db.resetDatabase();
    string outFile = "../tempXML.xml";
    remove(outFile.c_str());
    ofstream outputFile(outFile);
    regex regNode("<record id=  \"id[0-9]+\"><aankomst><datum><d>[0-9]+</d><m>[0-9]+</m><j>[0-9]+</j></datum><tijd><van>[0-9]{1,2}\\.[0-9]{2}</van><tot>[0-9]{1,2}\\.[0-9]{2}</tot></tijd><soort_vervoer>[a-zA-Z]+</soort_vervoer><bedrijf>[a-zA-Z0-9]+</bedrijf><positie><x>[0-9]+</x><y>[0-9]+</y><z>[0-9]+</z></positie></aankomst><eigenaar><naam>[a-zA-Z0-9]+</naam><containernr>[0-9]+</containernr></eigenaar><vertrek><datum><d>[0-9]+</d><m>[0-9]+</m><j>[0-9]+</j></datum><tijd><van>[0-9]{1,2}\\.[0-9]{2}</van><tot>[0-9]{1,2}\\.[0-9]{2}</tot></tijd><soort_vervoer>[a-zA-Z]+</soort_vervoer><bedrijf>[a-zA-Z0-9]+</bedrijf></vertrek><afmetingen><l>[0-9']+</l><b>[0-9']+</b><h>[0-9']+</h></afmetingen><gewicht><leeg>[0-9]+</leeg><inhoud>[0-9]+</inhoud></gewicht><inhoud><naam>[a-zA-Z]+</naam><soort>[a-zA-Z]+</soort><gevaar>[a-zA-Z]+</gevaar></inhoud><ISO>[0-9-]+</ISO></record>");
    regex regEndNode("</record>");
    regex regStart("<recordset>");
    regex regStop("</recordset>");
    bool started;
    string line, node;


    for(uint i = 0; i<xmlPaths.size(); i++)
    {
        string path = xmlPaths[i];
        ifstream input(path);
        started = false;

        if (input)
        {
            while (getline(input, line))
            {
                if (!started)
                {
                    //if not started, look for start of xml
                    if (regex_search(line, regStart))
                    {
                        started = true;
                        if (i == 0)
                        {
                            //OUTPUT line to file
                            outputFile<<line<<endl;
                            //cout<<line<<endl;
                        }
                    }
                }
                else //If started
                {
                    if (regex_search(line, regStop))
                    {
                        //At end of recordset
                        if (i == (xmlPaths.size()-1))//If at last xmlfile, do end resultset
                        {
                            // OUTPUT line to file
                            outputFile<<line<<endl;
                            //cout<<line<<endl;
                        }
                        //If end of recordset, stop while loop
                        break;
                    }

                    if (regex_search(line, regEndNode))
                    {
                        //if at end of node, check if node is complete
                        node = node + line;
                        if (regex_search(node, regNode))//Add ! to return incorrect xml nodes
                        {
                            //OUTPUT node to file
                            outputFile<<node<<endl;
                            //cout<<node<<endl;
                        }
                        else
                        {
                            cout<<"foutieve node tegen gekomen"<<endl;
                        }
                        node = "";
                    }
                    else//if not at end of set or node
                    {
                        node = node + line;
                    }
                }
            }
        }
        input.close();
        cout<<"check of: \""<<path<<"\" done"<<endl;
    }
    outputFile.close();
    return processData(outFile, db);
}
