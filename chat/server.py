import socket
import select
import sys
from _thread import *
import atexit
import signal
import time
import sched
from datetime import datetime
from functools import partial
import cv2

from common import *


list_of_clients = []
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s = sched.scheduler(time.time, time.sleep)


def client_has_disconnected(client):
	client.is_open = False
	list_of_clients.remove(client)


def send_bytes_to_client(client, message):
	if not client.is_open:
		return
	if not send_message(client.sock, message):
		print("Looks like client {} has disconnected.".format(client.uuid))
		client_has_disconnected(client)


def receive_message_from_client(client):
	if not client.is_open:
		return "", False
	content, got_message, error = receive_message(client.sock)
	if got_message:
		return content, True
	if error:
		print("Looks like client {} has disconnected.".format(client.uuid))
		client_has_disconnected(client)
	return "", False


def ping():
	for client in list_of_clients:
		if client.is_open:
			packet = Packet("~", {})
			send_bytes_to_client(client, packet.to_bytes())
	s.enter(1, 1, ping, ())

def exit_handler():
	server.close()
	print("Exiting.")

def signal_handler(sig, frame):
	exit()


def broadcast_message(bytestream):
	for client in list_of_clients:
		send_bytes_to_client(client, bytestream)


def serve_client(client):

	introduction = Packet("~",
		{"text": "Connection established. Client ID: {}".format(client.uuid)})
	send_bytes_to_client(client, introduction.to_bytes())
	other_users = [x for x in list_of_clients if x.uuid != client.uuid]
	if other_users:
		number_of_users = Packet("~",
			{"text": "Currently online: {}".format(", ".join([x.name_or_uuid() for x in other_users]))})
		send_bytes_to_client(client, number_of_users.to_bytes())

	while True:
		content, got_message = receive_message_from_client(client)
		if got_message:
			packets = parse_packets(content)
			for packet in packets:
				print(packet.to_dict())
				if "exiting" in packet.content:
					print("Client {} has gracefully disconnected.".format(client.uuid))
					client_has_disconnected(client)
					notify_others = Packet("~", {"text": "{} has disconnected.".format(client.name_or_uuid())})
					broadcast_message(notify_others.to_bytes())
					return
				if "name" in packet.content:
					name = packet.content["name"]
					client.username = name
					print("Client {} has registered username {}.".format(client.uuid, client.username))
					notify_others = Packet("~", {"text": "{} has logged on.".format(client.name_or_uuid())})
					broadcast_message(notify_others.to_bytes())
				if "text" in packet.content or "image" in packet.content:
					broadcast_message(packet.to_bytes())
					name = client.name_or_uuid()
					print("[{}] {}".format(name, packet.content["text"]))


def handle_connections():
	try:
		conn, addr = server.accept()
		conn.setblocking(1)
		conn.settimeout(0.005)
		client = Client(hex(hash(datetime.now()) % 2**16), conn, addr)
		list_of_clients.append(client)
		print("New connection: {}, {}".format(client.uuid, client.address))
		start_new_thread(serve_client, (client,))
	except BlockingIOError:
		pass
	except Exception as e:
		print("Error in handling: {}, {}".format(type(e), e))
		pass
	s.enter(0.1, 1, handle_connections, ())


def main():

	server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	server.setblocking(False)

	if len(sys.argv) != 2:
		print("usage:   .\server.py [PORT]")
		print("example: .\server.py 25565")
		exit()
	address = "127.0.0.1"
	port = int(sys.argv[1])

	server.bind((address, port))
	server.listen(10)

	print("Starting server at {}:{}".format(address, port))

	atexit.register(exit_handler)
	signal.signal(signal.SIGINT, signal_handler)

	s.enter(0, 1, ping, ())
	s.enter(0, 1, handle_connections, ())
	s.run()

if __name__ == "__main__":
	main()
