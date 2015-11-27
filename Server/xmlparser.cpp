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

            //time
            xml_node<> * time_Arrival = arrival->first_node("tijd");
                string from_Arrival = time_Arrival->first_node("van")->value();
                string till_Arrival = time_Arrival->first_node("tot")->value();

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

            //time
            xml_node<> * time_Departure = departure->first_node("tijd");
                string from_Departure = time_Departure->first_node("van")->value();
                string till_Departure = time_Departure->first_node("tot")->value();
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
            
            //empty db
            
            MYSQL_RES* res = select("show tables;");
            MYSQL_ROW row;
            while((row = mysql_fetch_row(res)) != NULL){
                cout << row[0] << endl;
            }
            mysql_free_result(res);
            
            //db->execute("");
        }
        theFile.close();
    }
    return true;
}