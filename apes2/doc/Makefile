FOP=fop.sh -d 
XSLTPROC=xsltproc
STYLESHEET=/opt/ipsquad/ipsdoc/fo/ipsdoc.xsl

PDFFILES=$(addsuffix .pdf, $(basename $(wildcard *.xml) $(wildcard *.axp)))

all: $(PDFFILES)

.SUFFIXES: .xml .pdf .ps .axp .fo
.PHONY: clean all

.xml.fo:
	$(XSLTPROC) -o $@ $(STYLESHEET) $<

.fo.ps:
	$(FOP) -fo $< -ps $@

.ps.pdf:
	ps2pdf14 $< $@

.axp.pdf:
	axpoint $< $@

clean:
	-rm -f *.pdf *.ps *.fo `find . -name "*~"` images/*.fig.bak
