// packet.cpp

#include <transmission/serial.hpp>
#include <transmission/packet.hpp>
#include <transmission/util.hpp>

#include <vector>
#include <set>
#include <chrono>
#include <string>
#include <iostream>
#include <iomanip>
#include <iterator>
#include <sstream>

namespace rvt
{

packet::packet() :
    sync_bytes(0),
    time{}, id(0), data{}, checksum(0),
    format(), warnings() { }

bool packet::is_valid() const
{
    std::vector<uint8_t> hexdump;
    hexdump << *this;
    uint16_t cs = xorchecksum(hexdump);
    if (cs != 0)
    {
        std::stringstream warn;
        warn << "Checksum mismatch -- " << std::hex << checksum
            << " given, " << cs << " computed";
        warnings.insert(warn.str());
    }
    return cs == 0;
}

uint16_t packet::updateChecksum()
{
    std::vector<uint8_t> bytes;
    bytes << sync_bytes << time << id
        << static_cast<uint16_t>(data.size()) << data;
    uint16_t cs = xorchecksum(bytes);
    return checksum = cs;
}

bool nextPacket(packet &pack, std::vector<uint8_t>::iterator &begin,
                        const std::vector<uint8_t>::iterator &end)
{
    static const uint8_t packet_min_size = 16;
    static const uint8_t packet_header_size = 14;

    for (; begin < end; ++begin)
    {
        auto read_iter(begin);
        // not enough data for a min-size packet
        if (end - read_iter < packet_min_size)
        {
            return false;
        }

        std::vector<uint8_t> buffer(read_iter, read_iter + packet_min_size);
        
        decltype(pack.sync_bytes) sync_bytes = 0;
        uint16_t data_length = 0;
        buffer >> sync_bytes >> pack.time >> pack.id >> data_length;

        if (sync_bytes != pack.sync_bytes)
        {
            continue;
        }
       
        // not enough data in the buffer 
        if (end - read_iter < packet_min_size + data_length)
        {
            return false;
        }

        pack.data.clear();
        pack.data.insert(pack.data.end(), read_iter + packet_header_size,
            read_iter + packet_header_size + data_length);
        
        buffer.clear();
        buffer.insert(buffer.end(),
            read_iter + packet_header_size + data_length,
            read_iter + packet_min_size + data_length);
        buffer >> pack.checksum;

        if (pack.is_valid())
        {
            begin += packet_min_size + data_length;
            return true;
        }
    }
    return false;
}

packet str2packet(const std::chrono::system_clock::time_point &time,
                  const std::string &fstr)
{
    packet pack;
    pack.time = time;

    auto tokens = split_quoted(fstr);

    for (auto token : tokens)
    {
        std::string value = "";
        if (begins_with(token, "!") && token.length() > 1)
        {
            value = token.substr(1);
            pack.sync_bytes = std::stoull(value, 0, 16);
        }
        else if (begins_with(token, "#") && token.length() > 1)
        {
            value = token.substr(1);
            pack.id = std::stoull(value);
        }
        else if (begins_with(token, "0x") && token.length() > 2)
        {
            value = token.substr(2);
            uint8_t byte_value = std::stoull(value, 0, 16);
            std::vector<uint8_t> bytes{byte_value};
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " 0x";
        }
        else if (token.length() >= 2 &&
               ((begins_with(token, "'")  && ends_with(token, "'")) ||
                (begins_with(token, "\"") && ends_with(token, "\""))))
        {
            value = token.substr(1, token.length() - 2);
            std::vector<uint8_t> bytes(value.begin(), value.end());
            bytes.push_back(0);
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " s";
        }
        else if (ends_with(token, "u8") && token.length() > 2)
        {
            value = token.substr(0, token.length() - 2);
            uint8_t byte_value = std::stoull(value);
            std::vector<uint8_t> bytes{byte_value};
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " u8";
        }
        else if (ends_with(token, "u16"))
        {
            value = token.substr(0, token.length() - 3);
            uint16_t num = std::stoull(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " u16";
        }
        else if (ends_with(token, "u32"))
        {
            value = token.substr(0, token.length() - 3);
            uint32_t num = std::stoull(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " u32";
        }
        else if (ends_with(token, "u64"))
        {
            value = token.substr(0, token.length() - 3);
            uint64_t num = std::stoull(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " u64";
        }
        else if (ends_with(token, "n8"))
        {
            value = token.substr(0, token.length() - 2);
            int8_t num = std::stoll(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " n8";
        }
        else if (ends_with(token, "n16"))
        {
            value = token.substr(0, token.length() - 3);
            int16_t num = std::stoll(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " n16";
        }
        else if (ends_with(token, "n32"))
        {
            value = token.substr(0, token.length() - 3);
            int32_t num = std::stoll(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " n32";
        }
        else if (ends_with(token, "n64"))
        {
            value = token.substr(0, token.length() - 3);
            int64_t num = std::stoll(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " n64";
        }
        else if (ends_with(token, "f"))
        {
            value = token.substr(0, token.length() - 1);
            float num = std::stof(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " f";
        }
        else if (ends_with(token, "d"))
        {
            value = token.substr(0, token.length() - 1);
            double num = std::stod(value);
            std::vector<uint8_t> bytes;
            bytes << num;
            pack.data.insert(pack.data.end(), bytes.begin(), bytes.end());
            pack.format += " d";
        }
        else
        {
            std::cout << "Unrecognized token: '"
                      << token << "'" << std::endl;
        }
    }
    pack.updateChecksum();
    return pack;
}

std::string packet2str(const packet &pack)
{
    return packet2str(pack, pack.format);
}

std::string packet2str(const packet &pack, const std::string &format)
{
    using namespace std::chrono;

    std::vector<uint8_t> data(pack.data);
    std::vector<std::string> tokens;
    std::istringstream iss(format);
    std::copy(std::istream_iterator<std::string>(iss),
              std::istream_iterator<std::string>(),
              std::back_inserter(tokens));

    auto since_epoch = pack.time.time_since_epoch();
    auto sec = duration_cast<seconds>(since_epoch);
    auto ms = duration_cast<milliseconds>(since_epoch - sec);

    std::stringstream result;
    result << "!" << std::setw(4) << std::right << std::setfill('0')
           << std::hex << pack.sync_bytes << std::dec
           << " @" << sec.count() << "." << std::setw(3)
           << std::setfill('0') << std::right << ms.count()
           << " #" << pack.id;

    for (auto token : tokens)
    {
        if (token == "s")
        {
            std::string str = "";
            data >> str;
            if (str.size() > 0)
            {
                if (result.tellp() > 0) result << " ";
                result << '"' << str << '"';
            }
        }
        else if (token == "u8" && data.size() >= 1)
        {
            uint8_t num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << std::to_string(num) << "u8";
        }
        else if (token == "u16" && data.size() >= 2)
        {
            uint16_t num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << std::to_string(num) << "u16";
        }
        else if (token == "u32" && data.size() >= 4)
        {
            uint32_t num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << num << "u32";
        }
        else if (token == "u64" && data.size() >= 8)
        {
            uint64_t num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << std::to_string(num) << "u64";
        }
        else if (token == "n8" && data.size() >= 1)
        {
            int8_t num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << std::to_string(num) << "n8";
        }
        else if (token == "n16" && data.size() >= 2)
        {
            int16_t num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << std::to_string(num) << "n16";
        }
        else if (token == "n32" && data.size() >= 4)
        {
            int32_t num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << std::to_string(num) << "n32";
        }
        else if (token == "n64" && data.size() >= 8)
        {
            int64_t num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << std::to_string(num) << "n64";
        }
        else if (token == "f" && data.size() >= 1)
        {
            float num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << std::to_string(num) << "f";
        }
        else if (token == "d" && data.size() >= 1)
        {
            double num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << std::to_string(num) << "d";
        }
        else if (token == "0x" && data.size() >= 1)
        {
            uint8_t num = 0;
            data >> num;
            if (result.tellp() > 0) result << " ";
            result << "0x" << std::hex << std::setfill('0')
                << std::setw(2) << std::right << static_cast<int>(num);
        }
    }

    if (data.size() > 0)
    {
        result << " (" << data.size() << " bytes)";
    }

    result << " " << std::setw(4) << std::setfill('0')
        << std::right << std::hex << pack.checksum;

    return result.str();
}

std::vector<uint8_t>& operator << (std::vector<uint8_t> &vec,
                                   const packet &pack)
{
    uint16_t size = pack.data.size();
    return vec << pack.sync_bytes << pack.time
        << pack.id << size
        << pack.data << pack.checksum;
}

std::vector<uint8_t>& operator >> (std::vector<uint8_t> &vec,
                                   packet &pack)
{
    uint16_t size = 0;
    vec >> pack.sync_bytes >> pack.time >> pack.id >> size;

    if (size > vec.size()) size = 0;

    pack.data.insert(pack.data.begin(), vec.begin(), vec.begin() + size);
    vec.erase(vec.begin(), vec.begin() + size);

    return vec >> pack.checksum;
}

std::ostream& operator << (std::ostream &os, const packet &pack)
{
    using namespace std::chrono;

    auto since_epoch = pack.time.time_since_epoch();
    auto sec = duration_cast<seconds>(since_epoch);
    auto ms = duration_cast<milliseconds>(since_epoch) - sec;

    std::stringstream ss;
    ss << std::setfill(' ') << std::setw(18) << std::dec << std::right
       << "time: " << std::left << std::setfill('0')
       << std::setw(9) << sec.count() << "."
       << std::setw(3) << std::right << ms.count() << " -- ";

    auto time = std::chrono::system_clock::to_time_t(pack.time);
    std::stringstream datestr;
    datestr << std::put_time(std::gmtime(&time), "%Y-%m-%d %H:%M:%S")
        << "." << std::right << std::setw(3)
        << std::setfill('0') << ms.count();

    ss << datestr.str() << std::endl << std::setfill(' ')
       << std::setw(18) << "sync bytes: " << std::hex << std::setw(4)
       << std::setfill('0') << pack.sync_bytes << std::endl;

    std::vector<uint8_t> bytestring;
    bytestring << pack;

    ss << std::setfill(' ') << std::setw(18) << "id: " << std::dec << pack.id
       << std::endl << std::setw(18) << "length: " << pack.data.size()
       << " / " << bytestring.size() << std::endl
       << std::setw(18) << "checksum: " << std::hex << std::setw(4)
       << std::setfill('0') << pack.checksum << std::endl << std::setfill(' ')
       << std::setw(18) << "format: " << pack.format << std::endl
       << std::setw(18) << "string: " << packet2str(pack) << std::endl
       << std::setw(18) << "valid: " << std::boolalpha << pack.is_valid() << std::endl
       << std::setw(18) << "warnings: ";
    size_t set_iterator = 0;
    for (auto warn : pack.warnings)
    {
        if (set_iterator > 0) ss << std::setw(18) << std::setfill(' ') << "";
        ss << std::left << warn << std::endl;
        ++set_iterator;
    }
    if (pack.warnings.size() == 0)
    {
        ss << "none" << std::endl;
    }

    ss << std::setfill(' ') << std::setw(18) << std::right << "data: "; 
    for (size_t i = 0; i < pack.data.size(); ++i)
    {
        uint8_t e = pack.data[i];
        ss << std::right << std::hex << std::setw(2)
            << std::setfill('0') << static_cast<int>(e) << " ";
        if (i % 16 == 7) ss << " ";
        if (i % 16 == 15 && i < pack.data.size() - 1)
            ss << std::endl << std::setw(18) << std::setfill(' ') << "";
    }
    ss << std::endl;

    ss << std::setfill(' ') << std::setw(18) << std::right << "hexdump: "; 
    for (size_t i = 0; i < bytestring.size(); ++i)
    {
        uint8_t e = bytestring[i];
        ss << std::right << std::hex << std::setw(2)
            << std::setfill('0') << static_cast<int>(e) << " ";
        if (i % 16 == 7) ss << " ";
        if (i % 16 == 15 && i < bytestring.size() - 1)
            ss << std::endl << std::setw(18) << std::setfill(' ') << "";
    }

    return os << ss.str() << std::endl;
}

} // namespace rvt

