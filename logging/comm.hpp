#ifndef COMM_H
#define COMM_H

#pragma pack(1) // very important

#include <iostream>
#include <iomanip>
#include <fstream>
#include <array>
#include <vector>
#include <cstring>
#include <sstream>
#include <chrono>
#include <stdexcept>

namespace rvt
{

// first two bytes at the beginning of every message,
// used to synchronize parsers
const unsigned short SYNC_BYTES = 0x14AA;

using timestamp_t = unsigned long long;

/* -----------------------------------------------------------------*/
/* GENERIC SERIALIZE-DESERIALIZE FUNCTIONS -------------------------*/

// takes pretty much any argument type and converts it to a byte
// array of length sizeof(T). this is only guaranteed to perform
// correctly for primitive types, as well as structs IFF the
// #pragma pack(1) directive is applied.
template <typename T>
std::array<unsigned char, sizeof(T)> serialize(const T& payload)
{
    std::array<unsigned char, sizeof(T)> buffer;
    std::memcpy(buffer.data(), &payload, sizeof(T));
    return buffer;
}

// takes a std::array of length sizeof(T) and converts it to an
// object of type T. type T cannot be deduced from the array,
// and must be provided, e.g. 'auto d = deserialize<double>(array)'
template <typename T>
T deserialize(const std::array<unsigned char, sizeof(T)>& buffer)
{
    T payload;
    std::memcpy(&payload, buffer.data(), sizeof(T));
    return payload;
}

// same as above, but takes a pointer to sizeof(T) contiguous
// bytes instead of an array of sizeof(T) bytes.
template <typename T>
T deserialize(const unsigned char *buffer)
{
    T payload;
    if (!buffer) return T();
    std::memcpy(&payload, buffer, sizeof(T));
    return payload;
}

/* -----------------------------------------------------------------*/
/* STANDARD TIMESTAMP FUNCTION--------------------------------------*/

// returns a timestamp_t (unsigned long long) equal to the number
// of nanoseconds since epoch (January 1st, 1970)
timestamp_t now()
{
    auto now = std::chrono::system_clock::now();
    return std::chrono::duration_cast<std::chrono::nanoseconds>
        (now.time_since_epoch()).count();
}

/* -----------------------------------------------------------------*/
/* PACKET HEADER DEFINITION ----------------------------------------*/

class header
{
    public:

    unsigned short sync_bytes = SYNC_BYTES;
    unsigned short payload_type = 0;
    unsigned short payload_length = 0;
    timestamp_t timestamp = 0;
    unsigned long crc32 = 0;

    const header& operator = (const header& other)
    {
        sync_bytes = other.sync_bytes;
        payload_type = other.payload_type;
        payload_length = other.payload_length;
        timestamp = other.timestamp;
        crc32 = other.crc32;
        return *this;
    }
};

/* -----------------------------------------------------------------*/
/* PACKET DEFINITION -----------------------------------------------*/

// packet<T> is a templated container for payloads defined in other
// files. the payloads are required to conform to a certain standard
// (see messages.hpp for more info), else this probably won't
// compile or other bad stuff will happen.

template <typename T> struct packet
{
    static const unsigned short header_length = sizeof(header);
    static const unsigned short payload_length = sizeof(T);
    static const unsigned short total_length = header_length + payload_length;
    
    static const unsigned short sync_bytes = SYNC_BYTES; 
    static const unsigned short payload_type = T::payload_type;

    packet() { }

    packet(const T& payload)
    {
        setPayload(payload);
        _header.payload_type = T::payload_type;
        _header.payload_length = payload_length;
        updateChecksum();
    }

    packet<T>& setPayload(const T& payload)
    {
        _payload = payload;
        updateChecksum();
        return *this;
    }

    const T& getPayload() const
    {
        return _payload;
    }

    packet<T>& setHeader(const header& header)
    {
        _header = header;
        updateChecksum();
        return *this;
    }

    const header& getHeader() const
    {
        return _header;
    }

    packet<T>& setTimestamp(unsigned long long ts)
    {
        _header.timestamp = ts;
        updateChecksum();
        return *this;
    }

    // converts a packet<T> to a byte array of length packet<T>::total_length
    std::array<unsigned char, total_length> serialize() const
    {
        std::array<unsigned char, total_length> buffer;
        auto header_bytes = rvt::serialize(_header);
        std::memcpy(buffer.data(), header_bytes.data(), header_length);
        auto payload_bytes = rvt::serialize(_payload);
        std::memcpy(buffer.data() + header_length,
            payload_bytes.data(), payload_length);
        return buffer;
    }

    // converts a byte array of length packet<T>::total_length to
    // a packet<T> object; the type T cannot be deduced from the array,
    // and must be provided, e.g.:
    //      auto p = packet<msg_type>::deserialize(array);
    static packet<T>
    deserialize(std::array<unsigned char, total_length>& buffer)
    {
        packet<T> pack;
        std::array<unsigned char, header_length> header_bytes;
        std::memcpy(header_bytes.data(), buffer.data(), header_length);
        auto head = rvt::deserialize<header>(header_bytes);
        auto read_checksum = head.crc32;
        pack.setHeader(head);
        std::array<unsigned char, payload_length> payload_bytes;
        std::memcpy(payload_bytes.data(),
            buffer.data() + header_length, payload_length);
        pack.setPayload(rvt::deserialize<T>(payload_bytes));
        auto calc_checksum = pack.getHeader().crc32;
        if (calc_checksum != read_checksum)
        {
            std::stringstream error;
            error << "Checksum error while parsing message type '"
                << T::name << "' (expected 0x"
                << std::hex << std::setw(4) << std::setfill('0')
                << (int) calc_checksum << ", got 0x"
                << std::hex << std::setw(4) << std::setfill('0')
                << (int) read_checksum << ")";
            throw std::runtime_error(error.str());
        }
        return pack;
    }

    private:

    header _header;
    T _payload;

    // calculates checksum for entire packet and writes to header
    void updateChecksum()
    {
        _header.crc32 = 0;
        auto buffer = this->serialize();
        unsigned long checksum = 0;
        for (auto e : buffer) checksum += e;
        _header.crc32 = checksum;
    }
};

/*------------------------------------------------------------------*/
/* MAKE PACKET FACTORY FUNCTION ------------------------------------*/

template <typename T> packet<T> makePacket(const T& msg)
{
    return packet<T>(msg);
}

/* -----------------------------------------------------------------*/
/* PAYLOAD & PACKET OSTREAM OUTPUT ---------------------------------*/

// default definition for payload output to std::ostream --
// can be overridden by a payload-unique definition
template <typename T, typename U = decltype(T::name)>
std::ostream& operator << (std::ostream& os, const T& payload)
{
    using namespace std;
    os << "<" << T::name << ",";
    auto buffer = rvt::serialize(payload);
    for (auto e : buffer)
        os << " " << setw(2) << setfill('0') << hex << (int) e;
    return os << ">";
}

// definition for packet output to std::ostream, prints something like:
//
//   <0x9977, 2, 37, 482649.280, 0x57f3 <msg, 00 56 82 95...>>
//
template <typename T>
std::ostream& operator << (std::ostream& os, const packet<T>& pack)
{
    using namespace std;
    auto head = pack.getHeader();
    return os << "<0x" << hex << setfill('0') << setw(4) << head.sync_bytes
        << ", 0x" << dec << (int) head.payload_type
        << ", " << dec << (int) head.payload_length
        << ", " << head.timestamp/1000000000
        << "." << setfill('0') << setw(9) << head.timestamp % 1000000000
        << ", 0x" << hex << setfill('0') << setw(4) << head.crc32
        << " " << pack.getPayload() << ">" << std::dec;
} 

/* -----------------------------------------------------------------*/
/* ARCHIVE CLASS DEFINITION ----------------------------------------*/

// archive is a helpful wrapper class for a binary log file or
// internal buffer. it stores packets internally as a std::vector
// of unsigned chars, and can can write to a file or accept a
// filename as the constructor argument. it's also possible
// to iterate over all of a specific kind of packet using the
// getPackets() method.
class archive
{
    public:

    archive() { }

    archive(const std::string& filename)
    {
        _bytes = fileToVector(filename);
    }

    template <typename T> std::vector<packet<T>> getPackets() const
    {
        std::vector<packet<T>> messages;
        for (size_t i = 0; i < _bytes.size(); ++i)
        {
            auto head = deserialize<header>(_bytes.data() + i);
            if (head.sync_bytes == rvt::SYNC_BYTES)
            {
                using namespace rvt;
                if (head.payload_type == T::payload_type)
                {
                    const size_t plen = packet<T>::total_length;
                    std::array<unsigned char, plen> buffer;
                    std::copy(_bytes.begin() + i,
                        _bytes.begin() + i + plen, buffer.begin());
                    try
                    {
                        auto pack = packet<T>::deserialize(buffer);
                        messages.push_back(pack);
                        i += plen - 1;
                    }
                    catch (const std::exception& e)
                    {
                        std::cout << e.what() << std::endl;
                    }
                }
            }
        }
        return messages;
    }

    template <typename T> void addPacket(const packet<T>& pack)
    {
        auto buffer = pack.serialize();
        _bytes.insert(_bytes.end(), buffer.begin(), buffer.end());
    }

    const std::vector<unsigned char>& data() const
    {
        return _bytes;
    }

    bool write(const std::string& filename)
    {
        std::ofstream out(filename);
        if (!out) return false;
        out.write((const char*) _bytes.data(), _bytes.size());
        out.close();
        return true;
    }

    archive& read(const std::string& filename)
    {
        _bytes = fileToVector(filename);
        return *this;
    }

    private:

    std::vector<unsigned char> _bytes;

    std::vector<unsigned char> fileToVector(const std::string& filename)
    {
        std::ifstream in(filename);
        in.seekg(0, in.end);
        size_t length = in.tellg();
        in.seekg(0, in.beg);
        std::vector<unsigned char> buffer(length);
        in.read((char*) buffer.data(), length);
        in.close();
        return buffer;
    }
};

/*------------------------------------------------------------------*/

} // namespace rvt

#endif // COMM_H
