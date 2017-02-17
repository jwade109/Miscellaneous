#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "pid_lib.h"

static char verbose = 0;
static int touch = -1;
static double external_error = 0;

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
    double P = 1, I = 0.02, D = 2;
    
    for (int i = 1; i < argc; i++)
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
        else if (strcmp(argv[i], "--verbose") == 0 || strcmp(argv[i], "-v") == 0)
        {
            verbose = 1;
        }
        else if (strcmp(argv[i], "--touch") == 0)
        {
            i++;
            sscanf(argv[i], "%d", &touch);
        }
        else if (strcmp(argv[i], "--external") == 0)
        {
            i++;
            sscanf(argv[i], "%lf", &external_error);
        }
        else
        {
            printf("usage: [-v --verbose] [-P Kp] [-I Ki] [-D Kd] [--initial val] [--setpoint val] ");
            printf("[--touch val] [--external val]\n");
            return 1;
        }
    }
    
    Controller c = pid_init(P, I, D, -1);
    
    double velocity = 0;
    printf("Kd: %.2f\tKi: %.2f\tKd: %.2f\n", c.Kp, c.Ki, c.Kd);
    wait(4000);
    int count = 0;
    for (double t = 0; t < 100000; t += dt)
    {
        double response = pid_seek(&c, position, setpoint, dt);
        velocity += (response - external_error) * dt;
        if (count == touch * 100)
        {
            printf("TOUCH\n");
            velocity = -setpoint * 2;
        }
        position += velocity * dt;
        printf(" %.2f:\t", t);
        if (verbose)
        {
            printf("(%.2f | %.2f | %.2f > %.2f )\t", c.prev_p_response, c.prev_i_response, c.prev_d_response, response);
        }
        else
        {
            printf("%.2f\t", response);
        }
        printf("%.2f\t", position);
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
        count++;
    }
}
