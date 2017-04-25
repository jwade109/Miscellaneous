#include <stdio.h>
#include <math.h>

int main(int argc, char** argv)
{
    double sigma_x, sigma_y, tau_xy;

    if (argc > 3)
    {
        sscanf(argv[1], "%lf", &sigma_x);
        sscanf(argv[2], "%lf", &sigma_y);
        sscanf(argv[3], "%lf", &tau_xy);
    }
    else
    {
        printf("not enough arguments.\n");
    }
    
    double sigma_avg = (sigma_x + sigma_y)/2;
    double R = sqrt(pow(sigma_avg - sigma_x, 2) + pow(tau_xy, 2));
    double phi_p1 = atan(tau_xy/(sigma_x - sigma_avg))*180/M_PI;
    double phi_p2, phi_s;
    if (phi_p1 <= 0)
    {
        phi_p2 = phi_p1 + 180;
        phi_s = phi_p1 + 90;
    }
    else
    {
        phi_p2 = phi_p1 - 180;
        phi_s = phi_p1 - 90;
    }
    
    printf("sigma max:  %g\n", sigma_avg + R);
    printf("sigma min:  %g\n", sigma_avg - R);
    printf("tau max:    %g\n", R);
    printf("theta p1:   %g\n", phi_p1/2);
    printf("theta p2:   %g\n", phi_p2/2);
    printf("theta s1:   %g\n", phi_s/2);
    
    return 0;
}
