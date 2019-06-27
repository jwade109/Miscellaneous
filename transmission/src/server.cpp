// server.cpp

#include <transmission/serial.hpp>
#include <transmission/server.hpp>

#include <iomanip>
#include <sstream>
#include <fstream>
#include <iterator>

namespace rvt
{

server::server() :

    history(),
    context{{"sync bytes", "0xAA 0x14"}},
    callbacks
    {
        {0, [] (server &serv, const packet &pack)
        {
            std::vector<uint8_t> data(pack.data);
            std::string key, value;
            data >> key >> value;
            if (key == "" || value == "") return 1;
            std::cout << "Add to context: " << key
                << " = " << value << std::endl;
            serv.context[key] = value;
            return 0;
        }},

        {1, [] (server &serv, const packet &pack)
        {
            std::vector<uint8_t> data(pack.data);
            uint16_t id;
            std::string format;
            data >> id >> format;
            std::cout << "Add to parse formats: "
                << id << " -> " << format << std::endl;
            serv.parse_formats[id] = format;
            return 0;
        }}
    } { }

void server::load(const std::string &ifname)
{
    std::ifstream infile(ifname, std::ios::binary);
    std::vector<uint8_t> bytes(
        std::istreambuf_iterator<char>{infile}, {});

    while (bytes.size() > 0)
    {
        packet pack;
        bytes >> pack;
        call(pack);
    }
}

int server::call(const packet& pack)
{
    history.push_back(pack);
    auto iter = callbacks.find(pack.id);
    if (iter != callbacks.end())
    {
        int ret = iter->second(*this, pack);
        std::cout << "Callback " << iter->first << " with packet ("
                  << packet2str(pack, get_format(pack.id)) << ")"
                  << " returned " << ret << std::endl;
        return ret;
    }
    return -1;
}

std::string server::get_format(uint16_t id) const
{
    auto iter = parse_formats.find(id);
    if (iter != parse_formats.end())
    {
        return iter->second;
    }
    return "";
}

std::ostream& operator << (std::ostream &os, const server &serv)
{
    std::stringstream ss;
    ss << "< Context >" << std::endl;
    for (auto e : serv.context)
    {
        ss << std::setw(20) << e.first << ": "
           << e.second << std::endl;
    }
    ss << "< Callbacks >" << std::endl;
    for (auto e : serv.callbacks)
    {
        ss << std::hex << std::right << std::setw(12) << e.first << ": "
           << std::left << e.second.target_type().name() << std::endl;
    }
    ss << "< Parse Formats >" << std::endl;
    for (auto e : serv.parse_formats)
    {
        ss << std::right << std::dec << std::setw(12) << e.first << ": "
           << std::left << e.second << std::endl;
    }
    ss << "< History >" << std::endl;
    for (int64_t i = 0; i < serv.history.size(); ++i)
    {
        auto pack = serv.history[i];
        ss << "  " << std::setw(4) << std::setfill('0')
           << std::hex << std::right << i << " "
           << packet2str(pack, serv.get_format(pack.id))
           << std::endl;
    }
    return os << ss.str();
}

} // namespace rvt
