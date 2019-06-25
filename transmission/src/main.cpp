// main.cpp

#include <transmission/util.hpp>
#include <transmission/packet.hpp>
#include <transmission/interpreter.hpp>

#include <string>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <thread>
#include <fstream>

namespace rvt
{

const std::string underline = "\033[4m";
const std::string reverse = "\033[7m";
const std::string red = "\033[31;40m";
const std::string clear = "\033[0m";

}

int main(int argc, char **argv)
{
    rvt::interpreter interp;

    auto now = std::chrono::system_clock::now();
    auto time = std::chrono::system_clock::to_time_t(now);
    std::stringstream datestr;
    datestr << std::put_time(std::gmtime(&time), "%Y-%m-%d-%H-%M-%S");
    interp.context["date"] = datestr.str();

    std::string filename = "params.txt";
    std::ifstream paramfile(filename);

    std::string line;
    size_t linenumber = 1;
    for (; std::getline(paramfile, line); linenumber++)
    {
        if (line.length() == 0) continue;
        if (rvt::begins_with(line, "//")) continue; // comments

        rvt::packet pack;
        try
        {
            pack = rvt::str2packet(std::chrono::system_clock::now(), line);
            std::cout << rvt::packet2str(pack, interp.get_format(pack.id)) << std::endl;
            interp.call(pack);
        }
        catch (const std::invalid_argument &e)
        {
            std::cerr << rvt::red << filename << ": error parsing line "
                << linenumber << " (invalid argument: " << e.what() << "):\n\n   "
                << line << rvt::clear << "\n" << std::endl;
        }
        // std::this_thread::sleep_for(std::chrono::milliseconds(100));
    }
    std::cout << interp << std::endl;

    for (auto p : interp.history)
    {
        std::cout << p << std::endl;    
    }

    return 0;
}


