#include <fstream>
#include <vector>

#include "comm.hpp"
#include "messages.hpp"
#include "arduino_msg.hpp"

int main(int argc, char** argv)
{
    std::string filename = "out.bin";
    if (argc > 1)
        filename = std::string(argv[1]);

    rvt::archive ar;
    ar.read(filename);

    for (auto p : ar.getPackets<rvt::sensor_raw>())
        std::cout << p << std::endl;
    for (auto p : ar.getPackets<rvt::motor_ctrl>())
        std::cout << p << std::endl;
    for (auto p : ar.getPackets<rvt::string_msg>())
        std::cout << p << std::endl;
    for (auto p : ar.getPackets<rvt::mode_response>())
        std::cout << p << std::endl;
    for (auto p : ar.getPackets<rvt::echo_firmware>())
        std::cout << p << std::endl;

    return 0;
}
