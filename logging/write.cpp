#include <fstream>

#include "comm.hpp"
#include "messages.hpp"

int main(int argc, char** argv)
{
    rvt::sensor_raw sv = {56, 101, 200, -54};
    rvt::motor_ctrl mc = {1, 2, 3, 4, 5, 6, 7, 8};
    rvt::string_msg str = {{'v','a','p','o','r','w','a','v','e','.'}};

    auto p1 = rvt::packet<rvt::sensor_raw>(sv);
    auto p2 = rvt::packet<rvt::motor_ctrl>(mc);
    auto p3 = rvt::packet<rvt::string_msg>(str);

    rvt::archive ar;
    ar.addPacket(p1.setTimestamp(rvt::now()));
    ar.addPacket(p2.setTimestamp(rvt::now()));
    ar.addPacket(p2.setTimestamp(rvt::now()));
    ar.addPacket(p3.setTimestamp(rvt::now()));
    ar.addPacket(p3.setTimestamp(rvt::now()));
    ar.addPacket(p3.setTimestamp(rvt::now()));

    std::cout << p1 << std::endl << p2 << std::endl << p3 << std::endl;

    ar.toFile("out.bin");

    return 0;
}
