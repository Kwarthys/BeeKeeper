import sys
import csv

if(len(sys.argv) >= 2):
	
	fileName = sys.argv[1];
	
	f = open(fileName.split(".csv")[0] + "_UpdatedScore.csv", 'w', newline='');
	writer = csv.writer(f);

	print("opening " + fileName);
	size = 0;
	with open(fileName) as csv_file:
			csv_reader = csv.reader(csv_file, delimiter=',')
			for row in csv_reader:
				if(len(row) > 1):
					newRow = "";
					if(size==0):
						newRow = row;
						newRow.append("mixedScore");
					else:
						score = int(float(row[5])/100 * int(row[6])/1000);
						newRow = row;
						newRow.append(str(score));
												
					writer.writerow(newRow);
					
					size += 1;
				
	print("read " + str(size) + " lines.");
	