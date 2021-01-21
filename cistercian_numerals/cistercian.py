#! /usr/bin/env python
# -*- coding: utf-8 -*-

import argparse
from math import floor, ceil, log
from os import popen
import sys
try:
    from shutil import get_terminal_size
except:
    get_terminal_size = None

base = 10000 # base of cistercian numeral system


def print_numeral(numeral):
    if not numeral:
        print("None")
        return
    for row in numeral:
        print("   {}".format("".join([x*2 for x in row])))


def print_numerals(numerals):
    if not numerals:
        return
    if not numerals[0]:
        return
    if get_terminal_size:
        columns, _ = get_terminal_size()
    else:
        _, columns = popen('stty size', 'r').read().split()
    columns = int(columns)
    effective_width = len(numerals[0][0]) * 2 + 3
    num_to_print = max(1, min(len(numerals), int(floor(columns/effective_width))))

    for r in range(0, len(numerals[0])):
        for numeral in numerals[:num_to_print]:
            row = numeral[r]
            sys.stdout.write("  {}".format("".join([x*2 for x in row])))
        print("")

    if num_to_print < len(numerals):
        print("")
        print_numerals(numerals[num_to_print:])


def empty_numeral(width):
    numeral = list()
    for i in range(0, width):
        numeral.append([" "]*width)
    return numeral


def base_numeral(width):
    numeral = list()
    half_width = int(floor(width/2.0))
    for i in range(0, width):
        numeral.append([" "]*half_width + ["█"] + [" "]*half_width)
    return numeral


def segment(width, value):
    half_width = int(floor(width/2.0))
    numeral = empty_numeral(width)
    if value == 0:
        return base_numeral(width)
    elif value == 1:
        numeral[0][-half_width:] = ["█"]*half_width
    elif value == 2:
        numeral[half_width-1][-half_width:] = ["█"]*half_width
    elif value == 3:
        for i in range(0, half_width):
            numeral[i][half_width+1+i] = "█"
    elif value == 4:
        for i in range(0, half_width):
            numeral[i][width-i-1] = "█"
    elif value == 5:
        return overlay(segment(width, 4), segment(width, 1))
    elif value == 6:
        for i in range(0, half_width):
            numeral[i][width-1] = "█"
    elif value == 7:
        return overlay(segment(width, 6), segment(width, 1))
    elif value == 8:
        return overlay(segment(width, 6), segment(width, 2))
    elif value == 9:
        return overlay(segment(width, 8), segment(width, 1))
    elif value > 9 and value < 100:
        return overlay(hinvert(segment(width, value // 10)), segment(width, value % 10))
    elif value > 99 and value < 1000:
        return overlay(vinvert(segment(width, value // 100)),
                       hinvert(segment(width, (value // 10) % 10)),
                       segment(width, value % 10))
    elif value > 999 and value < 10000:
        return overlay(vinvert(hinvert(segment(width, value // 1000))),
                       vinvert(segment(width, (value // 100) % 10)),
                       hinvert(segment(width, (value // 10) % 10)),
                       segment(width, value % 10))
    else:
        return None
    return overlay(base_numeral(width), numeral)


def overlay(*numerals):
    if not numerals:
        return None
    base = numerals[0]
    for numeral in numerals:
        for r in range(0, len(base)):
            for c in range(0, len(base)):
                if numeral[r][c] != " ":
                    base[r][c] = numeral[r][c]
    return base


def vinvert(numeral):
    newmeral = list()
    for row in reversed(numeral):
        newmeral.append(row)
    return newmeral


def hinvert(numeral):
    newmeral = list()
    for row in numeral:
        newmeral.append(row[::-1])
    return newmeral


def decompose(num):
    l = list()
    while num >= base:
        l.append(num % base)
        num //= base
    l.append(num)
    return l[::-1]


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("number", type=int)
    parser.add_argument("--width", "-w", type=int, default=7)
    args = parser.parse_args()

    if args.width % 2 == 0 or args.width < 5:
        print("Width must be an odd number of at least 5.")
        exit()

    num = args.number
    width = args.width
    digits = 1 if num == 0 else int(ceil(log(num, base)))
    # print("Digits = {}".format(digits))
    # print("decompose({}) = {}".format(num, decompose(num)))
    print("")
    print_numerals([segment(width, n) for n in decompose(num)])
    print("")


if __name__ == "__main__":
    main()
