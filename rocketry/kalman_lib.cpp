#include <Eigen/Dense>
#include <iostream>
#include "kalman_lib.h"

using namespace std;
using namespace Eigen;

KalmanFilter::KalmanFilter(const int M, const int N)
{
    this-> M = M;
    this-> N = N;
    X = VectorXd::Zero(N);
    P = MatrixXd::Identity(N,N);
    F = MatrixXd::Identity(N,N);
    H = MatrixXd::Identity(M,N);
    R = MatrixXd::Identity(M,M) * 0.5;
    Q = MatrixXd::Identity(N,N) * 0.1;
}

VectorXd KalmanFilter::step(const VectorXd Z)
{
    X = F*X;
    P = F*P*F.transpose() + Q;
    MatrixXd G = (P*H.transpose())*(H*P*H.transpose()+R).inverse();
    X = X + G*(Z - H*X);
    P = (MatrixXd::Identity(N,N)-G*H)*P;
    return X;
}
