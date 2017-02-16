#ifndef PID_LIB_H
#define PID_LIB_H

#include <inttypes.h>
#include <stdbool.h>

typedef struct
{
    double Kp; // proportional gain
    double Ki; // integral gain
    double Kd; // derivative gain
    double prev_error;
    double integral;
    double max_int;
    bool has_prev;
}
Controller;

Controller pid_init(double Kp, double Ki, double Kd, double max);

void pid_zero(Controller* c);

double pid_seek(Controller* c, double actual, double setpoint, double time);

void print(Controller* c);

#endif
