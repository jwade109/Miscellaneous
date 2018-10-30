#include <fstream>

#include "comm.hpp"
#include "messages.hpp"
#include "arduino_msg.hpp"

int main(int argc, char** argv)
{
    std::string filename = "out.bin";
    if (argc > 1)
        filename = std::string(argv[1]);

    rvt::sensor_raw sv = {56, 101, 200, -54};
    rvt::motor_ctrl mc = {1, 2, 3, 4, 5, 6, 7, 8};
    rvt::string_msg str = {"THE ROCKET IS GOING VERY FAST."};
    rvt::echo_firmware ef;
    rvt::mode_response mr = {3};

    auto p1 = rvt::makePacket(sv);
    auto p2 = rvt::makePacket(mc);
    auto p3 = rvt::makePacket(str);
    auto p4 = rvt::makePacket(ef);
    auto p5 = rvt::makePacket(mr);

    rvt::archive ar;
    ar.addPacket(p1.setTimestamp(rvt::now()));
    ar.addPacket(p2.setTimestamp(rvt::now()));
    ar.addPacket(p2.setTimestamp(rvt::now()));
    ar.addPacket(p3.setTimestamp(rvt::now()));
    ar.addPacket(p3.setTimestamp(rvt::now()));
    ar.addPacket(p3.setTimestamp(rvt::now()));
    ar.addPacket(p4.setTimestamp(rvt::now()));
    ar.addPacket(p5.setTimestamp(rvt::now()));

    ar.write(filename);

    return 0;
}
