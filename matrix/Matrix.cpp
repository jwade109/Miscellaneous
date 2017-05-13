#include "Matrix.h"
#include <malloc.h>
#include <iostream>
#include <math.h>

Matrix::Matrix(size_t rows, size_t cols)
{
    base = (double*) malloc(sizeof(double) * rows * cols);
    if (!base)
    {
        printf("Failed to allocate.\n");
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
    
    for (size_t i = 0; i < size(); i++)
    {
        *(base + i) = *(other.first() + i);
    }
}

Matrix::~Matrix()
{
    if (base != NULL) free(base);
    base = NULL;
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
    if (!verify(i, j))
    {
        static int count;
        count++;
        printf("Cannot write to (%ld, %ld) in %ld x %ld matrix\n",
            i, j, M, N);
        if (count > 15) while(1);
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
    printf("%lu x %lu matrix %s\n", M, N, title);
    for (size_t i = 0; i < M; i++)
    {
        for (size_t j = 0; j < N; j++)
        {
            printf("%5.2f\t", read(i, j));
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
     *  Modified from the MatrixMath library by Charlie Matlack on 12/18/10,
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

bool Matrix::verify(size_t i, size_t j) const
{
    return (i < M) && (i >= 0) && (j < N) && (j >= 0);
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
    
    // A = input matrix (m x p)
	// B = input matrix (p x n)
	// m = number of rows in A
	// p = number of columns in A = number of rows in B
	// n = number of columns in B
	// C = output matrix = A*B (m x n)
	
	int m = this->rows();
	int p = this->cols();
	int n = right.cols();
	
	Matrix C(m, n);
	
	int i, j, k;
	for (i=0;i<m;i++)
		for(j=0;j<n;j++)
		{
			// C.write(i, j, 0);
			for (k=0;k<p;k++)
			{
			    // double a = *(base + p*i+k);
			    double a = read(i, k);
			    // double b = *(right.first() + n*k+j);
			    double b = right.read(k, j);
				C.write(i, j, C.read(i, j)+a*b);
		    }
		}
    return C;
}

Matrix operator*(const Matrix& matrix, const double& factor)
{
    Matrix m = matrix;
    for (size_t i = 0; i < m.size(); i++)
    {
        *(m.first() + i) *= factor;
    }
    return m;
}

Matrix operator*(const double& factor, const Matrix& matrix)
{
    Matrix m = matrix;
    for (size_t i = 0; i < m.size(); i++)
    {
        *(m.first() + i) *= factor;
    }
    return m;
}
