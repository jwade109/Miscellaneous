#include <stdio.h>
#include "Matrix.h"

int main()
{
    Matrix id(3, 3);
    id = {4, 3, 1, 3, 2, 3, 1, 2, 2};
    id.print("[id]");
    Matrix n = id.inverse();
    n.print("[n = id inverse]");
    Matrix eye1 = id * n;
    Matrix eye2 = n * id;
    eye1.print("[id * n]");
    eye2.print("[n * id]");
    return 0;
}
