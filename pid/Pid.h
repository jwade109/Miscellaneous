// Pid.h
#ifndef PID_H
#define PID_H

#include <inttypes.h>
#include <stdbool.h>

class PID
{
    public:
        double Kp; // proportional gain
        double Ki; // integral gain
        double Kd; // derivative gain
        
        PID(double P, double I, double D);
        
        void zero(void);
        
        void target(double target);
        
        void limit_windup(double max);
        
        double seek(double actual, double time);
        
    private:
        double setpoint;
        double prev_error;
        double integral;
        double max_int;
        double prev_p_response;
        double prev_i_response;
        double prev_d_response;
        bool has_prev;
};

#endif
