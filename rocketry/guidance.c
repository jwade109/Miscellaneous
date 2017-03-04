#include "equations.h"
#include "../pid/pid_lib.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

    

int main(int argc, char** argv)
{
    system("clear");
    clock_t start = clock();
    
    int noWait = 0;
    int noPrint = 0;
    int compact = 0;
    
    double yb = 160, vb = 100, kl = 0.005, kh = 0.015, m = 5, target = 400;
    double dt = 0.5;

    for (int i = 1; i < argc; i++)
    {
        if (!strcmp(argv[i], "yb"))
        {
            sscanf(argv[i+1], "%lf", &yb);
        }
        else if (!strcmp(argv[i], "vb"))
        {
            sscanf(argv[i+1], "%lf", &vb);
        }
        else if (!strcmp(argv[i], "kl"))
        {
            sscanf(argv[i+1], "%lf", &kl);
        }
        else if (!strcmp(argv[i], "kh"))
        {
            sscanf(argv[i+1], "%lf", &kh);
        }
        else if (!strcmp(argv[i], "m"))
        {
            sscanf(argv[i+1], "%lf", &m);
        }
        else if (!strcmp(argv[i], "target"))
        {
            sscanf(argv[i+1], "%lf", &target);
        }
        else if (!strcmp(argv[i], "dt"))
        {
            sscanf(argv[i+1], "%lf", &dt);
        }
        else if (!strcmp(argv[i], "fast"))
        {
            noWait = 1;
        }
        else if (!strcmp(argv[i], "noprint"))
        {
            noPrint = 1;
            noWait = 1;
        }
        else if (!strcmp(argv[i], "compact"))
        {
            compact = 1;
        }
    }
    
    double vi = vb;
    double yi = yb;
    double ki = kl;
    
    double ld_ta = t_a(vb, m, kl);
    double ld_alt = alt(yb, vb, m, kl, ld_ta);
    double hd_ta = t_a(vb, m, kh);
    double hd_alt = alt(yb, vb, m, kh, hd_ta);

    if (!noPrint)
    {
        print_splash();
        printf("Burnout.\n");
        printf("target:\t%d meters\nyb:\t%d meters\nvb:\t%g m/s\n", (int) target, (int) yb, vb);
        printf("kl:\t%g kg/m\nkh:\t%g kg/m\nm:\t%g kg\n", kl, kh, m);
        printf("- - - - - - - - - -\n");
        
        printf("target:\t\t\t%g meters\n", target);
        printf("100%% passive alt:\t%d meters\n", (int) ld_alt);
        printf("100%% active alt:\t%d meters\n", (int) hd_alt);
        
        if (hd_alt > target)
            printf("CANNOT BRAKE ENOUGH. Holding flaps open until apogee.\n");
        else if (ld_alt < target)
            printf("WON'T REACH TARGET. Remaining passive until apogee.\n");
        else
            printf("Target can be reached. ");
        printf("Beginning guidance.\n- - - - - - - - - -\n");
        
        printf("(All values are displayed in SI units.)\n");
        printf("Time\t\tDest. Alt.\tFlap State\tAltitude\tVelocity\tAcceleration\n");
    }
    
    if (!noWait) wait(2);
    double t = 0;
    
    Controller flaps = pid_init(1,0,0,-1);
    
    while(vi > 0)
    {
        double ta_passive = t_a(vi, m, kl);
        double ya_passive = alt(yi, vi, m, kl, ta_passive);
        
        double error = -pid_seek(&flaps, ya_passive, target, dt);
        if (error > 1)
            ki = kh;
        else
            ki = kl;
        
        yi = alt(yi, vi, m, ki, dt);
        vi = vel(vi, m, ki, dt);
        double ai = accel(vi, m, ki, dt);
        if (!noPrint)
        {
            if (compact) printf("\r                                                                  \r");
            printf("%g\t\t%g\t\t%d\t\t%g\t\t%g\t\t%g", t, ya_passive, (int)error, yi, vi, ai);
            fflush(stdout);
        }
        t += dt;
        if (!noWait) wait(dt*0.9);
        if (!noPrint && !compact) printf("\n");
    }
    
    double elapsed = ((double)(clock() - start))/CLOCKS_PER_SEC;
    if (compact) printf("\n");
    printf("time elapsed: %g seconds (%g microseconds)\n\n", elapsed, 1000000*elapsed);
    
    return 0;
}
