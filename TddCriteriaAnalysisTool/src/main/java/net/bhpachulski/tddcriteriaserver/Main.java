package net.bhpachulski.tddcriteriaserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.IOException;
import net.bhpachulski.tddcriteriaserver.model.Eclemma.Report;
import net.bhpachulski.tddcriteriaserver.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteriaserver.model.TestSuiteSession;

/**
 *
 * @author bhpachulski
 */
public class Main {
    
    public static void main(String[] args) throws IOException {
        
        JacksonXmlModule module;
        module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        
        ObjectMapper xmlMapper;
        xmlMapper = new XmlMapper(module);
        
        File fProp = new File("/Users/bhpachulski/Documents/Projetos/GIT/EclemmaCriteriaTDD/TesteDoPlugin/bowlinggame/TDDBowlingGame/tddCriteria/tddCriteriaProjectProperties.xml");
        TDDCriteriaProjectProperties prop = xmlMapper.readValue(fProp, TDDCriteriaProjectProperties.class);
        
        File fJunit = new File("/Users/bhpachulski/Documents/Projetos/GIT/EclemmaCriteriaTDD/TesteDoPlugin/bowlinggame/TDDBowlingGame/tddCriteria/junitTrack/2015_7_1_09_35_52.xml");
        TestSuiteSession tss = xmlMapper.readValue(fJunit, TestSuiteSession.class);
        
        File fEclemma = new File("/Users/bhpachulski/Documents/Projetos/GIT/EclemmaCriteriaTDD/TesteDoPlugin/bowlinggame/TDDBowlingGame/tddCriteria/coverageTrack/2015_7_1_09_35_52.xml");
        Report rep = xmlMapper.readValue(fEclemma, Report.class);

    }
    
}
