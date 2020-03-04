import matplotlib.pyplot as plt
import csv
import numpy as np
import os

def column(matrix, i):
    return [row[i] for row in matrix];

def getJobName(job):
	if job == "Foraging" :
		return job;
	elif job == "FeedLarvae":
		return job;
	else:
		return "Other";

def getJobHatch(job):
	if job == "Foraging" :
		return '///////';
	elif job == "FeedLarvae":
		return '\\\\\\\\\\\\\\';
	elif job == "Other":
		return '';
		
def getDictWithsortedExpe(dict):
	sortedDict = {};
	for key in dict:
		rootKey = key[:-2]
		expeIndex = key.split("_")[-1]
	
		if rootKey not in sortedDict:
			sortedDict[rootKey] = [];
		sortedDict[rootKey].append(dict[key]);
		
	return sortedDict;
		
def getDictListsWithsortedExpeMean(dict):
	sortedDict = getDictWithsortedExpe(dict)
	
	for key in sortedDict:
		sortedDict[key] = tabMean(sortedDict[key]);
		
	return sortedDict;
		
def getDictValuesWithsortedExpeMean(dict):
	sortedDict = getDictWithsortedExpe(dict)
	print("sortedDict: " + str(sortedDict))
	for key in sortedDict:
		sortedDict[key] = moyenne(sortedDict[key]);
		
	return sortedDict;

def getFill(value, number):
	lalist = [];
	for i in range(number):
		lalist.append(value);
	return lalist;

#--------------- MATHS ----#

def tabMean(listOfTables):
	size = len(listOfTables);
	tabSize = len(listOfTables[0]);

	mean = [0] * tabSize;

	for tab in listOfTables:
		for i in range(tabSize):
			mean[i] += tab[i] / size;

	return mean;
		

def moyenne(tableau):
	return sum(tableau, 0.0) / len(tableau);

def variance(tableau):
	m=moyenne(tableau);
	return moyenne([(x-m)**2 for x in tableau]);

def ecartype(tableau):
	return variance(tableau)**0.5;

def tabErrorBar(listOfTables):
	ebars = [];
	for i in range(len(listOfTables[0])):
		tmpTab = []
		for tab in listOfTables:
			tmpTab.append(tab[i]);
		ebars.append(ecartype(tmpTab));
	return ebars;
	
#--------------------------#
	
keys = ["FeedLarvae","Foraging", "Other"];
def getDict():
	return {key: 0 for key in keys}

larvaeSurvivalSumUP = {}
sumUpJobData = {}
jobDatePerBeeTotal = {}
globalInteruptions = {}

GRAPHWITHMOT = True;

path = '../expe/'
if GRAPHWITHMOT:
	path = '../expeMot/'

files = []
# r=root, d=directories, f = files
for r, d, f in os.walk(path):
	for file in f:
		if '.csv' in file:
			files.append(os.path.join(r, file))
fileIndex = 1
for f in files:
	print(f + " " + str(fileIndex) + "/" + str(len(files)));
	fileIndex += 1
	fileName = f;

	mode = f.split("/")[-1];
	mode = mode.split("_")[0];
	print("detected mode : " + mode);

	data = [];
	jobDataPerBee = {};
	hjData = [];
	hjDataPerBee = {};
	larvaCounts = [];
	initLarvae = -1;
	larvae = 0;
	prevIndex = -1;
	foragerCount = 0;
	nurseCount = 0;
	otherJobCount = 0;
	hjAmount = 0;
	jobDurationPB = {};

	displaySize = len(files);

	size = 0;


	print("|", end="")
	for i in range(displaySize-1):
		print("-", end="");
	print("|");
	for i in range(fileIndex-1):
		print("|", end="");
	print("");
	print("|", end="");


	with open(fileName) as csv_file:
		csv_reader = csv.reader(csv_file, delimiter=',')
		for row in csv_reader:
			size += 1;

	with open(fileName) as csv_file:
		csv_reader = csv.reader(csv_file, delimiter=',')
		line_count = 0;
		for row in csv_reader:
			if line_count == 0:
				#print(f'Column names are {", ".join(row)}')
				line_count += 1;
			else:
				#print(f'\tturn {row[0]}, Bee ID {row[1]} was doing {row[2]} and had {row[3]} HJTiter.')
				index = int(row[0]);
				if(prevIndex != index):	
					if(prevIndex != -1):					
						total = (foragerCount + nurseCount + otherJobCount)/100;
						if initLarvae == -1: initLarvae = larvae;
						data.append([otherJobCount/total, nurseCount/total, foragerCount/total]);
						hjData.append(hjAmount/total);
						larvaCounts.append(larvae*100/initLarvae);
						
						foragerCount = 0;
						nurseCount = 0;
						otherJobCount = 0;
						hjAmount = 0;
						larvae = 0;
					prevIndex = index;
					
				beeID = int(row[1]);
				
				
				if(not beeID in hjDataPerBee):
					hjDataPerBee[beeID] = [];
					
				hjDataPerBee[beeID].append(int(float(row[3])*100));
				
				if(row[2] != "LarvaTask"):
					hjAmount += float(row[3]);
					if(not beeID in jobDataPerBee):
						jobDataPerBee[beeID] = getDict();
						
					if(row[2] in jobDataPerBee[beeID]):
						jobDataPerBee[beeID][row[2]] += 1;
					else:
						jobDataPerBee[beeID]['Other'] += 1;
				else:
					larvae += 1;
				
				if row[2] == "FeedLarvae":
					nurseCount += 1;
				elif row[2] == "Foraging" :
					foragerCount += 1;
				elif row[2] != "LarvaTask":
					otherJobCount +=1;

				#----- jobDurationPB -----#

				job = getJobName(row[2]);
				if beeID not in jobDurationPB:
					jobDurationPB[beeID] = [];
				elif len(jobDurationPB[beeID]) == 0:
					jobDurationPB[beeID].append({job : 1});
				elif job not in jobDurationPB[beeID][-1]:
					jobDurationPB[beeID].append({job : 1});
				else :
					jobDurationPB[beeID][-1][job] += 1;

				#-------------------------#

				line_count += 1
				if(line_count%(int(size/displaySize))==0):print("|", end="", flush=True)
				#print(row[0])
		print()
		print(f'Processed {line_count} lines.')
		
	total = (foragerCount + nurseCount + otherJobCount)/100;
	data.append([otherJobCount/total, nurseCount/total, foragerCount/total]);
	hjData.append(hjAmount/total);
	larvaCounts.append(larvae*100/initLarvae);
	
	#-------------------------------------------------------------------------#
	#-----------------------------READINGS ARE DONE---------------------------#
	#-------------------------------------------------------------------------#

	for beeID in jobDataPerBee.keys():
		for task in jobDataPerBee[beeID].keys():
			jobDataPerBee[beeID][task] /= len(data)/100;

	

	expeNumber =  f.split(".csv")[0].split("_")[-1]
	initState = mode;
	initBees = str(len(jobDataPerBee));
	initLarvae = str(initLarvae);
	simuLength = str(len(data));
	larvaeSurvival = int(larvaCounts[-1]*10)/10;
	smallTitle = initState + "_" + initBees + "_" + initLarvae + "_" + expeNumber;
	#smallTitle = "test";
	hugeTitle = initState + " initial distribution, " + initBees + " adult bees for " + initLarvae + " larvae during " + simuLength + " timesteps. " + str(larvaeSurvival) + "% larvae survived";

	sumUpJobData[smallTitle] = data;
	larvaeSurvivalSumUP[smallTitle] = larvaeSurvival;
	jobDatePerBeeTotal[smallTitle] = {"FeedLarvae" : moyenne(column(data,1)), "Foraging" : moyenne(column(data,2)), "Other" : moyenne(column(data,0))};
	
	interupts = 0;
	for beeID in jobDurationPB:
		interupts += len(jobDurationPB[beeID]);
	
	globalInteruptions[smallTitle] = interupts/len(jobDurationPB);
	
	
	ylims = [0,110];

	generationOK = False;
	
	tries = 0;
	
	while(not generationOK and tries < 4):
	
		tries += 1;

		plt.close('all');

		plt.figure(0, figsize=(9,7));
		#subplot(nrows, ncols, index)
		plt.subplots_adjust(hspace=0.5)
		plt.subplot(2,1,1, title='Colony work repartition');
		plt.suptitle(hugeTitle);
		#plt.plot(range(len(data)), column(data,0), label='otherCount')
		plt.plot(range(len(data)), column(data,1), label='nurseCount')
		plt.plot(range(len(data)), column(data,2), label='foragerCount')
		plt.legend()
		plt.ylabel("% of bees")
		plt.ylim(ylims);


		print("generating graphs");

		sampleSize = 6
		randomKeys = []
		beeIDs = list(jobDataPerBee.keys())

		while(len(randomKeys) != sampleSize):
			candidate = beeIDs[np.random.randint(low = 0, high = len(jobDataPerBee))];
			while(candidate in randomKeys):
				candidate = beeIDs[np.random.randint(low = 0, high = len(jobDataPerBee))];
			randomKeys.append(candidate);

		for i in range(sampleSize):
			ax = plt.subplot(4,3,i+7, title='bee' + str(randomKeys[i]))
			if(i == 0):
				plt.ylabel("Time spent%"); 
				
			# Create bars
			plt.bar([8000/4, 8000/2, 3*8000/4], jobDataPerBee[randomKeys[i]].values(), width = 8000/6)
			 
			# Create names on the x-axis
			plt.xticks([8000/4, 8000/2, 3*8000/4], keys)
			plt.ylim(ylims);

			if(i<sampleSize/2):
				ax.get_xaxis().set_visible(False);
			
			#plt.plot(range(len(hjData)), hjDataPerBee[randomKeys[i]], c='orange')

		plt.savefig(smallTitle + "Tasks.png");

		plt.figure(1, figsize=(9,7));
		plt.suptitle(hugeTitle);
		plt.subplots_adjust(hspace=0.4)
		plt.subplots_adjust(wspace=0.1)
		plt.subplot(2,1,1, title='Colony');
		plt.plot(range(len(hjData)), hjData, label='Adult bees mean JHTiters');
		plt.plot(range(len(larvaCounts)), larvaCounts, label='Larvae Count (% of init)');
		plt.legend()
		plt.ylabel("%")
		plt.ylim(ylims);

		for i in range(sampleSize):
			ax = plt.subplot(4,3,i+7, title='bee' + str(randomKeys[i]))
			plt.ylim([-30,110]);
			tt = 0;
			for b in jobDurationPB[randomKeys[i]]:
				theKey = list(b.keys())[0];
				theValue = list(b.values())[0];
				plt.bar(theValue + tt,-25, width=-theValue,align='edge', color='white',hatch = getJobHatch(theKey), label=theKey);
				tt += theValue;

			if(i==sampleSize-2):
				newHandles = [];
				newLabels = [];
				handles, labels = ax.get_legend_handles_labels();

				for li in range(len(labels)):
					l = labels[li];
					if l not in newLabels:
						newLabels.append(l);
						newHandles.append(handles[li]);
				
				plt.legend(handles=newHandles, labels=newLabels);

				generationOK = len(newLabels) == 3;
				plt.xlabel("times-teps");
				box = ax.get_position();
				ax.set_position([box.x0, box.y0,box.width, box.height]);
				plt.legend(handles=newHandles, labels=newLabels,loc='upper center', bbox_to_anchor=(0.5, -0.4), ncol=3);

			plt.plot(range(len(hjData)), hjDataPerBee[randomKeys[i]])

			if i != 0 and i != sampleSize/2:
				ax.get_yaxis().set_visible(False);
			else : plt.ylabel("JHTiter %");
			if(i<sampleSize/2):
				ax.get_xaxis().set_visible(False);


		plt.savefig(smallTitle + "HJ.png");
		#plt.show();
		plt.close('all')



		
		
		
		
#------------------------WORKS DIVISION----------------------#
jobDatePerBeeTotal = getDictWithsortedExpe(jobDatePerBeeTotal)
tmp = {};
for key in jobDatePerBeeTotal:
	laliste = {}
	tmp[key] = {"values" : {}, "errorBars" : {}}
	for taskDict in jobDatePerBeeTotal[key]:
		for taskKey in taskDict:
			if taskKey not in laliste: laliste[taskKey] = [];
			laliste[taskKey].append(taskDict[taskKey]);
	
	for taskKey in laliste:
		tmp[key]['values'][taskKey] = moyenne(laliste[taskKey])
		tmp[key]['errorBars'][taskKey] = ecartype(laliste[taskKey]);

jobDatePerBeeTotal = tmp

globalInteruptions = getDictValuesWithsortedExpeMean(globalInteruptions)

plt.figure(0,figsize=(11,7))
i = 1
for expeKey in jobDatePerBeeTotal:

	ax = plt.subplot(2,3,i, title=expeKey+" "+ str(int(globalInteruptions[expeKey]*10)/10) + " switches.")
	plt.ylim([-10,110])
	plt.bar(range(len(jobDatePerBeeTotal[expeKey]["values"])), list(jobDatePerBeeTotal[expeKey]["values"].values()), yerr=list(jobDatePerBeeTotal[expeKey]["errorBars"].values()))
	plt.xticks(range(len(jobDatePerBeeTotal[expeKey]["values"])), list(jobDatePerBeeTotal[expeKey]["values"].keys()))
	i=i+1
plt.savefig("LaborDivisionSummup.png");
#plt.show()





	
sumUpErrorBars = {}

keyManager = {}

for key in sumUpJobData.keys():
		sumUpJobData[key] = column(sumUpJobData[key],1)
		for i in range(len(sumUpJobData[key])):
			v = 0;
			n = 0;
			for k in range(i-50,i+50):
				if k < len(sumUpJobData[key]) and k > 0:
					v += sumUpJobData[key][k];
					n += 1;
			sumUpJobData[key][i] = v/n;

meanLarvaeSurvivalSummup = {}

for key in sumUpJobData.keys():
		larvaeSurvival = larvaeSurvivalSumUP[key]
		rootKey = key[:-2]
		expeIndex = key.split("_")[-1]
		#print("root:" + str(rootKey) + " expe:" + str(expeIndex)); 
	
		if rootKey not in keyManager:
			keyManager[rootKey] = [];
	
		if rootKey not in meanLarvaeSurvivalSummup:
			meanLarvaeSurvivalSummup[rootKey] = [];

		keyManager[rootKey].append(sumUpJobData[key]);
		meanLarvaeSurvivalSummup[rootKey].append(larvaeSurvival)

sumUpJobData = {}

tmp = {}

for key in meanLarvaeSurvivalSummup:
	tmp[key] = {"value":0, "error":0}
	tmp[key]["value"] = int(moyenne(meanLarvaeSurvivalSummup[key])*10)/10
	tmp[key]["error"] = int(ecartype(meanLarvaeSurvivalSummup[key])*10)/10

meanLarvaeSurvivalSummup =  tmp

for key in keyManager:
	sumUpJobData[key] = tabMean(keyManager[key])
	keyManager[key] = tabErrorBar(keyManager[key])


#------------------------Larvae DEATH------------------------#

larvaeDeathTitle = "Larvae deaths for all experiments";
if not GRAPHWITHMOT:
	larvaeDeathTitle += "\n(without our interruption mechanism)";

plt.close('all')
plt.figure(0,figsize=(5,5));
plt.suptitle(larvaeDeathTitle);
plt.ylabel("Larvae deaths (%)");
plt.ylim([0,50])
bi = 0
print(meanLarvaeSurvivalSummup)
for expeKey in meanLarvaeSurvivalSummup:
	plt.bar(bi, 100-meanLarvaeSurvivalSummup[expeKey]['value'], yerr=meanLarvaeSurvivalSummup[expeKey]['error'], color='red');
	bi += 1;
plt.xticks(range(len(meanLarvaeSurvivalSummup)), list(jobDatePerBeeTotal.keys()), rotation=15)
#plt.show()
plt.savefig("larvaeDeaths.png");

		
		
#------------------------SUMMUP GRAPH------------------------#
		
summupGraphTitle = "Nurse Counts for all experiments";
if not GRAPHWITHMOT:
	summupGraphTitle += "\n(without our interruption mechanism)";

styles = ['-', '--', '-.', ':','custom']
index = 0;
plt.figure(2,figsize=(5,5));
plt.suptitle(summupGraphTitle);
plt.xlabel("time-steps");
plt.ylabel("Number of larva feeding bee (%)");
plt.ylim([-10,110]);
for key in sumUpJobData.keys():
	leLabel = key;
	if(styles[index] == 'custom'):
		plt.errorbar(range(len(sumUpJobData[key])),sumUpJobData[key], yerr=keyManager[key], errorevery=100, label=leLabel, capthick=10, linestyle='--', dashes=(1,5));
	else:
		plt.errorbar(range(len(sumUpJobData[key])),sumUpJobData[key], yerr=keyManager[key], errorevery=100, label=leLabel, capthick=10, linestyle=styles[index]);


	index += 1;
plt.legend();
plt.savefig("summup.png");
#plt.show();
plt.close('all')
print('\a')
