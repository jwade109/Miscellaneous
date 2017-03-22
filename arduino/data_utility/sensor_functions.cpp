// sensor_functions.cpp
#include "sensor_functions.h"

static Adafruit_BMP280 bmp;
static Adafruit_BNO055 bno;

void sensor_init(Adafruit_BMP280 bmp_, Adafruit_BNO055 bno_)
{
	bmp = bmp_;
	bno = bno_;
}

float getAltitude()
{
    return bmp.readAltitude(1013.25);
}

float getAltitude(int count)
{
    double sum = 0;
    for (int i = 0; i < count; i++)
    {
        sum += getAltitude();
    }
    return sum/count;
}

imu::Vector<3> getAcceleration()
{
    // Possible vector values can be:
    // - VECTOR_ACCELEROMETER - m/s^2
    // - VECTOR_MAGNETOMETER  - uT
    // - VECTOR_GYROSCOPE     - rad/s
    // - VECTOR_EULER         - degrees
    // - VECTOR_LINEARACCEL   - m/s^2
    // - VECTOR_GRAVITY       - m/s^2
    return bno.getVector(Adafruit_BNO055::VECTOR_LINEARACCEL);
}

imu::Vector<3> getAcceleration(int count)
{
    imu::Vector<3> sum;
    for (int i = 0; i < count; i++)
    {
        sum = sum + getAcceleration();
    }
    return sum/count;
}

imu::Vector<3> getMagnetic()
{
    return bno.getVector(Adafruit_BNO055::VECTOR_MAGNETOMETER);
}

imu::Vector<3> getMagnetic(int count)
{
    imu::Vector<3> sum;
    for (int i = 0; i < count; i++)
    {
        sum = sum + getMagnetic();
    }
    return sum/count;
}

imu::Vector<3> getOrientation()
{
    return bno.getVector(Adafruit_BNO055::VECTOR_EULER);
}

imu::Vector<3> getOrientation(int count)
{
    imu::Vector<3> sum;
    for (int i = 0; i < count; i++)
    {
        sum = sum + getOrientation();
    }
    return sum/count;
}
