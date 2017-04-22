#include <stdio.h>
#include <inttypes.h>

uint8_t PORTD = 0;
uint8_t DDRD = 0;
const int INDICATOR = 3;
const int ERROR = 4;

void toBinary(char* c, uint8_t a)
{
    printf("%s", c);
    for(uint8_t i=0x80;i!=0;i>>=1) printf("%c",(a&i)?'1':'0'); 
    printf("\n");
}

void regWrite(uint8_t* reg, int pin, int state)
{
    if (state)  *reg |= 1 << pin;
    else        *reg &= ~(1 << pin);
}

int main()
{
    toBinary("DDRD: ", DDRD);
    regWrite(&DDRD, INDICATOR, 1);
    regWrite(&DDRD, ERROR, 1);
    toBinary("DDRD: ", DDRD);
    toBinary("PORTD: ", PORTD);
    regWrite(&PORTD, INDICATOR, 1);
    regWrite(&PORTD, ERROR, 1);
    toBinary("PORTD: ", PORTD);
    return 0;
}
