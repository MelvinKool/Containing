//#include "xmlparser.h"
#include "rapidxml.hpp"
#include "rapidxml_utils.hpp"
#include <string>
#include <iostream>
#include <fstream>
#include <vector>

using namespace rapidxml;
using namespace std;
/*
//places data in the database
void xmlparser::process_Data(database db)
{

}

//reads an xml node
void xmlparser::read_XML()
{

}*/

int main()
{
	xml_document<> doc; //create xml_document object
	ifstream theFile ("../INFO/XML/xml1.xml");
	vector<char> buffer((istreambuf_iterator<char>(theFile)), istreambuf_iterator<char>());
	buffer.push_back('\0');
	doc.parse<0>(&buffer[0]); //parse the contents of file
	xml_node<> * root = doc.first_node("recordset");//find our root node
	for (xml_node<> *record = root->first_node(); record; record = record->next_sibling())
	{
		string iso = record->first_node("ISO")->value();
		//aankomst
		/////////////////////////////////////////////////////////////////////
		xml_node<> * arrival = record->first_node("aankomst");
		string type_Transport = arrival->first_node("soort_vervoer")->value();
		string company = arrival->first_node("bedrijf")->value();
		//date
		xml_node<> * date = arrival->first_node("datum");
		string day = date->first_node("d")->value();
		string month = date->first_node("m")->value();
		string year = date->first_node("j")->value();
		//time
		xml_node<> * time = arrival->first_node("tijd");
		string from = time->first_node("van")->value();
		string till = time->first_node("tot")->value();
		//position
		xml_node<> * position = arrival->first_node("positie");
		string pos_X, pos_Y, pos_Z;
		pos_X = position->first_node("x")->value();
		pos_Y = position->first_node("y")->value();
		pos_Z = position->first_node("z")->value();
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
		/////////////////////////////////////////////////////////////////////
		xml_node<> * vertrek = record->first_node("vertrek");
		for (xml_node<> *child = vertrek->first_node(); child; child = child->next_sibling())
		{
		
		}
		xml_node<> * afmetingen = record->first_node("afmetingen");
		for (xml_node<> *child = afmetingen->first_node(); child; child = child->next_sibling())
		{
		
		}
		xml_node<> * gewicht = record->first_node("gewicht");
		for (xml_node<> *child = gewicht->first_node(); child; child = child->next_sibling())
		{
		
		}
		xml_node<> * inhoud = record->first_node("inhoud");
		for (xml_node<> *child = inhoud->first_node(); child; child = child->next_sibling())
		{
		
		}
	}
	//cout << doc << endl;
	//xml_node<> * node1 = root->first_node("record"); //find our node1 node
	return 0;
	
}


