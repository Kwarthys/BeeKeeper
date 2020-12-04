import matplotlib.pyplot as plt
import csv
import numpy as np
import os
import re

#defs

def getCleanPowerTen(crappyPower):
	valueLength = 1 + 3;
	if('E-' in crappyPower):
		key = re.sub(r'[0-9.]+', '', crappyPower.split('E')[0])
		print(key)
		value = crappyPower.split(key)[1].split('E')[0];
		if(len(value) > valueLength):
			value = value[0:valueLength]
		print(value)
		return key + value + 'E' + crappyPower.split('E')[1]
	else:
		return crappyPower;


def getListFromExpeDict(task, dict):
	theList = [];
	for timestep in dict:
		theList.append(dict[timestep][task]);

	return theList;

def getTimeStepsListFromExpeDict(dict):
	convertInDays = True;
	theList = [];
	for timestep in dict:
		v = timestep;
		if(convertInDays):
			timestep /= 60*60*24
		theList.append(timestep);

	return theList;
	
#PROGRAM

larvaTask = "LarvaTask";
nurseTask = "FeedLarvae";
foragerTask = "Foraging";
giveFoodTask = "GiveFood";
askFoodTask = "AskingFood";
otherTask = "Other";
meanHJKey = "MeanHJ";
meanEOKey = "MeanEO"
keys = ["FeedLarvae","Foraging", "Other"];

folderName = "expe";
path = '../' + folderName + '/';


files = []
# r=root, d=directories, f = files
for r, d, f in os.walk(path):
	for file in f:
		if '.csv' in file:
			files.append(os.path.join(r, file))
fileIndex = 1

allParsedExpes = {}

#parsing
for f in files:
	print(f + " " + str(fileIndex) + "/" + str(len(files)));
	fileIndex += 1
	fileName = f;
	fileTags = f.split("expe/")[1]
	fileTags = fileTags.split(".csv")[0]
	fileTags = fileTags.split("_");
	
	#A_Classic_NewBorn_1000_0_518400_HJInc1.8518518518518519E-6
	#fileImportantName = fileTags[1] + " " + fileTags[2] + " " + fileTags[3] + " " + fileTags[6];
	
	theKey = "HJRed";
	scientificParam = fileTags[7].split(theKey)[1];
	powerTen = scientificParam.split("E")[1];
	value = scientificParam.split("E")[0];
	value = value.split(".")[0];
	
	
	fileImportantName = fileTags[0] + ": " + fileTags[2] + " larvae:" + fileTags[4] + " " + getCleanPowerTen(fileTags[9])
	#if(len(fileTags) >= 9):
	#	fileImportantName += " " + fileTags[8]
	
	parsedExpe = {}
	
	size = 0
	with open(fileName) as csv_file:
		csv_reader = csv.reader(csv_file, delimiter=',')
		for row in csv_reader:
			size += 1;
	
	with open(fileName) as csv_file:
		csv_reader = csv.reader(csv_file, delimiter=',')
		line_count = 0;
		prevIndex = -1
		#print(f'\tturn {row[0]}, Bee ID {row[1]} was doing {row[2]} and had {row[3]} HJTiter ans row[4] EO.')
		for row in csv_reader:
			if(line_count != 0):
				index = int(row[0]);
				if(prevIndex != index):
					#push changes
					if(prevIndex != -1):
						parsedExpe[prevIndex][larvaTask] = larvaeCount;
						parsedExpe[prevIndex][nurseTask] = nurseCount;
						parsedExpe[prevIndex][foragerTask] = foragerCount;
						parsedExpe[prevIndex][giveFoodTask] = giveFoodCount;
						parsedExpe[prevIndex][askFoodTask] = askFoodCount;
						parsedExpe[prevIndex][otherTask] = otherCount + giveFoodCount + askFoodCount;
						parsedExpe[prevIndex][meanEOKey] = meanEO / (otherCount + foragerCount + nurseCount);
						parsedExpe[prevIndex][meanHJKey] = meanHJ / (otherCount + foragerCount + nurseCount);
						parsedExpe[prevIndex]["Total"] = nurseCount + foragerCount + otherCount;
					#newTimeStep, reset counters
					foragerCount = 0
					nurseCount = 0
					otherCount = 0
					larvaeCount = 0
					giveFoodCount = 0
					askFoodCount = 0
					meanHJ = 0.0
					meanEO = 0.0
					parsedExpe[index] = {}
					
				prevIndex = index
				task = row[2];
				if(task == larvaTask):
					larvaeCount +=1;
				else:
					meanHJ += float(row[3]);
					meanEO += float(row[4]);
					if(task == nurseTask):
						nurseCount +=1;
					elif(task == foragerTask):
						foragerCount +=1;
					elif(task == askFoodTask):
						askFoodCount += 1;
					elif(task == giveFoodTask):
						giveFoodCount += 1;
					else:
						otherCount += 1;
			
			line_count+=1;
			
	if(prevIndex != -1):
		parsedExpe[prevIndex][larvaTask] = larvaeCount;
		parsedExpe[prevIndex][nurseTask] = nurseCount;
		parsedExpe[prevIndex][foragerTask] = foragerCount;
		parsedExpe[prevIndex][giveFoodTask] = giveFoodCount;
		parsedExpe[prevIndex][askFoodTask] = askFoodCount;
		parsedExpe[prevIndex][otherTask] = otherCount + giveFoodCount + askFoodCount;
		parsedExpe[prevIndex][meanEOKey] = meanEO / (otherCount + foragerCount + nurseCount);
		parsedExpe[prevIndex][meanHJKey] = meanHJ / (otherCount + foragerCount + nurseCount);
		parsedExpe[prevIndex]["Total"] = nurseCount + foragerCount + otherCount;
	
	allParsedExpes[fileImportantName] = parsedExpe;
	
	#print(parsedExpe)
	#print("\n")
	
	#ploting that expe
	#plt.subplot(2,2, fileIndex-1, title=fileImportantName);
	#plt.plot(getTimeStepsListFromExpeDict(parsedExpe), getListFromExpeDict(nurseTask, parsedExpe), label=nurseTask);
	#plt.plot(getTimeStepsListFromExpeDict(parsedExpe), getListFromExpeDict(foragerTask, parsedExpe), label=foragerTask);
	#plt.plot(getTimeStepsListFromExpeDict(parsedExpe), getListFromExpeDict("Other", parsedExpe), label="Other");
	#plt.plot(getTimeStepsListFromExpeDict(parsedExpe), getListFromExpeDict(larvaTask, parsedExpe), label=larvaTask);
	
	#if(fileIndex-1 == 1):
		#plt.legend()
		
#print(allParsedExpes)	
#plt.show()


plt.figure(0, figsize=(25,15))
index = 1;
for key in allParsedExpes:
	expe = allParsedExpes[key];
	#         row, col
	plt.subplot(3,3, index, title=key);
	plt.plot(getTimeStepsListFromExpeDict(expe), getListFromExpeDict(nurseTask, expe), label=nurseTask);
	plt.plot(getTimeStepsListFromExpeDict(expe), getListFromExpeDict(foragerTask, expe), label=foragerTask);
	plt.plot(getTimeStepsListFromExpeDict(expe), getListFromExpeDict(otherTask, expe), label=otherTask);
	plt.plot(getTimeStepsListFromExpeDict(expe), getListFromExpeDict(larvaTask, expe), label=larvaTask);
	plt.plot(getTimeStepsListFromExpeDict(expe), getListFromExpeDict(meanEOKey, expe), label=meanEOKey);
	
	#print(getListFromExpeDict("Total", expe))
	#print("\n")
	
	if(index == 1):
		plt.legend()
	
	index += 1
#plt.show()
plt.savefig("Tasks.png");
index = 1;

plt.figure(1, figsize=(25,15))
for key in allParsedExpes:
	expe = allParsedExpes[key];
	
	plt.subplot(3,3, index, title=key);
	plt.plot(getTimeStepsListFromExpeDict(expe), getListFromExpeDict(meanHJKey, expe), label=meanHJKey);
	
	index += 1
		
#plt.show()		
plt.savefig("HJ.png");		

'''
index = 1;
plt.figure(2, figsize=(25,15))
for key in allParsedExpes:
	expe = allParsedExpes[key];
	
	plt.subplot(4,1, index, title=key);
	plt.plot(getTimeStepsListFromExpeDict(expe), getListFromExpeDict(askFoodTask, expe), label=askFoodTask);
	plt.plot(getTimeStepsListFromExpeDict(expe), getListFromExpeDict(giveFoodTask, expe), label=giveFoodTask);
	
	if(index == 1):
		plt.legend()
	
	index += 1
		
plt.savefig("Food.png");		
		
'''	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		