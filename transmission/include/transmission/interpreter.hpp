// interpreter.hpp

#ifndef RVT_INTERPRETER_HPP
#define RVT_INTERPRETER_HPP

#include <transmission/packet.hpp>

#include <vector>
#include <map>
#include <string>
#include <iostream>
#include <functional>

namespace rvt
{

class interpreter
{
    using callback = std::function<int(interpreter&, const packet&)>;

    public:

    interpreter(); 

    int call(const packet& pack);

    std::string get_format(uint16_t id) const;

    std::vector<packet> history;
    std::map<std::string, std::string> context;
    std::map<uint16_t, callback> callbacks;
    std::map<uint16_t, std::string> parse_formats;
};

std::ostream& operator << (std::ostream &os, const interpreter &interp);

} // namespace rvt

#endif // RVT_INTERPRETER_HPP
