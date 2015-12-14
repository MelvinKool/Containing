#ifndef XMLPARSER_H
#define XMLPARSER_H

#include "database.h"
#include <vector>
#include <string>

class XmlParser
{
    private:
        bool processData(std::vector<std::string> &xmlDocPaths, Database &db);
    public:
        void readXML(Database &db);
};

#endif //XMLPARSER_H