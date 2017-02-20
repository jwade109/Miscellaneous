#ifndef KALMAN_H
#define KALMAN_H

#include <inttypes.h>

typedef struct
{
    double q; // process noise covariance
    double r; // measurement noise covariance
    double x; // value
    double p; // estimation error covariance
    double k; // kalman gain
}
Kalman_State;

Kalman_State kalman_init(double q, double r, double p, double initial);

void kalman_zero(Kalman_State* state);

double kalman_update(Kalman_State* state, double measurement);

#endif
