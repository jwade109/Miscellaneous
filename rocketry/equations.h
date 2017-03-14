// equations.h

double accel(double v_burn, double m, double k, double t);

double vel(double v_burn, double m, double k, double t);

double alt(double y_burn, double v_burn, double m, double k, double t);

double t_a(double v_burn, double m, double k);

double map(double x, double xl, double xu, double yl, double yu);

double T(double mass, double velocity);

double V(double mass, double altitude);

void wait(double seconds);

void print_splash();

void load(int cycles);

double sigma(const double data[], int N);

double sigma_sample(const double data[], int N);
