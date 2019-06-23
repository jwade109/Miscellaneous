// interpreter.cpp

#include <vector>
#include <map>
#include <chrono>
#include <string>
#include <iostream>
#include <iomanip>
#include <functional>
#include <iterator>
#include <sstream>

std::vector<std::string> split_quoted(const std::string &fstr)
{
    std::vector<std::string> tokens;
    std::string token;
    char quote_char = ' ';
    for (size_t i = 0; i < fstr.length(); ++i)
    {
        char c = fstr[i];

        if (c == ' ' && quote_char != ' ')
        {
            token += c;
        }
        else if (c == ' ' || i == fstr.length() - 1)
        {
            if (c != ' ') token += c;
            if (token.length() > 0) tokens.push_back(token);
            token = "";
        }
        else if ((c == '\'' || c == '"') && quote_char == ' ')
        {
            quote_char = c;
        }
        else if (c == quote_char && c != ' ')
        {
            quote_char = ' ';
        }
        else if (c != ' ')
        {
            token += c;
        }
    }
    return tokens;
}

void parse(const std::string &fstr)
{
    auto print = [] (std::string token, std::string value, std::string type)
    {
        std::cout << std::setw(25) << token << std::setw(25) << value << std::setw(18) << type << std::endl;
    };

    auto begins_with = [] (std::string str, std::string begin)
    {
        return str.rfind(begin, 0) == 0;
    };

    auto ends_with = [] (std::string str, std::string ending)
    {
        if (ending.length() > str.length()) return false;
        return str.compare(str.length() - ending.length(), ending.length(), ending) == 0;
    };

    auto tokens = split_quoted(fstr);

    for (auto token : tokens)
    {
        std::string value = ""; 
        if (begins_with(token, "0x"))
        {
            
            if (token.length() > 2)
                value = token.substr(2);
            print(token, value, "hex");
        }
        else if (begins_with(token, "#"))
        {
            if (token.length() > 1)
                value = token.substr(1);
            print(token, value, "string");
        }
        else if (ends_with(token, "u8"))
        {
            value = token.substr(0, token.length() - 2);
            print(token, value, "uint8_t");
        }
        else if (ends_with(token, "u16"))
        {
            value = token.substr(0, token.length() - 3);
            print(token, value, "uint16_t");
        }
        else if (ends_with(token, "u32"))
        {
            value = token.substr(0, token.length() - 3);
            print(token, value, "uint32_t");
        }
        else if (ends_with(token, "u64"))
        {
            value = token.substr(0, token.length() - 3);
            print(token, value, "uint64_t");
        }
        else if (ends_with(token, "n8"))
        {
            value = token.substr(0, token.length() - 2);
            print(token, value, "int8_t");
        }
        else if (ends_with(token, "n16"))
        {
            value = token.substr(0, token.length() - 3);
            print(token, value, "int16_t");
        }
        else if (ends_with(token, "n32"))
        {
            value = token.substr(0, token.length() - 3);
            print(token, value, "int32_t");
        }
        else if (ends_with(token, "n64"))
        {
            value = token.substr(0, token.length() - 3);
            print(token, value, "int64_t");
        }
        else if (ends_with(token, "f"))
        {
            value = token.substr(0, token.length() - 1);
            print(token, value, "float");
        }
        else if (ends_with(token, "d"))
        {
            value = token.substr(0, token.length() - 1);
            print(token, value, "double");
        }
        else print(token, value, "unrecognized");
    }
}

class packet_header
{
    public:

    std::chrono::steady_clock::time_point time;
    uint16_t length;
};

class packet
{
    public:

    packet_header header;
    std::vector<uint8_t> data;
    uint16_t checksum;
};

int add_context_variable(const packet &pack)
{
    return 0;
}

int another_function(const packet &pack)
{
    return 1;
}

using callback = std::function<int(const packet&)>;

class interpreter
{
    public:

    interpreter() : _context{{"version", "#1.0.0"},
                             {"sync bytes", "0xAA 0x14"}},
                    _callbacks{{0, add_context_variable},
                               {1, another_function},
                               {0xfc4, [] (const packet&) { return 3 + 4; }}} { }

    const std::vector<uint8_t>& sync_bytes() const
    {
        return _sync_bytes;
    }

    const std::map<std::string, std::string>& context() const
    {
        return _context;
    }

    const std::map<uint16_t, std::function<int(const packet&)>> callbacks() const
    {
        return _callbacks;
    }

    private:

    std::vector<uint8_t> _sync_bytes;
    std::map<std::string, std::string> _context;
    std::map<uint16_t, callback> _callbacks;
};

std::ostream& operator << (std::ostream &os, const interpreter &interp)
{
    std::hash<callback*> hasher;

    os << "< Context >" << std::endl;
    for (auto e : interp.context())
    {
        os << e.first << ":" << std::endl
           << "           " << e.second << std::endl;
    }
    os << "< Callbacks >" << std::endl;
    for (auto e : interp.callbacks())
    {
        os << std::hex << std::right << std::setw(6) << e.first << ":    "
           << std::left << std::setw(40) << e.second.target_type().name() << " "
           << std::hex << hasher(&e.second) << std::endl;
    }
    return os;
}

int main()
{
    interpreter interp;
    std::cout << interp << std::endl;

    std::string str("3u8 76u16 89u32 456u64 "
                    "-7n8 83n16 -412n32 -291n64 "
                    "0x2f 4.001d 9.23f "
                    "#Hey #\"Hello there!\" wpoefk");
    std::cout << str << std::endl;
    parse(str);
    return 0;
}


