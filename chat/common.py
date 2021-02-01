import socket
import pickle
import uuid
from datetime import datetime


packet_delimiter = b">>$$"


class Client:
	def __init__(self, uuid, sock, address, is_open=True):
		self.uuid = uuid
		self.name = ""
		self.sock = sock
		self.address = address
		self.is_open = is_open
		self.username = None

	def name_or_uuid(self):
		if self.username is None:
			return str(self.uuid)
		return self.username


class Packet:
	def __init__(self, source, content):
		self.source = source
		self.content = content
		self.time = datetime.now()
		self.uuid = str(uuid.uuid1())

	def to_bytes(self):
		d = self.to_dict()
		bytestring = packet_delimiter + pickle.dumps(d)
		return bytestring

	def to_dict(self):
		d = self.content
		d["__from__"] = self.source
		d["__uuid__"] = self.uuid
		d["__time__"] = self.time
		return d

	def __str__(self):
		return str(self.to_dict())


def send_message(sock, message):
	try:
		sock.sendall(message)
		return True
	except ConnectionResetError:
		return False
	except ConnectionAbortedError:
		return False
	except Exception as e:
		print("Error in send_message: ", type(e), e)
		return False


# returns string, bool, bool -> message, got_message, encountered_errors
def receive_message(sock, max_length=4096):
	try:
		message = b""
		while True:
			try:
				got = sock.recv(max_length)
				message += got
				if not got:
					if message:
						return message, True, False
					else:
						return message, False, False
			except socket.timeout:
				if message:
					return message, True, False
				else:
					return message, False, False
	except ConnectionResetError:
		return "", False, True
	except ConnectionAbortedError:
		return "", False, True
	except Exception as e:
		print("Error in receive_message: ", type(e), e)
		return "", False, True


def parse_packets(bytestring):
	packetstrings = [x for x in bytestring.split(packet_delimiter) if x]
	packets = list()
	for pstr in packetstrings:
		try:
			d = pickle.loads(pstr)
		except Exception as e:
			print("Unhandled parsing error: {}, {}".format(type(e), e))
			continue
		source = d["__from__"]
		d.pop("__from__")
		uuid = d["__uuid__"]
		d.pop("__uuid__")
		time = d["__time__"]
		d.pop("__time__")
		packet = Packet(source, d)
		packet.uuid = uuid
		packet.time = time
		packets.append(packet)
	return packets
