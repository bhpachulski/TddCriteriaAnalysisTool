package br.grupointegrado.tddcriteriaanalysiscsv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import net.bhpachulski.tddcriteria.model.Eclemma.Report;
import net.bhpachulski.tddcriteria.model.Eclemma.Type;
import net.bhpachulski.tddcriteria.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteria.model.TestSuiteSession;
import net.bhpachulski.tddcriteria.model.analysis.TDDCriteriaProjectSnapshot;

/**
 *
 * @author bhpachulski
 */
public class ProjectsToCSV {
    
    private TDDCriteriaProjectProperties prop;
    
    private ObjectMapper xmlMapper;
    private JacksonXmlModule module;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_M_d_HH_mm_s");
    
    private static final String propFilePath = "/tddCriteria/tddCriteriaProjectProperties.xml";
    private static final String jUnitFolderPath = "/tddCriteria/junitTrack/";
    private static final String EclemmaFolderPath = "/tddCriteria/coverageTrack/";
    private static final String tddStageTrackPath = "tddStageTrack.txt";
    
    
    public static void main(String[] args) throws IOException, ParseException {
        
//        "dd/MM/yyyy HH:mm:ss"
        SimpleDateFormat sdfShow = new SimpleDateFormat("HH:mm:ss");
        
        ProjectsToCSV p = new ProjectsToCSV();
        Map<String, Map<Date, TDDCriteriaProjectSnapshot>> studentsTimeLine = p.readProjectByRootPath("/Users/bhpachulski/Documents/Projetos/GIT/experimentos/");
        
        System.out.println("RA; HORÁRIO; TDD STAGE; QNT. CASOS DE TESTE; PASSANDO; FALHANDO; CLASS; METHOD; LINE; INSTRUCTION; BRANCH;");
        
        for (Map.Entry<String, Map<Date, TDDCriteriaProjectSnapshot>> studentTimeLineES : studentsTimeLine.entrySet()) {
            
            for (Map.Entry<Date, TDDCriteriaProjectSnapshot> studentTimeLine : studentTimeLineES.getValue().entrySet()) {
               
                if (!studentTimeLine.getValue().getTddStage().isEmpty()){
                    
                    //RA
                    System.out.print(studentTimeLineES.getKey());
                    System.out.print("; ");
                    
                    //HORA
                    System.out.print(sdfShow.format(studentTimeLine.getKey()));                
                    System.out.print("; ");
                    
                    //TDD STAGE
                    System.out.print(studentTimeLine.getValue().getTddStage());
                    System.out.print("; ");
                 
                    if (studentTimeLine.getValue().getjUnitSession() != null) {
                        
                        studentTimeLine.getValue().getjUnitSession().setTestCases(ImmutableSet.copyOf(studentTimeLine.getValue().getjUnitSession().getTestCases()).asList());
                        
                        //JUNIT
                        //Qnt Casos de Teste 
                        System.out.print(studentTimeLine.getValue().getjUnitSession().getTestCases().size());
                        System.out.print("; ");

                        //Qnt Casos de Teste PASSANDO
                        System.out.print(studentTimeLine.getValue().getjUnitSession().getTestCases().stream().filter(t -> !t.isFailed()).count());
                        System.out.print("; ");

                        //Qnt Casos de Teste FALHANDO
                        System.out.print(studentTimeLine.getValue().getjUnitSession().getTestCases().stream().filter(t -> t.isFailed()).count());
                        System.out.print("; ");
                    } else {
                        System.out.print("SJUT; SJUT; SJUT;");
                    }
                    
                    if (studentTimeLine.getValue().getEclemmaSession() != null) {
                        studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.CLASS).collect(Collectors.toList()).stream().forEach((counter) -> {                        
                            System.out.print(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                            System.out.print("; ");
                        });
                        
                        studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.METHOD).collect(Collectors.toList()).stream().forEach((counter) -> {                        
                            System.out.print(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                            System.out.print("; ");
                        });
                        
                        studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.LINE).collect(Collectors.toList()).stream().forEach((counter) -> {                        
                            System.out.print(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                            System.out.print("; ");
                        });
                        
                        studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.INSTRUCTION).collect(Collectors.toList()).stream().forEach((counter) -> {                        
                            System.out.print(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                            System.out.print("; ");
                        });
                        
                        studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.BRANCH).collect(Collectors.toList()).stream().forEach((counter) -> {                        
                            System.out.print(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                            System.out.print("; ");
                        });
                    } else {
                        System.out.print("SEC; SEC; SEC; SEC; SEC;");
                    }
                
                    System.out.println("");
                }
            }
        }
        
    }
    
    public Map<String, Map<Date, TDDCriteriaProjectSnapshot>> readProjectByRootPath (String rootPath) throws IOException, ParseException {
        Map<String, Map<Date, TDDCriteriaProjectSnapshot>> studentsTimeline = new HashMap<>();
        
        for (File folder : Arrays.asList(new File(rootPath).listFiles())) {
            if (folder.isDirectory() && new File(folder.getAbsolutePath() + "/" + folder.getName().split("-")[0].trim()).exists()) {
                
                System.out.println(folder.getName());
                Map<Date, TDDCriteriaProjectSnapshot> timeline = readProject(folder.getAbsolutePath() + "/" + folder.getName().split("-")[0].trim());
                
                studentsTimeline.put(folder.getName().split("-")[0].trim(), timeline);                
            }
        }
        
        return studentsTimeline;
    }
    
    public Map<Date, TDDCriteriaProjectSnapshot> readProject(String projectFolder) throws IOException, ParseException {
        
        module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        xmlMapper = new XmlMapper(module);


        Map<Date, TDDCriteriaProjectSnapshot> projectTimeLine = new TreeMap<>();

        File fProp = new File(projectFolder + propFilePath);
        prop = xmlMapper.readValue(fProp, TDDCriteriaProjectProperties.class);
        
        List<File> eclemmaFiles = Arrays.asList(new File(projectFolder + EclemmaFolderPath).listFiles());        
        for (File eclemmaFile : eclemmaFiles) {

            if (!eclemmaFile.getName().startsWith("tddStageTrack")) {
                Report rep = xmlMapper.readValue(eclemmaFile, Report.class);
                
                TDDCriteriaProjectSnapshot snapshotPutEclemma = new TDDCriteriaProjectSnapshot();
                snapshotPutEclemma.setEclemmaSession(rep);
                
                projectTimeLine.put(sdf.parse(Files.getNameWithoutExtension(eclemmaFile.getName()).substring(0, 18)), snapshotPutEclemma);     
            }
            
        }
        System.out.println("Eclemma files: " + eclemmaFiles.size());

        List<File> jUnitFiles = Arrays.asList(new File(projectFolder + jUnitFolderPath).listFiles());
        for (File jUnitFile : jUnitFiles) {
            TestSuiteSession tss = xmlMapper.readValue(jUnitFile, TestSuiteSession.class);

            try {
                projectTimeLine.get(sdf.parse(Files.getNameWithoutExtension(jUnitFile.getName()).substring(0, 18))).setjUnitSession(tss);                            
            } catch (NullPointerException e) {
                TDDCriteriaProjectSnapshot snapshotPutJunit = new TDDCriteriaProjectSnapshot();
                snapshotPutJunit.setjUnitSession(tss);
                
                projectTimeLine.put(sdf.parse(Files.getNameWithoutExtension(jUnitFile.getName()).substring(0, 18)), snapshotPutJunit);
            }
            
        }
        System.out.println("JUnit files: " + jUnitFiles.size());        
        
        List<String> tddStages = java.nio.file.Files.readAllLines(Paths.get(projectFolder + EclemmaFolderPath + tddStageTrackPath));
        for (String lnTddStages : tddStages) {
            if (!lnTddStages.trim().isEmpty() && projectTimeLine.get(sdf.parse(lnTddStages.substring(0, 18))) != null){
                projectTimeLine.get(sdf.parse(lnTddStages.substring(0, 18))).setTddStage(lnTddStages.split(":")[1]);
            }
        }
        
        return projectTimeLine;
    }
    
    public static double regraDeTres(double total, double especificos) {
        return 100 * especificos / (double) total;
    }
    
}
