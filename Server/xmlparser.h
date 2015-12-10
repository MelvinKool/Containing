#ifndef XMLPARSER_H
#define XMLPARSER_H

#include "database.h"
#include <vector>
#include <string>
#include <iostream>
#include <regex>
#include <algorithm>

class XmlParser
{
    private:
        bool processData(std::vector<std::string> &xmlDocPaths, Database &db);
        int checkData(std::vector<std::string> &xmlPaths);
    public:
        void readXML(Database &db);
};

#endif //XMLPARSER_H
