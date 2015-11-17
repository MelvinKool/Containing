SOURCES=main.cpp connections.cpp server.cpp socket.cpp

#============================================

CC=g++ -std=c++11
CFLAGS=-c -Wall
LDFLAGS=
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=Server
LIBS=-pthread

all: $(SOURCES) $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS)
	$(CC) $(LDFLAGS) $(OBJECTS) -o $@ $(LIBS)

.cpp.o:
	$(CC) $(CFLAGS) $< -o $@

clean:
	touch tmp.o
	rm *.o $(EXECUTABLE)