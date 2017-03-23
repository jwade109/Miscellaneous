#include <iostream>
#include <cstdlib>
#include <math.h>
#include "SimpleKalman.h"

using namespace std;

int main()
{
    srand(time(0));
    
    int altitude = 400;
    cout << altitude << endl << endl;
    
    SimpleKalman filt(390, 2, 1, 2, 0.01);
        
    double range = 10;
    for (int i = 0; i < 100; i++)
    {
        double measured = altitude + (double)(rand()%100)/5 - 10;
        cout << measured << "\t" << filt.step(measured) << endl;
    }
    return 0;
}
