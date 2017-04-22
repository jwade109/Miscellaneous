#include "FlapControl.h"
#include <stdio.h>

int main()
{
    FlapControl flaps(1, 2, 5);
    flaps.deploy();
    flaps.deploy();
    flaps.deploy();
    flaps.deploy();
    flaps.deploy();
    flaps.deploy();
    flaps.deploy();
    flaps.deploy();
    flaps.retract();
    flaps.retract();
    flaps.retract();
    flaps.retract();
    flaps.retract();
    flaps.retract();
    flaps.retract();
    flaps.retract();
    flaps.retract();
    flaps.retract();
}
