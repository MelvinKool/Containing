SOURCES=main.cpp server.cpp connections.cpp socket.cpp xmlparser.cpp \
database.cpp JSONGenerator.cpp httpserver.cpp vector3f.cpp shortestPathDijkstra.cpp \
transport.cpp agv.cpp crane.cpp JSONReader.cpp Timer.cpp

#============================================
ifeq ($(shell expr `gcc -dumpversion | cut -f1 -d.` \>= 5),0)
  CC=g++-5 -std=c++11
else
  CC=g++ -std=c++11
endif

CFLAGS=-c $(shell mysql_config --cflags)
LDFLAGS=-pthread
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=Server
LIBS=$(shell mysql_config --libs)

debugger=gdb
deleaker=valgrind
deleakflags=--leak-check=full --show-leak-kinds=all

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

debug: $(EXECUTABLE)
	$(debugger) ./$(EXECUTABLE)

leaktest:$(EXECUTABLE)
	$(deleaker) $(deleakflags) ./$(EXECUTABLE)
