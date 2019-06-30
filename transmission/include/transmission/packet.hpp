// packet.hpp

#ifndef RVT_PACKET_HPP
#define RVT_PACKET_HPP

#include <vector>
#include <array>
#include <set>
#include <chrono>
#include <string>
#include <iostream>

namespace rvt
{

class packet
{
    public:

    static const uint8_t header_length = 16;

    packet();

    packet(const packet& pack);

    packet(const std::string& fmt);

    // header information is front loaded
    // SYNC | TIME | ID | LEN | CS | DATA...
    // 16 bytes in length

    uint16_t syncBytes() const;
    uint16_t& syncBytes();

    const std::chrono::system_clock::time_point& time() const;
    std::chrono::system_clock::time_point& time();

    uint16_t id() const;
    uint16_t& id();

    const std::string& name() const;
    std::string& name();

    uint16_t length() const;
 
    uint16_t checksum() const;
    uint16_t& checksum();

    std::array<uint8_t, 16> header() const;

    // end header data

    const std::vector<uint8_t>& data() const;
    std::vector<uint8_t>& data();

    bool isValid() const;
    
    uint16_t updateChecksum();

    const std::string& format() const;
    std::string& format();

    const std::set<std::string>& warnings() const;
    std::set<std::string>& warnings();

    private:

    uint16_t _sync_bytes;
    std::chrono::system_clock::time_point _time;
    uint16_t _id;
    std::string _name;
    std::vector<uint8_t> _data;
    uint16_t _checksum;
    std::string _format;
    mutable std::set<std::string> _warnings;

};

bool operator == (const packet& left, const packet& right);

std::vector<uint8_t>& operator << (std::vector<uint8_t> &bytes, const packet &pack);

std::vector<uint8_t>& operator >> (std::vector<uint8_t> &bytes, packet &pack);

bool nextPacket(packet &pack, std::vector<uint8_t>::iterator &begin,
                        const std::vector<uint8_t>::iterator &end);

packet str2packet(const std::chrono::system_clock::time_point &time,
                  const std::string &fstr);

std::string packet2str(const packet &pack);

std::string packet2str(const packet &pack, const std::string &fmt);

std::string pprintf(const std::string& fmt, const packet& pack);

std::ostream& operator << (std::ostream &os, const packet &pack);

std::vector<packet> fromFile(const std::string& binfile, uint16_t sync_bytes);

} // namespace rvt

#endif // RVT_PACKET_HPP
