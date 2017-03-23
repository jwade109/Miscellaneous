// SimpleKalman.cpp
#include "SimpleKalman.h"

using namespace std;

SimpleKalman::SimpleKalman()
{
    x = 0;
    p = 1;
    f = 1;
    h = 1;
    r = 0.5;
    q = 0.1;
}

SimpleKalman::SimpleKalman(double x, double p, double f, double r, double q)
{
    this->x = x;
    this->p = p;
    this->f = f;
    this->r = r;
    this->q = q;
    h = 1;
}

double SimpleKalman::step(double z)
{
    x = f*x;
    p = p*f*f + q;
    double g = (p*h)/(h*h*p+r);
    x = x + g*(z - h*x);
    p = (1-g*h)*p;
    return x;
}
