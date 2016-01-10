#ifndef JSONREADER_H
#define JSONREADER_H
#include <map>
#include "transport.h"
#include "Files/rapidjson/document.h"
#include "Files/rapidjson/rapidjson.h"

class JSONReader{
    private:
        char* transportFPath;
    public:
        JSONReader(char* transportFPath);
        void loadTransport(std::map<int,Transport>& transportMap);
    private:
        template <class T>
        std::string toString(T &jsonValue);
        void 
        //std::string toString(rapidjson::Value &jsonValue);
};
#endif
