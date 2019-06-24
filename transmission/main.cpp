// interpreter.cpp

#include <vector>
#include <array>
#include <map>
#include <set>
#include <chrono>
#include <thread>
#include <string>
#include <iostream>
#include <iomanip>
#include <functional>
#include <iterator>
#include <sstream>

/// \brief Converts a variable to bytes and stores them
/// \param vec The vector to store the bytes in
/// \param data A variable containing data to be converted
/// \return The original vector with added bytes
template <typename T, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator <<
    (std::vector<unsigned char> &vec, T data)
{
    unsigned char *c = reinterpret_cast<unsigned char*>(&data);
    for (int i = sizeof(T) - 1; i >= 0; --i)
    {
        vec.push_back(c[i]);
    }
    return vec;
}

/// \brief Converts a variable to bytes and stores them
/// \param vec The vector to store the bytes in
/// \param data A vector of data to be converted
/// \return The original vector with added bytes
template <typename T, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator <<
    (std::vector<unsigned char> &vec,
    const std::vector<T> &data)
{
    for (auto e : data)
        vec << e;
    return vec;
}

/// \brief Converts a variable to bytes and stores them
/// \param vec The vector to store the bytes in
/// \param data An array of data to be converted
/// \return The original vector with added bytes
template <typename T, size_t N, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator <<
    (std::vector<unsigned char> &vec,
    const std::array<T, N> &data)
{
    for (auto e : data)
        vec << e;
    return vec;
}

/// \brief Extracts bytes from a vector
/// \param vec The vector of bytes
/// \param data The variable to fill with bytes
/// \return The original vector, with a few less bytes
template <typename T, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator >>
    (std::vector<unsigned char> &vec, T& data)
{
    data = T();
    if (sizeof(T) > vec.size()) return vec;

    std::vector<unsigned char> extract;
    for (int i = sizeof(T) - 1; i >= 0; --i)
    {
        extract.push_back(vec[i]);
    }
    vec.erase(vec.begin(), vec.begin() + extract.size());
    unsigned char *ptr = reinterpret_cast<unsigned char*>(&data);
    std::copy(extract.begin(), extract.end(), ptr);
    return vec;
}

/// \brief Extracts bytes from a vector
/// \param vec The vector of bytes
/// \param data A vector of variables to populate
/// \return The original vector, with some bytes missing
template <typename T, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator >>
    (std::vector<unsigned char> &vec,
     std::vector<T> &data)
{
    while (vec.size() >= sizeof(T))
    {
        T elem;
        vec >> elem;
        data.push_back(elem);
    }
    return vec;
}

/// \brief Extracts bytes from a vector
/// \param vec The vector of bytes
/// \param data An array of variables to populate
/// \return The original vector, with some bytes missing
template <typename T, size_t N, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator >>
    (std::vector<unsigned char> &vec,
     std::array<T, N> &data)
{
    size_t index = 0;
    while (vec.size() >= sizeof(T) && index < N)
    {
        T elem;
        vec >> elem;
        data[index] = elem;
        ++index;
    }
    return vec;
}

/// \brief Extracts bytes from a vector and inserts them into
///        a null-terminated string
std::vector<uint8_t>& operator >> (std::vector<uint8_t> &vec, std::string &str)
{
    std::vector<uint8_t> data;
    size_t i = 0;
    for (; i < vec.size() && vec[i] != 0; ++i)
        data.push_back(vec[i]);
    str = std::string(data.begin(), data.end());
    if (vec.size() > i && vec[i] == 0) ++i;
    vec.erase(vec.begin(), vec.begin() + i);
    return vec;
}


std::string wrap(const std::string &str, size_t num_chars, size_t offset = 0)
{
    std::string copy(str);
    std::stringstream ss;
    ss << std::right;
    while (copy.length() > num_chars)
    {
        ss << std::setw(num_chars + offset + 1)
           << copy.substr(0, num_chars) + "\n";
        copy = copy.substr(num_chars, copy.length());
    }
    ss << std::setw(offset) << "" << copy;
    return ss.str();
}


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
            if ((c != quote_char || quote_char == ' ') && c != ' ') token += c;
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

class packet
{
    public:

    packet() : time(), id(0), data{}, checksum(0), format() { }

    std::chrono::system_clock::time_point time;
    uint16_t id;
    std::vector<uint8_t> data;
    uint16_t checksum;
    std::string format;
    std::set<std::string> warnings;

    bool is_valid() const
    {
        return false;
    }
};

packet str2packet(const std::chrono::system_clock::time_point &time,
                  const std::string &fstr)
{
    auto begins_with = [] (std::string str, std::string begin)
    {
        return str.rfind(begin, 0) == 0;
    };

    auto ends_with = [] (std::string str, std::string ending)
    {
        if (ending.length() > str.length()) return false;
        return str.compare(str.length() - ending.length(), ending.length(), ending) == 0;
    };

    packet pack;
    pack.time = time;

    auto tokens = split_quoted(fstr);

    for (auto token : tokens)
    {
        std::string value = ""; 
        if (begins_with(token, "0x"))
        {
            if (token.length() > 2)
                value = token.substr(2);
            uint8_t byte_value = std::stoull(value, 0, 16);
            std::vector<uint8_t> bytes{byte_value};
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "0x ";
        }
        else if (begins_with(token, "#"))
        {
            if (token.length() > 1)
                value = token.substr(1);
            std::vector<uint8_t> bytes(value.begin(), value.end());
            bytes.push_back(0);
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "# ";
        }
        else if (ends_with(token, "u8"))
        {
            value = token.substr(0, token.length() - 2);
            uint8_t byte_value = std::stoull(value);
            std::vector<uint8_t> bytes{byte_value};
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "u8 ";
        }
        else if (ends_with(token, "u16"))
        {
            value = token.substr(0, token.length() - 3);
            uint16_t num = std::stoull(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "u16 ";
        }
        else if (ends_with(token, "u32"))
        {
            value = token.substr(0, token.length() - 3);
            uint32_t num = std::stoull(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "u32 ";
        }
        else if (ends_with(token, "u64"))
        {
            value = token.substr(0, token.length() - 3);
            uint64_t num = std::stoull(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "u64 ";
        }
        else if (ends_with(token, "n8"))
        {
            value = token.substr(0, token.length() - 2);
            int8_t num = std::stoll(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "n8 ";
        }
        else if (ends_with(token, "n16"))
        {
            value = token.substr(0, token.length() - 3);
            int16_t num = std::stoll(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "n16 ";
        }
        else if (ends_with(token, "n32"))
        {
            value = token.substr(0, token.length() - 3);
            int32_t num = std::stoll(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "n32 ";
        }
        else if (ends_with(token, "n64"))
        {
            value = token.substr(0, token.length() - 3);
            int64_t num = std::stoll(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "n64 ";
        }
        else if (ends_with(token, "f"))
        {
            value = token.substr(0, token.length() - 1);
            float num = std::stof(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "f ";
        }
        else if (ends_with(token, "d"))
        {
            value = token.substr(0, token.length() - 1);
            double num = std::stod(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += "d ";
        }
    }

    return pack;
}

std::string packet2str(const packet &pack, const std::string &fmt = "")
{
    std::string format(fmt);
    if (format == "") format = pack.format;
    std::vector<uint8_t> data(pack.data);
    std::vector<std::string> tokens;
    std::istringstream iss(format);
    std::copy(std::istream_iterator<std::string>(iss),
              std::istream_iterator<std::string>(),
              std::back_inserter(tokens));

    using namespace std::chrono;

    auto since_epoch = pack.time.time_since_epoch();
    auto sec = duration_cast<seconds>(since_epoch);
    auto ms = duration_cast<milliseconds>(since_epoch) - sec;

    std::stringstream header;
    header << "@" << sec.count() << "." << std::setw(3)
           << std::setfill('0') << std::right << ms.count()
           << " *" << std::setw(7) << std::setfill(' ')
           << std::left << pack.id;

    std::string result(header.str());
    for (auto token : tokens)
    {
        if (token == "#")
        {
            std::string str;
            data >> str;
            if (result.size() > 0) result += " ";
            result += str;
        }
        else if (token == "u8" && data.size() >= 1)
        {
            uint8_t num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "u16" && data.size() >= 2)
        {
            uint16_t num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "u32" && data.size() >= 4)
        {
            uint32_t num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "u64" && data.size() >= 8)
        {
            uint64_t num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "n8" && data.size() >= 1)
        {
            int8_t num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "n16" && data.size() >= 2)
        {
            int16_t num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "n32" && data.size() >= 4)
        {
            int32_t num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "n64" && data.size() >= 8)
        {
            int64_t num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "f" && data.size() >= 1)
        {
            float num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "d" && data.size() >= 1)
        {
            double num;
            data >> num;
            if (result.size() > 0) result += " ";
            result += std::to_string(num);
        }
        else if (token == "0x" && data.size() >= 1)
        {
            uint8_t num;
            data >> num;
            std::stringstream ss;
            ss << std::hex << std::setfill('0')
               << std::setw(2) << std::right << static_cast<int>(num);
            if (result.size() > 0) result += " ";
            result += ss.str();
        }
    }

    // print any remaining bytes as hexadecimal
    for (auto e : data)
    {
        std::stringstream ss;
        ss << std::hex << std::setfill('0')
           << std::setw(2) << std::right << static_cast<int>(e);
        if (result.size() > 0) result += " ";
        result += ss.str();
    }

    return result;
}

std::ostream& operator << (std::ostream &os, const packet &pack)
{
    using namespace std::chrono;

    auto since_epoch = pack.time.time_since_epoch();
    auto sec = duration_cast<seconds>(since_epoch);
    auto ms = duration_cast<milliseconds>(since_epoch) - sec;

    std::stringstream ss;
    ss << "~ packet ~" << std::endl
       << std::setfill(' ') << std::setw(12) << std::dec << std::right
       << "time: " << std::left
       << sec.count() << "."
       << std::setw(3) << std::setfill('0')
       << std::right << ms.count() << std::endl
       << std::setfill(' ')
       << std::setw(12) << "id: " << pack.id << std::endl
       << std::setw(12) << "length: " << pack.data.size() << std::endl
       << std::setw(12) << "format: " << pack.format << std::endl
       << std::setw(12) << "string: " << packet2str(pack, pack.format) << std::endl
       << std::setw(12) << "checksum: " << std::hex << std::setw(4)
       << std::setfill('0') << pack.checksum << std::endl << std::setfill(' ')
       << std::setw(12) << "is_valid: " << std::boolalpha << pack.is_valid() << std::endl
       << std::setw(12) << "warnings: ";
    size_t set_iterator = 0;
    for (auto warn : pack.warnings)
    {
        if (set_iterator > 0) ss << std::setw(12) << std::setfill(' ') << "";
        ss << std::left << warn << std::endl;
        ++set_iterator;
    }
    if (pack.warnings.size() == 0)
    {
        ss << "none" << std::endl;
    }

    ss << std::setw(12) << std::right << "data: "; 
    for (size_t i = 0; i < pack.data.size(); ++i)
    {
        uint8_t e = pack.data[i];
        ss << std::right << std::hex << std::setw(2)
            << std::setfill('0') << static_cast<int>(e) << " ";
        if (i % 16 == 15) ss << std::endl << std::setw(12) << std::setfill(' ') << "";
    }
    return os << ss.str() << std::endl;
}

class interpreter
{
    using callback = std::function<int(interpreter&, const packet&)>;

    public:

    interpreter() : history(),
                    context{{"version", "1.0.0"},
                             {"sync bytes", "0xAA 0x14"}},
                    callbacks{
    
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
        }}} { }

    int call(const packet& pack)
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

    std::string get_format(uint16_t id) const
    {
        auto iter = parse_formats.find(id);
        if (iter != parse_formats.end())
        {
            return iter->second;
        }
        return "";
    }

    std::vector<packet> history;
    std::map<std::string, std::string> context;
    std::map<uint16_t, callback> callbacks;
    std::map<uint16_t, std::string> parse_formats;
};

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
    for (int64_t i = interp.history.size() - 1; i >= 0; --i)
    {
        auto pack = interp.history[i];
        ss << packet2str(pack, interp.get_format(pack.id)) << std::endl;
    }
    return os << ss.str();
}

int main(int argc, char **argv)
{
    interpreter interp;
    std::vector<std::string> pstrs;

    /*
    std::string def("3u8 15900u16 1823942u32 2348923499102912u64 "
                    "-7n8 83n16 -412n32 -291n64 "
                    "0x2f 4.001d 9.23f "
                    "#Hey #\"Hello there!\"");
    */

    for (int i = 1; i < argc - 1; i+= 2)
    {
        uint16_t id = std::stoull(argv[i]);
        std::string formatted(argv[i+1]);

        auto pack = str2packet(std::chrono::system_clock::now(), formatted);
        pack.id = id;
        pack.format = "";
        std::cout << packet2str(pack, interp.get_format(pack.id)) << std::endl;
        interp.call(pack);
        std::this_thread::sleep_for(std::chrono::milliseconds(100));
    }
    std::cout << interp << std::endl;

    return 0;
}


