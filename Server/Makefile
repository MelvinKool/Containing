SOURCES=main.cpp connections.cpp server.cpp socket.cpp agv.cpp container.cpp \
crane.cpp shortestPath.cpp transfer.cpp transport.cpp xmlparser.cpp \
database.cpp JSONGenerator.cpp httpserver.cpp

#============================================
CC=g++ -std=c++11
CFLAGS=-c -Wall $(shell mysql_config --cflags)
LDFLAGS=-pthread
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=Server
LIBS=$(shell mysql_config --libs)

all: $(SOURCES) $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS)
	$(CC) $(LDFLAGS) $(OBJECTS) -o $@ $(LIBS)

.cpp.o:
	$(CC) $(CFLAGS) $< -o $@

clean:
	touch tmp.o
	touch $(EXECUTABLE).tmp
	rm *.o $(EXECUTABLE)*
	clear

run: $(EXECUTABLE)
	./$(EXECUTABLE)

force: clean $(EXECUTABLE)