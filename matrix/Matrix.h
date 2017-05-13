#ifndef MATRIX_H
#define MATRIX_H

#include <cstdlib>
#include <iostream>

class Matrix
{
    public:
    
        Matrix(size_t rows, size_t cols);
        
        Matrix(const Matrix& other);
        
        ~Matrix();
        
        static Matrix identity(size_t rows, size_t cols);
        
        size_t rows() const;
        
        size_t cols() const;
        
        size_t size() const;
        
        double* first() const;
    
        double read(size_t i, size_t j) const;
        
        bool write(size_t i, size_t j, double value);
        
        void print() const;
        
        void print(const char* title) const;
        
        Matrix transpose() const;
        
        Matrix inverse() const;
        
        Matrix reduce() const;
        
        Matrix& operator=(const Matrix& right);
        
        Matrix& operator=(std::initializer_list<double> list);
        
        Matrix operator+(const Matrix& right) const;
        
        Matrix operator-(const Matrix& right) const;
        
        Matrix operator*(const Matrix& right) const;
        
        Matrix operator/(const Matrix& right) const;
                
    private:
    
        double* base;
        size_t M;
        size_t N;
        
        bool verify(size_t i, size_t j) const;
};

Matrix operator*(const Matrix& matrix, const double& factor);

Matrix operator*(const double& factor, const Matrix& matrix);

Matrix operator/(const double& divisor, const Matrix& matrix);

Matrix operator/(const Matrix& matrix, const double& divisor);

#endif
