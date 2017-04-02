#include "kalman.h"

Kalman_State kalman_init(double proc_cv, double meas_cv, double est_cv, double initial)
{
    Kalman_State kalman;
    kalman.q = proc_cv;
    kalman.r = meas_cv;
    kalman.p = est_cv;
    kalman.x = initial;
    return kalman;
}

double kalman_update(Kalman_State* state, double measurement)
{
    //prediction update
    state->p += state->q;

    //measurement update
    state->k = state->p / (state->p + state->r);
    state->x = state->x + state->k * (measurement - state->x);
    state->p = (1 - state->k) * state->p;
    
    return state->x;
}

