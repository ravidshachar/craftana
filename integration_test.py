#!/usr/bin/python3
import unittest
import requests
import socket
import sys

ADDR = sys.argv[1]
SERVER_PORT = 25565
EXPORTER_PORT = 25566
TIMEOUT = sys.argv[2]

class TestCraftana(unittest.TestCase):
  """Tests craftana running and exporter working"""

  def test_server_alive(self):
    if is_port_open(ADDR, SERVER_PORT) == False:
      self.fail("Minecraft server is unreachable")

  def test_api_alive(self):
    if is_port_open(ADDR, EXPORTER_PORT) == False:
      self.fail("Exporter is unreachable")

  def test_exporter(self):
    resp = requests.get("http://{}:{}/metrics".format(ADDR, EXPORTER_PORT))
    text = resp.text.split("\n")
    for row in text:
        if not re.match("(# HELP .+|# TYPE .+|\w+ \S+)"):
            self.fail("Bad exporter")

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

if __name__ == "__main__":
    initial = time.time()
    while initial + TIMEOUT <= time.time():
        if is_port_open(ADDR, SERVER_PORT):
            break
        time.sleep(0.1)
    unittest.main()
