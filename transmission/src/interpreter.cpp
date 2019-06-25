// interpreter.cpp

#include <transmission/serial.hpp>
#include <transmission/interpreter.hpp>

#include <iomanip>
#include <sstream>

namespace rvt
{

interpreter::interpreter() :
    history(),
    context{{"sync bytes", "0xAA 0x14"}},
    callbacks
    {
        {0, [] (interpreter &interp, const packet &pack)
        {
            std::vector<uint8_t> data(pack.data);
            std::string key, value;
            data >> key >> value;
            if (key == "" || value == "") return 1;
            std::cout << "Add to context: " << key
                << " = " << value << std::endl;
            interp.context[key] = value;
            return 0;
        }},

        {1, [] (interpreter &interp, const packet &pack)
        {
            std::vector<uint8_t> data(pack.data);
            uint16_t id;
            std::string format;
            data >> id >> format;
            std::cout << "Add to parse formats: "
                << id << " -> " << format << std::endl;
            interp.parse_formats[id] = format;
            return 0;
        }}
    } { }

int interpreter::call(const packet& pack)
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

std::string interpreter::get_format(uint16_t id) const
{
    auto iter = parse_formats.find(id);
    if (iter != parse_formats.end())
    {
        return iter->second;
    }
    return "";
}

std::ostream& operator << (std::ostream &os, const interpreter &interp)
{
    std::stringstream ss;
    ss << "< Context >" << std::endl;
    for (auto e : interp.context)
    {
        ss << std::setw(20) << e.first << ": "
           << e.second << std::endl;
    }
    ss << "< Callbacks >" << std::endl;
    for (auto e : interp.callbacks)
    {
        ss << std::hex << std::right << std::setw(12) << e.first << ": "
           << std::left << e.second.target_type().name() << std::endl;
    }
    ss << "< Parse Formats >" << std::endl;
    for (auto e : interp.parse_formats)
    {
        ss << std::right << std::dec << std::setw(12) << e.first << ": "
           << std::left << e.second << std::endl;
    }
    ss << "< History >" << std::endl;
    for (int64_t i = 0; i < interp.history.size(); ++i)
    {
        auto pack = interp.history[i];
        ss << "  " << std::setw(4) << std::setfill('0')
           << std::hex << std::right << i << " "
           << packet2str(pack, interp.get_format(pack.id))
           << std::endl;
    }
    return os << ss.str();
}

} // namespace rvt
