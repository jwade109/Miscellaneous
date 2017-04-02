// Pid.cpp
#include "Pid.h"
#include <stdio.h>

PID::PID(double P, double I, double D)
{
    Kp = P;
    Ki = I;
    Kd = D;
    prev_error = 0;
    integral = 0;
    has_prev = 0;
    max_int = 9999999;
}

void PID::zero(void)
{
    prev_error = 0;
    integral = 0;
    has_prev = 0;
}

void PID::target(double target)
{
    if (setpoint != target)
    {
        setpoint = target;
        zero();
    }
}

void PID::limit_windup(double max)
{
    max_int = max;
}

double PID::seek(double actual, double dt)
{
    double error = setpoint - actual, error_rate = 0;
    
    // assign steady state error with windup guards
    integral += error * dt;
    if (integral > max_int)
    {
        integral = max_int;
    }
    else if (integral < -max_int)
    {
        integral = -max_int;
    }
    
    // calculate error_rate if possible
    if (has_prev)
    {
        error_rate = (error - prev_error) / dt;
    }
    
    // store error for calculating error_rate next iteration
    prev_error = error;
    has_prev = 1;
    
    prev_p_response = Kp * error;
    prev_i_response = Ki * integral;
    prev_d_response = Kd * error_rate;
    
    return prev_p_response + prev_i_response + prev_d_response;
}
