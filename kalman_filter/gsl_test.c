#include <gsl/gsl_math.h>
#include <gsl/gsl_matrix.h>
#include <gsl/gsl_vector.h>
#include <stdio.h>

int main(int argc, char** argv)
{
	size_t m = 3, n = 3;

	if (argc > 1)
		sscanf(argv[1], "%zd", &m);
	if (argc > 2)
		sscanf(argv[2], "%zd", &n);

	gsl_matrix* mat = gsl_matrix_alloc(m, n);
	for (int i = 0; i < m; i++)
	{
		for (int j = 0; j < n; j++)
		{
			double value = i + j + i*j*0.32;
			gsl_matrix_set(mat, i, j, value);
			printf("%f\t", value);
		}
		printf("\n");
	}

	gsl_matrix_free(mat);

	return 0;
}
