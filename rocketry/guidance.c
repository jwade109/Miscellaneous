#include "equations.h"
#include "../pid/pid_lib.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <allegro5/allegro.h>
#include <allegro5/allegro_primitives.h>

int main(int argc, char** argv)
{
    clock_t start = clock();

    int noWait = 0;
    int noPrint = 0;
    int compact = 0;
    int graphical = 0;
    int window = 0;

    double yb = 3100, vb = 210, kl = 0.005, kh = 0.012, m = 5, target = 3800;
    double dt = 0.05;
    double P = 1, I = 0, D = 0;    

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
        else if (!strcmp(argv[i], "tune"))
        {
            i++;
            sscanf(argv[i], "%lf", &P);
            i++;
            sscanf(argv[i], "%lf", &I);
            i++;
            sscanf(argv[i], "%lf", &D);
        }
        else if (!strcmp(argv[i], "graphical"))
        {
            graphical = 1;
        }
        else if (!strcmp(argv[i], "window"))
        {
            window = 1;
        }
    }
    
    ALLEGRO_DISPLAY *display = NULL;
    if (window)
    {
        al_init();
        al_init_primitives_addon();
        display = al_create_display(1000, 1000);
        al_draw_filled_triangle(500,500,700,500,500,700, al_map_rgb(0,255,0));
        al_flip_display();
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
        printf("Burnout.\n");
        printf("target:\t%d meters\r\t\t\t\t100p:\t%d meters\n", (int) target, (int)ld_alt);
        printf("yb:\t%d meters\r\t\t\t\t100a:\t%d meters\n", (int) yb, (int) hd_alt); 
        printf("vb:\t%g m/s\r\t\t\t\tdt:\t%g sec\n", vb, dt);
        printf("kl:\t%g kg/m\n", kl);
        printf("kh:\t%g kg/m\r\t\t\t\tPID:\t%g %g %g\n", kh, P, I, D); 
        printf("m:\t%g kg\n", m);

        if (hd_alt > target)
            printf("WARNING: WON'T REACH TARGET (TOO HIGH).");
        else if (ld_alt < target)
            printf("WARNING: WON'T REACH TARGET (TOO LOW).");
        else
            printf("Target can be reached.");
        printf(" Beginning guidance.\n- - - - - - - - - -\n");
    
        if (!graphical)
        {
            printf("(All values are displayed in SI units.)\n");
            printf("Time\t\tDest. Alt.\tFlap State\tAltitude\tVelocity\tAcceleration\n");
        }
        else
        {
            printf("(Convergence graphically displayed as horizontal bar.)\n");
            printf("Time\tDest. Alt.\tVelocity\tError\t- v +\n");
        }
    }

    if (!noWait) wait(2);
    double t = 0;

    Controller flaps = pid_init(P,I,D,-1);

    double max_error = 0;
    while(vi > 0)
    {
        double ta_passive = t_a(vi, m, kl);
        double ya_passive = alt(yi, vi, m, kl, ta_passive);

        if (ya_passive - target > max_error)
            max_error = ya_passive - target;

        double error;
        if (ya_passive > (target + dt * 15))
            error = -pid_seek(&flaps, ya_passive, target, dt);
        else
            error = 0;
        
        if (error > 1)
            ki = kh;
        else
            ki = kl;

        yi = alt(yi, vi, m, ki, dt);
        vi = vel(vi, m, ki, dt);
        double ai = accel(vi, m, ki, dt);
        
        if (!noPrint)
        {
            if (!graphical)
            {
                printf("\r%g", t);
                printf("\r\t\t%g", ya_passive);
                printf("\r\t\t\t\t%d", (int) error);
                printf("\r\t\t\t\t\t\t%g", yi);
                printf("\r\t\t\t\t\t\t\t\t%g", vi);
                printf("\r\t\t\t\t\t\t\t\t\t\t%g", ai);
            }
            else
            {
                printf("%g", t);
                printf("\r\t%.4g", ya_passive);
                printf("\r\t\t\t%g\r\t\t\t\t\t", vi);
                for (int i = -10; i < 100; i++)
                {
                    int error = ya_passive - target;
                    if (i == 0)
                        printf(":");
                    else if (error > 0 && i > 0 && i < error * 50 / max_error)
                        printf("|");
                    else if (error < 0 && i < 0 && i > error * 50 / max_error)
                        printf("|");
                    else if (i < 0)
                        printf(" ");
                }
            }
            fflush(stdout);
        }
        if (!noWait) al_rest(dt);
        if (!noPrint) 
        {
            if (compact && vi > 0)
            {
                printf("\r                                                    ");
                printf("                                                         \r");
            }
            if (!compact) printf("\n");
        }
        t += dt;
    }

    double elapsed = ((double)(clock() - start))/CLOCKS_PER_SEC;
    if (compact && !noPrint) printf("\n");
    if (!noPrint) printf("- - - - - - - - - -\nApogee.\n");
    printf("Time elapsed: %g seconds (%g microseconds)\n", elapsed, 1000000*elapsed);
    if (!noPrint) printf("\n");

    if (window) while(1);
    al_destroy_display(display);

    return 0;
}
