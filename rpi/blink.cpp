#include <wiringPi.h>
#include <time.h>
#include <iostream>

#define LED 1

using namespace std;

int main(int argc, char** argv)
{
    wiringPiSetup();

    pinMode(LED, 1);

    printf("Blinking...\n");

    for (int i = 1; i < 12; i++)
    {
        cout << i % 2 << endl;
        digitalWrite(LED, i % 2);
        time_t now = time(0);
        while (time(0) - now < 1) { }
    }
    return argc;
}
