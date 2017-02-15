#include <stdio.h>
#include <stdlib.h>
#include "pid_lib.h"

int main(int argc, char** argv)
{
    struct Controller c = pid_init(0.3, 0, 0.2);
    
    int steps = 10;
    double position = 0, seek = 30, dt = 0.01;
    
    if (argc >= 2)
    {
        position = atoi(argv[1]);
    }
    if (argc >= 3)
    {   
        seek = atoi(argv[2]);
    }
    if (argc >= 4)
    {
        steps = atoi(argv[3]);
    }
    
    printf("%f\n", position);
    for (double i = 0; i < steps * dt; i += dt)
    {
        position += pid_seek(&c, position, seek, i);
        printf("%f\n", position);
    }
}
