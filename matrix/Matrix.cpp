#include "Matrix.h"
#include <malloc.h>

Matrix::Matrix(size_t rows, size_t cols)
{
    this->base = (double*) malloc(sizeof(double) * rows * cols);
    this->M = rows;
    this->N = cols;
    
    for (size_t i = 0; i < size(); i++)
    {
        *(base + i) = 3;
    }
}

Matrix::~Matrix()
{
    free(base);
}

size_t Matrix::rows() const
{
    return M;
}
        
size_t Matrix::cols() const
{
    return N;
}

size_t Matrix::size() const
{
    return M * N;
}

double* Matrix::first() const
{
    return base;
}

double Matrix::read(size_t i, size_t j) const
{
    if (!verify(i, j)) return 0;
    return *(base + i * N + j);
}

bool Matrix::write(size_t i, size_t j, double value)
{
    if (!verify(i, j)) return false;
    *(base + i * N + j) = value;
    return true;
}

void Matrix::print() const
{
    for (size_t i = 0; i < M; i++)
    {
        for (size_t j = 0; j < N; j++)
        {
            printf("%g\t", read(i, j));
        }
        printf("\n");
    }
    printf("\n");
}

bool Matrix::verify(size_t i, size_t j) const
{
    return (i < M) && (i >= 0) && (j < N) && (j >= 0);
}
