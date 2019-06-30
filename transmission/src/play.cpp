
#include <transmission/packet.hpp>
#include <transmission/server.hpp>

#include <algorithm>

int main(int argc, char **argv)
{
    if (argc < 2)
    {
        std::cerr << std::string(argv[0]) << ": "
            << "requires a filename" << std::endl;
        return 1;
    }

    std::string infile(argv[1]);

    auto packets = rvt::fromFile(infile, 0xAA14);

    if (packets.size() == 0)
    {
        std::cerr << "No packets found." << std::endl;
        return 1;
    }

    std::sort(begin(packets), end(packets),
    [] (const rvt::packet& left, const rvt::packet& right)
    {
        return left.time() < right.time();
    });

    rvt::server serv;
    for (auto p : packets)
    {
        serv.call(p);
    }

    auto real_start = std::chrono::system_clock::now();
    auto sim_start = packets[0].time();

    for (auto p : serv.history)
    {
        auto sim_time = p.time();
        auto real_time = std::chrono::system_clock::now();
        while (real_time - real_start < sim_time - sim_start)
            real_time = std::chrono::system_clock::now();

        std::cout << pprintf(rvt::packet::print_format, p) << std::endl;
    }

    return 0;
}
