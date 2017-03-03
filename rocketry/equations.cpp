#include "equations.h"
#include <stdio.h>
#include <math.h>
#include <time.h>
#include <stdbool.h>
#include <string.h>

/*
Precondition: all functions are called with valid arguments.
This is to save on computation time. Most functions are invalid
past apogee or before burnout, so take care to ensure any
function returns valid data.
*/

double vel(double v_burn, double m, double k, double t) // 0.12 μs on average
{
	return sqrt(m*g/k)*tan(atan(v_burn*sqrt(k/(m*g)))-t*sqrt(g*k/m));
}

double alt(double y_burn, double v_burn, double m, double k, double t) // 0.30 μs on average
{
    return sqrt(g*m/k)*log(cos(sqrt(g*k/m)*t - atan(sqrt(k/(g*m))*v_burn)))/sqrt(g*k/m) + (sqrt(g*k/m)*y_burn - sqrt(g*m/k)*log(1/sqrt(1 + k*v_burn*v_burn/(g*m))))/sqrt(g*k/m);
}

double t_a(double v_burn, double m, double k) // 0.10 μs on average
{
    return atan(v_burn*sqrt(k/(m*g)))/sqrt(g*k/m);
}

double T(double mass, double velocity)
{
    return 0.5 * mass * velocity * velocity;
}

double V(double mass, double altitude)
{
    return mass * g * altitude;
}

void wait(double seconds)
{
    clock_t begin = clock();
    double sec_passed;
    while (sec_passed < seconds)
    {
        sec_passed = ((double)(clock()-begin))/CLOCKS_PER_SEC;
    }
    return;
}

void print_splash()
{
    printf("\n\n");
    printf("\t                 :Ns                                `-:/+++`\n");
    printf("\t                +NNNh`                        `:+shNNNNNdo. \n");
    printf("\t               sNmNNNd.          `:/.   `:+shNNNNNNNhs/.    \n");
    printf("\t             `hN/ yNNNm-        dmdddyhmNNNNNNhs+:`         \n");
    printf("\t            .dm-   oNNNN/     `sdmNNNNNNds+:.               \n");
    printf("\t           -md.     /NNNN:.+ymNyNNdddds                     \n");
    printf("\t          /Nh`       -yyhmmho/-`.-hdyo.                     \n");
    printf("\t         oNs       -ohysyydh`                               \n");
    printf("\t       `yNm     :syo-  `hNNNm.   `yhho     shhy             \n");
    printf("\t      `dNNNdsshm+.       sNNNN:   :NNN:   +NNN-://////.     \n");
    printf("\t     .mNNNNNNNd`          +NNNN/   /NNN. -NNN: ydmNNdd/     \n");
    printf("\t    :NNNNNNNNNm:           :NNNNo   oNNh`dNN+    sNN-       \n");
    printf("\t   +NNNNNNNNNNNNNhooooooooooyNNNNy`  yNNdNNs     sNN-       \n");
    printf("\t  sNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNd. `dNNNh      sNN-         ΔVT inVenTs 2017.\n");
    printf("\t :NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN+  `NNN`      /NN.         Invent the Future.\n");
    printf("\n");
}

void load(int cycles)
{
    for (int i = 0; i < cycles; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            printf(".");
            fflush(stdout);
            wait(0.07);
        }
        printf("\r   \r");
        fflush(stdout);
        wait(0.1);
    }
}
