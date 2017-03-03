#include "equations.h"
#include "../pid/pid_lib.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

int main(int argc, char** argv)
{
    system("clear");
    print_splash();
    load(4);
    
    clock_t start = clock();

    double yb = 2600, vb = 120, kl = 0.005, kh = 0.01, m = 5;
    double target = 2900;

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
    }
    
    printf("Burnout.\n");
    printf("target: %d meters | yb: %d meters | vb: %g m/s | kl: %g kg/m | kh: %g kg/m | m: %g kg\n", (int) target, (int) yb, vb, kl, kh, m);
    printf("- - - - - - - - - -\n");
    
    double ld_ta = t_a(vb, m, kl);
    double ld_alt = alt(yb, vb, m, kl, ld_ta);
    double hd_ta = t_a(vb, m, kh);
    double hd_alt = alt(yb, vb, m, kh, hd_ta);
    
    printf("passive alt: %g meters | 100%% active alt: %g meters\n", ld_alt, hd_alt);
    
    if (hd_alt > target)
        printf("CANNOT BRAKE ENOUGH. holding flaps open until apogee.\n");
    else if (ld_alt < target)
        printf("WON'T REACH TARGET. remaining passive until apogee.\n");
    else
        printf("target can be reached. beginning guidance.\n");
    printf("- - - - - - - - - -\n");
    double elapsed = ((double)(clock() - start))/CLOCKS_PER_SEC;
    printf("[time elapsed: %g seconds (%g microseconds)]\n\n", elapsed, 1000000*elapsed);
    
    return 0;
}
