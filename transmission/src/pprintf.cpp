// pretty.cpp

#include <transmission/util.hpp>
#include <transmission/packet.hpp>

#include <sstream>
#include <iomanip>

namespace rvt
{

std::string pprintf(const std::string& fmt, const packet& pack)
{
    std::string ret(fmt);
    
    auto insert_if_find = [&] (const std::string &escape,
                               const std::string &str)
    {
        size_t pos = ret.find(escape);
        if (pos != std::string::npos)
        {
            ret.erase(pos, escape.length());
            ret.insert(pos, str);
        }
    };

    std::stringstream id;
    id << std::right << std::hex << std::setw(4)
        << std::setfill('0') << pack.id();
    std::stringstream sync;
    sync << std::right << std::hex << std::setw(4)
        << std::setfill('0') << pack.syncBytes();

    std::vector<std::pair<std::string, std::string>> conv
    {
        { "%@", dateString(pack.time()) },
        { "%!", sync.str() },
        { "%#", id.str() },
        { "%$", pack.name() },
        { "%s", packet2str(pack) }
    };

    for (auto &pair : conv)
    {
        insert_if_find(pair.first, pair.second);
    }

    return ret;
}

}
