#define DEBUG
#pragma pack(1)

#include <iostream>
#include <array>
#include <cstring>

namespace rvt
{

const unsigned short SYNC_BYTES = 0x5456;

template <typename T>
std::array<unsigned char, sizeof(T)> serialize(const T& payload)
{
    std::array<unsigned char, sizeof(T)> buffer;
    std::memcpy(buffer.data(), &payload, sizeof(T));
    return buffer;
}

template <typename T>
T deserialize(const std::array<unsigned char, sizeof(T)>& buffer)
{
    T payload;
    std::memcpy(&payload, buffer.data(), sizeof(T));
    return payload;
}

template <typename T> struct header
{
    const unsigned short sync_bytes = SYNC_BYTES;
    const unsigned short payload_type = T::payload_type;
    const unsigned short payload_length = sizeof(T);
    unsigned long long timestamp;
    unsigned long crc32;

    const header<T>& operator = (const header<T>& other)
    {
        timestamp = other.timestamp;
        crc32 = other.crc32;
        return *this;
    }
};

template <typename T> struct packet
{
    static const unsigned short header_length = sizeof(header<T>);
    static const unsigned short payload_length = sizeof(T);
    static const unsigned short total_length = header_length + payload_length;
    
    static const unsigned short sync_bytes = SYNC_BYTES; 
    static const unsigned short payload_type = T::payload_type;

    packet()
    {
        _header.crc32 = 0;
        _header.timestamp = 0;
    }

    void setPayload(const T& payload)
    {
        _payload = payload;
        _header.crc32 = calculateCRC32();
    }

    const T& getPayload() const
    {
        return _payload;
    }

    void setHeader(const header<T>& header)
    {
        _header = header;
        _header.crc32 = calculateCRC32();
    }

    const header<T>& getHeader() const
    {
        return _header;
    }

    void setTimestamp(unsigned long long ts)
    {
        _header.timestamp = ts;
    }
    
    std::array<unsigned char, total_length> serialize() const
    {
        std::array<unsigned char, total_length> buffer;
        auto header_bytes = rvt::serialize(_header);
        std::memcpy(buffer.data(), header_bytes.data(), header_length);
        auto payload_bytes = rvt::serialize(_payload);
        std::memcpy(buffer.data() + header_length, payload_bytes.data(), payload_length);
        return buffer;
    }

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

    static unsigned long calculateCRC32()
    {
        // TBD
        return 0x98765432;
    }
};

template <typename T, typename U = decltype(T::name)>
std::ostream& operator << (std::ostream& os, const T&)
{
    os << T::name;
}

template <typename T>
std::ostream& operator << (std::ostream& os, const packet<T>& pack)
{
    auto header = pack.getHeader();
    os << std::hex << "sync bytes: 0x" << (int) header.sync_bytes << std::endl;
    os << std::dec << "payload type: " << (int) header.payload_type << std::endl;
    os << std::dec << "payload len: " << (int) header.payload_length << std::endl;
    os << std::hex << "crc32: 0x" << header.crc32 << std::endl;
    os << pack.getPayload();
    return os;
}

struct sensor_raw
{
    static constexpr char const *name = "sensor_raw";
    static const unsigned short payload_type = 0x01;

    int x, y, z, a;
};

struct motor_ctrl
{
    static constexpr char const *name = "motor_ctrl";
    static const unsigned short payload_type = 0x02;
    int a, b;
};

} // namespace rvt

int main(int argc, char** argv)
{
    using namespace std;
    using namespace rvt;
    cout << "type: " << (int) sensor_raw::payload_type << endl;
    cout << "header len: " << (int) packet<sensor_raw>::header_length << endl;
    cout << "payload len: " << (int) packet<sensor_raw>::payload_length << endl;
    cout << "total len: " << (int) packet<sensor_raw>::total_length << endl;

    sensor_raw sv{56, 101, 200, -54};
    packet<sensor_raw> pack;
    pack.setPayload(sv);
    pack.setTimestamp(0x8989898989898989);
    auto buffer = pack.serialize();
    auto sn = packet<sensor_raw>::deserialize(buffer).getPayload();

    cout << sn.x << " " << sn.y << " " << sn.z << " " << sn.a << endl;
    for (auto e : buffer) std::cout << std::hex << (int) e << " ";
    cout << endl << pack << endl;

    cout << packet<motor_ctrl>() << endl;

    return 0;
}
