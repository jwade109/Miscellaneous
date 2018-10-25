
namespace rvt
{

/* SENSOR RAW MEASUREMENTS MESSAGE TYPE ----------------------------*/

struct sensor_raw
{
    static constexpr char const *name = "sensor_raw";
    static const unsigned short payload_type = 0x01;

    int x, y, z, a;
};

/* MOTOR CONTROL COMMAND MESSAGE TYPE ------------------------------*/

struct motor_ctrl
{
    static constexpr char const *name = "motor_ctrl";
    static const unsigned short payload_type = 0x02;

    unsigned short a[8];
};

std::ostream& operator << (std::ostream& os, const motor_ctrl& msg)
{
    os << "<" << motor_ctrl::name;
    for (auto e : msg.a)
        os << ", " << e;
    return os << ">";
}

/* -----------------------------------------------------------------*/

} // namespace rvt

