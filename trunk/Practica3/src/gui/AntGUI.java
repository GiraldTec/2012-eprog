package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.GInteraction;
import no.geosoft.cc.graphics.GObject;
import no.geosoft.cc.graphics.GScene;
import no.geosoft.cc.graphics.GSegment;
import no.geosoft.cc.graphics.GStyle;
import no.geosoft.cc.graphics.GWindow;

public class AntGUI extends JFrame implements GInteraction {
	private static final long serialVersionUID = 5565888368147220570L;
	
	static int boardSize = 32;
	public enum PieceType {
		NOTHING(0),	PATH(1), FOOD(2), EATENFOOD(3), ANT(4);		
		public int id;		
		PieceType(int i){id = i;}
	};
	private AntBoardManager antBoardManager;
	

	public AntGUI(int boardSize) {
		super("G Graphics Library - Demo 13");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create the GUI
		JPanel panelAntBoard = new JPanel();
		panelAntBoard.setLayout(new BorderLayout());
		getContentPane().add(panelAntBoard);

		// Create the graphic canvas
		GWindow window = new GWindow(new Color(0, 220, 220));
		panelAntBoard.add(window.getCanvas(), BorderLayout.CENTER);

		// Create scene
		GScene scene = new GScene(window);
		double w0[] = { 0.0, 0.0, 0.0 };
		double w1[] = { boardSize + 2.0, 0.0, 0.0 };
		double w2[] = { 0.0, boardSize + 2.0, 0.0 };
		scene.setWorldExtent(w0, w1, w2);

		// Create the Reversi game and graphics representation
		antBoardManager = new AntBoardManager(boardSize);
		GObject antBoard = new AntBoard();
		scene.add(antBoard);
		
		pack();
		setSize(new Dimension(600, 600));
		setVisible(true);

		// Make sure plot can be scrolled
		window.startInteraction(this);
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

		if (i < 0 || i >= antBoardManager.getSize() || j < 0
				|| j >= antBoardManager.getSize())
			return;

		switch (event) {
			case GWindow.MOTION:
				if (antBoardManager.isLegalPos(i, j)) {
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
				//if (antBoardManager.isLegalPos(i, j)) {
					antBoardManager.setPosValue(i, j, PieceType.FOOD);
					interaction.removeSegments();
					scene.redraw();
				//}
				break;
				
			case GWindow.BUTTON2_UP:
				antBoardManager.setPosValue(i, j, PieceType.NOTHING);
				interaction.removeSegments();
				scene.redraw();
				break;
		}

		scene.refresh();
	}

	class AntBoard extends GObject {
		private GSegment board;
		private GSegment[] grid;
		private ArrayList<GSegment> pieces;
		private GStyle[] pieceStyle;

		public AntBoard() {
			board = new GSegment();
			GStyle boardStyle = new GStyle();
			boardStyle.setBackgroundColor(new Color(0, 200, 0));
			board.setStyle(boardStyle);
			addSegment(board);

			GStyle gridStyle = new GStyle();
			gridStyle.setForegroundColor(new Color(0, 0, 0));
			gridStyle.setLineWidth(2);
			grid = new GSegment[(antBoardManager.getSize() + 1) * 2];

			for (int i = 0; i < grid.length; i++) {
				grid[i] = new GSegment();
				grid[i].setStyle(gridStyle);
				addSegment(grid[i]);
			}

			pieceStyle = new GStyle[4];
						
			// Path
			pieceStyle[0] = new GStyle();
			pieceStyle[0].setForegroundColor(new Color(255, 255, 255));
			pieceStyle[0].setBackgroundColor(new Color(245, 61, 0));

			// Food
			pieceStyle[1] = new GStyle();
			pieceStyle[1].setForegroundColor(new Color(155, 155, 155));
			pieceStyle[1].setBackgroundColor(new Color(0, 0, 0));
			
			// Eaten Food
			pieceStyle[2] = new GStyle();
			pieceStyle[2].setForegroundColor(new Color(255, 0, 0));
			pieceStyle[2].setBackgroundColor(new Color(0, 0, 0));
			
			// Ant
			pieceStyle[3] = new GStyle();
			pieceStyle[3].setForegroundColor(new Color(255, 255, 255));
			pieceStyle[3].setBackgroundColor(new Color(51, 102, 255));
			
			pieces = new ArrayList<GSegment>();
		}

		public void draw() {
			int size = antBoardManager.getSize();

			// Board
			board.setGeometryXy(new double[] { 1.0, 1.0, size + 1.0, 1.0,
					size + 1.0, size + 1.0, 1.0, size + 1.0, 1.0, 1.0 });

			// Grid lines
			for (int i = 0; i <= size; i++) {
				grid[i * 2 + 0].setGeometry(1.0, i + 1.0, size + 1.0, i + 1.0);
				grid[i * 2 + 1].setGeometry(i + 1.0, 1.0, i + 1.0, size + 1.0);
			}

			// Pieces
			PieceType[] state = antBoardManager.getState();
			int j = 0;
			for (int i = 0; i < state.length; i++) {
				if (state[i] != PieceType.NOTHING) {
					double x = i % size + 1.084;
					double y = i / size + 1.9;

					int[] xy = getTransformer().worldToDevice(x, y);

					GSegment piece;
					if (j < pieces.size())
						piece = (GSegment) pieces.get(j);
					else {
						piece = new GSegment();
						pieces.add(piece);
						addSegment(piece);
					}

					piece.setStyle(pieceStyle[state[i].id - 1]);					
					piece.setGeometry(Geometry.createRectangle(xy[0], xy[1], 15, 15));
					
					j++;
				}
			}
		}
	}

	class AntBoardManager {
		private int size;
		private PieceType[] state;

		public AntBoardManager(int newSize) {
			size = newSize;

			state = new PieceType[size * size];
			for (int i = 0; i < state.length; i++)
				state[i] = Math.random() > 0.9 ? PieceType.FOOD : PieceType.NOTHING;
			
			setPosValue(0,0,PieceType.PATH);
			setPosValue(0,1,PieceType.PATH);
			setPosValue(0,2,PieceType.PATH);
			setPosValue(0,3,PieceType.PATH);
			setPosValue(0,4,PieceType.PATH);
			setPosValue(1,4,PieceType.EATENFOOD);
			setPosValue(2,4,PieceType.PATH);
			setPosValue(3,4,PieceType.PATH);
			setPosValue(4,4,PieceType.ANT);
		}

		public int getSize() {
			return size;
		}

		public PieceType[] getState() {
			return state;
		}
		
		public void setPosValue(int i, int j, PieceType p) {
			state[i * size + j] = p;
		}

		public boolean isLegalPos(int i, int j) {
			return state[i * size + j] == PieceType.NOTHING;
		}
		
		public void move(int i, int j) {
			if (isLegalPos(i, j))
				state[i * size + j] = PieceType.ANT;
		}
				
	}

	public static void main(String[] args) {
		new AntGUI(boardSize);
	}
}
