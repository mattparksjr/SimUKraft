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
    "0:0": "minecraft:air",
    "1:0": "minecraft:stone",
    "2:0": "minecraft:grass",
    "3:0": "minecraft:dirt",
    "3:1": "minecraft:coarse_dirt",
    "3:2": "minecraft:podzol",
    "4:0": "minecraft:cobblestone",
    "5:0": "minecraft:oak_planks",
    "5:1": "minecraft:spruce_planks",
    "5:2": "minecraft:birch_planks",
    "5:3": "minecraft:jungle_planks",
    "5:4": "minecraft:acacia_planks",
    "5:5": "minecraft:dark_oak_planks",
    "6:0": "minecraft:oak_sapling",
    "6:1": "minecraft:spruce_sapling",
    "6:2": "minecraft:birch_sapling",
    "6:3": "minecraft:jungle_sapling",
    "6:4": "minecraft:acacia_sapling",
    "6:5": "minecraft:dark_oak_sapling",
    "7:5": "minecraft:bedrock",
    "8:0": "minecraft:water",
    "9:0": "minecraft:water",
    "10:0": "minecraft:lava",
    "11:0": "minecraft:lava",
    "12:0": "minecraft:sand",
    "12:1": "minecraft:red_sand",
    "13:0": "minecraft:gravel",
    "14:0": "minecraft:gold_ore",
    "15:0": "minecraft:iron_ore",
    "16:0": "minecraft:coal_ore",
    "17:0": "minecraft:oak_log",
    "17:1": "minecraft:spruce_log",
    "17:2": "minecraft:birch_log",
    "17:3": "minecraft:jungle_log",
    "17:4": "minecraft:oak_wood",
    "17:5": "minecraft:dark_oak_log",
    "20:0": "minecraft:glass",
    "35:0": "minecraft:white_wool",
    "35:1": "minecraft:orange_wool",
    "35:2": "minecraft:magenta_wool",
    "35:3": "minecraft:light_blue_wool",
    "35:4": "minecraft:yellow_wool",
    "35:5": "minecraft:lime_wool",
    "35:6": "minecraft:pink_wool",
    "35:7": "minecraft:gray_wool",
    "35:8": "minecraft:light_gray_wool",
    "35:9": "minecraft:cyan_wool",
    "35:10": "minecraft:purple_wool",
    "35:11": "minecraft:blue_wool",
    "35:12": "minecraft:brown_wool",
    "35:13": "minecraft:green_wool",
    "35:14": "minecraft:red_wool",
    "35:15": "minecraft:black_wool",

    

    "44:0": "minecraft:stone_slab",

    "50:0": "minecraft:torch",
    "50:1": "minecraft:torch",
    "50:2": "minecraft:torch",
    "50:3": "minecraft:torch",
    "50:4": "minecraft:torch",
    "50:5": "minecraft:torch",
    "50:6": "minecraft:torch",
    "50:7": "minecraft:torch",
    "61:0": "minecraft:furnace",
    "62:0": "minecraft:furnace",
    "65:0": "minecraft:ladder",
    "65:1": "minecraft:ladder",
    "65:2": "minecraft:ladder",
    "65:3": "minecraft:ladder",
    "65:4": "minecraft:ladder",
    "65:5": "minecraft:ladder",
    "65:6": "minecraft:ladder",
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

# Turns the old data key into new block data
def dataKeyToDict(line):
    keybits = line.split(';')

    map = {}

    keyCount = 0
    numFixed = 0

    for key in keybits:
        parts = key.split('=')

        # some builds are broken...
        if len(parts) != 2:
            break

        keyCount =+ 1

        keyForMap = parts[0]
        value = parts[1]

        if parts[1] in idToName:
            value = idToName[parts[1]]
            numFixed =+ 1
        map[keyForMap] = value

    if keyCount == numFixed:
        print("DONE!")
    return map

def readOldData(data):
    print("Starting file " + data)
    file = open(cwd + "/" + file_name + "/" + data, "r")

    name = readName(data)
    size = readSize(file.readline())

    dataKey = file.readline()

    author = readAuthor(dataKey)

    print("Structure defined as: name=" + name + " size=" + str(size[0]) + "x" + str(size[1]) + "x" + str(size[2]) + " author=" + author) 

    print(dataKeyToDict(dataKey))




for file_name in file_list:
    print("Working in " + file_name)
    data_files = os.listdir(cwd + "/" + file_name)
    for data in data_files:
        readOldData(data)

print("Done. Converted " + str(numConverted) + " files, and found " + str(numErrors) + " errors" )