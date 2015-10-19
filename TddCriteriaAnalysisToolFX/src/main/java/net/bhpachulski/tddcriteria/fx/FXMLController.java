package net.bhpachulski.tddcriteria.fx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import net.bhpachulski.tddcriteria.model.Eclemma.Report;
import net.bhpachulski.tddcriteria.model.Eclemma.Type;
import net.bhpachulski.tddcriteria.model.TDDCriteriaProjectProperties;
import net.bhpachulski.tddcriteria.model.TestSuiteSession;
import net.bhpachulski.tddcriteria.model.analysis.TDDCriteriaProjectSnapshot;

public class FXMLController implements Initializable {
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_M_d_HH_mm");
    SimpleDateFormat sdfShow = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private LineChartWithMarkers<String, Number> lineChart;
    private LineChartWithMarkers<String, Number> lineChartCoverage;
    
    private XYChart.Data<String, Number> testVerticalMarkerFirstIteration;
    private XYChart.Data<String, Number> testVerticalMarkerSecondIteration;
    private XYChart.Data<String, Number> testVerticalMarkerThirdIteration;
    
    private XYChart.Data<String, Number> coverageVerticalMarkerFirstIteration;
    private XYChart.Data<String, Number> coverageVerticalMarkerSecondIteration;
    private XYChart.Data<String, Number> coverageVerticalMarkerThirdIteration;
    
    private ChangeListener<Date> cbListenerIteracao1;
    private ChangeListener<Date> cbListenerIteracao2;
    private ChangeListener<Date> cbListenerIteracao3;
    
    private ObjectMapper xmlMapper;
    private JacksonXmlModule module;
    
    private File fProp;
    private TDDCriteriaProjectProperties prop;
    
    private StringConverter<Date> dateConverter;
    
    private static final String propFilePath = "/tddCriteria/tddCriteriaProjectProperties.xml";
    private static final String jUnitFolderPath = "/tddCriteria/junitTrack/";
    private static final String EclemmaFolderPath = "/tddCriteria/coverageTrack/";

    @FXML
    private Button btnFolder;

    @FXML
    private AnchorPane pane;

    @FXML
    private CheckBox checkCT;

    @FXML
    private CheckBox checkCTPassando;

    @FXML
    private CheckBox checkCTFalhando;

    @FXML
    private CheckBox checkInstruction;

    @FXML
    private CheckBox checkBranch;

    @FXML
    private CheckBox checkLine;

    @FXML
    private CheckBox checkComplexity;

    @FXML
    private CheckBox checkMethod;

    @FXML
    private CheckBox checkClass;

    @FXML
    private ComboBox<Date> cbIteracao1;

    @FXML
    private ComboBox<Date> cbIteracao2;

    @FXML
    private ComboBox<Date> cbIteracao3;

    private DirectoryChooser folderChooser = new DirectoryChooser();

    private String projectFolder = "/Users/bhpachulski/Documents/Projetos/GIT/EclemmaCriteriaTDD/TDDCriteriaBowlingGame/tddcriteriabowlinggame";

    private XYChart.Series lnQntCasosDeTeste = new XYChart.Series();
    private XYChart.Series lnQntCasosDeTestePassando = new XYChart.Series();
    private XYChart.Series lnQntCasosDeTesteFalhando = new XYChart.Series();
    private XYChart.Series lnInstruction = new XYChart.Series();
    private XYChart.Series lnBranch = new XYChart.Series();
    private XYChart.Series lnLine = new XYChart.Series();
    private XYChart.Series lnComplexity = new XYChart.Series();
    private XYChart.Series lnMethod = new XYChart.Series();
    private XYChart.Series lnClass = new XYChart.Series();

    @FXML
    private void exibirInformacoesAction(ActionEvent event) throws IOException, ParseException {
        lineChart.setTitle("Casos de teste");
        lineChartCoverage.setTitle("Cobertura");

        limparGraficoAction();
        initChecks();
        initComboIteracao();
        lerArquivos();
    }

    @FXML
    private void buscarDiretorioAction(ActionEvent event) throws IOException {
        this.configureChooser(folderChooser);
        File file = folderChooser.showDialog(pane.getScene().getWindow());
        if (file.isDirectory()) {
            projectFolder = file.getAbsolutePath();
        } else {
            System.out.println("SHOW ERROR MESSAGE !");
        }
    }

    @FXML
    private void limparGraficoAction() {
        lineChart.getData().clear();
        lineChartCoverage.getData().clear();

        lnQntCasosDeTeste = new XYChart.Series();
        lnQntCasosDeTestePassando = new XYChart.Series();
        lnQntCasosDeTesteFalhando = new XYChart.Series();
        lnInstruction = new XYChart.Series();
        lnBranch = new XYChart.Series();
        lnLine = new XYChart.Series();
//        lnComplexity = new XYChart.Series();
        lnMethod = new XYChart.Series();
        lnClass = new XYChart.Series();

        lineChart.removeAllVerticalValueMarkers();
        lineChartCoverage.removeAllVerticalValueMarkers();
        
        initComboIteracao();
        initChecks ();
    }
    
    public void initChecks () {
        checkCT.setSelected(true);
        checkCTPassando.setSelected(true);
        checkCTFalhando.setSelected(true);
        checkInstruction.setSelected(true);
        checkBranch.setSelected(true);
        checkLine.setSelected(true);
        checkComplexity.setSelected(true);
        checkMethod.setSelected(true);
        checkClass.setSelected(true);
        
        checkComplexity.setVisible(false);
    }

    @FXML
    private void checkCTAction(ActionEvent event) {
        if (checkCT.isSelected()) {
            lineChart.getData().add(lnQntCasosDeTeste);
        } else {
            lineChart.getData().remove(lnQntCasosDeTeste);
        }
    }

    @FXML
    private void checkCTPassandoAction(ActionEvent event) {
        if (checkCTPassando.isSelected()) {
            lineChart.getData().add(lnQntCasosDeTestePassando);
        } else {
            lineChart.getData().remove(lnQntCasosDeTestePassando);
        }
    }

    @FXML
    private void checkCTFalhandoAction(ActionEvent event) {
        if (checkCTFalhando.isSelected()) {
            lineChart.getData().add(lnQntCasosDeTesteFalhando);
        } else {
            lineChart.getData().remove(lnQntCasosDeTesteFalhando);
        }
    }

    @FXML
    private void checkCoverageInstruction(ActionEvent event) {
        if (checkInstruction.isSelected()) {
            lineChartCoverage.getData().add(lnInstruction);
        } else {
            lineChartCoverage.getData().remove(lnInstruction);
        }
    }

    @FXML
    private void checkCoverageBranch(ActionEvent event) {
        if (checkBranch.isSelected()) {
            lineChartCoverage.getData().add(lnBranch);
        } else {
            lineChartCoverage.getData().remove(lnBranch);
        }
    }

    @FXML
    private void checkCoverageLine(ActionEvent event) {
        if (checkLine.isSelected()) {
            lineChartCoverage.getData().add(lnLine);
        } else {
            lineChartCoverage.getData().remove(lnLine);
        }
    }

    @FXML
    private void checkCoverageComplexity(ActionEvent event) {
        if (checkComplexity.isSelected()) {
            lineChartCoverage.getData().add(lnComplexity);
        } else {
            lineChartCoverage.getData().remove(lnComplexity);
        }
    }

    @FXML
    private void checkCoverageMethod(ActionEvent event) {
        if (checkMethod.isSelected()) {
            lineChartCoverage.getData().add(lnMethod);
        } else {
            lineChartCoverage.getData().remove(lnMethod);
        }
    }

    @FXML
    private void checkCoverageClass(ActionEvent event) {
        if (checkClass.isSelected()) {
            lineChartCoverage.getData().add(lnClass);
        } else {
            lineChartCoverage.getData().remove(lnClass);
        }
    }
    
    @FXML
    private void savePropertiesFile () throws IOException {
        saveUpdatedXmlPropertiesFile();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        xmlMapper = new XmlMapper(module);

        lineChart = new LineChartWithMarkers<>(new CategoryAxis(), new NumberAxis());
        lineChart.setLayoutX(0.0);
        lineChart.setLayoutY(5.0);

        lineChart.setPrefWidth(1000.0);
        lineChart.setPrefHeight(300.0);
        lineChart.autosize();

        pane.getChildren().add(lineChart);

        lineChartCoverage = new LineChartWithMarkers<>(new CategoryAxis(), new NumberAxis());
        lineChartCoverage.setLayoutX(5.0);
        lineChartCoverage.setLayoutY(305.0);

        lineChartCoverage.setPrefWidth(1000.0);
        lineChartCoverage.setPrefHeight(300.0);
        lineChartCoverage.autosize();

        pane.getChildren().add(lineChartCoverage);
        
        dateConverter = new StringConverter<Date>() {

            @Override
            public String toString(Date object) {
                return sdfShow.format(object);
            }

            @Override
            public Date fromString(String string) {
                try {
                    return sdfShow.parse(string);
                } catch (ParseException ex) {
                    return null;
                }
            }
        };
        
        cbListenerIteracao1 = new ChangeListener<Date>() {

            @Override
            public void changed(ObservableValue<? extends Date> observable, Date oldValue, Date newValue) {                
                lineChart.removeHorizontalValueMarker(testVerticalMarkerFirstIteration);
                lineChartCoverage.removeHorizontalValueMarker(coverageVerticalMarkerFirstIteration);
                
                prop.setFirstIteration(newValue);
                testVerticalMarkerFirstIteration = new XYChart.Data<>(sdfShow.format(prop.getFirstIteration()), 5);
                coverageVerticalMarkerFirstIteration = new XYChart.Data<>(sdfShow.format(prop.getFirstIteration()), 5);
                lineChart.addVerticalValueMarker(testVerticalMarkerFirstIteration);
                lineChartCoverage.addVerticalValueMarker(coverageVerticalMarkerFirstIteration);
            }
            
        };
        
        cbListenerIteracao2 = new ChangeListener<Date>() {

            @Override
            public void changed(ObservableValue<? extends Date> observable, Date oldValue, Date newValue) {                
                lineChart.removeHorizontalValueMarker(testVerticalMarkerSecondIteration);
                lineChartCoverage.removeHorizontalValueMarker(coverageVerticalMarkerSecondIteration);
                
                prop.setSecondIteration(newValue);
                testVerticalMarkerSecondIteration = new XYChart.Data<>(sdfShow.format(prop.getSecondIteration()), 5);
                coverageVerticalMarkerSecondIteration = new XYChart.Data<>(sdfShow.format(prop.getSecondIteration()), 5);
                lineChart.addVerticalValueMarker(testVerticalMarkerSecondIteration);
                lineChartCoverage.addVerticalValueMarker(coverageVerticalMarkerSecondIteration);
            }
            
        };
        
        cbListenerIteracao3 = new ChangeListener<Date>() {

            @Override
            public void changed(ObservableValue<? extends Date> observable, Date oldValue, Date newValue) {                
                lineChart.removeHorizontalValueMarker(testVerticalMarkerThirdIteration);
                lineChartCoverage.removeHorizontalValueMarker(coverageVerticalMarkerThirdIteration);
                
                prop.setThirdIteration(newValue);
                testVerticalMarkerThirdIteration = new XYChart.Data<>(sdfShow.format(prop.getThirdIteration()), 5);
                coverageVerticalMarkerThirdIteration = new XYChart.Data<>(sdfShow.format(prop.getThirdIteration()), 5);
                lineChart.addVerticalValueMarker(testVerticalMarkerThirdIteration);
                lineChartCoverage.addVerticalValueMarker(coverageVerticalMarkerThirdIteration);
            }
            
        };

        initComboIteracao();
        
    }
    
    private void initComboIteracao() {
        
        cbIteracao1.getSelectionModel().selectedItemProperty().removeListener(cbListenerIteracao1);
        cbIteracao2.getSelectionModel().selectedItemProperty().removeListener(cbListenerIteracao2);
        cbIteracao3.getSelectionModel().selectedItemProperty().removeListener(cbListenerIteracao3);
        
        cbIteracao1.setDisable(true);
        cbIteracao2.setDisable(true);
        cbIteracao3.setDisable(true);
        
        cbIteracao1.setConverter(dateConverter);
        cbIteracao2.setConverter(dateConverter);
        cbIteracao3.setConverter(dateConverter);
        
        cbIteracao1.getItems().clear();
        cbIteracao2.getItems().clear();
        cbIteracao3.getItems().clear();
    }

    public void lerArquivos() throws IOException, ParseException {

        Map<Date, TDDCriteriaProjectSnapshot> projectTimeLine = new TreeMap<>();

        fProp = new File(projectFolder + propFilePath);
        
        prop = xmlMapper.readValue(fProp, TDDCriteriaProjectProperties.class);

        List<File> jUnitFiles = Arrays.asList(new File(projectFolder + jUnitFolderPath).listFiles());
        for (File jUnitFile : jUnitFiles) {
            TestSuiteSession tss = xmlMapper.readValue(jUnitFile, TestSuiteSession.class);

            TDDCriteriaProjectSnapshot snapshotPutJunit = new TDDCriteriaProjectSnapshot();
            snapshotPutJunit.setjUnitSession(tss);

            projectTimeLine.put(sdf.parse(Files.getNameWithoutExtension(jUnitFile.getName()).substring(0, 15)), snapshotPutJunit);
        }

        List<File> eclemmaFiles = Arrays.asList(new File(projectFolder + EclemmaFolderPath).listFiles());
        for (File eclemmaFile : eclemmaFiles) {
            Report rep = xmlMapper.readValue(eclemmaFile, Report.class);

            try {
                projectTimeLine.get(sdf.parse(Files.getNameWithoutExtension(eclemmaFile.getName()).substring(0, 15))).setEclemmaSession(rep);
            } catch (NullPointerException e) {
            }
        }

        lnQntCasosDeTeste.setName("Qnt. Casos de Teste");
        lnQntCasosDeTestePassando.setName("Qnt. Casos de Teste Passando");
        lnQntCasosDeTesteFalhando.setName("Qnt. Casos de Teste Falhando");
        
        lnInstruction.setName("Instruction Coverage");
        lnBranch.setName("Branch Coverage");
        lnLine.setName("Line Coverage");
        lnComplexity.setName("Complexity Coverage");
        lnMethod.setName("Method Coverage");
        lnClass.setName("Class Coverage");

        for (Map.Entry<Date, TDDCriteriaProjectSnapshot> entrySet : projectTimeLine.entrySet()) {
            Date key = entrySet.getKey();
            TDDCriteriaProjectSnapshot value = entrySet.getValue();

            //qnt casos de teste
            lnQntCasosDeTeste.getData().add(new XYChart.Data(sdfShow.format(key), value.getjUnitSession().getTestCases().size()));

            //qnt casos de teste passando
            lnQntCasosDeTestePassando.getData().add(new XYChart.Data(sdfShow.format(key), value.getjUnitSession().getTestCases().stream().filter(t -> t.getFailDetail() == null).count()));

            //qnt casos de teste falhando
            lnQntCasosDeTesteFalhando.getData().add(new XYChart.Data(sdfShow.format(key), value.getjUnitSession().getTestCases().stream().filter(t -> t.getFailDetail() != null).count()));

            if (value.getEclemmaSession() != null) {

                value.getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.INSTRUCTION).collect(Collectors.toList()).stream().forEach((counter) -> {
                    lnInstruction.getData().add(new XYChart.Data(sdfShow.format(key), this.regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered())));
                });

                value.getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.BRANCH).collect(Collectors.toList()).stream().forEach((counter) -> {
                    lnBranch.getData().add(new XYChart.Data(sdfShow.format(key), this.regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered())));
                });

                value.getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.LINE).collect(Collectors.toList()).stream().forEach((counter) -> {
                    lnLine.getData().add(new XYChart.Data(sdfShow.format(key), this.regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered())));
                });

//                value.getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.COMPLEXITY).collect(Collectors.toList()).stream().forEach((counter) -> {
//                    lnComplexity.getData().add(new XYChart.Data(sdfShow.format(key), this.regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered())));
//                });

                value.getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.METHOD).collect(Collectors.toList()).stream().forEach((counter) -> {
                    lnMethod.getData().add(new XYChart.Data(sdfShow.format(key), this.regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered())));
                });

                value.getEclemmaSession().getCounter().stream().filter(t -> t.getType() == Type.CLASS).collect(Collectors.toList()).stream().forEach((counter) -> {
                    lnClass.getData().add(new XYChart.Data(sdfShow.format(key), this.regraDeTres(counter.getMissed() + counter.getCovered(), counter.getCovered())));
                });

            }
        }

        lineChart.getData().clear();
        lineChart.getData().addAll(lnQntCasosDeTeste, lnQntCasosDeTestePassando, lnQntCasosDeTesteFalhando);
        lineChart.setCursor(Cursor.CROSSHAIR);

        lineChartCoverage.getData().clear();
        lineChartCoverage.getData().addAll(lnInstruction, lnBranch, lnLine, lnMethod, lnClass);
        lineChartCoverage.setCursor(Cursor.CROSSHAIR);

        testVerticalMarkerFirstIteration = new XYChart.Data<>(sdfShow.format(prop.getFirstIteration()), 5);
        testVerticalMarkerSecondIteration = new XYChart.Data<>(sdfShow.format(prop.getSecondIteration()), 5);
        testVerticalMarkerThirdIteration = new XYChart.Data<>(sdfShow.format(prop.getThirdIteration()), 5);
        
        coverageVerticalMarkerFirstIteration = new XYChart.Data<>(sdfShow.format(prop.getFirstIteration()), 5);
        coverageVerticalMarkerSecondIteration = new XYChart.Data<>(sdfShow.format(prop.getSecondIteration()), 5);
        coverageVerticalMarkerThirdIteration = new XYChart.Data<>(sdfShow.format(prop.getThirdIteration()), 5);
        
        lineChart.addVerticalValueMarker(testVerticalMarkerFirstIteration);
        lineChart.addVerticalValueMarker(testVerticalMarkerSecondIteration);
        lineChart.addVerticalValueMarker(testVerticalMarkerThirdIteration);
        
        lineChartCoverage.addVerticalValueMarker(coverageVerticalMarkerFirstIteration);
        lineChartCoverage.addVerticalValueMarker(coverageVerticalMarkerSecondIteration);
        lineChartCoverage.addVerticalValueMarker(coverageVerticalMarkerThirdIteration);

        cbIteracao1.getItems().addAll(projectTimeLine.keySet());
        cbIteracao2.getItems().addAll(projectTimeLine.keySet());
        cbIteracao3.getItems().addAll(projectTimeLine.keySet());
        
        cbIteracao1.getSelectionModel().select(prop.getFirstIteration());
        cbIteracao2.getSelectionModel().select(prop.getSecondIteration());
        cbIteracao3.getSelectionModel().select(prop.getThirdIteration());
        
        cbIteracao1.setDisable(false);
        cbIteracao2.setDisable(false);
        cbIteracao3.setDisable(false);
        
        cbIteracao1.setVisibleRowCount(6);
        cbIteracao2.setVisibleRowCount(6);
        cbIteracao3.setVisibleRowCount(6);
        
        cbIteracao1.getSelectionModel().selectedItemProperty().addListener(cbListenerIteracao1);        
        cbIteracao2.getSelectionModel().selectedItemProperty().addListener(cbListenerIteracao2);        
        cbIteracao3.getSelectionModel().selectedItemProperty().addListener(cbListenerIteracao3);
              
    }

    public double regraDeTres(double total, double especificos) {
        return 100 * especificos / (double) total;
    }

    private void configureChooser(DirectoryChooser chooser) {
        chooser.setTitle("Encontre a pasta principal do projeto");
        chooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }
    
    private void saveUpdatedXmlPropertiesFile () throws IOException {
        if (prop != null || fProp != null)
            xmlMapper.writeValue(fProp, prop);
    }
}
