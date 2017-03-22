#ifndef KALMAN_LIB_H
#define KALMAN_LIB_H

#include <Eigen/Dense>
#include <math.h>

using namespace std;
using namespace Eigen;

class KalmanFilter {
    
    public:
        VectorXd X;
        MatrixXd P;
        MatrixXd F;
        MatrixXd H;
        MatrixXd R;
        MatrixXd Q;
        int M;
        int N;
        
        KalmanFilter(const int M = 1, const int N = 1);
        
        VectorXd step(VectorXd Z);
};

#endif
