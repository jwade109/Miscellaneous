#! /usr/bin/env python3
# -*- coding: utf-8 -*-

from math import sqrt, sin, exp, pi
import os
import sys
import curses
from curses.textpad import rectangle
from time import sleep
from datetime import datetime, timedelta
from random import randint, random
import hashlib
from numba import njit

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


@njit
def decay(x):
    operand = min(500, abs(x))
    return 1 / exp(operand)

@njit
def water_droplet(r, c, t, k, rc, cc):
    time_decay = decay(t/30)
    if abs(time_decay) < 0.01:
        return 0, False
    if t < 0:
        return 0, True
    rad = sqrt((r - rc)**2 + (c - cc)**2)
    w = 0
    ampl = decay(rad*pi/t) * time_decay
    return ampl * sin(abs(rad) * k + w - t/4), True


def moving(r, c, t, k, s, rc, cc, vr, vc):
    v = sqrt(vr**2 + vc**2)
    rad = sqrt((r - (rc + vc*t))**2 + (c - (cc + vc*t))**2)
    dt = rad/v;
    t -= dt;
    rad = sqrt((r - (rc + vc*t))**2 + (c - (cc + vc*t))**2)
    return sin(rad*k - t) * dropoff(rad, s)


def linear(r, c, t, k, s, a, rc=0, cc=0):
    r = (r - rc)*a + (c - cc)*(1 - a)
    return sin(r*k - t) 


def spurious(r, c, t, k, s, rc, cc):
    rad = sqrt((r - rc)**2 + (c - cc)**2)
    return sin(rad*k - t) * dropoff(rad * t, s)


def dot(r, c, t, k, s, rc, cc):
    return 1 if r == int(rc) and c == int(cc) else 0


def tochr(value, r, c):
    if value >= 0.9:
        return "@"
    if value > 0.7:
        return "#"
    if value > 0.5:
        return "&"
    if value > 0.2:
        return "*"
    if value > -0.1:
        return "-"
    if r % 2 != c % 2:
        if value > -0.3:
            return ","
        if value > -0.5:
            return "."
    return " "


def main(screen):

    screen.clear()
    curses.curs_set(0)
    curses.noecho()
    curses.mousemask(1)
    screen.nodelay(1)
    curses.cbreak()
 
    message = None
    if len(sys.argv) > 1:
        message = sys.argv[1]

    cols, rows = get_terminal_size()
    cols = cols//2
    center_set = set()
    fps = 0

    t = 0.1
    try:
        while True:

            start = datetime.now()

            cols, rows = get_terminal_size()
            text_r = rows//4
            text_c = cols//4
            cols = cols//2
            for r in range(rows-1):
                for c in range(cols):
                    sum = 0
                    remove_set = set()
                    for x in center_set:
                        start_time, k, tr, rc, cc = x
                        mag, cont = water_droplet(r, c, (t - start_time)*tr, k, rc, cc)
                        sum += 3 * mag
                        if not cont:
                            remove_set.add(x)
                    for x in remove_set:
                        center_set.remove(x)
                    next_t = t + random() * 3
                    while len(center_set) < 4:
                        center_set.add((next_t, random() * 0.2 + 1.1,
                            random() * 1.6 + 4.5,
                            randint(int(rows*0.1), int(rows*0.9)),
                            randint(int(cols*0.1), int(cols*0.9))))
                        next_t += random() * 25
                    
                    sum += radial(r, c, t/2, 0.3, cols*1.5, -rows*0.5, -cols*1.5)
 
                    setval(screen, r, c, tochr(sum, r, c))
                    continue
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
            screen.addstr(0, 0, f"{fps:0.2f} FPS")

            screen.refresh()

            inter = datetime.now()
            sleep_for = 1/60 - (inter - start).total_seconds()
            if sleep_for > 0:
                sleep(sleep_for)

            end = datetime.now()
            fps = 1/(end - start).total_seconds()

            t += (end - start).total_seconds() * 3
    except KeyboardInterrupt:
        exit()

    print("Done.")


if __name__ == "__main__":
    os.environ.setdefault('ESCDELAY', '5')
    curses.wrapper(main)
