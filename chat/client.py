import os
import socket
import sys
import select
import atexit
import signal
import time
import sched
from datetime import datetime
import msvcrt
from functools import partial
import string
import numpy as np
import cv2
import curses
from curses.textpad import Textbox, rectangle
from common import *


screen_lines = []
username = ""
current_user_input = ""
ping_state = False


def render(screen):
	height, width = screen.getmaxyx()
	screen.erase()
	num_lines = min(len(screen_lines), height - 2)
	lines_to_print = screen_lines[-num_lines:]
	for i, line in enumerate(lines_to_print):
		screen.addstr(i, 0, line)
	if ping_state:
		screen.addch(0, width - 1, "*")
	screen.addstr(i + 1, 0, "({}) {}".format(username, current_user_input))
	screen.refresh()
	time.sleep(0.02)


def logprint(string, screen):
	screen_lines.append(string)
	render(screen)


def exit_handler(screen, server, username):
	on_exit = Packet(username, {"exiting": ""})
	send_message(server, on_exit.to_bytes())
	time.sleep(1)
	try:
		server.shutdown(socket.SHUT_RD)
	except:
		pass
	server.close()
	logprint("Exiting.", screen)


def try_connect(screen, username, address, port):
	try:
		server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		server.setblocking(True)
		server.settimeout(0.01)
		server.connect((address, port))
		introduction = Packet(username, {"name": username})
		send_message(server, introduction.to_bytes())
		return server
	except socket.timeout:
		pass
	except Exception as e:
		logprint("An unhandled error occurred while trying to connect: {}, {}".format(type(e), e), screen)
	return None


def try_connect_multiple(screen, username, address, port, num_attempts):
	server = try_connect(screen, username, address, port)
	attempts = 0
	while not server and attempts < num_attempts:
		logprint("Server inaccessible. Reattempting... {}/{}".format(attempts + 1, num_attempts), screen)
		time.sleep(1)
		server = try_connect(screen, username, address, port)
		attempts += 1
	return server


# return string, bool -> message, got_message
def get_newline_delimited_user(screen, blocking):
	global current_user_input
	while True:
		c = screen.getch()
		if c == 3:
			logprint("Exiting...", screen)
			time.sleep(1)
			screen.erase()
			exit()
		elif c == -1:
			pass
		elif c == 10: # enter
			copy = current_user_input
			current_user_input = ""
			return copy, True
		elif c == 8:
			if current_user_input:
				current_user_input = current_user_input[:-1]
		else:
			if chr(c) in string.printable:
				current_user_input += chr(c)
		if not blocking:
			return "", False
		else:
			render(screen)
	return "", False


def main(screen):

	global user_input
	global username

	if len(sys.argv) != 3:
		logprint("usage:   script, IP address, port number", screen)
		logprint("example: .\client.py spookyscary.ddns.net 25565", screen)
		exit()
	address = str(sys.argv[1])
	port = int(sys.argv[2])

	logprint("Connecting to chatroom at {}:{}...".format(address, port), screen)

	screen.timeout(0)

	logprint("Please enter your username.", screen)
	username, _ = get_newline_delimited_user(screen, True)
	if not username:
		logprint("Blank usernames are not allowed.", screen)
	while not username:
		username, _ = get_newline_delimited_user(screen, True)

	server = try_connect_multiple(screen, username, address, port, 10)
	if not server:
		logprint("Failed to connect to server.", screen)
		exit()

	atexit.register(partial(exit_handler, screen, server, username))
	s = sched.scheduler(time.time, time.sleep)

	while True:

		render(screen)

		content, got_message, error = receive_message(server)
		if got_message:
			packets = parse_packets(content)
			for packet in packets:
				if "text" in packet.content:
					name = packet.source
					t = packet.time
					text = packet.content["text"]
					logprint("[{}] [{}] {}".format(t.strftime("%I:%M:%S %p"), name, text), screen)
				if "image" in packet.content and "title" in packet.content:
					img = np.array(packet.content["image"], dtype=np.uint8)
					title = packet.content["title"]
					cv2.imshow(title, img)
					cv2.waitKey(0)
				if packet.content:
					reply = Packet(username, {"received": packet.uuid})
					send_message(server, reply.to_bytes())
				else:
					global ping_state
					ping_state = not ping_state
		if error:
			logprint("Looks like the server has disconnected. Will reattempt.", screen)
			server = try_connect_multiple(screen, username, address, port, 10)
			if not server:
				logprint("Failed to connect to server.", screen)
				exit()

		text_message, got_message = get_newline_delimited_user(screen, False)
		if got_message:
			if text_message == ":q":
				exit()
			else:
				txt = Packet(username, {"text": text_message})
				send_message(server, txt.to_bytes())

		# if msvcrt.kbhit():
		# 	try:
		# 		c = msvcrt.getch().decode()
		# 	except UnicodeDecodeError:
		# 		continue
		# 	if ord(c) == 13: # enter
		# 		if user_input.startswith("image"):
		# 			filename = user_input.split(" ")[1]
		# 			logprint("Sending image: {}".format(filename))
		# 			image = cv2.imread(filename)
		# 			image = cv2.resize(image, (500, int(500 * image.shape[0]/image.shape[1])))
		# 			packet = Packet(username, {"text": "[Image: {}]".format(filename), "title": filename, "image": image.tolist()})
		# 			send_message(server, packet.to_bytes())
		# 			user_input = ""
		# 		else:
		# 			txt = Packet(username, {"text": user_input})
		# 			send_message(server, txt.to_bytes())
		# 			user_input = ""
		# 	elif ord(c) == 8: # backspace
		# 		if user_input:
		# 			user_input = user_input[:-1]
		# 	elif c in string.printable:
		# 		user_input += c





if __name__ == "__main__":
	curses.wrapper(main)
