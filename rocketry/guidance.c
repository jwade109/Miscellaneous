#include "equations.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <string.h>

double gaussianNoise(double mu, double sigma)
{
	double u1, u2;
	do
	{
	   u1 = rand() * (1.0 / RAND_MAX);
	   u2 = rand() * (1.0 / RAND_MAX);
	}
	while (u1 <= 0.0000001);

	return mu + sigma * sqrt(-2.0 * log(u1)) * cos(2 * M_PI * u2);
	wait(0.1);
}

int main(int argc, char** argv)
{
    /*
     * seeds the random number generator
     */
    srand(time(NULL));

    /*
     * booleans for program behavior. noWait disables all realtime pausing.
     * noPrint disables all printing and just does calculations for time trials.
     * compact displays only one line of data. graphical displays a visual error
     * bar.
     */
    int noWait = 0;
    int noPrint = 0;
    int compact = 0;
    int graphical = 0;

    double yb = 3100, vb = 210, vmin = 8, kl = 0.005, kh = 0.012, m = 5, target = 3800;
    double dt = 0.05;
    double alt_sigma = 0, vel_sigma = 0;

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
        else if (!strcmp(argv[i], "graphical"))
        {
            graphical = 1;
        }
        else if (!strcmp(argv[i], "noisy"))
        {
            sscanf(argv[i+1], "%lf", &alt_sigma);
            sscanf(argv[i+2], "%lf", &vel_sigma);
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


    if (noPrint)
    {
        double data[20] = {9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4};
        int size = 20;
        printf("%g\n", sigma(data, size));
    }
    
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
    
        if (!graphical)
        {
            printf("(All values are displayed in SI units.)\n");
            printf("Time\t\tDest. Alt.\tClrvoynt Alt.\tFlap State\tAltitude\tVelocity\n");
        }
        else
        {
            printf("(Convergence graphically displayed as horizontal bar.)\n");
            printf("Time\tDest. Alt.\tVelocity\tError\t- v +\n");
        }
    }
    
    double t = 0; // keeps track of time since burnout
    double max_error = 0, min_error = target - yb; // for graphical display. computationally unnecessary
    if (!noWait) wait(2);
    
    /*
     * updates every dt seconds (as many times per second as possible, ideally)
     * and runs calculations to attenuate the flaps and reach apogee, until
     * velocity reaches some threshold minimum (apogee is reached).
     */
    int firstStep = 1;
    clock_t start = clock();
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
         * this block simulates sensor noise which follows a gaussian distribution
         * around the expected value, which perfectly follows the ideal equations.
         * this is unnecessary for the final program, since sensor noise is a given
         */
        double yi_measured = yi;
        double vi_measured = vi;
        
        if (alt_sigma > 0 || vel_sigma > 0)
        {
            yi_measured = gaussianNoise(yi, alt_sigma);
            vi_measured = gaussianNoise(vi, vel_sigma);
        }
        else
        {
            yi_measured = yi;
            vi_measured = vi;
        }
        
        /*
         * calculates a few things about possible trajectories and conservatively
         * chooses to engage or disengage the flaps. this process prevents
         * overshooting, but is likely to undershoot if the polling rate is too low
         */
        
        /* 100% accurate data about the current step, for the simulation */
        double ta_passive = t_a(vi, m, kl);
        double ya_passive = alt(yi, vi, m, kl, ta_passive);
        double error = ya_passive - target;
        
        /* noisy data about the next step, if the flaps are deployed */
        double yi_next = alt(yi_measured, vi_measured, m, kh, dt);
        double vi_next = vel(vi_measured, m, kh, dt);
        double ta_passive_next = t_a(vi_next, m, kl);
        double ya_passive_next = alt(yi_next, vi_next, m, kl, ta_passive_next);
        
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
        
        /* unnecessary to calculate acceleration but useful to know */
        // double ai = accel(vi, m, ki, dt);
        
        /* computationally unnecessary. used for graphical display */
        if (error > max_error)
            max_error = error;
        if (error < min_error)
            min_error = error;
        
        /* prints out useful data */
        if (!noPrint)
        {
            /* prints 6 columns of data if graphical is disabled */
            if (!graphical)
            {
                printf("\r%g", t);
                printf("\r\t\t%g", ya_passive);
                printf("\r\t\t\t\t%g", ya_passive_next);
                printf("\r\t\t\t\t\t\t%s (%d)", flap_state, (int) error);
                printf("\r\t\t\t\t\t\t\t\t%g", yi_measured - yi);
                printf("\r\t\t\t\t\t\t\t\t\t\t%g", vi_measured);
                // printf("\r\t\t\t\t\t\t\t\t\t\t\t\t%g", ai);
            }
            /* prints 3 columns of data and an error bar if graphical is enabled */
            else
            {
                printf("%g", t);
                printf("\r\t%.4g", ya_passive);
                printf("\r\t\t\t%g\r\t\t\t\t\t", vi);
                for (int i = -8; i < 100; i++)
                {
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
        
        t += dt; // advance t by dt
        firstStep = 0;
        if (!noWait) wait(dt*0.6); // wait dt seconds
        
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
    if (compact && !noPrint) printf("\n");
    if (!noPrint) printf("- - - - - - - - - -\nApogee. Final error: ");
    printf("%d meters\n", (int) min_error);
    printf("Time elapsed: %g seconds (%g microseconds)\n", elapsed, 1000000*elapsed);
    if (!noPrint) printf("\n");

    return 0;
}
