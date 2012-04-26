package gui;

import gui.ConfigPanel.ChoiceOption;
import gui.ConfigPanel.ConfigListener;
import gui.ConfigPanel.DoubleOption;
import gui.ConfigPanel.IntegerOption;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.math.plot.Plot2DPanel;

import GA.GAStudentsEngine;
import GACore.IGAEngine;
import GACore.IGARandom;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

/**
 * @author Ricardo Pragnell, Carlos Gabriel
 */

public class GAGUI extends JFrame implements PropertyChangeListener{
	
	private static final long serialVersionUID = 5393378737313833016L;
	private JPanel panelGenetics;
	private JPanel panelPruebas;
	private JPanel panelResultados;
	private boolean inputFieldsOK;	// datos de entrada correctos
	private GAStepThread stepThread;
	private double[] dataAbsoluteBest;
	private double[] dataGenerationBest;
	private double[] dataGenerationAverage;
	private double[] dataGenerationCount;
	private Plot2DPanel pGraphic;
	private JProgressBar progBar;
	private ChoiceOption<IGAEngine> functChoiceOpt;
	private DoubleOption<IGAEngine> paramsSelecDouble;
	private DoubleOption<IGAEngine> paramsCrossDouble;
	private DoubleOption<IGAEngine> paramsMutDouble;
	public String configData;
	
	public GAGUI(final IGAEngine gaEngine) {
		super("Programaci�n Evolutiva - Pr�ctica 2");
		pGraphic = new Plot2DPanel();
		panelGenetics = new JPanel();
		panelGenetics.setLayout(new MigLayout("", "[center]"));
		panelResultados = new JPanel();
		panelResultados.setLayout(new MigLayout("", "[center]"));
		panelPruebas = new JPanel();
		panelPruebas.setLayout(new MigLayout("flowy", "[left]"));
		
		//WARN: No usar EXIT_ON_CLOSE, threads petan con Plot2DPanel y bugs del JVM
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosed(WindowEvent e) {
	        PrintStream nullStream = new PrintStream(new OutputStream() {
	            public void write(int b) throws IOException {}
	            public void write(byte b[]) throws IOException {}
	            public void write(byte b[], int off, int len) throws IOException {}
	        });	        
	        System.setErr(nullStream);
	        System.setOut(nullStream);
	        System.exit(0);
	    }
		});
		
		JTabbedPane tabPanePrincipal = new JTabbedPane();
		
        //********** PANEL GENETICS **************************************//
		
		JPanel panelCentral = new JPanel(new MigLayout("", "[left]"));

		panelGenetics.add(new JLabel("General"), "split, gaptop 10");
		panelGenetics.add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// crea un panel central y lo asocia con la primera figura
		final ConfigPanel<IGAEngine> cp = creaPanelConfiguracion();
		// asocia el panel con la figura
		cp.setTarget(gaEngine);
		// carga los valores de la figura en el panel
		cp.initialize();
		panelGenetics.add(cp, "growx, wrap");
		
		panelGenetics.add(new JLabel("Probabilidad Cruce"), "split, gaptop 10");
		panelGenetics.add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// Slider prob cruce
		JSlider crossSlider = new JSlider(0,100,40);
		crossSlider.setMajorTickSpacing(20);
		crossSlider.setMinorTickSpacing(5);
		crossSlider.setPaintTicks(true);
		crossSlider.setPaintLabels(true);
		panelGenetics.add(crossSlider, "wrap, growx");
		
		crossSlider.addChangeListener(new ChangeListener() {
		    // This method is called whenever the slider's value is changed
		    public void stateChanged(ChangeEvent evt) {
		        JSlider slider = (JSlider)evt.getSource();

		        if (!slider.getValueIsAdjusting()) {
		            // Get new value
		            gaEngine.setProb_Cross((double) slider.getValue()/100);
		        }
		    }
		});
		
		panelGenetics.add(new JLabel("Probabilidad Mutaci�n"), "split, gaptop 10");
		panelGenetics.add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// Slider prob mutaci�n
		JSlider mutationSlider = new JSlider(0,100,40);
		mutationSlider.setMajorTickSpacing(20);
		mutationSlider.setMinorTickSpacing(5);
		mutationSlider.setPaintTicks(true);
		mutationSlider.setPaintLabels(true);
		panelGenetics.add(mutationSlider, "wrap, growx");
		
		mutationSlider.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent evt) {
		        JSlider slider = (JSlider)evt.getSource();

		        if (!slider.getValueIsAdjusting()) {
		            gaEngine.setProb_Mut((double) slider.getValue()/100);
		        }
		    }
		});
		
		JCheckBox checkBoxElite = new JCheckBox("Emplear Elitismo", true);
		checkBoxElite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JCheckBox cb = (JCheckBox)evt.getSource();
				gaEngine.setUseElitism(cb.isSelected());
			}
		});
		panelGenetics.add(checkBoxElite, "wrap, gaptop 11");
		
		panelGenetics.add(new JLabel("Mensajes"), "split, gaptop 10");
		panelGenetics.add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// crea una etiqueta que dice si todo es valido
		final String textoTodoValido = "Todos los campos OK";
		final String textoHayErrores = "Hay errores en algunos campos";
		final JLabel valido = new JLabel(textoHayErrores);
		// este evento se lanza cada vez que la validez cambia
		cp.addConfigListener(new ConfigListener() {
			public void configChanged(boolean isConfigValid) {
				inputFieldsOK = isConfigValid;
				valido.setText(isConfigValid ? textoTodoValido: textoHayErrores);
			}
		});
		panelGenetics.add(valido, "wrap, gap 10");
				
		panelGenetics.add(new JSeparator(), "wrap, growx, gaptop 10");
		
		// crea una etiqueta que indica la figura que se esta editando
		final JLabel panelEnEdicion = new JLabel("Presione Run:");
		panelCentral.add(panelEnEdicion, "wrap, span");
		
		progBar = new JProgressBar();
		progBar.setStringPainted(true);
		panelCentral.add(progBar, "wrap, span, gaptop 15, gapbottom 10, center");
		
		// usado por todos los botones
		JButton boton;
		
		// pedir el path del archivo alumnos
		boton = new JButton("Cargar");
		boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showOpenDialog(GAGUI.this);			

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            //This is where a real application would open the file.
		            ((GAStudentsEngine) gaEngine).setStudentPath(file.getName());
		            System.out.println("Opening: " + file.getName() + ".");
		        } else {
		        	System.out.println("Open command cancelled by user.");
		        }
			}
		});
		panelCentral.add(boton);
		
		// bot�n para ejecutar la evolucion
		boton = new JButton("Run");
		boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if (inputFieldsOK) {
					panelEnEdicion.setText("Ejecutando evoluci�n...");
					
					// creamos nuevos arrays donde guardar resultados de la evoluci�n
					dataAbsoluteBest = new double[gaEngine.getNum_Max_Gen()];
					dataGenerationBest = new double[gaEngine.getNum_Max_Gen()];
					dataGenerationAverage = new double[gaEngine.getNum_Max_Gen()];
					dataGenerationCount = new double[gaEngine.getNum_Max_Gen()];
					
					pGraphic.removeAllPlots();
					
					// cargamos configuraci�n adicional
					gaEngine.loadConfig(configData);
					
					// inicializamos el motor gen�tico
					gaEngine.init();
					
					// tenemos luz verde para ejecutar?
					if (stepThread == null || (stepThread != null && stepThread.isDone()))
					{
					
					// bucle de evoluci�n, ejecutamos cada step en un thread distinto (para no bloquar la interfaz)
					stepThread = new GAStepThread(gaEngine,(Object) dataAbsoluteBest,(Object) dataGenerationAverage,(Object) dataGenerationBest,(Object) dataGenerationCount) {
						protected void done() {
							try {
								progBar.setValue(0);
								panelEnEdicion.setText("Evoluci�n completada");
								pGraphic.addLinePlot("Mejor Absoluto", Color.blue, dataGenerationCount,	dataAbsoluteBest);
								pGraphic.addLinePlot("Mejor de la Generaci�n", Color.red, dataGenerationCount, dataGenerationBest);
								pGraphic.addLinePlot("Media de la Generaci�n", Color.green, dataGenerationCount, dataGenerationAverage);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(GAGUI.this, "Error", "Hubo un error durante la evoluci�n.", JOptionPane.ERROR_MESSAGE);
							}
						}
					};

					// A property listener used to update the progress bar
					PropertyChangeListener listener = new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent event) {
							if ("progress".equals(event.getPropertyName())) {
								progBar.setValue((Integer) event.getNewValue());
							}
						}
					};
					stepThread.addPropertyChangeListener(listener);
					stepThread.execute();
					}
					
				}
			}
		});
		panelCentral.add(boton);

		boton = new JButton("Stop");
		boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stepThread != null)
				{
					panelEnEdicion.setText("Parando evoluci�n...");
					
					// Detener la evoluci�n
					stepThread.cancel(true);
					
					panelEnEdicion.setText("Evoluci�n cancelada");
				}
			}
		});
		panelCentral.add(boton);
		
		panelGenetics.add(panelCentral, "bottom");
		
		// Gr�fica
		pGraphic.addLegend("SOUTH");
		pGraphic.setSize(600, 600);
		pGraphic.setPreferredSize(new Dimension(600, 600));
		panelGenetics.add(pGraphic, "split, grow, gaptop 11, gapleft 20, gapbottom 11, gapright 11, dock east");
		
		//********** PANEL RESULTS **************************************//
		
		// Grafica results
		panelResultados.add(new JLabel("Soluci�n encontrada"), "split, gaptop 10");
		panelResultados.add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Set random data for now
        int numGroups = 20;
        String category;
        for (int i=1; i<=numGroups; i++){
        	category = "G" + i;
        	dataset.addValue(IGARandom.getRInt(10)+5, "Listo", category);
        	dataset.addValue(IGARandom.getRInt(10)+5, "Normal", category);
        	dataset.addValue(IGARandom.getRInt(10)+5, "Subnormal", category);
        }
        		
        JFreeChart chart = ChartFactory.createStackedBarChart(
		//JFreeChart chart = ChartFactory.createStackedBarChart3D (
	            "Grupos Soluci�n",        // chart title
	            "N�mero de grupo",        // domain axis label
	            "Valor",                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips?
	            false                     // URLs?
	        );
		
		final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.lightGray);
        
        CategoryItemRenderer renderer = plot.getRenderer();
        //renderer.setSeriesItemLabelsVisible(0, false);
        
        plot.getRenderer().setSeriesPaint(0, linearGradient(new Color(65, 105, 225), new Color(135, 206, 250), 3, 0));
        plot.getRenderer().setSeriesPaint(1, linearGradient(new Color(65, 105, 225), new Color(135, 206, 250), 3, 1));
        plot.getRenderer().setSeriesPaint(2, linearGradient(new Color(65, 105, 225), new Color(135, 206, 250), 3, 2));
        
        /* Esto por si interesa m�s tarde
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        Marker marker = new ValueMarker(5.0);
        marker.setPaint(Color.red);
        plot.addRangeMarker(marker);*/
        
        Stroke stroke = new BasicStroke();
        plot.addDomainMarker(new CategoryMarker("G3", Color.red, stroke, Color.black, stroke, 0.4f)); 
        plot.addDomainMarker(new CategoryMarker("G14", Color.red, stroke, Color.black, stroke, 0.4f));
        
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        renderer.setBaseItemLabelsVisible(true);

		ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(850, 270));
        panelResultados.add(chartPanel, "gaptop 20");
        
        //********** PANEL PRUEBAS AUTOM�TICAS **************************************//
        JLabel titleLabel = new JLabel("Pruebas Autom�ticas:");
        Font font = new Font("Verdana", Font.BOLD, 15);
        titleLabel.setFont(font);
        panelPruebas.add(titleLabel, "span, wrap, gapbottom 5, gaptop 5");
        
        titleLabel = new JLabel("                                      Valor inicial:                   Valor final:     Incremento:       Selecci�n:");
        panelPruebas.add(titleLabel, "wrap, span, gapleft 5");
                
        
        final ConfigPanel<IGAEngine> cpauto = creaPanelPruebasAuto();
        // asocia el panel con la figura
        cpauto.setTarget(gaEngine);
		// carga los valores de la figura en el panel
        cpauto.initialize();
        panelPruebas.add(cpauto, "split, wrap, growy, gapright 10, gapleft 5");
        
        final JPanel panelInterval = new JPanel();
        panelInterval.setLayout(new GridLayout(10,1,0,-1));
        
        JSpinner tamGruposText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        tamGruposText.setMinimumSize(new Dimension(30,5));
        panelInterval.add(tamGruposText);
        JSpinner tamPobText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(tamPobText);
        JSpinner numGenText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(numGenText);
        JSpinner alfaText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(alfaText);
        JSpinner blankText = new JSpinner();
        panelInterval.add(blankText);
        blankText.setVisible(false);
        JSpinner selecText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(selecText);
        JSpinner blankText2 = new JSpinner();
        panelInterval.add(blankText2);
        blankText2.setVisible(false);
        JSpinner crossText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(crossText);
        JSpinner blankText3 = new JSpinner();
        panelInterval.add(blankText3);  
        blankText3.setVisible(false);
        JSpinner mutText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(mutText);
        
        panelPruebas.add(panelInterval, "gapbottom 2, wrap, gapright 10");
        
        final JPanel panelIncrements = new JPanel();
        panelIncrements.setLayout(new GridLayout(10,1,0,-1));
        
        JSpinner tamGruposIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        tamGruposText.setMinimumSize(new Dimension(30,5));
        panelInterval.add(tamGruposIncr);
        JSpinner tamPobIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(tamPobIncr);
        JSpinner numGenIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(numGenIncr);
        JSpinner alfaIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(alfaIncr);
        JSpinner blankIncr = new JSpinner();
        panelInterval.add(blankIncr);
        blankText.setVisible(false);
        JSpinner selecIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(selecIncr);
        JSpinner blankIncr2 = new JSpinner();
        panelInterval.add(blankIncr2);
        blankText2.setVisible(false);
        JSpinner crossIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(crossIncr);
        JSpinner blankIncr3 = new JSpinner();
        panelInterval.add(blankIncr3);  
        blankText3.setVisible(false);
        JSpinner mutIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(mutIncr);
       
        
        panelPruebas.add(panelIncrements, "gapbottom 2, wrap");
        
        final JPanel panleRadioBut = new JPanel();
        panleRadioBut.setLayout(new GridLayout(10,1,0,-8));
        
        JRadioButton tamGruposBut = new JRadioButton();
        panleRadioBut.add(tamGruposBut);
        JRadioButton tamPobBut = new JRadioButton();
        panleRadioBut.add(tamPobBut);
        JRadioButton numGenBut = new JRadioButton();
        panleRadioBut.add(numGenBut);
        JRadioButton alfaBut = new JRadioButton();
        panleRadioBut.add(alfaBut);
        JRadioButton blankBut = new JRadioButton();
        panleRadioBut.add(blankBut);
        blankBut.setVisible(false);
        JRadioButton selecBut = new JRadioButton();
        panleRadioBut.add(selecBut);
        JRadioButton blankBut2 = new JRadioButton();
        panleRadioBut.add(blankBut2);
        blankBut2.setVisible(false);
        JRadioButton crossBut = new JRadioButton();
        panleRadioBut.add(crossBut);
        JRadioButton blankBut3 = new JRadioButton();
        panleRadioBut.add(blankBut3);  
        blankBut3.setVisible(false);
        JRadioButton mutBut = new JRadioButton();
        panleRadioBut.add(mutBut);        
        
        //Group the radio buttons.
        ButtonGroup radioButGroup = new ButtonGroup();
        radioButGroup.add(tamGruposBut);
        radioButGroup.add(tamPobBut);
        radioButGroup.add(numGenBut);
        radioButGroup.add(alfaBut);
        radioButGroup.add(selecBut);
        radioButGroup.add(crossBut);
        radioButGroup.add(mutBut);
                
        panelPruebas.add(panleRadioBut, "gaptop 0");
		
		// Tabs
		tabPanePrincipal.add(panelGenetics, "Algoritmo Gen�tico");		
		tabPanePrincipal.add(panelResultados, "Grupos Resultado");
		tabPanePrincipal.add(panelPruebas, "Pruebas Autom�ticas");
		
		add(tabPanePrincipal);
	}
	
	public ConfigPanel<IGAEngine> creaPanelConfiguracion() {
		String[] selectorNames = new String[] { "Ruleta", "Torneo Det", "Torneo Prob", "Ranking", "M�todo Propio" };
		String[] crossNames = new String[] { "PMX", "OX", "Variante OX", "Ordinal", "M�todo Propio" };
		String[] mutNames = new String[] { "Inserci�n", "Intercambio", "Inversi�n", "Heur�stica" };
	
		ConfigPanel<IGAEngine> config = new ConfigPanel<IGAEngine>();
		
		config.addOption(new IntegerOption<IGAEngine>(  // -- entero
			"Tama�o grupos", 					// texto a usar como etiqueta del campo
			"N�mero de estudiantes que forman cada grupo",  // texto a usar como 'tooltip' cuando pasas el puntero
			"groupSize",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 500));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
		config.addOption(new IntegerOption<IGAEngine>(  // -- entero
			"Tama�o poblaci�n", 					// texto a usar como etiqueta del campo
			"N�mero de individuos que forman la poblaci�n",  // texto a usar como 'tooltip' cuando pasas el puntero
			"population_Size",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 1000));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
		config.addOption(new IntegerOption<IGAEngine>(
			"Num Generaciones", 					// texto a usar como etiqueta del campo
			"N�mero de generaciones que dura la evoluci�n",  // texto a usar como 'tooltip' cuando pasas el puntero
			"num_Max_Gen",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 10000));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
	    config.addOption(new DoubleOption<IGAEngine>(  
			"Par�metro Alfa", 						// texto a usar como etiqueta del campo
			"Par�metro Alfa",       				// texto a usar como 'tooltip' cuando pasas el puntero
			"alfaValue",  						    // campo (espera que haya un getGrosor y un setGrosor)
			0.0, 1.0));								// min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)

		// Selection
		functChoiceOpt = new ChoiceOption<IGAEngine>(
				"Funci�n de selecci�n",
				"Funci�n selecci�n",
				"selectorName",
				selectorNames);	
		config.addOption(functChoiceOpt);
		
		paramsSelecDouble = new DoubleOption<IGAEngine>(
				"Parametros Seleccion",
				"Parametros Seleccion",
				"selecParams", 1.0, 100.0);
		config.addOption(paramsSelecDouble);
		
		((JComboBox)functChoiceOpt.getControlRef()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox)e.getSource();
		        String funcName = (String)combo.getSelectedItem();
		        paramsSelecDouble.getTextFieldRef().setEnabled(true);
				if(funcName == "Ruleta")
					paramsSelecDouble.getLabelRef().setText("Params Ruleta");
				else if(funcName == "Torneo Prob")
					paramsSelecDouble.getLabelRef().setText("Params Torneo Prob");
				else if(funcName == "Torneo Det")
					paramsSelecDouble.getLabelRef().setText("Params Torneo Det");
				else if(funcName == "Ranking")
					paramsSelecDouble.getLabelRef().setText("Params Ranking");
				else if(funcName == "M�todo Propio")
					paramsSelecDouble.getLabelRef().setText("Params M�todo Propio");
			}
		});
		
		// Cross
		functChoiceOpt = new ChoiceOption<IGAEngine>(
				"Funci�n de cruce",
				"Funci�n cruce",
				"crossName",
				crossNames);
		config.addOption(functChoiceOpt);
		
		paramsCrossDouble = new DoubleOption<IGAEngine>(
				"Parametros Cruce",
				"Parametros Cruce",
				"crossParams", 1.0, 100.0);
		config.addOption(paramsCrossDouble);
		
		((JComboBox)functChoiceOpt.getControlRef()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox)e.getSource();
		        String funcName = (String)combo.getSelectedItem();
		        paramsCrossDouble.getTextFieldRef().setEnabled(true);
				if(funcName == "PMX")
					paramsCrossDouble.getLabelRef().setText("Params PMX");
				else if(funcName == "OX")
					paramsCrossDouble.getLabelRef().setText("Params OX");
				else if(funcName == "Variante OX")
					paramsCrossDouble.getLabelRef().setText("Params Variante OX");
				else if(funcName == "Ordinal")
					paramsCrossDouble.getLabelRef().setText("Params Ordinal");
				else if(funcName == "M�todo Propio")
					paramsCrossDouble.getLabelRef().setText("Params M�todo Propio");
			}
		});
		
		// Mutation
		functChoiceOpt = new ChoiceOption<IGAEngine>(
				"Funci�n de mutaci�n",
				"Funci�n mutaci�n",
				"mutName",
				mutNames);
		config.addOption(functChoiceOpt);
		
		paramsMutDouble = new DoubleOption<IGAEngine>(
				"Parametros Mutaci�n",
				"Parametros Mutaci�n",
				"mutParams", 1.0, 100.0);
		config.addOption(paramsMutDouble);
		
		((JComboBox)functChoiceOpt.getControlRef()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox)e.getSource();
		        String funcName = (String)combo.getSelectedItem();
		        paramsMutDouble.getTextFieldRef().setEnabled(true);
				if(funcName == "Inserci�n")
					paramsMutDouble.getLabelRef().setText("Params Inserci�n");
				else if(funcName == "Intercambio")
					paramsMutDouble.getLabelRef().setText("Params Intercambio");
				else if(funcName == "Inversi�n")
					paramsMutDouble.getLabelRef().setText("Params Inversi�n");
				else if(funcName == "Heur�stica")
				{
					paramsMutDouble.getLabelRef().setText("Params Heur�stica");
					paramsMutDouble.getTextFieldRef().setEnabled(false);
				}
			}
		});
		
		return config;
	}
	
	public ConfigPanel<IGAEngine> creaPanelPruebasAuto() {
		String[] selectorNames = new String[] { "Ruleta", "Torneo Det", "Torneo Prob", "Ranking", "M�todo Propio" };
		String[] crossNames = new String[] { "PMX", "OX", "Variante OX", "Ordinal", "M�todo Propio" };
		String[] mutNames = new String[] { "Inserci�n", "Intercambio", "Inversi�n", "Heur�stica" };
	
		ConfigPanel<IGAEngine> config = new ConfigPanel<IGAEngine>();
		
		config.addOption(new IntegerOption<IGAEngine>(  // -- entero
			"Tama�o grupos", 					// texto a usar como etiqueta del campo
			"N�mero de estudiantes que forman cada grupo",  // texto a usar como 'tooltip' cuando pasas el puntero
			"groupSize",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 500));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
		config.addOption(new IntegerOption<IGAEngine>(  // -- entero
			"Tama�o poblaci�n", 					// texto a usar como etiqueta del campo
			"N�mero de individuos que forman la poblaci�n",  // texto a usar como 'tooltip' cuando pasas el puntero
			"population_Size",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 1000));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
		config.addOption(new IntegerOption<IGAEngine>(
			"Num Generaciones", 					// texto a usar como etiqueta del campo
			"N�mero de generaciones que dura la evoluci�n",  // texto a usar como 'tooltip' cuando pasas el puntero
			"num_Max_Gen",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 10000));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
	    config.addOption(new DoubleOption<IGAEngine>(  
			"Par�metro Alfa", 						// texto a usar como etiqueta del campo
			"Par�metro Alfa",       				// texto a usar como 'tooltip' cuando pasas el puntero
			"alfaValue",  						    // campo (espera que haya un getGrosor y un setGrosor)
			0.0, 1.0));								// min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)

		// Selection
			
		config.addOption(new ChoiceOption<IGAEngine>(
				"Funci�n de selecci�n",
				"Funci�n selecci�n",
				"selectorName",
				selectorNames));
		
		config.addOption(new DoubleOption<IGAEngine>(
				"Parametros Seleccion",
				"Parametros Seleccion",
				"selecParams", 1.0, 100.0));
		
		// Cross
		config.addOption(new ChoiceOption<IGAEngine>(
				"Funci�n de cruce",
				"Funci�n cruce",
				"crossName",
				crossNames));
		
		config.addOption(new DoubleOption<IGAEngine>(
				"Parametros Cruce",
				"Parametros Cruce",
				"crossParams", 1.0, 100.0));
		
		// Mutation
		config.addOption(new ChoiceOption<IGAEngine>(
				"Funci�n de mutaci�n",
				"Funci�n mutaci�n",
				"mutName",
				mutNames));
		
		config.addOption(new DoubleOption<IGAEngine>(
				"Parametros Mutaci�n",
				"Parametros Mutaci�n",
				"mutParams", 1.0, 100.0));
		
		return config;
	}
	
	private Color linearGradient(Color c1, Color c2, int max, int level)
	{		
		float ratio = (float)level / (float)max;
        int red = (int)(c2.getRed() * ratio + c1.getRed() * (1 - ratio));
        int green = (int)(c2.getGreen() * ratio +  c1.getGreen() * (1 - ratio));
        int blue = (int)(c2.getBlue() * ratio + c1.getBlue() * (1 - ratio));
        Color c = new Color(red, green, blue);
        return c;
	}
	
		
	// construye y muestra la interfaz
	public void runGUI() {
		// LookAndFeel + posici�n inicial
		try
	    {
	      UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    SwingUtilities.updateComponentTreeUI(this);
		pack();
		setLocationRelativeTo(null); // centra el frame en la pantalla
		setVisible(true);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progBar.setValue(progress);
        } 
	}

}