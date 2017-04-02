gcc -o gsl_test gsl_test.c -I${HOME}/gsl/include -lm -L${HOME}/gsl/lib -lgsl -lgslcblas
echo "Compiled new gsl_test executable."
