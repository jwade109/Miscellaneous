#include "equations.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <string.h>

/*
 * booleans for program behavior. noWait disables all realtime pausing.
 * noPrint disables all printing and just does calculations for time trials.
 * compact displays only one line of data
 */
int noWait = 0;
int noPrint = 0;
int compact = 0;

int main(int argc, char** argv)
{
    /*
     * seeds the random number generator
     */
    srand(time(NULL));

    double yb = 3100, vb = 210, vmin = 8, kl = 0.005, kh = 0.012, m = 5, target = 3800;
    double dt = 0.05;
    
    /*
     * this god-awful for loop processes all the possible arguments for
     * command line invokation
     */

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
        else if (!strcmp(argv[i], "vmin"))
        {
            sscanf(argv[i+1], "%lf", &vmin);
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

    /*
     * initialize the step velocity to burnout velocity, the step height to burnout height,
     * and the step drag gain to the passive drag gain.
     */
    double vi = vb;
    double yi = yb;
    double ki = kl;

    /*
     * calculate the range of achievable target altitudes given
     * the vehicle's current trajectory and characteristics
     */
    double ld_ta = t_a(vb, m, kl);
    double ld_alt = alt(yb, vb, m, kl, ld_ta);
    double hd_ta = t_a(vb, m, kh);
    double hd_alt = alt(yb, vb, m, kh, hd_ta);
    
    /*
     * header information is printed here. this includes target, burnout height, burnout
     * velocity, low drag gain, high drag gain, vehicle mass, low-drag altitude (100p),
     * high-drag altitude (100a), the minimum flap velocity, and the simulation timestep.
     */
    if (!noPrint)
    {
        printf("Burnout.\n");
        printf("target:\t%d meters\r\t\t\t\t100p:\t%d meters\n", (int) target, (int)ld_alt);
        printf("yb:\t%d meters\r\t\t\t\t100a:\t%d meters\n", (int) yb, (int) hd_alt); 
        printf("vb:\t%g m/s\r\t\t\t\tvmin:\t%g m/s\n", vb, vmin);
        printf("kl:\t%g kg/m\r\t\t\t\tdt:\t%g sec\n", kl, dt);
        printf("kh:\t%g kg/m\n", kh);
        printf("m:\t%g kg\n", m);

        if (hd_alt > target)
            printf("WARNING: WON'T REACH TARGET (TOO HIGH).");
        else if (ld_alt < target)
            printf("WARNING: WON'T REACH TARGET (TOO LOW).");
        else
            printf("Target can be reached.");
        printf(" Beginning guidance.\n- - - - - - - - - -\n");
    
        printf("(All values are displayed in SI units.)\n");
        printf("Time\t\tDest. Alt.\tClrvoynt Alt.\tFlap State\tAltitude\tVelocity\n");
    }
    
    double t = 0; // keeps track of time since burnout
    double max_alt = 0;
    int firstStep = 1;
    clock_t start = clock();
    
    if (!noWait) wait(2);
    
    /*
     * updates every dt seconds (as many times per second as possible, ideally)
     * and runs calculations to attenuate the flaps and reach apogee, until
     * velocity reaches some threshold minimum (apogee is reached).
     */
    while(vi > 0)
    {
        /*
         * at this point in the loop the experimentally measured values
         * should be determined. for the algorithm to work, only instantaneous
         * altitude and velocity must be known, within a reasonable degree of
         * certainty. if more time must be taken to minimize the variance of the
         * measurements, that's fine -- this is fairly robust to low polling
         * rates, though I think at least 5 Hz is necessary
         */
         
        /*
         * this block is unnecessary for the final algorithm - in this
         * simulation, the next step's instantaneous altitude and velocity
         * are calcuted using the ideal equations. in the field, they will
         * be measured experimentally.
         */
        if (!firstStep)
        {
            yi = alt(yi, vi, m, ki, dt);
            vi = vel(vi, m, ki, dt);
        }
            
        /*
         * calculates a few things about possible trajectories and conservatively
         * chooses to engage or disengage the flaps. this process prevents
         * overshooting, but is likely to undershoot if the polling rate is too low
         */
        
        /* data about the current step */
        double ta_passive = t_a(vi, m, kl);
        double ya_passive = alt(yi, vi, m, kl, ta_passive);
        double error = ya_passive - target;
        
        /* data about the next step, if the flaps are deployed */
        double yi_next = alt(yi, vi, m, kh, dt);
        double vi_next = vel(vi, m, kh, dt);
        double ta_passive_next = t_a(vi_next, m, kl);
        double ya_passive_next = alt(yi_next, vi_next, m, kl, ta_passive_next);
        
        if (yi > max_alt) max_alt = yi;
        
        char* flap_state;
        if (ya_passive_next > target && vi_next > vmin)
        {
            ki = kh;
            flap_state = "ON";
        }
        else
        {
            ki = kl;
            flap_state = "--";
        }
        
        if (!noPrint)
        {
            /* prints 6 columns of data */
            printf("\r%g", t);
            printf("\r\t\t%g", ya_passive);
            printf("\r\t\t\t\t%g", ya_passive_next);
            printf("\r\t\t\t\t\t\t%s (%d)", flap_state, (int) error);
            printf("\r\t\t\t\t\t\t\t\t%g", yi);
            printf("\r\t\t\t\t\t\t\t\t\t\t%g", vi);
            fflush(stdout);
        }
        
        t += dt; // advance t by dt
        firstStep = 0;
        if (!noWait) wait(dt*0.7); // wait dt seconds
        
        /* erases the old line if compact is enabled, starts a new line if not */
        if (!noPrint)
        {
            if (compact && vi > vmin)
            {
                printf("\r                                                    ");
                printf("                                                         \r");
            }
            if (!compact) printf("\n");
        }
    }

    double elapsed = ((double)(clock() - start))/CLOCKS_PER_SEC;
    
    if (!noPrint)
    {
        if (compact) printf("\n");
        printf("- - - - - - - - - -\nApogee. Final error: ");
        printf("%d meters\n", (int) (max_alt - target));
        printf("\n");
    }
    else
        printf("Time elapsed: %g seconds (%g microseconds)\n", elapsed, 1000000*elapsed);

    return 0;
}
