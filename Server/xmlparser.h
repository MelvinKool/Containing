#ifndef XMLPARSER_H
#define XMLPARSER_H

#include "database.h"
#include <vector>
#include <string>

class xmlparser
{
    private:
        void process_Data(std::vector<std::string> &xmlDocPaths, database *db);
        std::vector<std::string> data;
    public:
        void read_XML(database *db);
};

#endif // XMLPARSER_H
