import os
import json


# General
cwd = "C:/Users/mattp/Documents/MOD DEV/old-data/buildings"
file_list = os.listdir(cwd)
parent = os.path.dirname(cwd)

# Migration specfic
numErrors = 0
numConverted = 0

idToName = {
    "0": "minecraft:air",
    "1": "minecraft:stone"
}

# read the name from the line. original used the file name for some reason
def readName(line):
    name = "Undefined"
    if(data.startswith("PKID")):
        loc = data.index("-")
        name = data[loc+1:-4]
    else:
        name = data[:-4]
    return name

# read the size
def readSize(line):
    bits = line.split("x")
    if len(bits) != 3:
        print("Error caught while reading size...")
        numErrors += 1
    return [bits[0], bits[1], bits[2]]

# get author
def readAuthor(line):
    author = "Undefined"
    keybits = line.split(';')
        
    for key in keybits:
        parts = key.split('=')
        if len(parts) != 2:
            break
        
        # why would you store diffrent data on new lines, only to encode the author here?!?!?!
        if(parts[0].lower == "AU"):
            author = parts[1]
    return author

def dataKeyToDict(line):
    

def readOldData(data):
    print("Starting file " + data)
    file = open(cwd + "/" + file_name + "/" + data, "r")

    name = readName(data)
    size = readSize(file.readline())

    dataKey = file.readline()

    author = readAuthor(dataKey)

    print("Structure defined as: name=" + name + " size=" + str(size[0]) + "x" + str(size[1]) + "x" + str(size[2]) + " author=" + author) 

    #map = {}
   # keybits = key.split(';')

    # make sure we got the map
   # if not map:
   #    print("ERROR: FAILED TO GENERATE MAP FOR" + data)
   #     numErrors = 1 + numErrors
   #     return
    
   # for bit in map:
  #      blockData = map[bit].split(":")
   #     if len(blockData) != 2:
   #         break
#
   #     if blockData[1] in idToName:
   #         print("found match")
   #         map[bit] = idToName[blockData[1]]
  #  print(map)

for file_name in file_list:
    print("Working in " + file_name)
    data_files = os.listdir(cwd + "/" + file_name)
    for data in data_files:
        readOldData(data)
print("Done. Converted " + str(numConverted) + " files, and found " + str(numErrors) + " errors" )