// utility.hpp

#ifndef RVT_UTILITY_HPP
#define RVT_UTILITY_HPP

#include <string>
#include <vector>

namespace rvt
{

std::string wrap(const std::string &str, size_t num_chars, size_t offset = 0);

std::vector<std::string> split_quoted(const std::string &fstr);

} // namespace rvt

#endif // RVT_UTILITY_HPP

