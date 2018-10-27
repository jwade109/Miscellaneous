#include <fstream>
#include <vector>

#include "comm.hpp"
#include "messages.hpp"

int main(int argc, char** argv)
{
    std::ifstream in("out.bin");

    in.seekg(0, in.end);
    size_t length = in.tellg();
    in.seekg(0, in.beg);

    std::vector<unsigned char> buffer(length);
    in.read((char*) buffer.data(), length);

    for (int i = 0; i < buffer.size();)
    {
        if (((buffer[i+1] << 8) | buffer[i]) == rvt::SYNC_BYTES)
        {
            unsigned short payload_type = buffer[i+2];
            using namespace rvt;
            if (payload_type == 0x01)
            {
                std::array<unsigned char, packet<sensor_raw>::total_length> bytes;
                for (auto& e : bytes) e = buffer[i++];
                std::cout << packet<sensor_raw>::deserialize(bytes) << std::endl;
            }
            else if (payload_type == 0x02)
            {
                std::array<unsigned char, packet<motor_ctrl>::total_length> bytes;
                for (auto& e : bytes) e = buffer[i++];
                std::cout << packet<motor_ctrl>::deserialize(bytes) << std::endl;
            }
            else if (payload_type == 0x03)
            {                
                std::array<unsigned char, packet<string_msg>::total_length> bytes;
                for (auto& e : bytes) e = buffer[i++];
                std::cout << packet<string_msg>::deserialize(bytes) << std::endl;
            }
            else
            {
                std::cout << std::hex << "0x" << i << ": "
                          << "unknown message encountered: 0x"
                          << std::hex << payload_type << std::endl;
                ++i;
            }
        }
        else ++i;
    }

    in.close();

    return 0;
}
