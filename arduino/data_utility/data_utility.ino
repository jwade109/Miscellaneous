// data_utility.ino
#include <Wire.h>
#include <SPI.h>
#include <SimpleKalmanFilter.h>

#include "equations.h"
#include "sensor_functions.h"

#define BMP_SCK     13
#define BMP_MISO    12
#define BMP_MOSI    11 
#define BMP_CS      10

#define NOMINAL_DT  0.05
#define LOW_DRAG    0.005
#define HIGH_DRAG   0.012
#define MASS        5
#define V_MIN       8
#define TARGET      3800

Adafruit_BMP280 bmp(BMP_CS, BMP_MOSI, BMP_MISO,  BMP_SCK);
Adafruit_BNO055 bno = Adafruit_BNO055();

// calibration offsets
double y0;
imu::Vector<3> a0;
imu::Vector<3> theta0;

// altitude and previous altitude
double yi, yi_prev;

// time and previous time
double ti = 0, ti_prev;
// accumulated velocity from acceleration
double vi_acc;

bool firstTime = true;

SimpleKalmanFilter kalman(0.2, 0.1, 0.1);

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

    Serial.print("Calibrating.\n");
    y0 = getAltitude(50);
    a0 = getAcceleration(200);
    theta0 = getOrientation(50);

    Serial.println("----------------------------------------------------");
    ti = micros()/1000000.0;
}

void loop()
{
    yi_prev = yi;
    
    /* records the data from the last step */
    ti_prev = ti;

    /* measures how long this step took to slow down for nominal dt */
    double t_start = micros()/1000000.0;

    /* collects all raw data for this timestep */
    ti = t_start;
    double dt = ti - ti_prev;

    double real_value = 50 + 10 * sin(1.5*ti);
    double measurement = real_value + random(-200,200)/100.0;

    Serial.print(15);
    Serial.print(" ");
    Serial.print(real_value);
    Serial.print(" ");
//    Serial.print(measurement);
    
    double filtered = kalman.updateEstimate(measurement);
    
    Serial.print(" ");
    Serial.println(filtered);

    if (firstTime)
    {
        firstTime = false;
        return;
    }

    double actual_dt = micros()/1000000.0 - t_start;
    if (actual_dt < NOMINAL_DT)
    {
        delay((NOMINAL_DT - actual_dt) * 1000);
    }
}

