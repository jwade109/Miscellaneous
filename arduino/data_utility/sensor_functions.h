// sensor_functions.h
#ifndef SENSORFUNCTIONS_H
#define SENSORFUNCTIONS_H

#include <Adafruit_Sensor.h>
#include <Adafruit_BMP280.h>
#include <Adafruit_BNO055.h>
#include <utility/imumaths.h>

void sensor_init(Adafruit_BMP280 bmp, Adafruit_BNO055 bno);

float getAltitude();

float getAltitude(int count);

imu::Vector<3> getAcceleration();

imu::Vector<3> getAcceleration(int count);

imu::Vector<3> getMagnetic();

imu::Vector<3> getMagnetic(int count);

imu::Vector<3> getOrientation();

imu::Vector<3> getOrientation(int count);

#endif
