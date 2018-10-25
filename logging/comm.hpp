#ifndef COMM_H
#define COMM_H

#pragma pack(1) // very important

#include <iostream>
#include <iomanip>
#include <array>
#include <cstring>

namespace rvt
{

// first two bytes at the beginning of every message,
// used to synchronize parsers
const unsigned short SYNC_BYTES = 0x9977;

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

/* -----------------------------------------------------------------*/
/* PACKET HEADER DEFINITION ----------------------------------------*/

template <typename T> class header
{
    public:

    static const unsigned short header_length = sizeof(header<T>);

    const unsigned short sync_bytes = SYNC_BYTES;
    const unsigned short payload_type = T::payload_type;
    const unsigned short payload_length = sizeof(T);
    unsigned long long timestamp = 0;
    unsigned long crc32 = 0;

    const header<T>& operator = (const header<T>& other)
    {
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
    static const unsigned short header_length = sizeof(header<T>);
    static const unsigned short payload_length = sizeof(T);
    static const unsigned short total_length = header_length + payload_length;
    
    static const unsigned short sync_bytes = SYNC_BYTES; 
    static const unsigned short payload_type = T::payload_type;

    packet() { }

    packet(const T& payload)
    {
        setPayload(payload);
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

    packet<T>& setHeader(const header<T>& header)
    {
        _header = header;
        updateChecksum();
        return *this;
    }

    const header<T>& getHeader() const
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
        std::memcpy(buffer.data() + header_length, payload_bytes.data(), payload_length);
        return buffer;
    }

    // converts a byte array of length packet<T>::total_length to a packet<T>
    // object; the type T cannot be deduced from the array, and must be provided,
    // e.g.:
    //      auto p = packet<msg_type>::deserialize(array);
    static packet<T> deserialize(std::array<unsigned char, total_length>& buffer)
    {
        packet<T> pack;
        std::array<unsigned char, header_length> header_bytes;
        std::memcpy(header_bytes.data(), buffer.data(), header_length);
        pack.setHeader(rvt::deserialize<header<T>>(header_bytes));
        std::array<unsigned char, payload_length> payload_bytes;
        std::memcpy(payload_bytes.data(), buffer.data() + header_length, payload_length);
        pack.setPayload(rvt::deserialize<T>(payload_bytes));
        return pack;
    }

    private:

    header<T> _header;
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
        << ", " << head.timestamp
        << ", 0x" << hex << setfill('0') << setw(4) << head.crc32
        << " " << pack.getPayload() << ">" << std::dec;
} 

/* -----------------------------------------------------------------*/

} // namespace rvt

#endif // COMM_H
