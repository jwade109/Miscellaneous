// utility.cpp

#include <transmission/util.hpp>

#include <string>
#include <vector>
#include <sstream>
#include <iomanip>

namespace rvt
{

std::string wrap(const std::string &str, size_t num_chars, size_t offset)
{
    std::string copy(str);
    std::stringstream ss;
    ss << std::right;
    while (copy.length() > num_chars)
    {
        ss << std::setw(num_chars + offset + 1)
           << copy.substr(0, num_chars) + "\n";
        copy = copy.substr(num_chars, copy.length());
    }
    ss << std::setw(offset) << "" << copy;
    return ss.str();
}


std::vector<std::string> split_quoted(const std::string &fstr)
{
    std::vector<std::string> tokens;
    std::string token;
    char quote_char = ' ';
    for (size_t i = 0; i < fstr.length(); ++i)
    {
        char c = fstr[i];
        if (c == ' ' && quote_char != ' ')
        {
            token += c;
        }
        else if (c == ' ' || i == fstr.length() - 1)
        {
            if ((c != quote_char || quote_char == ' ') && c != ' ') token += c;
            if (token.length() > 0) tokens.push_back(token);
            token = "";
        }
        else if ((c == '\'' || c == '"') && quote_char == ' ')
        {
            quote_char = c;
        }
        else if (c == quote_char && c != ' ')
        {
            quote_char = ' ';
        }
        else if (c != ' ')
        {
            token += c;
        }
    }
    return tokens;
}

} // namespace rvt

