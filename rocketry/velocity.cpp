#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <stdbool.h>
#include <string.h>

#define g 9.81
#define pi 3.14159265359

double velocity(double v_0, double t, double k, double m)
{
	if (v_0 < 0 || k <= 0 || m <= 0 || t < 0) return -1;
	double vel = sqrt(m*g/k)*tan(atan(v_0*sqrt(k/(m*g)))-t*sqrt(g*k/m));
	double ta = atan(v_0*sqrt(k/(m*g)))/sqrt(g*k/m);
	if (t > ta) return 0;
	return vel;
}

int main(int argc, char** argv)
{
	clock_t begin = clock();

	bool fast = false;
	double v_0 = 100, t = 0, k1 = 0.005, k2 = 4*k1, m = 10;

	if (argc > 1)
		sscanf(argv[1], "%lf", &v_0);
	if (argc > 2)
		sscanf(argv[2], "%lf", &k1);
	if (argc > 3)
		sscanf(argv[3], "%lf", &k2);
	for (int i = 1; i < argc; i++)
	{
		if (strcmp(argv[i], "--fast") == 0)
			fast = true;
	}

	double v_last_1 = v_0, v_last_2 = v_0;
	int count = 0;

	while (v_last_1 > 0 || v_last_2 > 0)
	{
		if (!fast) printf("%lf\t%lf\t%lf\n", t, v_last_1, v_last_2);
		v_last_1 = velocity(v_0, t, k1, m);
		v_last_2 = velocity(v_0, t, k2, m);
		count += 2;
		t += 0.1;
	}

	long int time = clock() - begin;
	if (fast) printf("%d calls to velocity() took %lf microseconds\n", count, 1000000*((float)time)/CLOCKS_PER_SEC);
	return 0;
}
