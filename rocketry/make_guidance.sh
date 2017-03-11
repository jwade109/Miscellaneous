gcc -o guidance -Wall guidance.c equations.c ../pid/pid_lib.c -lm -lallegro -lallegro_main -lallegro_primitives
echo "Compiled new guidance executable."
