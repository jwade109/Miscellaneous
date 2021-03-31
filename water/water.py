#! /usr/bin/env python3
# -*- coding: utf-8 -*-

from math import sqrt, sin, exp
from os import popen
import sys
import curses
from curses.textpad import rectangle
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


def main(screen):

    screen.clear()
    curses.curs_set(0)

    message = None
    if len(sys.argv) > 1:
        message = sys.argv[1]

    t = 0.1
    try:
        while True:
            cols, rows = get_terminal_size()
            text_r = rows//4
            text_c = cols//4
            cols = cols//2
            for r in range(rows-1):
                for c in range(cols):
                    setval(screen, r, c, tochr(
                        radial(r, c, t/2, 0.3, cols*1.5, -rows, -cols) + \
                        radial(r, c, t/2, 0.3, cols*1.5, 2*rows,-1.9*cols) + \
                        radial(r, c, t/1.5, 0.3, 80, 0.25*rows, 0.25*cols) * 0.7 + \
                        radial(r, c, t/2.0, 0.6, 40, 0.70*rows, 0.60*cols) * 0.5 + \
                        radial(r, c, t*1.5, 0.8, 40, 0.24*rows, 0.75*cols) * 0.3))
            if message:
                screen.addstr(text_r - 1, text_c - 2, " "*(len(message) + 4))
                screen.addstr(text_r    , text_c - 2, " "*(len(message) + 4))
                screen.addstr(text_r + 1, text_c - 2, " "*(len(message) + 4))
                screen.addstr(text_r, text_c, message)
                ulr = text_r - 2
                ulc = text_c - 3
                lrr = text_r + 2
                lrc = text_c + len(message) + 2
                rectangle(screen, ulr, ulc, lrr, lrc)

            screen.refresh()
            sleep(0.05)
            t += 0.2
    except KeyboardInterrupt:
        exit()

    print("Done.")


if __name__ == "__main__":
    curses.wrapper(main)
