#include <MatrixMath.h>

double A[2][2];
double x[2][1];

void setup() {
    for (int i = 0; i < 2; i++)
    {
        x[i][0] = (double)(10 - i);
        for (int j = 0; j < 2; j++)
        {
            A[i][j] = (double)((1+i)*(1+j)*(1+j) + 3.2);
        }
    }

    Serial.begin(9600);
    long int t = micros();
    Matrix.Print((float*) A, 2, 2, "Matrix A");
    Matrix.Print((float*) x, 2, 1, "Vector x");
    double b[2][1];
    Matrix.Multiply((float*) A, (float*) x, 2, 2, 1, (float*) b);
    Matrix.Print((float*) b, 2, 1, "Vector A * x = b");
    double Ainverse[2][2];
    Matrix.Copy((float*) A, 2, 2, (float*) Ainverse);
    Matrix.Invert((float*) Ainverse, 2);
    Matrix.Print((float*) Ainverse, 2, 2, "Inverse Matrix A");
    double Atranspose[2][2];
    Matrix.Transpose((float*) A, 2, 2, (float*) Atranspose);
    Matrix.Print((float*) Atranspose, 2, 2, "Matrix A Transpose");
    Serial.print("Time passed: ");
    Serial.print(micros() - t);
    Serial.println(" microseconds");
}

void loop() {
  // put your main code here, to run repeatedly:

}
