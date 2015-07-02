package net.bhpachulski.tddcriteriaserver.model.Eclemma;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bhpachulski
 */
public class SourceFile {
    
    @JacksonXmlProperty
    private String name;
    
    @JacksonXmlProperty(localName = "line")
    private List<Line> line = new ArrayList<>();
    
    @JacksonXmlProperty(localName = "counter")
    private List<Counter> counter = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Line> getLine() {
        return line;
    }

    public void setLine(List<Line> line) {
        this.line = line;
    }

    public List<Counter> getCounter() {
        return counter;
    }

    public void setCounter(List<Counter> counter) {
        this.counter = counter;
    }

}
