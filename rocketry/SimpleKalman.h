// SimpleKalman.h
#ifndef SIMPLEKALMAN_H
#define SIMPLEKALMAN_H

using namespace std;

class SimpleKalman {
    
    public:
        double x;
        double p;
        double f;
        double h;
        double r;
        double q;
        
        SimpleKalman();
		
		SimpleKalman(double x, double p, double f, double r, double q);
        
        double step(double z);
};

#endif
