package net.bhpachulski.tddcriteria.model.Eclemma;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 *
 * @author bhpachulski
 */
public class Line {

    @JacksonXmlProperty
    private int nr;
    
    @JacksonXmlProperty
    private int mi;
    
    @JacksonXmlProperty
    private int ci;
    
    @JacksonXmlProperty
    private int mb;
    
    @JacksonXmlProperty
    private int cb;    

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public int getMi() {
        return mi;
    }

    public void setMi(int mi) {
        this.mi = mi;
    }

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
        this.ci = ci;
    }

    public int getMb() {
        return mb;
    }

    public void setMb(int mb) {
        this.mb = mb;
    }

    public int getCb() {
        return cb;
    }

    public void setCb(int cb) {
        this.cb = cb;
    }
    
    
    
}
