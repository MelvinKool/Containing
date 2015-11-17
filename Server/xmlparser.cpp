#include "xmlparser.h"
#include "rapidxml.hpp"
#include "rapidxml_utils.hpp"
#include <iostream>
#include <fstream>
#include <vector>

using namespace rapidxml;
using namespace std;
/*
//places data in the database
void xmlparser::process_Data(database db){

}

//reads an xml node
void xmlparser::read_XML(){

}*/

int main(){
	xml_document<> doc; //create xml_document object
	ifstream theFile ("../INFO/XML/xml1.xml");
	vector<char> buffer((istreambuf_iterator<char>(theFile)), istreambuf_iterator<char>());
	buffer.push_back('\0');
	doc.parse<0>(&buffer[0]); //parse the contents of file
	xml_node<> * root = doc.first_node("recordset");//find our root node
	xml_node<> * test = root->first_node("record");
	cout << test->value() << endl;
	//xml_node<> * node1 = root->first_node("record"); //find our node1 node
	return 0;
}


