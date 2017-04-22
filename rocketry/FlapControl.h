#ifndef FLAP_CONTROL_H
#define FLAP_CONTROL_H

#include <stdbool.h>
#include <inttypes.h>

class FlapControl
{
    public:
        FlapControl(uint8_t pospin, uint8_t negpin, uint8_t maxstate);
        void deploy();
        void retract();
        void kill();
    private:
        uint8_t state;
        uint8_t maxstate;
        uint8_t pospin;
        uint8_t negpin;
};

#endif
