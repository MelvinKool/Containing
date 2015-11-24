SOURCES=main.cpp connections.cpp server.cpp socket.cpp agv.cpp container.cpp crane.cpp \
shortestPath.cpp transfer.cpp transport.cpp xmlparser.cpp database.cpp

#============================================
CC=g++ -std=c++11
CFLAGS=-c -Wall
# -I/lib/Containing/include
LDFLAGS=-pthread
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=Server
LIBS=-l sqlite3
#-L/lib/Containing/lib -lmysqlcppconn -lmysqlcppconn-static

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
	#bash run.sh

force: clean $(EXECUTABLE)
