#include <iostream>
#include <vector>
#include <string>

int main(int argc, char **argv)
{
    // prints helpful user message
    std::cout << "Enter a list of strings to alphabetize.\n"
              << "Press enter after every string.\n"
              << "An empty string will indicate the end of the list.\n"
              << std::flush;

    // initialize the list of strings and the input string itself;
    // we're using vector because we want the list to be able to grow
    std::vector<std::string> strings;
    std::string input;

    // continually get input from the user, from a source
    // which C++ calls "std::cin", or "standard c-in"
    while (std::getline(std::cin, input))
    {
        // if the input is empty, we're done -- break out of the loop
        if (input.empty()) break;

        // add the user provided string to the list
        strings.push_back(input);
    }

    // alphabetize the list using std::sort()
    std::cout << "End of list. Alphabetizing...\n" << std::flush;
    std::sort(strings.begin(), strings.end());

    // print the list contents to the user
    for (auto s : strings) std::cout << s << "\n" << std::flush;

    return 0;
}
