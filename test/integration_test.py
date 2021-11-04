#!/usr/bin/python3
import pytest
import requests
import socket
import sys
import time
import re

SERVER_PORT = 25565
EXPORTER_PORT = 25566

@pytest.fixture
def wait_for_connection(address, timeout):
	initial = time.time()
	while time.time() <= initial + timeout:
		if can_http("http://{}:{}/metrics".format(address, EXPORTER_PORT)):
			break
		time.sleep(0.1)
	return True

def test_server_alive(wait_for_connection, address):
	assert is_port_open(address, SERVER_PORT)

def test_api_alive(wait_for_connection, address):
	assert is_port_open(address, EXPORTER_PORT)

def test_exporter(wait_for_connection, address):
	resp = requests.get("http://{}:{}/metrics".format(address, EXPORTER_PORT))
	text = [row for row in resp.text.split("\n") if row]
	test_case = True
	for row in text:
		test_case = test_case and re.match(r"(# HELP .+|# TYPE .+|\w+ \S+)", row)
	assert test_case

def is_port_open(ip, port):
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	try:
		s.connect((ip, port))
		s.shutdown(2)
	except Exception as e:
		return False
	else:
		return True
	finally:
		s.close()

def can_http(url):
	try:
		requests.get(url)
	except:
		return False
	else:
		return True