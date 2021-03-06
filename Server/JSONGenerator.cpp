#include <iostream>
#include <string>
#include <vector>
#include "vector3f.h"
#include "JSONGenerator.h"

using namespace std;
using namespace rapidjson;

//creates a default json document
rapidjson::Document JSONGenerator::createJSONDocument()
{
	// document is the root of a json message
	rapidjson::Document document;
	// define the document as an object rather than an array
	document.SetObject();
	return document;
}

template<class Allocator>
rapidjson::Value* document_to_value(rapidjson::Document & document, Allocator & alloc) {
  rapidjson::Value * rtn = new rapidjson::Value();

  if(document.IsObject()) {

    rtn->SetObject();
    for (rapidjson::Value::MemberIterator itr = document.MemberBegin(); itr != document.MemberEnd(); ++itr) {
      rtn->AddMember(itr->name, itr->value, alloc);
    }
    return rtn;

  } else if (document.IsArray()) {

    rtn->SetArray();
    for (int64_t i = 0; i < document.Size(); i++) {
      rtn->PushBack(document[i], alloc);
    }
    return rtn;

  } else {

    delete rtn;
    return NULL;

  }
}

//generate JSON-string telling a crane what container to transfer to an AGV
/*
    craneId:        Id of crane that has to mave the container
    containerId:    Vector of coordinates of destination
    destAGV:        Id of destination AGV
*/
string JSONGenerator::craneTransferContainer(int craneId, int containerId, int destAGV)
{
	//{'Command': 'craneMoveContainer', 'craneId': 157, 'containerId': 1, 'target': 89}
	rapidjson::Document document = createJSONDocument();
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "craneMoveContainer", allocator);
	document.AddMember("craneId", craneId, allocator);
	document.AddMember("containerId", containerId, allocator);
	document.AddMember("target", destAGV, allocator);
    return toString(document);
}

//generates JSON for doing a container transfer between crane and a sortfield
/*
    craneId:        Id of crane that moves container
    containerId:    Id of cantainer being moved
    sortFieldId:    Id of destination sortfield
    targetVect:     vector of relative destination in sortfield
*/
string JSONGenerator::craneTransferContainer(int craneId, int containerId, int sortFieldId, vector3f targetVect)
{
	//{'Command': 'craneMoveContainer', 'craneId': 30, 'containerId': 1, 'sortField': 22, 'target': [0,0,0]}
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "craneMoveContainer", allocator);
	document.AddMember("craneId", craneId, allocator);
	document.AddMember("containerId", containerId, allocator);
	document.AddMember("sortField",sortFieldId, allocator);
	rapidjson::Value target(rapidjson::kArrayType);
	target.PushBack(targetVect.getX(), allocator).PushBack(targetVect.getY(), allocator).PushBack(targetVect.getZ(),allocator);
	document.AddMember("target", target, allocator);
	return toString(document);
}

//Method for attaching a container to an AGV
/*
    agvId:          Id of destination AGV
    containerId:    Id of container being attatched
*/
string JSONGenerator::agvAttachContainer(int agvId, int containerId)
{
	//{'Command': 'agvAttachContainer', 'agvId': 90, 'containerId': 2}
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command","agvAttachContainer", allocator);
	document.AddMember("agvId", agvId, allocator);
	document.AddMember("containerId", containerId, allocator);
	return toString(document);
}

//generates JSON for spawning seaship
//type: "SeaShip"
/*
    contIds:    List of containers of ship or train
    shipId:     Id for new ship
*/
string JSONGenerator::spawnSeaShip(vector<int> contIds,int shipId)
{
	//{'Command': 'spawnShip', 'containers': [2,56,84,12,54]}
	rapidjson::Document document = createJSONDocument();
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command","spawnSeaShip", allocator);
	document.AddMember("id", shipId, allocator);
	rapidjson::Value containers(rapidjson::kArrayType);
	for(int i : contIds)
	{
		containers.PushBack(i,allocator);
	}
	document.AddMember("containers", containers, allocator);
    return toString(document);
}

//generates JSON for spawning bargeships
//type: "BargeShip"
string JSONGenerator::spawnBargeShip(vector3f location,vector<int> contIDs,int shipID)
{
	//{'Command': 'spawnShip', 'containers': [i for i in range(5, 40)]}
	//"{"Command" : "spawnShip", "position", [15.0,0.0,13.75], "id",1, "containers", [1,2,3,4,5,6,7]}"
	rapidjson::Document document = createJSONDocument();
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command","spawnBargeShip", allocator);
	rapidjson::Value position(rapidjson::kArrayType);
	position.PushBack(location.getX(), allocator).PushBack(location.getY(), allocator).PushBack(location.getZ(), allocator);
	document.AddMember("position",position,allocator);
	document.AddMember("id", shipID, allocator);
	rapidjson::Value containers(rapidjson::kArrayType);
	for(int i : contIDs)
	{
		containers.PushBack(i,allocator);
	}
	document.AddMember("containers", containers, allocator);
    return toString(document);
}

//generates JSON for spawning train
string JSONGenerator::spawnTrain(vector<int> contIDs,int trainID)
{
	//{'Command': 'spawnTrain', 'containers': [i for i in range(5, 40)]}
	rapidjson::Document document = createJSONDocument();
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command","spawnTrain", allocator);
	document.AddMember("id", trainID, allocator);
	rapidjson::Value containers(rapidjson::kArrayType);
	for(int i : contIDs)
	{
		containers.PushBack(i,allocator);
	}
	document.AddMember("containers", containers, allocator);
	return toString(document);
}

//generates JSON for spawning trucks
string JSONGenerator::spawnTruck(vector3f location, int contID, int truckId)
{
	//{'Command': 'spawnTruck', 'position': [835.75, 0, 0], 'container': 1, 'id': 0}
	rapidjson::Document document = createJSONDocument();
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command","spawnTruck", allocator);
	rapidjson::Value position(rapidjson::kArrayType);
	position.PushBack(location.getX(), allocator).PushBack(location.getY(), allocator).PushBack(location.getZ(), allocator);
	document.AddMember("position",position,allocator);
	document.AddMember("id", truckId, allocator);
	document.AddMember("container", contID, allocator);
	return toString(document);
}

//JSON for crane
//PUT THOSE OBJECTS IN CRANE CLASS
//(int objectId, const char* vehicleType, vector3f coordinate, float maximumSpeed,
//float holderSpeed, float grabberSpeed, float grabber_y_offset, vector3f grabberPos, bool has_holder)
//vector3f rotation???
string JSONGenerator::spawnCrane(Crane& crane, int craneId,vector3f rotation)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("id",craneId, allocator);
	Value s;
	s.SetString(crane.vehicleType.c_str(),strlen(crane.vehicleType.c_str()),allocator);    // can contain null character, length derived at compile time
	document.AddMember("type", s ,allocator);
	document.AddMember("speed", crane.unloaded_Speed, allocator);
	rapidjson::Value spawnLocation(rapidjson::kArrayType);
	spawnLocation.PushBack(crane.currentLocation.getX(), allocator)
				.PushBack(crane.currentLocation.getY(), allocator)
				.PushBack(crane.currentLocation.getZ(),allocator);
	document.AddMember("position",spawnLocation,allocator);
	rapidjson::Value spawnRotation(rapidjson::kArrayType);
	spawnRotation.PushBack(rotation.getX(), allocator)
				.PushBack(rotation.getY(), allocator)
				.PushBack(rotation.getZ(),allocator);
	document.AddMember("rotation",spawnRotation,allocator);
	rapidjson::Value grabber(rapidjson::kObjectType);
	grabber.AddMember("holderSpeed",crane.currentGrabber.holderSpeed, allocator);
	//grabberPos
	rapidjson::Value grabberPositon(rapidjson::kArrayType);
	grabberPositon.PushBack(crane.currentGrabber.position.getX(), allocator)
				.PushBack(crane.currentGrabber.position.getY(), allocator)
				.PushBack(crane.currentGrabber.position.getZ(),allocator);
	grabber.AddMember("position", grabberPositon, allocator);
	grabber.AddMember("speed",crane.currentGrabber.speed,allocator);
	grabber.AddMember("y_offset",crane.currentGrabber.y_offset,allocator);
	grabber.AddMember("has_holder",crane.currentGrabber.has_holder, allocator);
	document.AddMember("grabber", grabber, allocator);
	return toString(document);
}


//JSON for crane
//PUT THOSE OBJECTS IN CRANE CLASS
//(int objectId, const char* vehicleType, vector3f coordinate, float maximumSpeed,
//float holderSpeed, float grabberSpeed, float grabber_y_offset, vector3f grabberPos, bool has_holder)
string JSONGenerator::spawnAGV(AGV& agv, int agvId, vector3f rotation)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("id",agvId, allocator);
	Value s;
	s.SetString("AGV",strlen("AGV"),allocator);    // can contain null character, length derived at compile time
	//s = vehicleType.c_str();
	document.AddMember("type", s ,allocator);
	document.AddMember("speed", agv.unloaded_Speed, allocator);
	rapidjson::Value spawnLocation(rapidjson::kArrayType);
	spawnLocation.PushBack(agv.currentLocation.getX(), allocator)
				.PushBack(agv.currentLocation.getY(), allocator)
				.PushBack(agv.currentLocation.getZ(),allocator);
	document.AddMember("position",spawnLocation,allocator);
	rapidjson::Value spawnRotation(rapidjson::kArrayType);
	spawnRotation.PushBack(rotation.getX(), allocator)
				.PushBack(rotation.getY(), allocator)
				.PushBack(rotation.getZ(),allocator);
	document.AddMember("rotation",spawnRotation,allocator);
	return toString(document);
}

std::string JSONGenerator::despawnObject(int transportID, const char* vehicleType, int containerId)
{
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "despawnVehicle", allocator);
	Value s;
	s.SetString(vehicleType,strlen(vehicleType),allocator);
	document.AddMember("vehicleType",s,allocator);
	document.AddMember("vehicleId", transportID, allocator);
	document.AddMember("containerId", containerId, allocator);
	return toString(document);
}

//spawn multiple objects with one JSON string
string JSONGenerator::spawnObjects(vector<string>& spawnStrings)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "spawnObjects", allocator);
	rapidjson::Value spawnCommandList(rapidjson::kArrayType);
	for(string spawnString : spawnStrings)
	{
		/*Document tempDocument;
		tempDocument.Parse(spawnString.c_str());*/
		Value s;
		const char* spawnString_c_str = spawnString.c_str();
		s.SetString(spawnString_c_str,strlen(spawnString_c_str),allocator);
		spawnCommandList.PushBack(s, allocator);
		/*Document tempDocument;
		tempDocument.Parse(spawnString.c_str());
		rapidjson::Document::AllocatorType& tempDocAllocator = tempDocument.GetAllocator();
		Value* tempValue = document_to_value(tempDocument, tempDocAllocator);
		//string toString(*tempValue);
		spawnCommandList.PushBack(*tempValue, allocator);*/
	}
	document.AddMember("objects", spawnCommandList, allocator);
	/*for (rapidjson::Value::MemberIterator itr = document.MemberBegin(); itr != document.MemberEnd(); ++itr) {
		cout <<"name: " << toString(itr->name) << " value " << toString(itr->value) << endl;
	  //rtn->AddMember(itr->name, itr->value, alloc);
  }*/
	return toString(document);
}

//generates JSON for moving a vehicle
string JSONGenerator::moveTo(int vehicleId, vector<vector3f> coordinates, float totalDistance, int containerId)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "moveAGV", allocator);
	document.AddMember("vehicleId", vehicleId, allocator);
	document.AddMember("container",containerId, allocator);
	// create a rapidjson array type with similar syntax to vector
	// chain methods as rapidjson provides a fluent interface when modifying its objects
	document.AddMember("totalDistance", totalDistance, allocator);
	rapidjson::Value route(rapidjson::kArrayType);
	for(vector3f coord: coordinates)
	{
		rapidjson::Value coordinate(rapidjson::kArrayType);
		coordinate.PushBack(coord.getX(), allocator).PushBack(coord.getY(), allocator).PushBack(coord.getZ(),allocator);
		route.PushBack(coordinate, allocator);
	}
	//add the route to the document
	document.AddMember("Route", route, allocator);
	//	fromScratch["object"]["hello"] = "Yourname";
	return toString(document);
}

string JSONGenerator::generateCommandList(int containerId, vector<string>& commandList)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command","containerCommands", allocator);
	document.AddMember("container", containerId, allocator);
	rapidjson::Value commandArray(rapidjson::kArrayType);
	for(string& s : commandList){
		Value commandStr;
		commandStr.SetString(s.c_str(),strlen(s.c_str()),allocator);
		commandArray.PushBack(commandStr, allocator);
	}
	document.AddMember("commands", commandArray, allocator);
	return toString(document);
}