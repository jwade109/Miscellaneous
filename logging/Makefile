CFLAGS=-std=c++11 -Wall -Wpedantic -Wextra -g

all: write read ts

write: write.cpp comm.hpp messages.hpp arduino_msg.hpp
	g++ write.cpp $(CFLAGS) -o write

read: read.cpp comm.hpp messages.hpp arduino_msg.hpp
	g++ read.cpp $(CFLAGS) -o read

ts: ts.cpp comm.hpp
	g++ ts.cpp $(CFLAGS) -o ts

.PHONY: clean

clean:
	rm write read ts 2>/dev/null
