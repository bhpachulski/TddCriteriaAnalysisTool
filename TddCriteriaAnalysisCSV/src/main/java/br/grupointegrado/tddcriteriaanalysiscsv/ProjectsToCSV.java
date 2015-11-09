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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import net.bhpachulski.tddcriteria.model.Eclemma.Report;
import net.bhpachulski.tddcriteria.model.Eclemma.Type;
import net.bhpachulski.tddcriteria.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteria.model.TestSuiteSession;
import net.bhpachulski.tddcriteria.model.analysis.TDDCriteriaProjectSnapshot;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author bhpachulski
 */
public class ProjectsToCSV {

    private TDDCriteriaProjectProperties prop;

    private ObjectMapper xmlMapper;
    private JacksonXmlModule module;

    private File fProp;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    SimpleDateFormat sdfShow = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static final String propFilePath = "/tddCriteria/tddCriteriaProjectProperties.xml";
    private static final String jUnitFolderPath = "/tddCriteria/junitTrack/";
    private static final String EclemmaFolderPath = "/tddCriteria/coverageTrack/";
    private static final String tddStageTrackPath = "tddStageTrack.txt";

    public static void main(String[] args) throws IOException, ParseException {

        SimpleDateFormat sdfShow = new SimpleDateFormat("HH:mm:ss");

        ProjectsToCSV p = new ProjectsToCSV();
        String retorno = p.getCSVDadosResumido(new File("/Users/bhpachulski/Documents/Projetos/GIT/experimentos/"));

        System.out.println("**************************************************************************************");
        System.out.println("**************************************************************************************");
        System.out.println("**************************************************************************************");

        System.out.println(retorno);
    }

    public String getCSVDadosResumido(File file) throws IOException, ParseException {

        String projectFolder;
        StringBuilder fileContent = new StringBuilder();

        if (file.isDirectory()) {

            projectFolder = file.getAbsolutePath();

            Map<String, Map<Date, TDDCriteriaProjectSnapshot>> studentsTimeLine = readProjectByRootPath(projectFolder);

            fileContent.append("RA; HORÁRIO INICIO RED; HORÁRIO FIM RED; TEMPO RED; QNT CASOS DE TESTE RED; QNT CASOS DE TESTE PASSANDO RED; QNT CASOS DE TESTE FALHANDO RED; COBERTURA DE CLASS RED; COBERTURA DE METODO RED; COBERTURA DE LINHAS RED; COBERTURA DE INSTRUCAOES RED; COBERTURA DE RAMOS RED; " + 
                    "HORÁRIO INICIO GREEN; HORÁRIO FIM GREEN; TEMPO GREEN; QNT CASOS DE TESTE GREEN; QNT CASOS DE TESTE PASSANDO GREEN; QNT CASOS DE TESTE FALHANDO GREEN; COBERTURA DE CLASS GREEN; COBERTURA DE METODO GREEN; COBERTURA DE LINHAS GREEN; COBERTURA DE INSTRUCAOES GREEN; COBERTURA DE RAMOS GREEN; " +
                    "HORÁRIO INICIO REFACTOR; HORÁRIO FIM REFACTOR; TEMPO REFACTOR; QNT CASOS DE TESTE REFACTOR; QNT CASOS DE TESTE PASSANDO REFACTOR; QNT CASOS DE TESTE FALHANDO REFACTOR; COBERTURA DE CLASS REFACTOR; COBERTURA DE METODO REFACTOR; COBERTURA DE LINHAS REFACTOR; COBERTURA DE INSTRUCAOES REFACTOR; COBERTURA DE RAMOS REFACTOR; \n");

            for (Map.Entry<String, Map<Date, TDDCriteriaProjectSnapshot>> studentTimeLineES : studentsTimeLine.entrySet()) {

                TDDCriteriaProjectProperties propAluno = studentTimeLineES.getValue().entrySet().stream().map(Map.Entry::getValue).findFirst().get().getCriteriaProjectProperties();

                Map<Date, TDDCriteriaProjectSnapshot> firstIteration = new TreeMap<>();
                Map<Date, TDDCriteriaProjectSnapshot> secondIteration = new TreeMap<>();
                Map<Date, TDDCriteriaProjectSnapshot> thirdteration = new TreeMap<>();
                Map<Date, TDDCriteriaProjectSnapshot> fourthIteration = new TreeMap<>();
                Map<Date, TDDCriteriaProjectSnapshot> fifithIteration = new TreeMap<>();
                Map<Date, TDDCriteriaProjectSnapshot> sixthIteration = new TreeMap<>();

                studentTimeLineES.getValue().entrySet().stream().filter(e -> (new DateTime(e.getKey()).isBefore(new DateTime(propAluno.getFirstIteration())))
                        || (new DateTime(e.getKey()).isEqual(new DateTime(propAluno.getFirstIteration()))))
                        .forEach(e -> {

                            firstIteration.put(e.getKey(), e.getValue());

                        });

                studentTimeLineES.getValue().entrySet().stream().filter(e -> (new DateTime(e.getKey()).isBefore(new DateTime(propAluno.getSecondIteration())))
                        || (new DateTime(e.getKey()).isEqual(new DateTime(propAluno.getSecondIteration()))))
                        .forEach(e -> {

                            secondIteration.put(e.getKey(), e.getValue());

                        });

                studentTimeLineES.getValue().entrySet().stream().filter(e -> (new DateTime(e.getKey()).isBefore(new DateTime(propAluno.getThirdIteration())))
                        || (new DateTime(e.getKey()).isEqual(new DateTime(propAluno.getThirdIteration()))))
                        .forEach(e -> {

                            thirdteration.put(e.getKey(), e.getValue());

                        });

                studentTimeLineES.getValue().entrySet().stream().filter(e -> (new DateTime(e.getKey()).isBefore(new DateTime(propAluno.getFourthIteration())))
                        || (new DateTime(e.getKey()).isEqual(new DateTime(propAluno.getFourthIteration()))))
                        .forEach(e -> {

                            fourthIteration.put(e.getKey(), e.getValue());

                        });

                studentTimeLineES.getValue().entrySet().stream().filter(e -> (new DateTime(e.getKey()).isBefore(new DateTime(propAluno.getFifthIteration())))
                        || (new DateTime(e.getKey()).isEqual(new DateTime(propAluno.getFifthIteration()))))
                        .forEach(e -> {

                            fifithIteration.put(e.getKey(), e.getValue());

                        });

                studentTimeLineES.getValue().entrySet().stream().filter(e -> (new DateTime(e.getKey()).isBefore(new DateTime(propAluno.getSixthIteration())))
                        || (new DateTime(e.getKey()).isEqual(new DateTime(propAluno.getSixthIteration()))))
                        .forEach(e -> {

                            sixthIteration.put(e.getKey(), e.getValue());

                        });
                
                fileContent.append("'" + propAluno.getCurrentStudent().getId() + "'; '" + propAluno.getCurrentStudent().getName() + "';");

                fileContent.append(getIterationValues(firstIteration, "RED"));
                fileContent.append(getIterationValues(firstIteration, "GREEN"));
                fileContent.append(getIterationValues(firstIteration, "REFACTOR"));

                fileContent.append(getIterationValues(secondIteration, "RED"));
                fileContent.append(getIterationValues(secondIteration, "GREEN"));
                fileContent.append(getIterationValues(secondIteration, "REFACTOR"));

                fileContent.append(getIterationValues(thirdteration, "RED"));
                fileContent.append(getIterationValues(thirdteration, "GREEN"));
                fileContent.append(getIterationValues(thirdteration, "REFACTOR"));

                fileContent.append(getIterationValues(fourthIteration, "RED"));
                fileContent.append(getIterationValues(fourthIteration, "GREEN"));
                fileContent.append(getIterationValues(fourthIteration, "REFACTOR"));

                fileContent.append(getIterationValues(fifithIteration, "RED"));
                fileContent.append(getIterationValues(fifithIteration, "GREEN"));
                fileContent.append(getIterationValues(fifithIteration, "REFACTOR"));

                fileContent.append(getIterationValues(sixthIteration, "RED"));
                fileContent.append(getIterationValues(sixthIteration, "GREEN"));
                fileContent.append(getIterationValues(sixthIteration, "REFACTOR"));

                fileContent.append("\n");
            }

        } else {
//            System.out.println("SHOW ERROR MESSAGE !");
        }

        return fileContent.toString();

    }

    public String getCSVDadosCompletos(File file) throws IOException, ParseException {

        String projectFolder;
        StringBuilder fileContent = new StringBuilder();

        if (file.isDirectory()) {

            projectFolder = file.getAbsolutePath();

            Map<String, Map<Date, TDDCriteriaProjectSnapshot>> studentsTimeLine = readProjectByRootPath(projectFolder);

            fileContent.append("RA; HORÁRIO; TDD STAGE; QNT. CASOS DE TESTE; PASSANDO; FALHANDO; CLASS; METHOD; LINE; INSTRUCTION; BRANCH; \n");

            for (Map.Entry<String, Map<Date, TDDCriteriaProjectSnapshot>> studentTimeLineES : studentsTimeLine.entrySet()) {

                for (Map.Entry<Date, TDDCriteriaProjectSnapshot> studentTimeLine : studentTimeLineES.getValue().entrySet()) {

                    if (!studentTimeLine.getValue().getTddStage().isEmpty()) {

                        //RA
                        fileContent.append(studentTimeLineES.getKey());
                        fileContent.append("; ");

                        //HORA
                        fileContent.append(sdfShow.format(studentTimeLine.getKey()));
                        fileContent.append("; ");

                        //TDD STAGE
                        fileContent.append(studentTimeLine.getValue().getTddStage());
                        fileContent.append("; ");

                        if (studentTimeLine.getValue().getjUnitSession() != null) {

                            studentTimeLine.getValue().getjUnitSession().setTestCases(ImmutableSet.copyOf(studentTimeLine.getValue().getjUnitSession().getTestCases()).asList());

                            //JUNIT
                            //Qnt Casos de Teste 
                            fileContent.append(studentTimeLine.getValue().getjUnitSession().getTestCases().size());
                            fileContent.append("; ");

                            //Qnt Casos de Teste PASSANDO
                            fileContent.append(studentTimeLine.getValue().getjUnitSession().getTestCases().stream().filter(t -> !t.isFailed()).count());
                            fileContent.append("; ");

                            //Qnt Casos de Teste FALHANDO
                            fileContent.append(studentTimeLine.getValue().getjUnitSession().getTestCases().stream().filter(t -> t.isFailed()).count());
                            fileContent.append("; ");
                        } else {
                            fileContent.append("SJUT; SJUT; SJUT;");
                        }

                        if (studentTimeLine.getValue().getEclemmaSession() != null) {
                            studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.CLASS).collect(Collectors.toList()).stream().forEach((counter) -> {
                                fileContent.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                                fileContent.append("; ");
                            });

                            studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.METHOD).collect(Collectors.toList()).stream().forEach((counter) -> {
                                fileContent.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                                fileContent.append("; ");
                            });

                            studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.LINE).collect(Collectors.toList()).stream().forEach((counter) -> {
                                fileContent.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                                fileContent.append("; ");
                            });

                            studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.INSTRUCTION).collect(Collectors.toList()).stream().forEach((counter) -> {
                                fileContent.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                                fileContent.append("; ");
                            });

                            studentTimeLine.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.BRANCH).collect(Collectors.toList()).stream().forEach((counter) -> {
                                fileContent.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                                fileContent.append("; ");
                            });
                        } else {
                            fileContent.append("SEC; SEC; SEC; SEC; SEC;");
                        }

                        fileContent.append("\n");
                    }
                }
            }

        } else {
            System.out.println("SHOW ERROR MESSAGE !");
        }

        return fileContent.toString();
    }

    public Map<String, Map<Date, TDDCriteriaProjectSnapshot>> readProjectByRootPath(String rootPath) throws IOException, ParseException {
        Map<String, Map<Date, TDDCriteriaProjectSnapshot>> studentsTimeline = new HashMap<>();

        for (File folder : Arrays.asList(new File(rootPath).listFiles())) {
            if (folder.isDirectory() && new File(folder.getAbsolutePath() + "/" + folder.getName().split("-")[0].trim()).exists()) {

                System.out.println(" ----------------------------------- ");
                System.out.println(" * " + folder.getName());
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

        fProp = new File(projectFolder + propFilePath);
        prop = xmlMapper.readValue(fProp, TDDCriteriaProjectProperties.class);

        List<String> tddStages = java.nio.file.Files.readAllLines(Paths.get(projectFolder + EclemmaFolderPath + tddStageTrackPath));
        for (String lnTddStages : tddStages) {

            if (!lnTddStages.trim().isEmpty()) {

                TDDCriteriaProjectSnapshot snapshotPutTDDStage = new TDDCriteriaProjectSnapshot();
                snapshotPutTDDStage.setTddStage(lnTddStages.split(":")[1]);

                projectTimeLine.put(sdf.parse(lnTddStages.substring(0, 19)), snapshotPutTDDStage);

                projectTimeLine.get(sdf.parse(lnTddStages.substring(0, 19))).setCriteriaProjectProperties(prop);

            }
        }

        List<File> eclemmaFiles = Arrays.asList(new File(projectFolder + EclemmaFolderPath).listFiles());
        for (File eclemmaFile : eclemmaFiles) {

            if (eclemmaFile.getName().endsWith("xml")) {

                try {
                    Report rep = xmlMapper.readValue(eclemmaFile, Report.class);
                    projectTimeLine.get(sdf.parse(Files.getNameWithoutExtension(eclemmaFile.getName()).substring(0, 19))).setEclemmaSession(rep);
                } catch (Exception e) {
//                    System.out.println("Eclemma File Discartado");
                }
            }

        }
        System.out.println("   + " + "Eclemma files: " + eclemmaFiles.size());

        List<File> jUnitFiles = Arrays.asList(new File(projectFolder + jUnitFolderPath).listFiles());
        for (File jUnitFile : jUnitFiles) {
            if (jUnitFile.getName().endsWith("xml")) {

                try {
                    TestSuiteSession tss = xmlMapper.readValue(jUnitFile, TestSuiteSession.class);
                    projectTimeLine.get(sdf.parse(Files.getNameWithoutExtension(jUnitFile.getName()).substring(0, 19))).setjUnitSession(tss);
                } catch (Exception e) {
//                    System.out.println("JUnit File Discartado");
                }
            }
        }
        System.out.println("   + " + "JUnit files: " + jUnitFiles.size());

        return projectTimeLine;
    }

    public static double regraDeTres(double total, double especificos) {
        return 100 * especificos / (double) total;
    }

    public String getIterationValues(Map<Date, TDDCriteriaProjectSnapshot> iteration, String tddStage) {
        
        StringBuilder studentLine = new StringBuilder();

        if (iteration.entrySet().stream().filter(e -> e.getValue().getTddStage().trim().equals(tddStage)).count() > 0) {

            Entry<Date, TDDCriteriaProjectSnapshot> first = iteration.entrySet().stream().filter(e -> e.getValue().getTddStage().trim().equals(tddStage)).findFirst().get();
            Entry<Date, TDDCriteriaProjectSnapshot> last = iteration.entrySet().stream().filter(e -> e.getValue().getTddStage().trim().equals(tddStage)).reduce((a, b) -> b).get();

            studentLine.append("'" + sdfShow.format(first.getKey()) + "';");
            studentLine.append("'" + sdfShow.format(last.getKey())+ "';");

            Interval diferencaHoras = new Interval(new DateTime(first.getKey()), new DateTime(last.getKey()));
            studentLine.append(diferencaHoras.toPeriod().getMinutes());
            
            if (last.getValue().getjUnitSession() != null) {
                //Qnt Casos de Teste 
                studentLine.append(last.getValue().getjUnitSession().getTestCases().size());
                studentLine.append(";");

                //Qnt Casos de Teste PASSANDO
                studentLine.append(last.getValue().getjUnitSession().getTestCases().stream().filter(t -> !t.isFailed()).count());
                studentLine.append(";");

                //Qnt Casos de Teste FALHANDO
                studentLine.append(last.getValue().getjUnitSession().getTestCases().stream().filter(t -> t.isFailed()).count());
                studentLine.append(";");
            } else {
                studentLine.append("0;0;0;");
            }
            
            if (last.getValue().getEclemmaSession() != null) {
                last.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.CLASS).collect(Collectors.toList()).stream().forEach((counter) -> {
                    studentLine.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                    studentLine.append(";");
                });

                last.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.METHOD).collect(Collectors.toList()).stream().forEach((counter) -> {
                    studentLine.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                    studentLine.append(";");
                });

                last.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.LINE).collect(Collectors.toList()).stream().forEach((counter) -> {
                    studentLine.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                    studentLine.append(";");
                });

                last.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.INSTRUCTION).collect(Collectors.toList()).stream().forEach((counter) -> {
                    studentLine.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                    studentLine.append(";");
                });

                last.getValue().getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.BRANCH).collect(Collectors.toList()).stream().forEach((counter) -> {
                    studentLine.append(regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered()));
                    studentLine.append(";");
                });
            } else {
                studentLine.append("0;0;0;0;0;");
            }

        } else {
            //System.out.println("Não foi encontrado um estágio " + tddStage);
        }

        return studentLine.toString();
    }

}
