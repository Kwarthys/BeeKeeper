import matplotlib.pyplot as plt
import csv
import numpy as np
import os
import re


def getJobHatch(job):
	if job == "Foraging" :
		return '/';
	elif job == "FeedLarvae":
		return '\\';
	else:
		return '';

larvaTask = "LarvaTask";
nurseTask = "FeedLarvae";
foragerTask = "Foraging";
giveFoodTask = "GiveFood";
askFoodTask = "AskingFood";
otherTask = "Other";


folderName = "expe/bees";
path = '../' + folderName + '/';

files = []
for r, d, f in os.walk(path):
	for file in f:
		if '.csv' in file:
			files.append(os.path.join(r, file))
fileIndex = 1


bees = {}

for f in files:
	print(f + " " + str(fileIndex) + "/" + str(len(files)));
	
	beeName = f.split("/bees")[1].split(".")[0].split("_")[7];
	
	bees[beeName] = {}
	bees[beeName]["EO"] = []
	bees[beeName]["HJ"] = []
	bees[beeName]["Tasks"] = []
	
	fileIndex += 1
	
	with open(f) as csv_file:
		csv_reader = csv.reader(csv_file, delimiter=',')
		line_count = 0;
		
		lastTask = "";
		
		for row in csv_reader:
			
			if(len(row)>2 and row[2] != ""):		
				beeTask = row[0]
				beeHJ = float(row[1])
				beeEO = float(row[2])
				
				bees[beeName]["EO"].append(beeEO);
				bees[beeName]["HJ"].append(beeHJ);
				
				if(lastTask == beeTask):
					bees[beeName]["Tasks"][-1][1] += 1
				else:
					#print(bees[beeName]["Tasks"])
					bees[beeName]["Tasks"].append([beeTask,1])
					
				lastTask = beeTask

	#print(bees[beeName]["Tasks"])
#print(bees)

plt.figure(0, figsize=(25,15))
index = 1
for beeName in bees:
	#         row, col
	ax = plt.subplot(3,8, index, title=beeName);
	
	incr = 0;
	for tuple in bees[beeName]["Tasks"]:
		#plt.bar(theValue + tt,-25, width=-theValue,align='edge', color='white',hatch = getJobHatch(theKey), label=theKey);
		plt.bar(tuple[1] + incr, 1, width=-tuple[1], align='edge', color='white',hatch = getJobHatch(tuple[0]), label=tuple[0]);
		
		incr += tuple[1]
	
	
	size = len(bees[beeName]["EO"])
	plt.plot(range(size), bees[beeName]["EO"], label="EO")
	plt.plot(range(size), bees[beeName]["HJ"], label="HJ")
	
	newHandles = [];
	newLabels = [];
	handles, labels = ax.get_legend_handles_labels();

	for li in range(len(labels)):
		l = labels[li];
		if l not in newLabels:
			newLabels.append(l);
			newHandles.append(handles[li]);
	
	plt.legend(handles=newHandles, labels=newLabels);
	
	index+=1
plt.savefig("beeLives.png")
	
	
	
	
	
	
	
	
	
	
	
	