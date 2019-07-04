cmake_minimum_required(VERSION 3.1)

project(transmission)

set(FLAGS "-ggdb -fsanitize=address -fno-omit-frame-pointer -fsanitize=undefined -Wall -Wpedantic -Wextra")
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${FLAGS}")

set(EXECUTABLE_OUTPUT_PATH ${CMAKE_SOURCE_DIR}/bin)
set(LIBRARY_OUTPUT_PATH ${CMAKE_SOURCE_DIR}/bin)

include_directories(include/)

add_library(transmission
    src/packet.cpp
    src/pprintf.cpp
    src/server.cpp
    src/serial.cpp
    src/util.cpp
)

add_executable(rvc src/compiler.cpp)
target_link_libraries(rvc transmission)

add_executable(main src/main.cpp)
target_link_libraries(main transmission)

add_executable(hexdump src/hexdump.cpp)
target_link_libraries(hexdump transmission)

add_executable(play src/play.cpp)
target_link_libraries(play transmission)
