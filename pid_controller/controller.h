#ifndef CONTROLLER_H
#define CONTROLLER_H

struct Controller
{
    double Kp; // proportional gain
    double Ki; // integral gain
    double Kd; // derivative gain
    double lastP;
    double currentTime;
    double integral;
};

#endif 
