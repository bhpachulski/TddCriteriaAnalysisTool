package net.bhpachulski.tddcriteriaserver.model.Eclemma;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Date;

/**
 *
 * @author bhpachulski
 */
public class SessionInfo {
    
    @JacksonXmlProperty
    private String id;
    
    @JacksonXmlProperty
    private Date start;
    
    @JacksonXmlProperty
    private Date dump;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getDump() {
        return dump;
    }

    public void setDump(Date dump) {
        this.dump = dump;
    }
    
    
    
}
