import codecs

f = codecs.open("Ma bibliothèque.bib", "r", encoding='utf-8')
fw = codecs.open("bib.bib","w", encoding='utf-8')

forbiddenStarters = ["doi", "issn", "isbn", "urldate", "langid", "location", "number", "url", "file", "abstract", "note"]

for x in f:
	starter = x.split(' = ')[0];
	print(starter)
	forbidden = False;
	
	for word in forbiddenStarters :
		if word in starter :
			forbidden = True;
			
	print(forbidden)
	if(not forbidden):
		fw.write(x);