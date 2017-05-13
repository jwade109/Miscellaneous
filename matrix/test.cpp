#include <stdio.h>
#include "Matrix.h"
#include "Kalman.h"

int main()
{
    {
    const int M = 2, N = 2;
    KalmanFilter kalman(M, N);
    Matrix Z(M);
    Z = {2, 1};
    for (int i = 0; i < 10; i++)
    {
        kalman.X.print("[State]");
        Z.print("[Measurement]");
        kalman.step(Z);
    }
}
