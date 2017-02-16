#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "pid_lib.h"

int main(int argc, char** argv)
{    
    int steps = 10;
    double position = 0, seek = 100, dt = 0.01;
    double P = 0.3, I = 0.1, D = 0;
    
    for (int i = 1; i < argc; i++)
    {
        if (argc % 2 == 1)
        {
            if (strcmp(argv[i], "-p") == 0)
            {
                i++;
                P = atoi(argv[i]);
            }
            else if (strcmp(argv[i], "-i") == 0)
            {
                i++;
                I = atoi(argv[i]);
            }
            else if (strcmp(argv[i], "-d") == 0)
            {
                i++;
                D = atoi(argv[i]);
            }
            else if (strcmp(argv[i], "-a") == 0)
            {
                i++;
                position = atoi(argv[i]);
            }
            else if (strcmp(argv[i], "-b") == 0)
            {
                i++;
                seek = atoi(argv[i]);
            }
            else if (strcmp(argv[i], "-c") == 0)
            {
                i++;
                steps = atoi(argv[i]);
            }
        }
        else
        {
            printf("usage: [-p Kp] [-i Ki] [-d Kd] [-a start] [-b seek] [-c steps]\n");
            return 1;
        }
    }
    
    Controller c = pid_init(P, I, D, 15);
    
    printf("%f\n", position);
    for (double i = 0; i < steps * dt; i += dt)
    {
        double stearing = pid_seek(&c, position, seek, i);
        position += stearing;
        printf("%f\t\t%f\n", position, stearing);
    }
}
