package net.bhpachulski.tddcriteria.model.analysis;

import javax.xml.bind.annotation.XmlTransient;
import net.bhpachulski.tddcriteria.model.Eclemma.Report;
import net.bhpachulski.tddcriteria.model.TestSuiteSession;

/**
 *
 * @author bhpachulski
 */
public class TDDCriteriaProjectSnapshot {
    
    @XmlTransient
    private TestSuiteSession jUnitSession;
    
    @XmlTransient
    private Report eclemmaSession;
    
    

    public TDDCriteriaProjectSnapshot() {
    }

    public TDDCriteriaProjectSnapshot(TestSuiteSession jUnitSession, Report eclemmaSession) {
        this.jUnitSession = jUnitSession;
        this.eclemmaSession = eclemmaSession;
    }
    
    public TestSuiteSession getjUnitSession() {
        return jUnitSession;
    }

    public void setjUnitSession(TestSuiteSession jUnitSession) {
        this.jUnitSession = jUnitSession;
    }

    public Report getEclemmaSession() {
        return eclemmaSession;
    }

    public void setEclemmaSession(Report eclemmaSession) {
        this.eclemmaSession = eclemmaSession;
    }
    
    
    
}
