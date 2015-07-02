package net.bhpachulski.tddcriteriaserver.model.analysis;

import javax.xml.bind.annotation.XmlTransient;
import net.bhpachulski.tddcriteriaserver.model.Eclemma.Report;
import net.bhpachulski.tddcriteriaserver.model.TestSuiteSession;

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
