#ifndef ARDUINO_MSG_H
#define ARDUINO_MSG_H

#include <iostream>

namespace rvt
{

/*------------------------------------------------------------------*/
/* MODE RESPONSE DEFINITION ----------------------------------------*/

struct mode_response
{
    static constexpr char const *name = "mode_response";
    static const unsigned short payload_type = 0x10;

    unsigned char mode;
};

std::ostream& operator << (std::ostream& os, const mode_response& msg)
{
    std::string mode;
    switch (msg.mode)
    {
        case 1: mode = "Arm"; break;
        case 2: mode = "Firing"; break;
        case 3: mode = "Post Firing"; break;
        case 4: mode = "N/A"; break;
        case 5: mode = "Simulation Mode"; break;
        case 6: mode = "Post Simulation"; break;
        default : mode = "Default";
    }

    return os << "<" << mode_response::name << ", " << mode << ">";
}

/*------------------------------------------------------------------*/
/* DATA PACKET #2 MESSAGE DEFINITION -------------------------------*/

struct data_msg_02
{
    static constexpr char const *name = "data_msg_02";
    static const unsigned short payload_type = 0x51;

    unsigned long ctrlr_time;
    unsigned char oper_mode;
    unsigned short err_status;
    float pres_oxi, pres_comb, temp_prec, temp_comb, thrust;
    unsigned char new_data;
};

/*------------------------------------------------------------------*/
/* DATA PACKET #3 MESSAGE DEFINITION -------------------------------*/

struct data_msg_03
{
    static constexpr char const *name = "data_msg_02";
    static const unsigned short payload_type = 0x52;

    unsigned long ctrlr_time;
    unsigned char oper_mode;
    unsigned short err_status;
    float pres_oxi, pres_comb, temp_prec, temp_comb, temp_post, thrust;
    unsigned char new_data;
};

/*------------------------------------------------------------------*/
/* ECHO FIRMWARE VERSION COMMAND -----------------------------------*/

struct echo_firmware
{
    static constexpr char const *name = "echo_firmware";
    static const unsigned short payload_type = 0x60;

    bool fluff;
};

/*------------------------------------------------------------------*/
/* PIN 7 LED STATE COMMAND -----------------------------------------*/

struct set_led_7_state
{
    static constexpr char const *name = "set_led_7_state";
    static const unsigned short payload_type = 0x61;

    bool state;
};

/*------------------------------------------------------------------*/
/* CLEAR INPUT BUFFER COMMAND --------------------------------------*/

struct clear_input_buffer
{
    static constexpr char const *name = "clear_input_buffer";
    static const unsigned short payload_type = 0x62;

    bool fluff;
};

/*------------------------------------------------------------------*/
/* SET MODE COMMAND ------------------------------------------------*/

struct set_mode
{
    static constexpr char const *name = "set_mode";
    static const unsigned short payload_type = 0x63;

    unsigned char mode;
};

/*------------------------------------------------------------------*/

} // namespace rvt

#endif // ARDUINO_MSG_H
