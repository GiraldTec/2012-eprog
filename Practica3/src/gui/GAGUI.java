package gui;

import gui.ConfigPanel.ChoiceOption;
import gui.ConfigPanel.ConfigListener;
import gui.ConfigPanel.DoubleOption;
import gui.ConfigPanel.IntegerOption;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
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
import no.geosoft.cc.graphics.GInteraction;
import no.geosoft.cc.graphics.GObject;
import no.geosoft.cc.graphics.GScene;
import no.geosoft.cc.graphics.GSegment;
import no.geosoft.cc.graphics.GStyle;
import no.geosoft.cc.graphics.GWindow;

import org.math.plot.Plot2DPanel;

import practica3.AntBoardManager;
import practica3.AntBoardManager.PieceType;
import GA.GAStudentsEngine;
import GACore.IGAEngine;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

/**
 * @author Ricardo Pragnell, Carlos Gabriel
 */

public class GAGUI extends JFrame implements PropertyChangeListener, GInteraction{
	
	private static final long serialVersionUID = 5393378737313833016L;
	private JPanel panelGenetics;
	private JPanel panelPruebas;
	private JPanel panelResultados;
	private boolean inputFieldsOK;
	private GAStepThread stepThread;
	private double[] dataAbsoluteBest;
	private double[] dataGenerationBest;
	private double[] dataGenerationAverage;
	private double[] dataGenerationCount;
	private Plot2DPanel pGraphic;
	private Plot2DPanel pGraphicPruebsAuto;
	private JProgressBar progBar;
	private ChoiceOption<IGAEngine> functChoiceOpt;
	private DoubleOption<IGAEngine> paramsSelecDouble;
	private DoubleOption<IGAEngine> paramsCrossDouble;
	private DoubleOption<IGAEngine> paramsMutDouble;
	private int selectedRadio=0, boardSize=32;
	private AntBoardManager boardManager;
	private double currEvaluatedValue=0, oldEvaluatedValue=0,selectedMaxVal=0, selectedIncrement=0;
	public String configData;
	
	
	public GAGUI(final IGAEngine gaEngine) {
		super("Programación Evolutiva - Práctica 2");
		pGraphic = new Plot2DPanel();
		pGraphicPruebsAuto = new Plot2DPanel();
		panelGenetics = new JPanel();
		panelGenetics.setLayout(new MigLayout("", "[center]"));
		panelResultados = new JPanel();
		panelResultados.setLayout(new BorderLayout());
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
		boardManager = new AntBoardManager(boardSize);
		
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
		
		panelGenetics.add(new JLabel("Probabilidad Mutación"), "split, gaptop 10");
		panelGenetics.add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// Slider prob mutación
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
		            System.out.println("Opening: " + file.getName());
		            
		            ((GAStudentsEngine) gaEngine).setStudentPath(file.getName());
		        } else {
		        	System.out.println("Open command cancelled by user.");
		        }
			}
		});
		panelCentral.add(boton);
		
		// botón para ejecutar la evolucion
		boton = new JButton("Run");
		boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if (inputFieldsOK) {
					panelEnEdicion.setText("Ejecutando evolución...");
					
					// creamos nuevos arrays donde guardar resultados de la evolución
					dataAbsoluteBest = new double[gaEngine.getNum_Max_Gen()];
					dataGenerationBest = new double[gaEngine.getNum_Max_Gen()];
					dataGenerationAverage = new double[gaEngine.getNum_Max_Gen()];
					dataGenerationCount = new double[gaEngine.getNum_Max_Gen()];
					
					pGraphic.removeAllPlots();
					
					// cargamos configuración adicional
					gaEngine.loadConfig(configData);
					
					// inicializamos el motor genético
					gaEngine.init();
					
					// tenemos luz verde para ejecutar?
					if (stepThread == null || (stepThread != null && stepThread.isDone()))
					{
					
					// bucle de evolución, ejecutamos cada step en un thread distinto (para no bloquar la interfaz)
					stepThread = new GAStepThread(gaEngine,(Object) dataAbsoluteBest,(Object) dataGenerationAverage,(Object) dataGenerationBest,(Object) dataGenerationCount) {
						protected void done() {
							try {
								progBar.setValue(0);
								panelEnEdicion.setText("Evolución completada");
								pGraphic.addLinePlot("Mejor Absoluto", Color.blue, dataGenerationCount,	dataAbsoluteBest);
								pGraphic.addLinePlot("Mejor de la Generación", Color.red, dataGenerationCount, dataGenerationBest);
								pGraphic.addLinePlot("Media de la Generación", Color.green, dataGenerationCount, dataGenerationAverage);
								
								
							} catch (Exception e) {
								JOptionPane.showMessageDialog(GAGUI.this, "Error", "Hubo un error durante la evolución.", JOptionPane.ERROR_MESSAGE);
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
					panelEnEdicion.setText("Parando evolución...");
					
					// Detener la evolución
					stepThread.cancel(true);
					
					panelEnEdicion.setText("Evolución cancelada");
				}
			}
		});
		panelCentral.add(boton);
		
		panelGenetics.add(panelCentral, "bottom");
		
		// Gráfica
		pGraphic.addLegend("SOUTH");
		pGraphic.setSize(600, 600);
		pGraphic.setPreferredSize(new Dimension(600, 600));
		panelGenetics.add(pGraphic, "split, grow, gaptop 11, gapleft 20, gapbottom 11, gapright 11, dock east");
		
		//********** PANEL PRUEBAS RESULTADOS **************************************//
		
		// Create the graphic canvas
		GWindow window = new GWindow(new Color(0, 220, 220));
		panelResultados.add(window.getCanvas(), BorderLayout.CENTER);
		
		// Create scene
		GScene scene = new GScene(window);
		double w0[] = { 0.0, 0.0, 0.0 };
		double w1[] = { boardSize + 2.0, 0.0, 0.0 };
		double w2[] = { 0.0, boardSize + 2.0, 0.0 };
		scene.setWorldExtent(w0, w1, w2);

		GUIAntBoard antBoard = new GUIAntBoard(boardManager);
		scene.add(antBoard);

		// Make sure plot can be scrolled
		window.startInteraction(this);
		
		JPanel saveLoadPanel = new JPanel(new GridLayout(1, 2));
		saveLoadPanel.add(new JButton("Cargar"));
		saveLoadPanel.add(new JButton("Guardar"));
		panelResultados.add(saveLoadPanel, BorderLayout.SOUTH);
		                        
        //********** PANEL PRUEBAS AUTOMÁTICAS **************************************//
                
        JLabel titleLabel = new JLabel("Pruebas Automáticas:");
        Font font = new Font("Verdana", Font.BOLD, 15);
        titleLabel.setFont(font);
        panelPruebas.add(titleLabel, "span, wrap, gaptop 15, gapleft 5");
        
        final ConfigPanel<IGAEngine> cpauto = creaPanelPruebasAuto();
        cpauto.setBorder(BorderFactory.createTitledBorder("Valor inicial:"));
        // asocia el panel con la figura
        cpauto.setTarget(gaEngine);
		// carga los valores de la figura en el panel
        cpauto.initialize();
        final JPanel panelconfigu = new JPanel();
        panelconfigu.setLayout(new GridLayout(1,1,0,0));
        panelconfigu.add(cpauto);        
        panelPruebas.add(panelconfigu, "split, wrap, gapleft 10, top, gaptop 40");
        
        final JPanel panelInterval = new JPanel();
        panelInterval.setLayout(new GridLayout(10,1,0,0));
        panelInterval.setBorder(BorderFactory.createTitledBorder("Valor final:"));
        
        final JSpinner tamGruposText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        tamGruposText.setMinimumSize(new Dimension(30,5));
        panelInterval.add(tamGruposText);
        final JSpinner tamPobText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(tamPobText);
        final JSpinner numGenText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(numGenText);
        final JSpinner alfaText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(alfaText);
        JSpinner blankText = new JSpinner();
        panelInterval.add(blankText);
        blankText.setVisible(false);
        final JSpinner selecText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(selecText);
        JSpinner blankText2 = new JSpinner();
        panelInterval.add(blankText2);
        blankText2.setVisible(false);
        final JSpinner crossText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(crossText);
        JSpinner blankText3 = new JSpinner();
        panelInterval.add(blankText3);  
        blankText3.setVisible(false);
        final JSpinner mutText = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelInterval.add(mutText);
        
        panelPruebas.add(panelInterval, "gapbottom 2, wrap, top, gaptop 40");
        
        final JPanel panelIncrements = new JPanel();
        panelIncrements.setLayout(new GridLayout(10,1,0,0));
        panelIncrements.setBorder(BorderFactory.createTitledBorder("Incremento"));
        
        final JSpinner tamGruposIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        tamGruposIncr.setMinimumSize(new Dimension(30,5));
        panelIncrements.add(tamGruposIncr);
        final JSpinner tamPobIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelIncrements.add(tamPobIncr);
        final JSpinner numGenIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelIncrements.add(numGenIncr);
        final JSpinner alfaIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelIncrements.add(alfaIncr);
        JSpinner blankIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelIncrements.add(blankIncr);
        blankIncr.setVisible(false);
        final JSpinner selecIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelIncrements.add(selecIncr);
        JSpinner blankIncr2 = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelIncrements.add(blankIncr2);
        blankIncr2.setVisible(false);
        final JSpinner crossIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelIncrements.add(crossIncr);
        JSpinner blankIncr3 = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelIncrements.add(blankIncr3);  
        blankIncr3.setVisible(false);
        final JSpinner mutIncr = new JSpinner(new SpinnerNumberModel(10, 1, 5000, 1));
        panelIncrements.add(mutIncr);  
        
        panelPruebas.add(panelIncrements, "gapbottom 2, wrap, top, gaptop 40");
        
        final JPanel panleRadioBut = new JPanel();
        panleRadioBut.setLayout(new GridLayout(10,1,0,-8));
        panleRadioBut.setBorder(BorderFactory.createTitledBorder("Select"));
        
        JRadioButton tamGruposBut = new JRadioButton();
        panleRadioBut.add(tamGruposBut);
        tamGruposBut.setSelected(true);
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
        final ButtonGroup radioButGroup = new ButtonGroup();
        radioButGroup.add(tamGruposBut);
        radioButGroup.add(tamPobBut);
        radioButGroup.add(numGenBut);
        radioButGroup.add(alfaBut);
        radioButGroup.add(selecBut);
        radioButGroup.add(crossBut);
        radioButGroup.add(mutBut);
                        
        panelPruebas.add(panleRadioBut, "wrap, top, gaptop 40");
        
        pGraphicPruebsAuto.addLegend("SOUTH");
        pGraphicPruebsAuto.setPreferredSize(new Dimension(350, 510));
        panelPruebas.add(pGraphicPruebsAuto, "gapleft 5, gaptop 5, top");
                
        boton = new JButton("Stop");
		boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stepThread != null)
				{
					JRadioButton selected = (JRadioButton) radioButGroup.getSelection();
					System.out.println(selected.getName());
					// Detener la evolución
					stepThread.cancel(true);
				}
			}
		});
		panelPruebas.add(boton, "dock south, gapleft 80, gapright 600");
        
        // botón para ejecutar la evolucion
		boton = new JButton("Run");
		boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
											
				// consigue los valores seleccionados por el radioBut
				@SuppressWarnings("rawtypes")				
				Enumeration elements = radioButGroup.getElements();
				selectedRadio = 0;
			    while (elements.hasMoreElements()) {
			      AbstractButton button = (AbstractButton)elements.nextElement();
			      selectedRadio++;
			      if (button.isSelected()) {
			        System.out.println("Selected radio: " + selectedRadio);
			        break;
			      }
			    }
			    			    
			    switch (selectedRadio)
			    {
			    	case 0 : break;
			    	case 1 : 
			    		currEvaluatedValue = (Integer)((GAStudentsEngine)gaEngine).getGroupSize();
			    		selectedIncrement = (Integer) ((SpinnerNumberModel)tamGruposIncr.getModel()).getNumber(); 
			    		selectedMaxVal=(Integer)(tamGruposText.getValue());
			    		break;
			    	case 2 : 	
			    		currEvaluatedValue = (Integer)((GAStudentsEngine)gaEngine).getPopulation_Size();
			    		selectedIncrement=(Integer)((SpinnerNumberModel)tamPobIncr.getModel()).getNumber(); 
    					selectedMaxVal=(Integer)(tamPobText.getValue());
    					break;
			    	case 3 : 
			    		currEvaluatedValue = (Integer)((GAStudentsEngine)gaEngine).getNum_Max_Gen();
			    		selectedIncrement=(Integer)(numGenIncr.getValue()); 
    					selectedMaxVal=(Integer)(numGenText.getValue());
    					break;
			    	case 4 : 
			    		currEvaluatedValue = ((GAStudentsEngine)gaEngine).getAlfaValue();
			    		selectedIncrement=(Integer)(alfaIncr.getValue()); 
    					selectedMaxVal=(Integer)(alfaText.getValue());
    					break;
			    	case 5 :
			    		currEvaluatedValue = ((GAStudentsEngine)gaEngine).getSelecParams();
			    		selectedIncrement=(Integer)(selecIncr.getValue()); 
    					selectedMaxVal=(Integer)(selecText.getValue());
    					break;
			    	case 6 :
			    		currEvaluatedValue = ((GAStudentsEngine)gaEngine).getCrossParams();
			    		selectedIncrement=(Integer)(crossIncr.getValue()); 
    					selectedMaxVal=(Integer)(crossText.getValue());
    					break;
			    	case 7 :
			    		currEvaluatedValue = ((GAStudentsEngine)gaEngine).getMutParams();
			    		selectedIncrement=(Integer)(mutIncr.getValue()); 
						selectedMaxVal=(Integer)(mutText.getValue());
						break;
			    }
			    
			    oldEvaluatedValue = currEvaluatedValue;
			    
			    System.out.println("MaxVal: " + selectedMaxVal + " | Increment: " + selectedIncrement);
		
				pGraphicPruebsAuto.removeAllPlots();
				
				// vamos actualizando el valor hasta llegar al máximo
				while (currEvaluatedValue <= selectedMaxVal){
						int currGeneration = gaEngine.getCurrent_Generation();
					
						// creamos nuevos arrays donde guardar resultados de la evolución
						dataAbsoluteBest = new double[gaEngine.getNum_Max_Gen()];
						dataGenerationBest = new double[gaEngine.getNum_Max_Gen()];
						dataGenerationAverage = new double[gaEngine.getNum_Max_Gen()];
						dataGenerationCount = new double[gaEngine.getNum_Max_Gen()];
						
						// cargamos configuración adicional
						gaEngine.loadConfig(configData);
						
						// inicializamos el motor genético
						gaEngine.init();
						
						while (!gaEngine.isEvol_Complete() && currGeneration < gaEngine.getNum_Max_Gen()){
								try {
									gaEngine.runEvolutionStep();
								} catch (InstantiationException e1) {
									e1.printStackTrace();
								} catch (IllegalAccessException e1) {
									e1.printStackTrace();
								}
								dataAbsoluteBest[currGeneration] = gaEngine.getAbsoluteBest().getEvaluatedValue();
								dataGenerationBest[currGeneration] = gaEngine.getGenerationBest().getEvaluatedValue();
								dataGenerationAverage[currGeneration] = gaEngine.getPopulation_Average();
								dataGenerationCount[currGeneration] = currGeneration;
								currGeneration = gaEngine.getCurrent_Generation();
						}
						
						pGraphicPruebsAuto.addLinePlot("Mejor Absoluto", Color.blue, dataGenerationCount,	dataAbsoluteBest);
						pGraphicPruebsAuto.addLinePlot("Mejor de la Generación", Color.red, dataGenerationCount, dataGenerationBest);
						pGraphicPruebsAuto.addLinePlot("Media de la Generación", Color.green, dataGenerationCount, dataGenerationAverage);
						
						// Actualizar el engine con el siguiente incremento
						currEvaluatedValue += selectedIncrement;												
						switch (selectedRadio)
					    {
					    	case 0 : break;
					    	case 1 :
					    		((GAStudentsEngine)gaEngine).setGroupSize((int)currEvaluatedValue);
					    		break;
					    	case 2 :
		    					((GAStudentsEngine)gaEngine).setPopulation_Size((int)currEvaluatedValue);
		    					break;
					    	case 3 :
		    					((GAStudentsEngine)gaEngine).setNum_Max_Gen((int)currEvaluatedValue);
		    					break;
					    	case 4 :
		    					((GAStudentsEngine)gaEngine).setAlfaValue(currEvaluatedValue);
		    					break;
					    	case 5 : 	
					    		((GAStudentsEngine)gaEngine).setSelecParams(currEvaluatedValue);
		    					break;
					    	case 6 : 	
					    		((GAStudentsEngine)gaEngine).setCrossParams(currEvaluatedValue);
		    					break;
					    	case 7 : 	
					    		((GAStudentsEngine)gaEngine).setMutParams(currEvaluatedValue);
								break;
					    }
				}
				// volvemos a colocar su valor inicial
				switch (selectedRadio)
			    {
			    	case 0 : break;
			    	case 1 :
			    		((GAStudentsEngine)gaEngine).setGroupSize((int)oldEvaluatedValue);
			    		break;
			    	case 2 :
    					((GAStudentsEngine)gaEngine).setPopulation_Size((int)oldEvaluatedValue);
    					break;
			    	case 3 :
    					((GAStudentsEngine)gaEngine).setNum_Max_Gen((int)oldEvaluatedValue);
    					break;
			    	case 4 :
    					((GAStudentsEngine)gaEngine).setAlfaValue(oldEvaluatedValue);
    					break;
			    	case 5 : 	
			    		((GAStudentsEngine)gaEngine).setSelecParams(oldEvaluatedValue);
    					break;
			    	case 6 : 	
			    		((GAStudentsEngine)gaEngine).setCrossParams(oldEvaluatedValue);
    					break;
			    	case 7 : 	
			    		((GAStudentsEngine)gaEngine).setMutParams(oldEvaluatedValue);
						break;
			    }
				
			}
		});
		panelPruebas.add(boton, "dock south, gapleft 80, gapright 600");
						
		//***** Tabs ********//
		
		tabPanePrincipal.add(panelGenetics, "Algoritmo Genético");		
		tabPanePrincipal.add(panelResultados, "Grupos Resultado");
		tabPanePrincipal.add(panelPruebas, "Pruebas Automáticas");
		
		add(tabPanePrincipal);
	}
	
	public ConfigPanel<IGAEngine> creaPanelConfiguracion() {
		String[] selectorNames = new String[] { "Ruleta", "Torneo Det", "Torneo Prob", "Ranking", "Shuffle" };
		String[] crossNames = new String[] { "PMX", "OX", "Variante OX", "Ordinal", "Cremallera" };
		String[] mutNames = new String[] { "Inserción", "Intercambio", "Inversión", "Heurística" };
	
		ConfigPanel<IGAEngine> config = new ConfigPanel<IGAEngine>();
		
		config.addOption(new IntegerOption<IGAEngine>(  // -- entero
			"Tamaño grupos", 					// texto a usar como etiqueta del campo
			"Número de estudiantes que forman cada grupo",  // texto a usar como 'tooltip' cuando pasas el puntero
			"groupSize",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 500));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
		config.addOption(new IntegerOption<IGAEngine>(  // -- entero
			"Tamaño población", 					// texto a usar como etiqueta del campo
			"Número de individuos que forman la población",  // texto a usar como 'tooltip' cuando pasas el puntero
			"population_Size",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 1000));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
		config.addOption(new IntegerOption<IGAEngine>(
			"Num Generaciones", 					// texto a usar como etiqueta del campo
			"Número de generaciones que dura la evolución",  // texto a usar como 'tooltip' cuando pasas el puntero
			"num_Max_Gen",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 10000));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
	    config.addOption(new DoubleOption<IGAEngine>(  
			"Parámetro Alfa", 						// texto a usar como etiqueta del campo
			"Parámetro Alfa",       				// texto a usar como 'tooltip' cuando pasas el puntero
			"alfaValue",  						    // campo (espera que haya un getGrosor y un setGrosor)
			0.0, 1.0));								// min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)

		// Selection
		functChoiceOpt = new ChoiceOption<IGAEngine>(
				"Función de selección",
				"Función selección",
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
				{
					paramsSelecDouble.getLabelRef().setText("Params Ruleta");
					paramsSelecDouble.getTextFieldRef().setEnabled(false);
				}
				else if(funcName == "Torneo Prob")
				{
					paramsSelecDouble.getLabelRef().setText("Params Torneo Prob");
				}
				else if(funcName == "Torneo Det")
				{
					paramsSelecDouble.getLabelRef().setText("Params Torneo Det");
					paramsSelecDouble.getTextFieldRef().setEnabled(false);
				}
				else if(funcName == "Ranking")
				{
					paramsSelecDouble.getLabelRef().setText("Params Ranking");
					paramsSelecDouble.getTextFieldRef().setEnabled(false);
				}
				else if(funcName == "Shuffle")
				{
					paramsSelecDouble.getLabelRef().setText("Params Shuffle");
					paramsSelecDouble.getTextFieldRef().setEnabled(false);
				}
			}
		});
		
		// Cross
		functChoiceOpt = new ChoiceOption<IGAEngine>(
				"Función de cruce",
				"Función cruce",
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
		        paramsCrossDouble.getTextFieldRef().setEnabled(false);
				if(funcName == "PMX")
					paramsCrossDouble.getLabelRef().setText("Params PMX");
				else if(funcName == "OX")
					paramsCrossDouble.getLabelRef().setText("Params OX");
				else if(funcName == "Variante OX")
					paramsCrossDouble.getLabelRef().setText("Params Variante OX");
				else if(funcName == "Ordinal")
					paramsCrossDouble.getLabelRef().setText("Params Ordinal");
				else if(funcName == "Cremallera")
					paramsCrossDouble.getLabelRef().setText("Params Cremallera");
			}
		});
		
		// Mutation
		functChoiceOpt = new ChoiceOption<IGAEngine>(
				"Función de mutación",
				"Función mutación",
				"mutName",
				mutNames);
		config.addOption(functChoiceOpt);
		
		paramsMutDouble = new DoubleOption<IGAEngine>(
				"Parametros Mutación",
				"Parametros Mutación",
				"mutParams", 1.0, 100.0);
		config.addOption(paramsMutDouble);
		
		((JComboBox)functChoiceOpt.getControlRef()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox)e.getSource();
		        String funcName = (String)combo.getSelectedItem();
		        paramsMutDouble.getTextFieldRef().setEnabled(false);
				if(funcName == "Inserción"){
					paramsMutDouble.getLabelRef().setText("Params Inserción");
				}
				else if(funcName == "Intercambio"){
					paramsMutDouble.getLabelRef().setText("Params Intercambio");
				}
				else if(funcName == "Inversión"){
					paramsMutDouble.getLabelRef().setText("Params Inversión");
				}
				else if(funcName == "Heurística")
				{
					paramsMutDouble.getLabelRef().setText("Params Heurística");
				}
			}
		});
		
		return config;
	}
	
	public ConfigPanel<IGAEngine> creaPanelPruebasAuto() {
		String[] selectorNames = new String[] { "Ruleta", "Torneo Det", "Torneo Prob", "Ranking", "Método Propio" };
		String[] crossNames = new String[] { "PMX", "OX", "Variante OX", "Ordinal", "Método Propio" };
		String[] mutNames = new String[] { "Inserción", "Intercambio", "Inversión", "Heurística" };
	
		ConfigPanel<IGAEngine> config = new ConfigPanel<IGAEngine>();
		
		config.addOption(new IntegerOption<IGAEngine>(  // -- entero
			"Tamaño grupos", 					// texto a usar como etiqueta del campo
			"Número de estudiantes que forman cada grupo",  // texto a usar como 'tooltip' cuando pasas el puntero
			"groupSize",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 500));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
		config.addOption(new IntegerOption<IGAEngine>(  // -- entero
			"Tamaño población", 					// texto a usar como etiqueta del campo
			"Número de individuos que forman la población",  // texto a usar como 'tooltip' cuando pasas el puntero
			"population_Size",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 1000));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
		config.addOption(new IntegerOption<IGAEngine>(
			"Num Generaciones", 					// texto a usar como etiqueta del campo
			"Número de generaciones que dura la evolución",  // texto a usar como 'tooltip' cuando pasas el puntero
			"num_Max_Gen",  						     // campo (espera que haya un getGrosor y un setGrosor)
			1, 10000));							     // min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
	    config.addOption(new DoubleOption<IGAEngine>(  
			"Parámetro Alfa", 						// texto a usar como etiqueta del campo
			"Parámetro Alfa",       				// texto a usar como 'tooltip' cuando pasas el puntero
			"alfaValue",  						    // campo (espera que haya un getGrosor y un setGrosor)
			0.0, 1.0));								// min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)

		// Selection
			
		config.addOption(new ChoiceOption<IGAEngine>(
				"Función de selección",
				"Función selección",
				"selectorName",
				selectorNames));
		
		config.addOption(new DoubleOption<IGAEngine>(
				"Parametros Seleccion",
				"Parametros Seleccion",
				"selecParams", 1.0, 100.0));
		
		// Cross
		config.addOption(new ChoiceOption<IGAEngine>(
				"Función de cruce",
				"Función cruce",
				"crossName",
				crossNames));
		
		config.addOption(new DoubleOption<IGAEngine>(
				"Parametros Cruce",
				"Parametros Cruce",
				"crossParams", 1.0, 100.0));
		
		// Mutation
		config.addOption(new ChoiceOption<IGAEngine>(
				"Función de mutación",
				"Función mutación",
				"mutName",
				mutNames));
		
		config.addOption(new DoubleOption<IGAEngine>(
				"Parametros Mutación",
				"Parametros Mutación",
				"mutParams", 1.0, 100.0));
		
		return config;
	}
			
	// construye y muestra la interfaz
	public void runGUI() {
		// LookAndFeel + posición inicial
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

	public void event(GScene scene, int event, int x, int y) {
		if (scene == null)
			return;

		GObject interaction = scene.find("interaction");
		if (interaction == null) {
			interaction = new GObject("interaction");
			scene.add(interaction);
		}

		interaction.removeSegments();

		double[] w = scene.getTransformer().deviceToWorld(x, y);

		int i = (int) w[1] - 1;
		int j = (int) w[0] - 1;

		if (i < 0 || i >= boardSize || j < 0 || j >= boardSize)
			return;

		switch (event) {
			case GWindow.MOTION:
				if (boardManager.isLegalPos(i, j)) {
					GSegment highlight = new GSegment();
					GStyle highlightStyle = new GStyle();
					highlightStyle.setBackgroundColor(new Color(1.0f, 1.0f, 1.0f, 0.7f));
					highlight.setStyle(highlightStyle);
					interaction.addSegment(highlight);
	
					highlight.setGeometryXy(new double[] { j + 1.0, i + 1.0,
							j + 2.0, i + 1.0, j + 2.0, i + 2.0, j + 1.0, i + 2.0,
							j + 1.0, i + 1.0 });
				}
				break;
	
			case GWindow.BUTTON1_UP:
				if (boardManager.getPos(i,j) == PieceType.NOTHING){
					boardManager.setPosValue(i, j, PieceType.FOOD);
					interaction.removeSegments();
					scene.redraw();
				}
				break;
			
			case GWindow.BUTTON2_UP:
				boardManager.setAntPos(i, j);
				interaction.removeSegments();
				scene.redraw();
				break;
			
			case GWindow.BUTTON3_UP:
				if (boardManager.getPos(i,j) != PieceType.ANT){
					boardManager.setPosValue(i, j, PieceType.NOTHING);
					interaction.removeSegments();
					scene.redraw();
				}
				break;			
		}

	scene.refresh();
	}
}