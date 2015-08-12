package net.bhpachulski.tddcriteria.model.Eclemma;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bhpachulski
 */
@JacksonXmlRootElement(localName = "class")
public class Classs {
    
    @JacksonXmlProperty
    private String name;
    
    @JacksonXmlProperty(localName = "method")
    private Methodd methodd;
    
    @JacksonXmlProperty(localName = "counter")
    private List<Counter> counter = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Methodd getMethodd() {
        return methodd;
    }

    public void setMethodd(Methodd methodd) {
        this.methodd = methodd;
    }

    public List<Counter> getCounter() {
        return counter;
    }

    public void setCounter(List<Counter> counter) {
        this.counter = counter;
    }

    
    
}
