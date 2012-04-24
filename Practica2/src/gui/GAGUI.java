package gui;

import gui.ConfigPanel.ChoiceOption;
import gui.ConfigPanel.ConfigListener;
import gui.ConfigPanel.DoubleOption;
import gui.ConfigPanel.IntegerOption;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
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
		super("Programación Evolutiva - Práctica 2");
		pGraphic = new Plot2DPanel();
		panelGenetics = new JPanel();
		panelGenetics.setLayout(new MigLayout("", "[center]"));
		panelResultados = new JPanel();
		panelResultados.setLayout(new MigLayout("", "[center]"));
		panelPruebas = new JPanel();
		panelPruebas.setLayout(new MigLayout("debug,flowy", "[left]"));
		
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
		panelCentral.add(progBar, "wrap, span, gaptop 15, gapbottom 10");
		
		// usado por todos los botones
		JButton boton;

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
		panelCentral.add(boton, "bottom, gapright 15, gapleft 7");

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
		
		// Grafica results
		panelResultados.add(new JLabel("Solución encontrada"), "split, gaptop 10");
		panelResultados.add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Set random data for now
        String category;
        for (int i=1; i<=20; i++){
        	category = "G" + i;
        	dataset.addValue(IGARandom.getRInt(10)+5, "Listo", category);
        	dataset.addValue(IGARandom.getRInt(10)+5, "Normal", category);
        	dataset.addValue(IGARandom.getRInt(10)+5, "Subnormal", category);
        }
        		
		JFreeChart chart = ChartFactory.createStackedBarChart(
	            "Grupos Solución",        // chart title
	            "Número de grupo",        // domain axis label
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
        
        plot.getRenderer().setSeriesPaint(0, new Color(65, 105, 225));
        plot.getRenderer().setSeriesPaint(1, new Color(0, 191, 255));
        plot.getRenderer().setSeriesPaint(2, new Color(135, 206, 250));
               
        /* Esto por si interesa más tarde
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
        
        // panel pruebas automaticas
        /*final ConfigPanel<IGAEngine> cpauto = creaPanelConfiguracion();
		// asocia el panel con la figura
        cpauto.setTarget(gaEngine);
		// carga los valores de la figura en el panel
        cpauto.initialize();
		panelPruebas.add(cpauto, "split, wrap");
		
		// botón para ejecutar la evolucion
		boton = new JButton("Run");
		panelPruebas.add(boton, " wrap");
		boton = new JButton("Run");
		panelPruebas.add(boton, "split, wrap");
		boton = new JButton("Run");
		panelPruebas.add(boton, "split, wrap");
		boton = new JButton("Run");
		panelPruebas.add(boton, "split, wrap");
		boton = new JButton("Run");
		panelPruebas.add(boton, "split, wrap");
		boton = new JButton("Run");
		panelPruebas.add(boton, "");*/
		
		// Tabs
		tabPanePrincipal.add(panelGenetics, "Algoritmo Genético");		
		tabPanePrincipal.add(panelResultados, "Grupos Resultado");
		tabPanePrincipal.add(panelPruebas, "Pruebas Automáticas");
		
		add(tabPanePrincipal);
	}
	
	public ConfigPanel<IGAEngine> creaPanelConfiguracion() {
		//String[] functionNames = new String[] { "Alumnos" };
		String[] selectorNames = new String[] { "Ruleta", "Torneo Det", "Torneo Prob", "Ranking", "Método Propio" };
		String[] crossNames = new String[] { "PMX", "OX", "Variante OX", "Ordinal", "Método Propio" };
		String[] mutNames = new String[] { "Inserción", "Intercambio", "Inversión", "Heurística" };
	
		ConfigPanel<IGAEngine> config = new ConfigPanel<IGAEngine>();
		
		config.addOption(
				new IntegerOption<IGAEngine>(  // -- entero
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
		
		/*functChoiceOpt = new ChoiceOption<IGAEngine>(
				"Función de evaluación",
				"Función a evolucionar",
				"functionName",
				functionNames);
		config.addOption(functChoiceOpt);*/

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
		        paramsSelecDouble.getTextFieldRef().enable();
				if(funcName == "Ruleta")
					paramsSelecDouble.getLabelRef().setText("Params Ruleta");
				else if(funcName == "Torneo Prob")
					paramsSelecDouble.getLabelRef().setText("Params Torneo Prob");
				else if(funcName == "Torneo Det")
					paramsSelecDouble.getLabelRef().setText("Params Torneo Det");
				else if(funcName == "Ranking")
					paramsSelecDouble.getLabelRef().setText("Params Ranking");
				else if(funcName == "Método Propio")
					paramsSelecDouble.getLabelRef().setText("Params Método Propio");
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
		        paramsCrossDouble.getTextFieldRef().enable();
				if(funcName == "PMX")
					paramsCrossDouble.getLabelRef().setText("Params PMX");
				else if(funcName == "OX")
					paramsCrossDouble.getLabelRef().setText("Params OX");
				else if(funcName == "Variante OX")
					paramsCrossDouble.getLabelRef().setText("Params Variante OX");
				else if(funcName == "Ordinal")
					paramsCrossDouble.getLabelRef().setText("Params Ordinal");
				else if(funcName == "Método Propio")
					paramsCrossDouble.getLabelRef().setText("Params Método Propio");
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
		        paramsMutDouble.getTextFieldRef().enable();
				if(funcName == "Inserción")
					paramsMutDouble.getLabelRef().setText("Params Inserción");
				else if(funcName == "Intercambio")
					paramsMutDouble.getLabelRef().setText("Params Intercambio");
				else if(funcName == "Inversión")
					paramsMutDouble.getLabelRef().setText("Params Inversión");
				else if(funcName == "Heurística")
				{
					paramsMutDouble.getLabelRef().setText("Params Heurística");
					paramsMutDouble.getTextFieldRef().disable();
				}
			}
		});
		
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

}