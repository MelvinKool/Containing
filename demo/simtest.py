#!/usr/bin/python

import sys
import json
import time
import socket


def init_commands(file_path, cams=True):
  spawn_objects = {}
  with open(file_path) as spawnfl:
    spawn_objects = dict(eval(spawnfl.read()))

  commands = [
      {'cmdt': 0, 'cmd': spawn_objects},
      {'cmdt': 1, 'cmd': {'Command': 'spawnTruck', 'position': [835.75, 0, 0], 'container': 1, 'id': 0}},
      {'cmdt': 1, 'cmd': {'Command': 'containerCommands', 'container': 1, 'commands': [
            {'Command': 'moveTo', 'vehicleId': 89, 'Route': [[793.75, 0.0, -51.5], [835.75, 0.0, -51.5], [835.75, 0.0, -40.0]], 'totalDistance': 1, 'container': 1},
            {'Command': 'craneMoveContainer', 'craneId': 157, 'containerId': 1, 'target': 89},
            {'Command': 'agvAttachContainer', 'agvId': 89, 'containerId': 1},
            {'Command': 'moveTo', 'vehicleId': 89, 'Route': [[835.75, 0.0, -51.5], [793.75, 0.0, -51.5], [793.75, 0.0, -73.5]], 'totalDistance': 1},
          ]}}
  #     {'cmdt': 1, 'cmd': {'Command': 'spawnTruck', 'position': [835.75, 0, 0], 'container': 1}},
  #     {'cmdt': 1, 'cmd': {'Command': 'craneMoveContainer', 'craneId': 8, 'containerId': 3, 'target': [45.75, 1.0, -73.5]}},
  #     {'cmdt': 1, 'cmd': {'Command': 'moveTo', 'vehicleId': 68, 'Route': [[51.25, 0.0, -73.5]], 'totalDistance': 1}},
  #     {'cmdt': 250 - 70, 'cmd': {'Command': 'craneMoveContainer', 'craneId': 8, 'containerId': 4, 'target': [51.25, 1.0, -73.5]}},
  #     {'cmdt': 1, 'cmd': {'Command': 'moveTo', 'vehicleId': 90, 'Route': [[827.75, 0.0, -51.5], [842.75, 0.0, -51.5], [842.75, 0.0, -40.0]], 'totalDistance': 15.0 + 27.0}},
  #     {'cmdt': 2, 'cmd': {'Command': 'spawnTruck', 'position': [842.75, 0, 0], 'container': 2}},
  #     {'cmdt': 3, 'cmd': {'Command': 'craneMoveContainer', 'craneId': 157, 'containerId': 1, 'target': [835.75, 1, -40]}},
  #     {'cmdt': 4, 'cmd': {'Command': 'craneMoveContainer', 'craneId': 158, 'containerId': 2, 'target': [842.75, 1, -40]}},
  #     {'cmdt': 15, 'cmd': {'Command': 'spawnTrain', 'containers': [i for i in range(5, 40)]}},
  #     {'cmdt': 56, 'cmd': {'Command': 'agvAttachContainer', 'agvId': 90, 'containerId': 2}},
  #     #{'cmdt': 56, 'cmd': {'Command': 'craneMoveContainer', 'craneId': 63, 'containerId': 4, 'target': [43.0, 1, -720.0]}},
  #     {'cmdt': 55, 'cmd': {'Command': 'moveTo', 'vehicleId': 89, 'Route': [[835.75, 0.0, -51.5], [793.75, 0.0, -51.5], [793.75, 0.0, -73.5]], 'totalDistance': 94.0 + 27.0}},
  #     {'cmdt': 58, 'cmd': {'Command': 'moveTo', 'vehicleId': 90, 'Route': [[842.75, 0.0, -51.5], [798.5, 0.0, -51.5], [798.5, 0.0, -73.5]], 'totalDistance': 15.0 + 27.0}},
  #     {'cmdt': 71, 'cmd': {'Command': 'craneMoveContainer', 'craneId': 30, 'containerId': 1, 'target': [793.75, 0.0, -95.5]}}, # 2:50
  #     {'cmdt': 250, 'cmd': {'Command': 'craneMoveContainer', 'craneId': 30, 'containerId': 2, 'target': [798.5, 0.0, -95.5]}},
  #     {'cmdt': 250 - 71, 'cmd': {'Command': 'agvAttachContainer', 'agvId': 67, 'containerId': 3}},
  #     {'cmdt': 250 - 70, 'cmd': {'Command': 'moveTo', 'vehicleId': 67, 'Route': [[45.75, 0.0, -51.5], [793.75, 0.0, -51.5], [793.75, 0.0, -73.5]], 'totalDistance': 1}}
  ]

  if cams:
    commands += [
      {'cmdt': 1, 'cmd': {'Command': 'setCamera', 'container': 1}},
      {'cmdt': 68, 'cmd': {'Command': 'setCamera', 'container': 3}},
      {'cmdt': 55, 'cmd': {'Command': 'setCamera', 'container': 1}},
      {'cmdt': 100 + 60, 'cmd': {'Command': 'setCamera', 'container': 2}},
      {'cmdt': 250, 'cmd': {'Command': 'setCamera', 'container': 3}},
      {'cmdt': 25, 'cmd': {'Command': 'trainCam'}},
    ]
  return commands


class Server(object):

  def __init__(self, address, commands, port=1337):
    self.bind_addr = (address, port)
    self.commands = commands

  def start(self):
    self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    self.sock.bind(self.bind_addr)
    self.sock.listen(5)
    self.await_connection()

  def await_connection(self):
    print 'server listening'
    conn, addr = self.sock.accept()
    print 'new connection'
    self.sim = conn.makefile()
    self.start_sim()

  def start_sim(self):
    sim_base_time = time.time()
    while True:
      for i, command in enumerate(self.commands):
        if command and command['cmdt'] <= int(time.time()) - sim_base_time:
          print 'sending: %s' % json.dumps(command['cmd'])
          self.send_command(command['cmd'])
          commands[i] = None
      time.sleep(0.1)

  def send_command(self, command):
    self.sim.write('%s\n' % json.dumps(command))
    self.sim.flush()


if __name__ == '__main__':
  while True:
    if 'nocams' in sys.argv:
      commands = init_commands('spawns.json', False)
    else:
      commands = init_commands('spawns.json')
    server = Server('127.0.0.1', commands)
    server.start()
