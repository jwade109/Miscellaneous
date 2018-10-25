#define DEBUG
#pragma pack(1)

#include "comm.hpp"
#include "messages.hpp"

int main(int argc, char** argv)
{
    rvt::sensor_raw sv{56, 101, 200, -54};
    rvt::motor_ctrl mc = {1, 2, 3, 4, 5, 6, 7, 8};

    std::cout << rvt::packet<rvt::sensor_raw>(sv).setTimestamp(345762450) << std::endl;
    std::cout << rvt::packet<rvt::motor_ctrl>(mc).setTimestamp(5) << std::endl;

    return 0;
}
