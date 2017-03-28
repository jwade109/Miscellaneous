/*
 *  MatrixMath.h Library for Matrix Math
 *
 *  Created by Charlie Matlack on 12/18/10.
 *  Modified from code by RobH45345 on Arduino Forums, algorithm from 
 *  NUMERICAL RECIPES: The Art of Scientific Computing.
 *
 *  Modified by Wade Foster on 2017/03/27.
 */

#ifndef MatrixMath_h
#define MatrixMath_h

namespace Matrix
{
	void copy(float* A, int n, int m, float* B);
	void multiply(float* A, float* B, int m, int p, int n, float* C);
	void add(float* A, float* B, int m, int n, float* C);
	void subtract(float* A, float* B, int m, int n, float* C);
	void transpose(float* A, int m, int n, float* C);
	void scale(float* A, int m, int n, float k);
	int invert(float* A, int n);
};

#endif
