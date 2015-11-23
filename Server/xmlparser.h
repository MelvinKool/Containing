#ifndef XMLPARSER_H
#define XMLPARSER_H

//#include "database.h"
#include <vector>
#include <string>

class xmlparser
{
	private:
		void process_Data(std::vector<std::string> &xmlDocPaths);
		std::vector<std::string> data;
	public:
		xmlparser();
        void read_XML();
};

#endif // XMLPARSER_H
