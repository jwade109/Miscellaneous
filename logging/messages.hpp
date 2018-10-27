#ifndef MESSAGE_H
#define MESSAGE_H

#pragma pack(1)

namespace rvt
{

/* SENSOR RAW MEASUREMENTS MESSAGE TYPE ----------------------------*/

struct sensor_raw
{
    static constexpr char const *name = "sensor_raw";
    static const unsigned short payload_type = 0x01;

    double x, y, z, a, b, c, d, e, f, g, h, i, j, k;
};

/* MOTOR CONTROL COMMAND MESSAGE TYPE ------------------------------*/

struct motor_ctrl
{
    static constexpr char const *name = "motor_ctrl";
    static const unsigned short payload_type = 0x02;

    unsigned short a[16];
};

std::ostream& operator << (std::ostream& os, const motor_ctrl& msg)
{
    os << "<" << motor_ctrl::name;
    for (auto e : msg.a)
        os << ", " << e;
    return os << ">";
}

/* STRING MESSAGE TYPE ---------------------------------------------*/

struct string_msg
{
    static constexpr char const* name = "string_msg";
    static const unsigned short payload_type = 0x03;

    char str[10];
};

std::ostream& operator << (std::ostream& os, const string_msg& msg)
{
    os << "<" << string_msg::name << ", \"";
    for (auto e : msg.str)
        os << e;
    return os << "\">";
}

} // namespace rvt

#endif // MESSAGE_H
