#include <Eigen/Dense>
#include <iostream>

#include "kalman_lib.h"

using namespace std;
using namespace Eigen;

int main()
{
    int M, N;
    cin >> M;
    cin >> N;
    KalmanFilter filter(M,N);
    
    filter.F(0,0) = 0.9;
    filter.X(0) = 100;
    
    VectorXd Z = VectorXd::Zero(M);
    int pos = 100;
    for (int i = 0; i < 10; i++)
    {
        Z(0) = pos;
        VectorXd x = filter.step(Z);
        cout << x(0) << ", " << pos << endl;
        pos = pos * 0.9;
    }
}
