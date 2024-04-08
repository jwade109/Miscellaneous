#! /usr/bin/env python3

import sys
from dataclasses import dataclass
from itertools import combinations


@dataclass
class OpsTable:
    a: int = None
    b: int = None
    add: int = None
    sub: int = None
    mul: int = None
    div: int = None
    sub_inv: int = None
    div_inv: int = None


@dataclass
class Op():
    a: int = None
    b: int = None
    op: chr = None
    res: int = None


def generate_ops_table(a, b):
    o = OpsTable()
    o.a = a
    o.b = b
    o.add = a + b
    if a >= b:
        o.sub = a - b
    elif b >= a:
        o.sub_inv = b - a
    o.mul = a * b
    if b != 0 and a >= b and a % b == 0:
        o.div = a // b
    elif a != 0 and b >= a and b % a == 0:
        o.div_inv = b // a
    return o


def generate_ordered_pairs(numbers):
    return list(combinations(numbers, 2))


def print_pretty(operations):
    for op in operations:
        print(f"{op.a} {op.op} {op.b} = {op.res}")


def solve(numbers, operations=[], level=0):

    space = " " * 2 * level
    # if len(numbers) > 1:
        # print(f"{space}{numbers}")

    if len(numbers) == 1:
        if numbers[0] == 24:
            print_pretty(operations)
            print()
            return True

    for a, b in generate_ordered_pairs(numbers):
        subproblem = list(numbers)
        subproblem.remove(a)
        subproblem.remove(b)
        ops = generate_ops_table(a, b)

        op = Op(a=a, b=b, op=None)

        op.op = '+'
        op.res = ops.add
        solve([*subproblem, ops.add], [*operations, op], level + 1)

        # subtraction
        op.op = '-'
        op.res = ops.sub
        if not ops.sub is None:
            # print(f"{space}{a} - {b} = {ops.sub}")
            solve([*subproblem, ops.sub], [*operations, op], level + 1)

        # multiplication
        op.op = '*'
        op.res = ops.mul
        # print(f"{space}{a} * {b} = {ops.mul}")
        solve([*subproblem, ops.mul], [*operations, op], level + 1)

        # division
        op.op = '/'
        op.res = ops.div
        if not ops.div is None:
            # print(f"{space}{a} / {b} = {ops.div}")
            solve([*subproblem, ops.div], [*operations, op], level + 1)

        op.a, op.b = op.b, op.a

        # inv subtraction
        op.op = '-'
        op.res = ops.sub_inv
        if not ops.sub_inv is None:
            # print(f"{space}{b} - {a} = {ops.sub_inv}")
            solve([*subproblem, ops.sub_inv], [*operations, op], level + 1)

        # inv division
        op.op = '/'
        op.res = ops.div_inv
        if not ops.div_inv is None:
            # print(f"{space}{b} / {a} = {ops.div_inv}")
            solve([*subproblem, ops.div_inv], [*operations, op], level + 1)

    return False


def main():
    numbers = [int(x) for x in sys.argv[1:]]
    solve(numbers)



if __name__ == "__main__":
    main()
