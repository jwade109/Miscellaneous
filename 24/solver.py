#! /usr/bin/env python3

import sys
from dataclasses import dataclass
from itertools import combinations
from copy import copy


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

    if len(numbers) == 1:
        if numbers[0] == 24:
            return operations
        else:
            return []

    sols = []

    for a, b in generate_ordered_pairs(numbers):
        subproblem = list(numbers)
        subproblem.remove(a)
        subproblem.remove(b)
        ops = generate_ops_table(a, b)

        op = Op(a=a, b=b, op='+')

        op.res = ops.add
        s = solve([*subproblem, ops.add],
            [*operations, copy(op)], level + 1)
        if s:
            sols.append(s)

        # subtraction
        op.op = '-'
        op.res = ops.sub
        if not ops.sub is None:
            s = solve([*subproblem, ops.sub],
                [*operations, copy(op)], level + 1)
            if s:
                sols.append(s)

        # multiplication
        op.op = '*'
        op.res = ops.mul
        s = solve([*subproblem, ops.mul], 
            [*operations, copy(op)], level + 1)
        if s:
            sols.append(s)

        # division
        op.op = '/'
        op.res = ops.div
        if not ops.div is None:
            s = solve([*subproblem, ops.div],
                [*operations, copy(op)], level + 1)
            if s:
                sols.append(s)

        op.a, op.b = op.b, op.a

        # inv subtraction
        op.op = '-'
        op.res = ops.sub_inv
        if not ops.sub_inv is None:
            s = solve([*subproblem, ops.sub_inv],
                [*operations, copy(op)], level + 1)
            if s:
                sols.append(s)

        # inv division
        op.op = '/'
        op.res = ops.div_inv
        if not ops.div_inv is None:
            s = solve([*subproblem, ops.div_inv],
                [*operations, copy(op)], level + 1)
            if s:
                sols.append(s)

    if not sols:
        return None

    # print(f"{' ' * level * 2}{numbers} Return: {sols}")

    return sols


def main():
    numbers = [int(x) for x in sys.argv[1:]]
    s = solve(numbers)
    for sol in s:
        steps = sol
        while type(steps[0]) == list:
            steps = steps[0]
        print_pretty(steps)
        print()


if __name__ == "__main__":
    main()

