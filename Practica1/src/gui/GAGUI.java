package gui;

import gui.ConfigPanel.ChoiceOption;
import gui.ConfigPanel.ConfigListener;
import gui.ConfigPanel.DoubleOption;
import gui.ConfigPanel.IntegerOption;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.math.plot.Plot2DPanel;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

import GACore.IGAEngine;

/**
 * @author Ricardo Pragnell, Carlos Gabriel
 */


public class GAGUI extends JFrame implements PropertyChangeListener{
	
	private static final long serialVersionUID = 5393378737313833016L;
	private boolean inputFieldsOK;	// datos de entrada correctos
	private GAStepThread stepThread;
	private double[] dataAbsoluteBest;
	private double[] dataGenerationBest;
	private double[] dataGenerationAverage;
	private double[] dataGenerationCount;
	private Plot2DPanel pGraphic;
	private JProgressBar progBar;
	public String configData;	
	
	@SuppressWarnings("rawtypes")
	public GAGUI(final IGAEngine<?> gaEngine) {
		super("Programaci�n Evolutiva - Pr�ctica 1");
		pGraphic = new Plot2DPanel();
		//WARN: No usar EXIT_ON_CLOSE, threads petan con Plot2DPanel y bugs del JVM
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosed(WindowEvent e) {
	        PrintStream nullStream = new PrintStream(new OutputStream() {
	            public void write(int b) throws IOException {
	            }

	            public void write(byte b[]) throws IOException {
	            }

	            public void write(byte b[], int off, int len) throws IOException {
	            }
	        });
	        System.setErr(nullStream);
	        System.setOut(nullStream);
	        System.exit(0);
	    }
		});
		
		setLayout(new MigLayout("", "[center]"));
		JPanel panelCentral = new JPanel(new MigLayout("", "[left]"));
	
		add(new JLabel("General"), "split, gaptop 10");
		add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// crea un panel central y lo asocia con la primera figura
		final ConfigPanel<IGAEngine> cp = creaPanelConfiguracion();
		// asocia el panel con la figura
		cp.setTarget(gaEngine);
		// carga los valores de la figura en el panel
		cp.initialize();
		add(cp, "growx, wrap");
		
		add(new JLabel("Probabilidad Cruce"), "split, gaptop 10");
		add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// Slider prob cruce
		JSlider crossSlider = new JSlider(0,100,40);
		crossSlider.setMajorTickSpacing(20);
		crossSlider.setMinorTickSpacing(5);
		crossSlider.setPaintTicks(true);
		crossSlider.setPaintLabels(true);
		add(crossSlider, "wrap, growx");
		
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
		
		add(new JLabel("Probabilidad Mutaci�n"), "split, gaptop 10");
		add(new JSeparator(), "growx, wrap, gaptop 10");
		
		// Slider prob mutaci�n
		JSlider mutationSlider = new JSlider(0,100,40);
		mutationSlider.setMajorTickSpacing(20);
		mutationSlider.setMinorTickSpacing(5);
		mutationSlider.setPaintTicks(true);
		mutationSlider.setPaintLabels(true);
		add(mutationSlider, "wrap, growx");
		
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
		add(checkBoxElite, "wrap, gaptop 11");
		
		add(new JLabel("Mensajes"), "split, gaptop 10");
		add(new JSeparator(), "growx, wrap, gaptop 10");
		
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
		add(valido, "wrap, gap 10");
				
		add(new JSeparator(), "wrap, growx, gaptop 10");
		
		// crea una etiqueta que indica la figura que se esta editando
		final JLabel panelEnEdicion = new JLabel("Presione Run");
		panelCentral.add(panelEnEdicion, "wrap, span");
		
		progBar = new JProgressBar();
		progBar.setStringPainted(true);
		panelCentral.add(progBar, "wrap, span, gaptop 15, gapbottom 10");
		
		// usado por todos los botones
		JButton boton;

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
		panelCentral.add(boton, "bottom, gapright 15, gapleft 7");

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
		add(panelCentral, "bottom");
		
		// Gr�fica
		pGraphic.addLegend("SOUTH");
		pGraphic.setSize(600, 600);
		pGraphic.setPreferredSize(new Dimension(600, 600));
		add(pGraphic, "split, grow, gaptop 11, gapleft 20, gapbottom 11, gapright 11, dock east");
	}
	
	@SuppressWarnings("rawtypes")
	public ConfigPanel<IGAEngine> creaPanelConfiguracion() {
		String[] functionNames = new String[] { "Funci�n 1", "Funci�n 2", "Funci�n 3", "Funci�n 4", "Funci�n 5" };
		String[] selectorNames = new String[] { "Ruleta", "Torneo Det", "Torneo Prob" };
		String[] crossNames = new String[] { "Monopunto", "Bipunto" };
	
		ConfigPanel<IGAEngine> config = new ConfigPanel<IGAEngine>();
		
		config.addOption(
				new IntegerOption<IGAEngine>(  // -- entero
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
				"Precisi�n", 									// texto a usar como etiqueta del campo
				"Precisi�n del resultado",       				// texto a usar como 'tooltip' cuando pasas el puntero
				"precision",  						    		// campo (espera que haya un getGrosor y un setGrosor)
				0.001, 0.99));							// min y max (usa Integer.MIN_VALUE /MAX_VALUE para infinitos)
		
		// a�adimos funcionalidad para manejar la n de la funci�n 5
		ChoiceOption<IGAEngine> functChoiceOpt = new ChoiceOption<IGAEngine>("Funci�n de evaluaci�n","Funci�n a evolucionar","functionName",functionNames);
		config.addOption(functChoiceOpt);
		((JComboBox)functChoiceOpt.getControlRef()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox)e.getSource();
		        String funcName = (String)combo.getSelectedItem();
				if(funcName == "Funci�n 5")	{
					String str = JOptionPane.showInputDialog(null, "Introduzca un valor para n : ", "Configuraci�n Funci�n 5", 1);
					 if(str != null)
						 configData = str;
					 else
						 configData = "8";
				}
			}
		});		
		
		config.addOption(new ChoiceOption<IGAEngine>(	 // -- eleccion de objeto no-configurable
			    "Funci�n de selecci�n",						// etiqueta 
			    "Funci�n selecci�n", 						// tooltip
			    "selectorName",   							 		// campo (debe haber un getColor y un setColor)
			    selectorNames))
			    .addOption(new ChoiceOption<IGAEngine>(	 // -- eleccion de objeto no-configurable
			    "Funci�n de cruce",						// etiqueta 
			    "Funci�n cruce", 						// tooltip
			    "crossName",   							// campo (debe haber un getColor y un setColor)
			    crossNames));
	
		return config;
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