import csv;
import os;
from graphviz import Digraph;


def fromListOfExpeToMean(listOfExpe):
	print(listOfExpe)
	sortedList = {};
	for expe in listOfExpe:
		expeElements = expe.split("_");
		rootKey = expeElements[0] + "_" + expeElements[1] + "_" + expeElements[2] + "_" + expeElements[3];
		
		if rootKey not in sortedList:
				sortedList[rootKey] = [];
				sortedList[rootKey].append(listOfExpe[expe]);
			
	print(sortedList)
	for expe in sortedList:
		sortedList[expe] = mean(sortedList[expe]);
	print(sortedList)
	return sortedList;

def mean(tab):
	return sum(tab, 0.0) / len(tab);

folderName = "expeNewNoMot";						#-----------------------------------------------------------------------#
path = '../' + folderName + '/';	
	
CSVHeader = False;
	
files = []
# r=root, d=directories, f = files
for r, d, f in os.walk(path):
	for file in f:
		if '.csv' in file:
			files.append(os.path.join(r, file))
			
fileIndex = 1

jobSwitchesMatrix = {}; 

for f in files:
	print(f + " " + str(fileIndex) + "/" + str(len(files)));
	fileIndex += 1;
	fileName = f;

	root = f.split("/")[-1].split(".csv")[0];
	modelState = root.split("_")[0];
	mode = root.split("_")[1];
	nbBees = root.split("_")[2];
	nbLarvae = root.split("_")[3];
	simuLenght = root.split("_")[4];
	expeNumber = root.split("_")[5];
	
	expeName = modelState+"_"+mode+"_"+nbBees+"_"+nbLarvae+"_"+expeNumber;
	
	jobSwitchesMatrix[expeName] = {};
	

	expeModSubtitle = "";
	GRAPHWITHMOT = True;
	GRAPHWITHPHYSIO = True;
	if("NoMot" in modelState):
		GRAPHWITHMOT=False;
	if("NoPhysio" in modelState):
		GRAPHWITHPHYSIO=False;
	
	if(GRAPHWITHMOT and GRAPHWITHPHYSIO):
		expeModSubtitle = "";
	elif(GRAPHWITHMOT and not GRAPHWITHPHYSIO):
		expeModSubtitle = "\nExperiment without pheromome effects.";
	elif(not GRAPHWITHMOT and GRAPHWITHPHYSIO):
		expeModSubtitle = "\nExperiment without our interruption.";
	elif(not GRAPHWITHMOT and not GRAPHWITHPHYSIO):
		expeModSubtitle = "\nExperiment without our interruption nor pheromones.";
	
	jobDataPerBee = {};
	
	with open(fileName) as csv_file:
		csv_reader = csv.reader(csv_file, delimiter=',')
		line_count = 0;
		for row in csv_reader:
			if line_count == 0:
				#print(f'Column names are {", ".join(row)}')#HEADER
				line_count += 1;
			else:
				#print(f'\tturn {row[0]}, Bee ID {row[1]} was doing {row[2]} and had {row[3]} HJTiter.')
				job = row[2];
				beeID = row[1];
				if(job != "LarvaTask"):
					if(not row[1] in jobDataPerBee):
						jobDataPerBee[beeID] = [];
						jobDataPerBee[beeID].append({job:1});
					else:
						if(row[2] not in jobDataPerBee[beeID][-1]):		#jobdataperbee[beeID] = [{task1 : 200}, {task3:150} ..]
							jobDataPerBee[beeID].append({job:1});
						else:
							jobDataPerBee[beeID][-1][job] += 1;
	
	for beeID in jobDataPerBee:
		for i in range(len(jobDataPerBee[beeID])-1):
			switchName = list(jobDataPerBee[beeID][i].keys())[-1] + "_" + list(jobDataPerBee[beeID][i+1].keys())[-1];
			
			if(switchName not in jobSwitchesMatrix[expeName]):
				jobSwitchesMatrix[expeName][switchName] = 1.0/len(jobDataPerBee);
			else:
				jobSwitchesMatrix[expeName][switchName] += 1.0/len(jobDataPerBee);

			
jobSwitchesMatrix = fromListOfExpeToMean(jobSwitchesMatrix);
for expe in jobSwitchesMatrix:
	dot = Digraph(comment='comment', engine='circo');
	
	for switchName in jobSwitchesMatrix[expe]:
		value = float(int(jobSwitchesMatrix[expe][switchName]*10)/10);
		if(value != 0):
			dot.edge(switchName.split("_")[0], switchName.split("_")[1], label=str(value));
	dot.render('test'+expe+'.gv', view=True);
		