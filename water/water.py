#! /usr/bin/env python3
# -*- coding: utf-8 -*-

from math import sqrt, sin, exp
from os import popen
import sys
import curses
from time import sleep

try:
    from shutil import get_terminal_size
except:
    get_terminal_size = None


def setval(screen, r, c, value):
    try:
        screen.addstr(r, c*2, value*2)
    except:
        pass


def dropoff(x, radius, minrad=0.01):
    return exp(-(x / max(minrad, radius))**2)


def radial(r, c, t, k, s, rc=0, cc=0):
    r = sqrt((r - rc)**2 + (c - cc)**2)
    return sin(r*k - t) * dropoff(r, s)


def linear(r, c, t, k, s, a, rc=0, cc=0):
    r = (r - rc)*a + (c - cc)*(1 - a)
    return sin(r*k - t) 


def tochr(value):
    if value >= 0.9:
        return "@"
    if value > 0.7:
        return "#"
    if value > 0.4:
        return "*"
    if value > -0.2:
        return "-"
    if value > -0.7:
        return "."
    return " "


def main(stdscr):

    stdscr.clear()

    t = 0.1
    try:
        while True:
            cols, rows = get_terminal_size()
            cols = cols//2
            for r in range(rows-1):
                for c in range(cols):
                    setval(stdscr, r, c, tochr(
                        radial(r, c, t/2, 0.3, 200, -rows, -cols) + \
                        radial(r, c, t/2, 0.3, 200, 2*rows,-1.9*cols) + \
                        radial(r, c, t/1.5, 0.3, 80, 0.25*rows, 0.25*cols) * 1.0 + \
                        radial(r, c, t/2.0, 0.6, 40, 0.70*rows, 0.60*cols) * 0.5 + \
                        radial(r, c, t*1.5, 0.8, 40, 0.24*rows, 0.75*cols) * 0.3))
            stdscr.refresh()
            sleep(0.05)
            t += 0.2
    except KeyboardInterrupt:
        exit()

    print("Done.")


if __name__ == "__main__":
    curses.wrapper(main)
