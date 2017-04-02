// KalmanFilter.cpp

#include "KalmanFilter.h"
#include "MatrixMath.h"

using namespace std;

KalmanFilter::KalmanFilter(float sensorCov, float processCov)
{
    for (int i = 0; i < STATES; i++)
    {
        X[i][0] = 0;
        for (int j = 0; j < STATES; j++)
        {
            F[i][j] = 0;
            P[i][j] = 0;
            Q[i][j] = 0;
            Identity[i][j] = 0;
            if (i == j)
            {
                F[i][j] = 1;
                P[i][j] = 1;
                Q[i][j] = processCov;
                Identity[i][j] = 1;
            }
        }
        for (int j = 0; j < MEAS; j++)
        {
            H[i][j] = 0;
        }
    }
    for (int i = 0; i < MEAS; i++)
    {
        for (int j = 0; j < MEAS; j++)
        {
            R[i][j] = 0;
            if (i == j)
            {
                R[i][j] = sensorCov;
            }
        }
    }
    int min = MEAS;
    if (STATES < MEAS)
    {
        min = STATES;
    }
    for (int i = 0; i < min; i++)
    {
        H[i][i] = 1;
    }
}


float* KalmanFilter::step(float* Z)
{
    // X = F*X;
    {
        float copyOfX[STATES][1];
        Matrix::copy((float*) X, STATES, 1, (float*) copyOfX);
        Matrix::multiply((float*) F, (float*) copyOfX, STATES, STATES, 1, (float*) X);
    }
    // P = F*P*F.transpose() + Q;
    {
        float FT[STATES][STATES];
        Matrix::transpose((float*) F, STATES, STATES, (float*) FT);
        float PxFT[STATES][STATES];
        Matrix::multiply((float*) P, (float*) FT, STATES, STATES, STATES, (float*) PxFT);
        float FxPxFT[STATES][STATES];
        Matrix::multiply((float*) F, (float*) PxFT, STATES, STATES, STATES, (float*) FxPxFT);
        Matrix::add((float*) FxPxFT, (float*) Q, STATES, STATES, (float*) P);
    }
    // G = (P*H.transpose())*(H*P*H.transpose()+R).inverse();
    float G[STATES][MEAS];
    {
        float HT[STATES][MEAS];
        Matrix::transpose((float*) H, MEAS, STATES, (float*) HT);
        float PxHT[STATES][MEAS];
        Matrix::multiply((float*) P, (float*) HT, STATES, STATES, MEAS, (float*) PxHT);
        float HxPxHT[MEAS][MEAS];
        Matrix::multiply((float*) H, (float*) PxHT, MEAS, STATES, MEAS, (float*) HxPxHT);
        float HxPxHTandR[MEAS][MEAS];
        Matrix::add((float*) HxPxHT, (float*) R, MEAS, MEAS, (float*) HxPxHTandR);
        // invert HxPxHTandR without renaming
        int val = Matrix::invert((float*) HxPxHTandR, MEAS);
        Matrix::multiply((float*) PxHT, (float*) HxPxHTandR, STATES, MEAS, MEAS, (float*) G);
    }
    // X = X + G*(Z - H*X);
    {
        float HxX[MEAS][1];
        Matrix::multiply((float*) H, (float*) X, MEAS, STATES, 1, (float*) HxX);
        float ZminHxX[MEAS][1];
        Matrix::subtract((float*) Z, (float*) HxX, MEAS, 1, (float*) ZminHxX);
        float GxZminHxX[STATES][1];
        Matrix::multiply((float*) G, (float*) ZminHxX, STATES, MEAS, 1, (float*) GxZminHxX);
        float result[STATES][1];
        Matrix::add((float*) X, (float*) GxZminHxX, STATES, 1, (float*) result);
        Matrix::copy((float*) result, STATES, 1, (float*) X);
    }
    // P = (Identity(STATES,STATES)-G*H)*P;
    {
        float GxH[STATES][STATES];
        Matrix::multiply((float*) G, (float*) H, STATES, MEAS, STATES, (float*) GxH);
        float IminGxH[STATES][STATES];
        Matrix::subtract((float*) Identity, (float*) GxH, STATES, STATES, (float*) IminGxH);
        float result[STATES][STATES];
        Matrix::multiply((float*) IminGxH, (float*) P, STATES, STATES, STATES, (float*) result);
        Matrix::copy((float*) result, STATES, STATES, (float*) P);
    }
    // return X;
    return (float*) X;
}
