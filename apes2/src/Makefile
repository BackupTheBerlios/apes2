TARGET=apes
MANIFEST=Apes.mf
MAINCLASS=org.ipsquad.apes.ApesMain
APPLETFILE=ApesApplet.java

MAINFILES=org/ipsquad/apes/ApesMain.java
JARLIBS=jgraph.jar JSX.jar
JUNITLIB=junit.jar
ABBOTLIBS=junit-3.7.jar abbot.jar

APES_DOC= org.ipsquad.utils org.ipsquad.apes org.ipsquad.apes.ui \
org.ipsquad.apes.ui.actions org.ipsquad.apes.ui.tools \
org.ipsquad.apes.adapters org.ipsquad.apes.spem \
org.ipsquad.apes.spem.basic org.ipsquad.apes.spem.core \
org.ipsquad.apes.spem.diagram org.ipsquad.apes.spem.modelmanagement \
org.ipsquad.apes.spem.process.components org.ipsquad.apes.spem.process.structure \
org.ipsquad.apes.processing

CLASSPATH=.:$(subst  jar ,jar:,$(addprefix ../lib/, $(JARLIBS)))
ABBOT_CP=$(subst  jar ,jar:,$(addprefix ../lib/, $(ABBOTLIBS)))

JAVAFILES=$(shell find java/ -name "*.java")
UNITFILES=$(shell find units/ -name "*.java")


.PHONY: all run jar clean resources cleanall units docs tests

all: build resources

build: $(JAVAFILES) web/$(APPLETFILE)
	-mkdir build
	-mkdir build/web
	touch build
	(cd java; javac -classpath $(CLASSPATH) -d ../build $(MAINFILES))
	(cd web; javac -d ../build/web $(APPLETFILE))

resources:
	cp -rf resources build/
	cp -rf icons build/
	cp -rf web/* build/web/
	rm -rf build/web/*.java
	rm -rf `find build/ -name CVS`
	(cd build/web/; find . -type f | cut -c3- | grep -v '^list$$' > list)

$(MANIFEST):
	echo "Manifest-Version: 1.0" > $(MANIFEST)
	echo "Main-Class: $(MAINCLASS)" >> $(MANIFEST)
	echo "" >> $(MANIFEST)
	

run: build resources
	echo "Debut :"
	echo $(CLASSPATH)
	echo "Fin :"
	(cd build; java -classpath $(CLASSPATH) $(MAINCLASS))

jar: $(TARGET).jar

$(TARGET).jar: $(MANIFEST) build resources
	for file in $(addprefix ../lib/, $(JARLIBS)) ; do (cd build; jar xf $$file) done
	(cd build; jar cfm ../$(TARGET).jar ../$(MANIFEST) *)


units: build resources $(UNITFILES)
	(cd units; javac -classpath $(CLASSPATH):../lib/$(JUNITLIB):../build `find . -name "*.java"`)

rununits: units
	(cd units; java -classpath $(CLASSPATH):../lib/$(JUNITLIB):../build junit.swingui.TestRunner)

tests: $(TARGET).jar ./tests/ApesTest.java
	(cd tests; javac -classpath $(ABBOT_CP) ApesTest.java)

runtests: tests
	(cd tests ; java -classpath .:$(ABBOT_CP) ApesTest)

docs:
	doxygen
	-mkdir docs/javadoc
	(cd java; javadoc -author -version -use -private -classpath $(CLASSPATH) -d ../docs/javadoc $(APES_DOC))

clean:
	-rm -rf `find . -name "*~"`
	-rm -rf build
	-rm -rf `find units -name "*.class"`
	-rm -rf `find tests -name "*.class"`
	-rm -rf $(MANIFEST)
	-rm -rf docs
