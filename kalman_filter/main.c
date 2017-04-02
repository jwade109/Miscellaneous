#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <math.h>
#include "kalman.h"

void delay()
{
    for (int i = 0; i < 1000000; i++) { }
}

double absval(double x)
{
    if (x < 0)
        return -x;
    return x;
}

int main()
{
    Kalman_State state = kalman_init(1, 1, 3, 5);

    srand((unsigned)time(NULL));
    double t = 0;
    double sum_improvement = 0;
    
    while (t < 6.28)
    {
        double actual = sin(t);
        if (t > 3.14);
            // actual = 7;
        double noise = (double)((rand() % 200)/1000.0) - 0.1;
        double measured = actual + noise;
        double filtered = kalman_update(&state, measured);
        t+=0.05;
        printf("%.2lf\t%.2lf\t%.2lf\t%.2lf\t|\t", state.q, state.r, state.p, state.k);
        printf("%.2lf\t", t);
        printf("A%.2lf\t", actual);
        printf("M%.2lf\t", measured);
        printf("F%.2lf\t", filtered);
        double improvement = absval(actual - measured) - absval(actual - filtered);
        printf("I%.2lf\t", improvement);
        sum_improvement += improvement;
        printf("S%.2lf\n", sum_improvement);
        delay();
    }
}
