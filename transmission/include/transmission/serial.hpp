// serial.hpp

#ifndef RVT_SERIAL_HPP
#define RVT_SERIAL_HPP

#include <vector>
#include <array>
#include <string>
#include <chrono>

namespace rvt
{

/// \brief Converts a variable to bytes and stores them
/// \param vec The vector to store the bytes in
/// \param data A variable containing data to be converted
/// \return The original vector with added bytes
template <typename T, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator <<
    (std::vector<unsigned char> &vec, T data)
{
    unsigned char *c = reinterpret_cast<unsigned char*>(&data);
    for (int i = sizeof(T) - 1; i >= 0; --i)
    {
        vec.push_back(c[i]);
    }
    return vec;
}

/// \brief Converts a variable to bytes and stores them
/// \param vec The vector to store the bytes in
/// \param data A vector of data to be converted
/// \return The original vector with added bytes
template <typename T, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator <<
    (std::vector<unsigned char> &vec,
    const std::vector<T> &data)
{
    for (auto e : data)
        vec << e;
    return vec;
}

/// \brief Converts a variable to bytes and stores them
/// \param vec The vector to store the bytes in
/// \param data An array of data to be converted
/// \return The original vector with added bytes
template <typename T, size_t N, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator <<
    (std::vector<unsigned char> &vec,
    const std::array<T, N> &data)
{
    for (auto e : data)
        vec << e;
    return vec;
}

/// \brief Extracts bytes from a vector
/// \param vec The vector of bytes
/// \param data The variable to fill with bytes
/// \return The original vector, with a few less bytes
template <typename T, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator >>
    (std::vector<unsigned char> &vec, T& data)
{
    if (sizeof(T) > vec.size())
    {
        return vec;
    }

    std::vector<unsigned char> extract;
    for (int i = sizeof(T) - 1; i >= 0; --i)
    {
        extract.push_back(vec[i]);
    }
    vec.erase(vec.begin(), vec.begin() + extract.size());
    unsigned char *ptr = reinterpret_cast<unsigned char*>(&data);
    std::copy(extract.begin(), extract.end(), ptr);
    return vec;
}

/// \brief Extracts bytes from a vector
/// \param vec The vector of bytes
/// \param data A vector of variables to populate
/// \return The original vector, with some bytes missing
template <typename T, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator >>
    (std::vector<unsigned char> &vec,
     std::vector<T> &data)
{
    while (vec.size() >= sizeof(T))
    {
        T elem;
        vec >> elem;
        data.push_back(elem);
    }
    return vec;
}

/// \brief Extracts bytes from a vector
/// \param vec The vector of bytes
/// \param data An array of variables to populate
/// \return The original vector, with some bytes missing
template <typename T, size_t N, typename U =
    std::enable_if_t<std::is_fundamental<T>::value, T>>
std::vector<unsigned char>& operator >>
    (std::vector<unsigned char> &vec,
     std::array<T, N> &data)
{
    size_t index = 0;
    while (vec.size() >= sizeof(T) && index < N)
    {
        T elem;
        vec >> elem;
        data[index] = elem;
        ++index;
    }
    return vec;
}

/// \brief Extracts bytes from a vector and inserts them into
///        a null-terminated string
std::vector<uint8_t>& operator >> (std::vector<uint8_t> &vec, std::string &str);

/// \brief Inserts a chrono::time_point into a vector;
///        time is represented as a uint64_t of milliseconds
template <typename Clock, typename Dur>
std::vector<uint8_t>& operator << (std::vector<uint8_t> &vec,
    const std::chrono::time_point<Clock, Dur> &tp)
{
    using namespace std::chrono;
    auto since_epoch = tp.time_since_epoch();
    auto sec = duration_cast<milliseconds>(since_epoch);
    return vec << sec.count();
}

/// \brief Extracts a chrono::time_point from a vector
template <typename Clock, typename Dur>
std::vector<uint8_t>& operator >> (std::vector<uint8_t> &vec,
    std::chrono::time_point<Clock, Dur> &tp)
{
    using namespace std::chrono;
    uint64_t millis;
    vec >> millis;
    tp = time_point<Clock, Dur>(milliseconds(millis));
    return vec;
}

} // namespace rvt

#endif // RVT_SERIAL_HPP

