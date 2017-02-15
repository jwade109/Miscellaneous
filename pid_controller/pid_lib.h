#ifndef PID_LIB_H
#define PID_LIB_H

#include <inttypes.h>
#include "controller.h"

struct Controller pid_init(double Kp, double Ki, double Kd);

double pid_seek(struct Controller* c, double actual, double desired, double time);

void print(struct Controller* c);

#endif
