// KalmanLight.cpp
#include "KalmanLight.h"
#include "../MatrixMath/MatrixMath.h"
#include <stdio.h>

using namespace std;

KalmanFilter::KalmanFilter()
{
    for (int i = 0; i < N; i++)
    {
        X[i][0] = 0;
        for (int j = 0; j < N; j++)
        {
            F[i][j] = 0;
            P[i][j] = 0;
            Q[i][j] = 0;
            if (i == j)
            {
                F[i][j] = 1;
                P[i][j] = 1;
                Q[i][j] = 0.01;
            }
        }
        for (int j = 0; j < M; j++)
        {
            H[i][j] = 0;
        }
    }
    for (int i = 0; i < M; i++)
    {
        for (int j = 0; j < M; j++)
        {
            R[i][j] = 0;
            if (i == j)
            {
                R[i][j] = 0.5;
            }
        }
    }
}


float* KalmanFilter::step(float* Z)
{
    Matrix::multiply((float*) H, Z, N, M, 1, (float*) X);
    return (float*) X;
    /*
    X = F*X;
    P = F*P*F.transpose() + Q;
    MatrixXd G = (P*H.transpose())*(H*P*H.transpose()+R).inverse();
    X = X + G*(Z - H*X);
    P = (MatrixXd::Identity(N,N)-G*H)*P;
    return X;
    */
}
