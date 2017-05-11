#include <stdio.h>
#include "Matrix.h"

int main()
{
    Matrix m(4, 7);
    for (size_t i = 0; i < m.size(); i++)
    {
    //    *(m.first() + i) = 121.5/(i+1);
    }
    m.write(3, 3, 1.23456789123456789);
    Matrix y = m + m;
    m.print();
    y.print();
    return 0;
}
