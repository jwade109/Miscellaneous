// KalmanFilter.h

#ifndef KALMAN_FILTER_H
#define KALMAN_FILTER_H

#include "Dims.h"

using namespace std;

class KalmanFilter {
    
    public:
        float X[N][1]; // Nx1
        float P[N][N]; // NxN
        float F[N][N]; // NxN
        float H[N][M]; // NxM
        float R[M][M]; // MxM
        float Q[N][N]; // NxN
        
        KalmanFilter(float sensorCov, float processCov);
        
        float* step(float* Z);

    private:
        float Identity[N][N];
};

#endif
