// KalmanFilter.h

#ifndef KALMAN_FILTER_H
#define KALMAN_FILTER_H

#include "Dims.h"

using namespace std;

class KalmanFilter {
    
    public:
        float X[STATES][1]; // Nx1
        float P[STATES][STATES]; // NxN
        float F[STATES][STATES]; // NxN
        float H[STATES][MEAS]; // NxM
        float R[MEAS][MEAS]; // MxM
        float Q[STATES][STATES]; // NxN
        
        KalmanFilter(float sensorCov, float processCov);
        
        float* step(float* Z);

    private:
        float Identity[STATES][STATES];
};

#endif
