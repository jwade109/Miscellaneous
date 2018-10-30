#include <iostream>
#include "comm.hpp"

int main()
{
    rvt::timestamp_t now = rvt::now();
    std::cout << now/1000000000
        << "." << now%1000000000 << std::endl;
    return 0;
}
