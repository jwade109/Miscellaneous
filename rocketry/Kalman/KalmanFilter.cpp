// KalmanFilter.cpp

#include "KalmanFilter.h"
#include "MatrixMath.h"

using namespace std;

KalmanFilter::KalmanFilter(float sensorCov, float processCov)
{
    for (int i = 0; i < N; i++)
    {
        X[i][0] = 0;
        for (int j = 0; j < N; j++)
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
                R[i][j] = sensorCov;
            }
        }
    }
    int min = M;
    if (N < M)
    {
        min = N;
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
        float copyOfX[N][1];
        Matrix::copy((float*) X, N, 1, (float*) copyOfX);
        Matrix::multiply((float*) F, (float*) copyOfX, N, N, 1, (float*) X);
    }
    // P = F*P*F.transpose() + Q;
    {
        float FT[N][N];
        Matrix::transpose((float*) F, N, N, (float*) FT);
        float PxFT[N][N];
        Matrix::multiply((float*) P, (float*) FT, N, N, N, (float*) PxFT);
        float FxPxFT[N][N];
        Matrix::multiply((float*) F, (float*) PxFT, N, N, N, (float*) FxPxFT);
        Matrix::add((float*) FxPxFT, (float*) Q, N, N, (float*) P);
    }
    // G = (P*H.transpose())*(H*P*H.transpose()+R).inverse();
    float G[N][M];
    {
        float HT[N][M];
        Matrix::transpose((float*) H, M, N, (float*) HT);
        float PxHT[N][M];
        Matrix::multiply((float*) P, (float*) HT, N, N, M, (float*) PxHT);
        float HxPxHT[M][M];
        Matrix::multiply((float*) H, (float*) PxHT, M, N, M, (float*) HxPxHT);
        float HxPxHTandR[M][M];
        Matrix::add((float*) HxPxHT, (float*) R, M, M, (float*) HxPxHTandR);
        // invert HxPxHTandR without renaming
        int val = Matrix::invert((float*) HxPxHTandR, M);
        Matrix::multiply((float*) PxHT, (float*) HxPxHTandR, N, M, M, (float*) G);
    }
    // X = X + G*(Z - H*X);
    {
        float HxX[M][1];
        Matrix::multiply((float*) H, (float*) X, M, N, 1, (float*) HxX);
        float ZminHxX[M][1];
        Matrix::subtract((float*) Z, (float*) HxX, M, 1, (float*) ZminHxX);
        float GxZminHxX[N][1];
        Matrix::multiply((float*) G, (float*) ZminHxX, N, M, 1, (float*) GxZminHxX);
        float result[N][1];
        Matrix::add((float*) X, (float*) GxZminHxX, N, 1, (float*) result);
        Matrix::copy((float*) result, N, 1, (float*) X);
    }
    // P = (Identity(N,N)-G*H)*P;
    {
        float GxH[N][N];
        Matrix::multiply((float*) G, (float*) H, N, M, N, (float*) GxH);
        float IminGxH[N][N];
        Matrix::subtract((float*) Identity, (float*) GxH, N, N, (float*) IminGxH);
        float result[N][N];
        Matrix::multiply((float*) IminGxH, (float*) P, N, N, N, (float*) result);
        Matrix::copy((float*) result, N, N, (float*) P);
    }
    // return X;
    return (float*) X;
}
