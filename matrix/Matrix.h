#ifndef MATRIX_H
#define MATRIX_H

#include <cstdlib>

class Matrix
{
    public:
    
        Matrix(size_t rows, size_t cols);
        
        ~Matrix();
    
        size_t rows() const;
        
        size_t cols() const;
        
        size_t size() const;
        
        double* first() const;
    
        double read(size_t i, size_t j) const;
        
        bool write(size_t i, size_t j, double value);
        
        void print() const;
        
        Matrix operator+(const Matrix& other)
        {
            if (other.rows() != rows() || other.cols() != cols())
            {
                return Matrix(1, 1);
            }
            Matrix m(rows(), cols());
            return m;
        }
        
    private:
    
        double* base;
        size_t M;
        size_t N;
        
        bool verify(size_t i, size_t j) const;
};

#endif
