#ifndef XMLPARSER_H
#define XMLPARSER_H

#include "database.h"
#include <vector>
#include <string>

class xmlparser
{
    private:
        bool process_Data(std::vector<std::string> &xmlDocPaths, database *db);
    public:
        void read_XML(database *db);
};

#endif // XMLPARSER_H