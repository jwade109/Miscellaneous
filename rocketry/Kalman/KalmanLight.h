// KalmanLight.h

#ifndef KALMAN_LIGHT_H
#define KALMAN_LIGHT_H

#include "Defines.h"

using namespace std;

class KalmanFilter {
    
    public:
        float X[N][1]; // Nx1
        float P[N][N]; // NxN
        float F[N][N]; // NxN
        float H[N][M]; // NxM
        float R[M][M]; // MxM
        float Q[N][N]; // NxN
        
        KalmanFilter();
        
        float* step(float* Z);

};

#endif
