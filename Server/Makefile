SOURCES=main.cpp connections.cpp server.cpp socket.cpp agv.cpp container.cpp crane.cpp \
shortestPath.cpp transfer.cpp transport.cpp xmlparser.cpp
#database.cpp

#============================================
CC=g++ -std=c++11
CFLAGS=-c -I./include -Wall
LDFLAGS=-pthread
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=Server
LIBS=
#-L./lib -lmysqlcppconn -lmysqlcppconn-static

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