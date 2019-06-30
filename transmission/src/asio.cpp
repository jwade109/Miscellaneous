// asio.cpp

#include <iostream>
#include <future>
#include <chrono>
#include <thread>

int main()
{
    std::future<int> future = std::async(std::launch::async, [] ()
    {
        std::this_thread::sleep_for(std::chrono::seconds(1));
        std::cout << "Completed!" << std::endl;
        return 6;
    });

    std::cout << "Waiting for thread to complete..." << std::endl;

    std::future_status status;
    do
    {
        status = future.wait_for(std::chrono::milliseconds(250));
        if (status == std::future_status::deferred)
        {
            std::cout << "Deferred..." << std::endl;
        }
        if (status == std::future_status::timeout)
        {
            std::cout << "Timeout..." << std::endl;
        }
        if (status == std::future_status::ready)
        {
            std::cout << "Ready!" << std::endl;
        }
    }
    while (status != std::future_status::ready);

    std::cout << "Result is " << future.get() << std::endl;
}
