#include "pid_lib.h"
#include <stdio.h>

struct Controller pid_init(double Kp, double Ki, double Kd)
{
    struct Controller c;
    c.Kp = Kp;
    c.Ki = Ki;
    c.Kd = Kd;
    c.lastP = 0;
    c.currentTime = -1;
    c.integral = 0;
    return c;
}

double pid_seek(struct Controller* c, double actual, double desired, double time)
{
    double P = desired - actual, I, D;
    if (c -> currentTime == -1)
    {
        I = 0;
        D = 0;
    }
    else
    {
        double dt = time - c -> currentTime;
        c -> integral += P * dt;
        I = c -> integral;
        D = (P - c -> lastP) * dt;
        c -> lastP = P;
    }
    c -> currentTime = time;
    
    return c -> Kp * P + c -> Ki * I + c -> Kd * D;
}

void print(struct Controller* c)
{
    struct Controller con = *c;
    printf("Controller: time = %f, Kp = %f, Ki = %f, Kd = %f, lastP = %f, integral = %f\n", con.currentTime, con.Kp, con.Ki, con.Kd, con.lastP, con.integral);
}
