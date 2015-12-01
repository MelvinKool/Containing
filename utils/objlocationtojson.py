#!/usr/bin/python

import re
import sys

import simplejson as json


def strtolist(string):
  string = string.replace('f', '')
  return [float(r) for r in re.findall('[0-9\-\.]+', string)]

def convert():
  obj = {}
  with sys.stdin as fl:
    content = fl.readlines()
    for i, line in enumerate(content):
      tp = None
      nei = None
      if line.startswith('SeaShip'):
        tp = 'SeaShip'
      elif line.startswith('FreightShip'):
        tp = 'FreightShip'
      elif line.startswith('Train'):
        tp = 'Train'
      elif line.startswith('TruckCrane'):
        tp = 'TruckCrane'
      elif line.startswith('Storage'):
        tp = 'Storage'
      elif line.startswith('AGV'):
        tp = 'AGV'

      if tp:
        rotstr = line[line.index('Rotation') + 8::]
        rot = strtolist(rotstr)
        nei = line.index(':')
        count = int(line[len(tp):nei])
        obj[tp] = {
            'rotation': rot,
            'count': count,
            'positions': [strtolist(l.strip()) for l in content[i + 1:i + count + 1]]}

  print json.dumps(obj)

if __name__ == '__main__':
  convert()
