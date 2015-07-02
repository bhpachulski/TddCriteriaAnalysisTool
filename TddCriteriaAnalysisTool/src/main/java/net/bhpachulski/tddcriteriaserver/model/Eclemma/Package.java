package net.bhpachulski.tddcriteriaserver.model.Eclemma;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bhpachulski
 */
public class Package {
    
    @JacksonXmlProperty
    private String name;
    
    @JacksonXmlProperty(localName = "class")
    private List<Classs> classs = new ArrayList<>();
    
    @JacksonXmlProperty
    private SourceFile sourcefile;
    
    @JacksonXmlProperty(localName = "counter")
    private List<Counter> counter = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Classs> getClasss() {
        return classs;
    }

    public void setClasss(List<Classs> classs) {
        this.classs = classs;
    }

    public SourceFile getSourcefile() {
        return sourcefile;
    }

    public void setSourcefile(SourceFile sourcefile) {
        this.sourcefile = sourcefile;
    }

    public List<Counter> getCounter() {
        return counter;
    }

    public void setCounter(List<Counter> counter) {
        this.counter = counter;
    }

}
