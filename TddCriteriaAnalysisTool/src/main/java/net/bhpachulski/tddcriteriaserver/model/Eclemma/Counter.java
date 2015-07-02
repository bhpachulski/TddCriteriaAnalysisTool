package net.bhpachulski.tddcriteriaserver.model.Eclemma;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 *
 * @author bhpachulski
 */
public class Counter {

    @JacksonXmlProperty
    private Type type;
    
    @JacksonXmlProperty
    private int missed;
    
    @JacksonXmlProperty
    private int covered;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getMissed() {
        return missed;
    }

    public void setMissed(int missed) {
        this.missed = missed;
    }

    public int getCovered() {
        return covered;
    }

    public void setCovered(int covered) {
        this.covered = covered;
    }
}
