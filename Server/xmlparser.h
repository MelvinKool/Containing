#ifndef XMLPARSER_H
#define XMLPARSER_H

#include "database.h"
#include <vector>
#include <string>
#include <iostream>
#include <regex>
#include <algorithm>
#include <fstream>
#include <stdio.h>


class XmlParser
{
    public:
        void readXML(Database &db);
    private:
        bool processData(std::string &xmlDocPath, Database &db);
        bool checkData(std::vector<std::string> &xmlPaths, Database &db);
};

#endif //XMLPARSER_H
