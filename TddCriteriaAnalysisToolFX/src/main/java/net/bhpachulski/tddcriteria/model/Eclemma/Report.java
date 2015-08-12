package net.bhpachulski.tddcriteria.model.Eclemma;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bhpachulski
 */
public class Report {
    
    @JacksonXmlProperty
    private String name;
    
    private Group group;
    
    private SessionInfo sessioninfo;
    
    @JacksonXmlProperty(localName = "counter")
    private List<Counter> counter = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SessionInfo getSessioninfo() {
        return sessioninfo;
    }

    public void setSessioninfo(SessionInfo sessioninfo) {
        this.sessioninfo = sessioninfo;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Counter> getCounter() {
        return counter;
    }

    public void setCounter(List<Counter> counter) {
        this.counter = counter;
    }

    
}
