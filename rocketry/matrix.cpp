#include <stdio.h>
#include "MatrixMath/MatrixMath.h"
#include "Kalman/KalmanLight.h"
#include "Kalman/Defines.h"

using namespace std;

void printm(float* A, int m, int n)
{
	for (int i = 0; i < m; i++)
	{
		for (int j = 0; j < n; j++)
		{
			printf("%g\t", A[n*i+j]);
		}
		printf("\n");
	}
}


void println(void)
{
    printf("\n");
}

int main()
{
   
    return 0;
}
