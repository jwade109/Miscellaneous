// main.cpp

#include <transmission/packet.hpp>
#include <transmission/interpreter.hpp>

#include <string>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <thread>
#include <fstream>

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
        if (line[0] == '#') continue; // comments

        std::stringstream linestream(line);

        rvt::packet pack;
        try
        {
            uint16_t id;
            linestream >> id;
            std::string formatted(linestream.str());
            pack = rvt::str2packet(std::chrono::system_clock::now(), formatted);
            pack.id = id;
        }
        catch (std::exception)
        {
            std::cout << filename << ": error parsing line "
                << linenumber << ":\n > " << line << std::endl;
            return 1;
        }
        std::cout << rvt::packet2str(pack, interp.get_format(pack.id)) << std::endl;
        interp.call(pack);
        std::this_thread::sleep_for(std::chrono::milliseconds(100));
    }
    std::cout << interp << std::endl;

    return 0;
}


