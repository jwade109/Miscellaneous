#include "Equations.h"
#include <stdio.h>
#include <math.h>
#include <time.h>
#include <stdbool.h>
#include <string.h>

/*
 * Precondition: all functions are called with valid arguments.
 * This is to save on computation time. Most functions are invalid
 * past apogee or before burnout, so take care to ensure any
 * function returns valid data.
 */

static const double g = 9.81;

double Equation::accel(double v_burn, double m, double k, double t)
{
    double v = vel(v_burn, m, k, t);
    return -g - (k/m)*v*v;
}

double Equation::vel(double v_burn, double m, double k, double t) // 0.12 μs on average
{
	return sqrt(m*g/k)*tan(atan(v_burn*sqrt(k/(m*g)))-t*sqrt(g*k/m));
}

double Equation::alt(double y_burn, double v_burn, double m, double k, double t) // 0.30 μs on average
{
    return sqrt(g*m/k)*log(cos(sqrt(g*k/m)*t - atan(sqrt(k/(g*m))*v_burn)))/sqrt(g*k/m) + (sqrt(g*k/m)*y_burn - sqrt(g*m/k)*log(1/sqrt(1 + k*v_burn*v_burn/(g*m))))/sqrt(g*k/m);
}

double Equation::t_a(double v_burn, double m, double k) // 0.10 μs on average
{
    return atan(v_burn*sqrt(k/(m*g)))/sqrt(g*k/m);
}