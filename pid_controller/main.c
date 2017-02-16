#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "pid_lib.h"

void wait(int millis)
{
    for (int i = 0; i < millis * 200000; i++)
    {
        // do nothing
    }
}

int main(int argc, char** argv)
{
    double position = 0, setpoint = 10, dt = 0.01;
    double P = 1, I = 0, D = 800;
    
    for (int i = 1; i < argc; i++)
    {
        if (argc % 2 == 1)
        {
            if (strcmp(argv[i], "-P") == 0)
            {
                i++;
                sscanf(argv[i], "%lf", &P);
            }
            else if (strcmp(argv[i], "-I") == 0)
            {
                i++;
                sscanf(argv[i], "%lf", &I);
            }
            else if (strcmp(argv[i], "-D") == 0)
            {
                i++;
                sscanf(argv[i], "%lf", &D);
            }
            else if (strcmp(argv[i], "--initial") == 0)
            {
                i++;
                sscanf(argv[i], "%lf", &position);
            }
            else if (strcmp(argv[i], "--setpoint") == 0)
            {
                i++;
                sscanf(argv[i], "%lf", &setpoint);
            }
        }
        else
        {
            printf("usage: [-P Kp] [-I Ki] [-D Kd] [--initial val] [--setpoint val]\n");
            return 1;
        }
    }
    
    Controller c = pid_init(P, I, D, -1);
    
    double velocity = 0;
    printf("Kd: %.2f\tKi: %.2f\tKd: %.2f\n", c.Kp, c.Ki, c.Kd);
    wait(4000);
    printf("%f\n", position);
    for (double i = 0; i < 100000; i += dt)
    {
        double response = pid_seek(&c, position, setpoint, i);
        velocity += response * dt;
        position += velocity * dt;
        printf(" %.2f\t%.2f\t", i, position);
        int i = 0;
        for (int i = 0; i < 100; i++)
        {
            if (i == 50)
                printf("*");
            else if (i < position * 50 / setpoint)
                printf("|");
            else if (i < 50)
                printf(" ");
        }
        printf("\n");
        wait(dt * 1000);
    }
}
