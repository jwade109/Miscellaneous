#include <iostream>
#include <fstream>
#include <string.h>
#include <malloc.h>
#include <time.h>

using namespace std;

typedef struct
{
    int year;
    int month;
    int day;
} date;

typedef struct
{
    date day;
    string description;
    string category;
    int amount;
} transaction;

int main()
{
    const uint8_t commands = 6;
    string* keywords = (string*) malloc(sizeof(string) * commands);
    keywords[0] = "quit";
    keywords[1] = "save";
    keywords[2] = "delete";
    keywords[3] = "revert";
    keywords[4] = "new";
    keywords[5] = "list";
    
    time_t now = time(0);
    tm* also_now = localtime(&now);
    cout << ctime(&now);
    
    fstream file;
    file.open("history.txt", ios_base::app);

    while(1)
    {
        cout << "($) ";
        string cmd;
        cin >> cmd;
        file << cmd << "\n";
        
        uint8_t index = commands;
        
        for (int i = 0; i < commands; i++)
        {
            if (!cmd.compare(keywords[i]) ||
                (cmd[0] == keywords[i][0] && cmd.length() == 1))
            {
                index = i;
                i = commands;
            }
        }
        
        if (index == 0)
        {
            file.close();
            return 0;
        }
        if (index < commands)
            cout << keywords[index] << endl;
    }
    file.close();
    return 0;
}


