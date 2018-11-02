MAINCLASSNAME	= MusicRecs
SUBDIRS := $(wildcard */.)
FLAGS = -classpath ".:sqlite-jdbc-3.23.1.jar"

all:
	make clean
	make compile
	make run
	
compile:
	javac $(MAINCLASSNAME).java

run:
	java $(FLAGS) $(MAINCLASSNAME)

clean: 
	-@ rm *.class
	-@ for dir in $(SUBDIRS) ; do \
        rm $$dir/*.class ; \
    done
	clear