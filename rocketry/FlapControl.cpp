#include "FlapControl.h"
#include <stdio.h>
#include <inttypes.h>

FlapControl::FlapControl(uint8_t pospin, uint8_t negpin, uint8_t maxstate)
{
    this->pospin = pospin;
    this->negpin = negpin;
    this->maxstate = maxstate;
    state = 0;
}

void FlapControl::deploy()
{
    if (state == maxstate) kill();
    else
    {
        state++;
        printf("DEPLOYING (%" PRIu8 ")\n", state);
        #ifdef ARDUINO_H
        digitalWrite(negpin, LOW);
        digitalWrite(pospin, HIGH);
        #endif
    }
}

void FlapControl::retract()
{
    if (state == 0) kill();
    else
    {
        state--;
        printf("RETRACTING (%" PRIu8 ")\n", state);
        #ifdef ARDUINO_H
        digitalWrite(pospin, LOW);
        digitalWrite(negpin, HIGH);
        #endif
    }
}

void FlapControl::kill()
{
    printf("KILLING FLAPS (%" PRIu8 ")\n", state);
    #ifdef ARDUINO_H
    digitalWrite(pospin, LOW);
    digitalWrite(negpin, HIGH);
    #endif
}

