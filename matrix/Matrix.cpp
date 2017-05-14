#include "Matrix.h"
#include <malloc.h>
#include <iostream>
#include <math.h>

Matrix::Matrix()
{
    base = NULL;
    M = N = 0;
}

Matrix::Matrix(size_t rows)
{
    base = (double*) malloc(sizeof(double) * rows);
    if (!base)
    {
        M = N = 0;
        return;
    }
    M = rows;
    N = 1;
    
    for (size_t i = 0; i < size(); i++)
    {
        *(base + i) = 0;
    }
}

Matrix::Matrix(size_t rows, size_t cols)
{
    base = (double*) malloc(sizeof(double) * rows * cols);
    if (!base)
    {
        M = N = 0;
        return;
    }
    M = rows;
    N = cols;
    
    for (size_t i = 0; i < size(); i++)
    {
        *(base + i) = 0;
    }
}

Matrix::Matrix(const Matrix& other)
{
    M = other.rows();
    N = other.cols();
    base = (double*) malloc(sizeof(double) * M * N);
    if (!base)
    {
        M = N = 0;
        return;
    }
    
    for (size_t i = 0; i < size(); i++)
    {
        *(base + i) = *(other.first() + i);
    }
}

Matrix::~Matrix()
{
    free(base);
}

Matrix Matrix::identity(size_t rows, size_t cols)
{
    Matrix id(rows, cols);
    for (size_t i = 0; i < rows && i < cols; i++)
    {
        id.write(i, i, 1);
    }
    return id;
}

Matrix Matrix::random(size_t rows, size_t cols)
{
    srand(time(0));

    Matrix r(rows, cols);
    for (size_t i = 0; i < r.size(); i++)
    {
        *(r.first() + i) = ((double) rand()/RAND_MAX) * 2 - 1;
    }
    return r;
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
    if (i >= M || i < 0 || j >= N || j < 0)
    {
        printf("Cannot read from (%ld, %ld) in %ld x %ld matrix\n", i, j, M, N);
        return 0;
    }
    return *(base + i * N + j);
}

bool Matrix::write(size_t i, size_t j, double value)
{
    if (i >= M || i < 0 || j >= N || j < 0)
    {
        printf("Cannot write to (%ld, %ld) in %ld x %ld matrix\n", i, j, M, N);
        return false;
    }
    *(base + i * N + j) = value;
    return true;
}

void Matrix::print() const
{
    print("");
}

void Matrix::print(const char* title) const
{
    printf(" %lu x %lu matrix %s\n", M, N, title);
    for (size_t i = 0; i < M; i++)
    {
        printf("  ");
        for (size_t j = 0; j < N; j++)
        {
            printf("%8.3f  ", read(i, j));
        }
        printf("\n");
    }
    printf("\n");
}

Matrix Matrix::transpose() const
{
    Matrix m(N, M);
    for (size_t i = 0; i < M; i++)
    {
        for (size_t j = 0; j < N; j++)
        {
            m.write(j, i, read(i, j));
        }
    }
    return m;
}

Matrix Matrix::inverse() const
{
    if (M != N)
    {
        printf("Cannot invert non-square matrix\n");
        return Matrix(0, 0);
    }
    Matrix m = *this;
    
    /*
     *  Modified from the MatrixMath library by Charlie Matlack,
     *  modified from code by RobH45345 on Arduino Forums, algorithm from 
     *  NUMERICAL RECIPES: The Art of Scientific Computing.
     */
    
    // keeps track of current pivot row
    size_t pivrow;
    // k: overall index along diagonal; i: row index; j: col index
    size_t k, i, j;
    // keeps track of rows swaps to undo at end
    size_t pivrows[M];
    // used for finding max value and making column swaps
    float tmp;

    for (k = 0; k < M; k++)
    {
        // find pivot row, the row with biggest entry in current column
        tmp = 0;
        for (i = k; i < M; i++)
        {
            if (fabs(m.read(i, k)) >= tmp)
            {
                tmp = fabs(m.read(i, k));
                pivrow = i;
            }
        }

        // check for singular matrix
        if (m.read(pivrow, k) == 0.0f)
        {
            printf("Cannot invert singular matrix\n");
            return Matrix(0, 0);
        }

        // execute pivot (row swap) if needed
        if (pivrow != k)
        {
            // swap row k with pivrow
            for (j = 0; j < M; j++)
            {
                tmp = m.read(k, j);
                m.write(k, j, m.read(pivrow, j));
                m.write(pivrow, j, tmp);
            }
        }
        pivrows[k] = pivrow; // record row swap (even if no swap happened)

        tmp = 1.0f/m.read(k, k); // invert pivot element
        // this element of input matrix becomes result matrix
        m.write(k, k, 1.0f);		

        // perform row reduction (divide every element by pivot)
        for (j = 0; j < M; j++)
        {
            m.write(k, j, m.read(k, j) * tmp);
        }

        // eliminate all other entries in this column
        for (i = 0; i < M; i++)
        {
            if (i != k)
            {
                tmp = m.read(i, k);
                m.write(i, k, 0.0f);
                for (j = 0; j < M; j++)
                {
                    m.write(i, j, m.read(i, j) - m.read(k, j) * tmp);
                }
            }
        }
    }

    // undo pivot row swaps by doing column swaps in reverse order
    for (k = M-1; k >= 0 && k < M; k--)
    {
        if (pivrows[k] != k)
        {
            for (i = 0; i < M; i++)
            {
                tmp = m.read(i, k);
                m.write(i, k, m.read(i, pivrows[k]));
                m.write(i, pivrows[k], tmp);
            }
        }
    }
    return m;
}

Matrix Matrix::reduce() const
{
    Matrix m = *this;
    
    for (size_t i = 0; i < M; i++)
    {
        for (size_t r = 0; r < M; r++)
        {
            float div = m.read(i, i);
            float mult = m.read(r, i) / m.read(i, i);

            for (int c = 0; c < N; c++)
            {
                if (r == i)
                {
                    m.write(r, c, m.read(r, c)/div);
                }
                else
                {
                    m.write(r, c, m.read(r, c) - (m.read(i, c) * mult));
                }
            }
        }
    }
    return m;
}

Matrix& Matrix::operator=(const Matrix& right)
{
    M = right.rows();
    N = right.cols();
    free(base);
    base = (double*) malloc(sizeof(double) * M * N);
    for (size_t i = 0; i < size(); i++)
    {
        *(base + i) = *(right.first() + i);
    }
    return *this;
}

Matrix& Matrix::operator=(std::initializer_list<double> list)
{
    if (list.size() == 1)
    {
        for (size_t i = 0; i < size(); i++)
        {
            *(base + i) = *list.begin();
        }
    }
    size_t i = 0;
    for (auto ptr = list.begin(); ptr < list.end() && i < size(); ptr++)
    {
        *(base + i) = *ptr;
        i++;
    }
    return *this;
}

Matrix Matrix::operator+(const Matrix& right) const
{
    if (right.rows() != M || right.cols() != N)
    {
        printf("Cannot add %lu x %lu and %lu x %lu matrices\n",
            N, M, right.rows(), right.cols());
        return Matrix(0, 0);
    }
    Matrix m(M, N);
    for (size_t i = 0; i < size(); i++)
    {
        *(m.first() + i) = *(base + i) + *(right.first() + i);
    }
    return m;
}

Matrix Matrix::operator-(const Matrix& right) const
{
    if (right.rows() != M || right.cols() != N)
    {
        printf("Cannot subtract %lu x %lu and %lu x %lu matrices\n",
            N, M, right.rows(), right.cols());
        return Matrix(0, 0);
    }
    Matrix m(M, N);
    for (size_t i = 0; i < size(); i++)
    {
        *(m.first() + i) = *(base + i) - *(right.first() + i);
    }
    return m;
}

Matrix Matrix::operator*(const Matrix& right) const
{
    if (cols() != right.rows())
    {
        printf("Cannot multiply %lu x %lu and %lu x %lu matrices\n",
            N, M, right.rows(), right.cols());
        return Matrix(0, 0);
    }
    
    int m = M;
    int p = N;
    int n = right.cols();
    Matrix C(m, n);

    for (size_t i = 0; i < m; i++)
    {
        for(size_t j = 0; j < n; j++)
        {
            for (size_t k = 0; k < p; k++)
            {
                C.write(i, j, C.read(i, j) + read(i, k)* right.read(k, j));
            }
        }
    }
    return C;
}

Matrix Matrix::operator/(const Matrix& matrix) const
{
    Matrix inverse = matrix.inverse();
    return *this * inverse;
}

Matrix operator*(const Matrix& matrix, const double factor)
{
    Matrix m = matrix;
    for (size_t i = 0; i < m.size(); i++)
    {
        *(m.first() + i) *= factor;
    }
    return m;
}

Matrix operator*(const double factor, const Matrix& matrix)
{
    Matrix m = matrix;
    for (size_t i = 0; i < m.size(); i++)
    {
        *(m.first() + i) *= factor;
    }
    return m;
}

Matrix operator/(const double divisor, const Matrix& matrix)
{
    return matrix * (1/divisor);
}

Matrix operator/(const Matrix& matrix, const double divisor)
{
    return matrix * (1/divisor);
}
