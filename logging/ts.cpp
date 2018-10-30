#include <iostream>
#include <iomanip>
#include "comm.hpp"

int main()
{
    while (true)
    {
        rvt::timestamp_t now = rvt::now();
        std::cout << "seconds since Jan 1st, 1970: "
            << now/1000000000
            << "." << std::setfill('0') << std::setw(9)
            << now % 1000000000 << "\r" << std::flush;
    }
    return 0;
}
