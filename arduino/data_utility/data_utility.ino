// data_utility.ino
#include <Wire.h>
#include <SPI.h>

#include "sensor_functions.h"
#include "RocketMath.h"

#define BMP_SCK     13
#define BMP_MISO    12
#define BMP_MOSI    11
#define BMP_CS      10

#define NOMINAL_DT 0.001

Adafruit_BMP280 bmp(BMP_CS, BMP_MOSI, BMP_MISO,  BMP_SCK);
Adafruit_BNO055 bno = Adafruit_BNO055();

// calibration offsets
double y0;
imu::Vector<3> a0;
imu::Vector<3> theta0;

double alt_prev;

KalmanFilter filter(0,0);

void setup()
{
    Serial.begin(9600);
    if (!bmp.begin())
    {
        Serial.println("Failed to initialize BMP280.");
        while (1);
    }
    if(!bno.begin())
    {
        Serial.println("Failed to initialize BNO055.");
        while(1);
    }
    sensor_init(bmp, bno);

    y0 = getAltitude(50);
    a0 = getAcceleration(200);
    theta0 = getOrientation(50);

    filter.F[0][1] = 0.02;
    filter.F[0][0] = 1;
    filter.F[1][1] = 1;
    filter.R[0][0] = 0.08;
    filter.R[1][1] = 0.02;
}

void loop()
{
    double accel = getAcceleration().z() - a0.z();
    double alt = getAltitude() - y0;

    float Z[2] = {alt, accel};

    float* X = filter.step((float*) Z);

    double better_alt = X[0];
    double better_accel = X[1];

    double vel = better_alt - alt_prev;

    alt_prev = better_alt;

//    Serial.print(better_alt);
    Serial.print("\t");
    Serial.print(vel/0.02);
    Serial.println("\t");
//    Serial.println(better_accel);
}

