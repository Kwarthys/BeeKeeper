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

def getFill(value, number):
	lalist = [];
	for i in range(number):
		lalist.append(value);
	return lalist;
	
	
keys = ["FeedLarvae","Foraging", "Other"];
def getDict():
	return {key: 0 for key in keys}


sumUpJobData = {}


path = '../expe/'

files = []
# r=root, d=directories, f = files
for r, d, f in os.walk(path):
	for file in f:
		if '.csv' in file:
			files.append(os.path.join(r, file))

for f in files:
	print(f)

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

	displaySize = 30;

	size = 0;


	print("|", end="")
	for i in range(displaySize-1):
		print("-", end="");
	print("|");
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

	for beeID in jobDataPerBee.keys():
		for task in jobDataPerBee[beeID].keys():
			jobDataPerBee[beeID][task] /= len(data)/100;


	initState = mode;
	initBees = str(len(jobDataPerBee));
	initLarvae = str(initLarvae);
	simuLength = str(len(data));
	smallTitle = initState + "_" + initBees + "_" + initLarvae;
	#smallTitle = "test";
	hugeTitle = initState + " init repartition, " + initBees + " bees for " + initLarvae + " larvae during " + simuLength + " timesteps. " + str(int(larvaCounts[-1]*10)/10) + "% larvae survived";

	sumUpJobData[smallTitle] = data;
	
	ylims = [0,110];

	generationOK = False;
	
	while(not generationOK):

		plt.figure(figsize=(9,7));
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

		plt.figure(figsize=(9,7));
		plt.suptitle(hugeTitle);
		plt.subplots_adjust(hspace=0.4)
		plt.subplots_adjust(wspace=0.1)
		plt.subplot(2,1,1, title='Colony');
		plt.plot(range(len(hjData)), hjData, label='bees mean HJTiters');
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
				plt.xlabel("timesteps");
				box = ax.get_position();
				ax.set_position([box.x0, box.y0,box.width, box.height]);
				plt.legend(handles=newHandles, labels=newLabels,loc='upper center', bbox_to_anchor=(0.5, -0.4), ncol=3);

			plt.plot(range(len(hjData)), hjDataPerBee[randomKeys[i]])

			if i != 0 and i != sampleSize/2:
				ax.get_yaxis().set_visible(False);
			else : plt.ylabel("HJTiter %");
			if(i<sampleSize/2):
				ax.get_xaxis().set_visible(False);


		plt.savefig(smallTitle + "HJ.png");
		#plt.show();
	
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
		


styles = ['-', '--', '-.', ':','custom']
index = 0;
print(sumUpJobData.keys());
plt.figure(figsize=(9,7));
plt.suptitle("Nurse Count for all experiments");
plt.xlabel("time-steps");
plt.ylabel("Number of larva feeding bee (%)");
for key in sumUpJobData.keys():
	if(styles[index] == 'custom'):
		plt.plot(range(len(sumUpJobData[key])),sumUpJobData[key] , label=key, linestyle='--', dashes=(1,5));
	else:
		plt.plot(range(len(sumUpJobData[key])), sumUpJobData[key], label=key,linestyle=styles[index]);
	index += 1;
plt.legend();
plt.savefig("summup.png");
#plt.show();
#test
