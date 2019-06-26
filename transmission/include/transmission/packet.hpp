// packet.hpp

#ifndef RVT_PACKET_HPP
#define RVT_PACKET_HPP

#include <vector>
#include <set>
#include <chrono>
#include <string>
#include <iostream>

namespace rvt
{

class packet
{
    public:

    packet();
    
    std::vector<uint8_t> sync_bytes;
    std::chrono::system_clock::time_point time;
    uint16_t id;
    std::vector<uint8_t> data;
    uint16_t checksum;
    std::set<std::string> warnings;

    bool is_valid() const;

    uint16_t updateChecksum();
};

std::vector<uint8_t>& operator << (std::vector<uint8_t> &bytes, const packet &pack);

std::vector<uint8_t>& operator >> (std::vector<uint8_t> &bytes, packet &pack);

packet str2packet(const std::chrono::system_clock::time_point &time,
                  const std::string &fstr);

std::string packet2str(const packet &pack, const std::string &fmt = "");

std::ostream& operator << (std::ostream &os, const packet &pack);

} // namespace rvt

#endif // RVT_PACKET_HPP
