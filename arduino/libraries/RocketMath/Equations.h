// Equations.h

#ifndef EQUATIONS_H
#define EQUATIONS_H

namespace Equation
{
    double accel(double v_burn, double m, double k, double t);
    
    double vel(double v_burn, double m, double k, double t);
    
    double alt(double y_burn, double v_burn, double m, double k, double t);
    
    double t_a(double v_burn, double m, double k);
}

#endif