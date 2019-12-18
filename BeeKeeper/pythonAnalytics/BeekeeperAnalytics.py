import matplotlib.pyplot as plt
import csv
import numpy as np

def column(matrix, i):
    return [row[i] for row in matrix]
	
	
keys = ["FeedLarvae","Foraging", "Other"];
def getDict():
	return {key: 0 for key in keys}

import csv

data = [];
jobDataPerBee = {};

prevIndex = 0
foragerCount = 0
nurseCount = 0
otherJobCount = 0

displaySize = 30;

size = 0

print("|", end="")
for i in range(displaySize-1):
	print("-", end="")
print("|")
print("|", end="")


with open('../tasks.txt') as csv_file:
	csv_reader = csv.reader(csv_file, delimiter=',')
	for row in csv_reader:
		size += 1;

with open('../tasks.txt') as csv_file:
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
				total = (foragerCount + nurseCount + otherJobCount)/100;
				if total == 0: total = 1;
				data.append([otherJobCount/total, nurseCount/total, foragerCount/total]);
				foragerCount = 0;
				nurseCount = 0;
				otherJobCount = 0;
				prevIndex = index;
				
			beeID = int(row[1]);
			
			if(row[2] != "LarvaTask"):
				if(not beeID in jobDataPerBee):
					jobDataPerBee[beeID] = getDict();
					
				if(row[2] in jobDataPerBee[beeID]):
					jobDataPerBee[beeID][row[2]] += 1/80; #8000 timesteps, into %of timesteps
				else:
					jobDataPerBee[beeID]['Other'] += 1/80;
			
			if row[2] == "FeedLarvae":
				nurseCount += 1;
			elif row[2] == "Foraging" :
				foragerCount += 1;
			else:
				otherJobCount +=1;

			line_count += 1
			if(line_count%(int(size/displaySize))==0):print("|", end="", flush=True)
			#print(row[0])
	print()
	print(f'Processed {line_count} lines.')

#subplot(nrows, ncols, index)
plt.subplots_adjust(hspace=0.5)
plt.subplot(2,1,1);
#plt.plot(range(len(data)), column(data,0), label='otherCount')
plt.plot(range(len(data)), column(data,1), label='nurseCount')
plt.plot(range(len(data)), column(data,2), label='foragerCount')
plt.legend()
plt.xlabel("timesteps")
plt.ylabel("% of bees")

sampleSize = 6
randomKeys = []
beeIDs = list(jobDataPerBee.keys())

while(len(randomKeys) != sampleSize):
	candidate = beeIDs[np.random.randint(low = 0, high = len(jobDataPerBee))];
	while(candidate in randomKeys):
		candidate = beeIDs[np.random.randint(low = 0, high = len(jobDataPerBee))];
	randomKeys.append(candidate);

for i in range(sampleSize):
	plt.subplot(4,3,i+7, title='bee' + str(randomKeys[i]))
	if(i == 0):
		plt.ylabel("% of time spent");
	plt.bar(keys, jobDataPerBee[randomKeys[i]].values())


plt.show()
