import matplotlib.pyplot as plt
import csv

def column(matrix, i):
    return [row[i] for row in matrix]

import csv

data = [];

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
		size += 1

with open('../tasks.txt') as csv_file:
	csv_reader = csv.reader(csv_file, delimiter=',')
	line_count = 0
	for row in csv_reader:
		if line_count == 0:
			#print(f'Column names are {", ".join(row)}')
			line_count += 1
		else:
			#print(f'\tturn {row[0]}, Bee ID {row[1]} was doing {row[2]} and had {row[3]} HJTiter.')
			index = int(row[0])
			if(prevIndex != index):	
				total = (foragerCount + nurseCount + otherJobCount)/100;
				if total == 0: total = 1
				data.append([otherJobCount/total, nurseCount/total, foragerCount/total]);
				foragerCount = 0
				nurseCount = 0
				otherJobCount = 0
				prevIndex = index
			
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

#plt.plot(range(len(data)), column(data,0), label='otherCount')
plt.plot(range(len(data)), column(data,1), label='nurseCount')
plt.plot(range(len(data)), column(data,2), label='foragerCount')
plt.legend()
plt.xlabel("timesteps")
plt.ylabel("% of bees")
plt.show()
