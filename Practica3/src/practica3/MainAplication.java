package practica3;

import gui.GAGUI;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import GA.GAAntEngine;
import GACore.IGAEngine;
import GACore.IGARandom;

public class MainAplication {
	private static IGAEngine gaEngine;
	private static GAGUI gui;
	
	// configuracion de formato de salida de los logs
	private static SimpleFormatter myFormater = new SimpleFormatter() {

		public synchronized String format(LogRecord record) {
		return ""+record.getLevel() + " -- " + record.getMessage() + "\n";
		}
	};
	
	public static void main(String[] args) {
		IGARandom.setSeed(55);					// damos valor a la semilla
		
		gaEngine = new GAAntEngine();		// creamos el motor genético
		gui = new GAGUI(gaEngine);				// creamos la interfaz
		gaEngine.loadConfig(gui.configData);	// cargamos configuración adicional (opcional)
		gui.runGUI();
		
		Logger log = Logger.getLogger("");
		for (Handler h : log.getHandlers()) log.removeHandler(h);
		log = Logger.getLogger("Engine");
		log.addHandler(new ConsoleHandler());
		log.getHandlers()[0].setFormatter(myFormater);

		// opciones: FINEST, FINER, FINE, INFO, CONFIG, WARNING, SEVERE
		log.getHandlers()[0].setLevel(Level.OFF);
	}
}
