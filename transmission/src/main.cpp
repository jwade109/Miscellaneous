// main.cpp

#include <transmission/util.hpp>
#include <transmission/packet.hpp>
#include <transmission/server.hpp>

#include <string>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <thread>
#include <fstream>
#include <chrono>
#include <deque>

namespace rvt
{

const std::string underline = "\033[4m";
const std::string reverse = "\033[7m";
const std::string red = "\033[31;40m";
const std::string clear = "\033[0m";

}

bool compile(const std::string &ifname,
             const std::string &ofname)
{
    std::ifstream infile(ifname);
    std::ofstream outfile(ofname, std::ios::binary);
    std::vector<uint8_t> output_buffer;

    std::string line;
    size_t linenumber = 1;
    for (; std::getline(infile, line); linenumber++)
    {
        if (line.length() == 0) continue;
        if (rvt::begins_with(line, "//")) continue; // comments

        try
        {
            auto pack = rvt::str2packet(
                std::chrono::system_clock::now(), line);
            output_buffer << pack;
            std::this_thread::sleep_for(std::chrono::milliseconds(40));
        }
        catch (const std::invalid_argument &e)
        {
            std::cerr << rvt::red << ifname << ": error parsing line "
                << linenumber << " (invalid argument: "
                << e.what() << "):\n\n   "
                << line << rvt::clear << "\n" << std::endl;
            return false;
        }
    }

    outfile.write(reinterpret_cast<char*>(output_buffer.data()),
                  output_buffer.size());
    return true;
}

int main(int argc, char **argv)
{
    rvt::server serv;

    auto now = std::chrono::system_clock::now();
    auto time = std::chrono::system_clock::to_time_t(now);
    std::stringstream datestr;
    datestr << std::put_time(std::gmtime(&time), "%Y-%m-%d-%H-%M-%S");
    serv.context["date"] = datestr.str();

    std::string filename = "params.txt";
    std::string binfile = "inbuf.bin";
    if (!compile(filename, binfile))
    {
        std::cerr << rvt::red << "Error compiling!"
            << rvt::clear << std::endl;
        return 1;
    }

    serv.load(binfile);

    std::cout << serv << std::endl;

    for (auto p : serv.history)
    {
        // std::cout << p << std::endl;    
    }

    return 0;
}


