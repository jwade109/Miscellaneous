#include <fstream>

#include "comm.hpp"
#include "messages.hpp"

int main(int argc, char** argv)
{
    rvt::sensor_raw sv{56, 101, 200, -54};
    rvt::motor_ctrl mc = {1, 2, 3, 4, 5, 6, 7, 8};
    rvt::string_msg str = {{'v','a','p','o','r','w','a','v','e','.'}};

    auto p1 = rvt::packet<rvt::sensor_raw>(sv).setTimestamp(345762450);
    auto p2 = rvt::packet<rvt::motor_ctrl>(mc).setTimestamp(5001);
    auto p3 = rvt::packet<rvt::string_msg>(str).setTimestamp(99993333);

    std::cout << p1 << std::endl << p2 << std::endl << p3 << std::endl;

    auto s1 = p1.serialize();
    auto s2 = p2.serialize();
    auto s3 = p3.serialize();

    std::ofstream out("out.bin");
    
    out.write((const char*) s3.data(), decltype(p3)::total_length);
    for (int i = 0; i < 100; ++i)
    {
        out.write((const char*) s1.data(), decltype(p1)::total_length);
        out.write((const char*) s2.data(), decltype(p2)::total_length);
    }

    out.close();

    return 0;
}
