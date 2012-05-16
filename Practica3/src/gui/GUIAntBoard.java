package gui;

import java.awt.Color;
import java.util.ArrayList;

import practica3.AntBoardManager;
import practica3.AntBoardManager.PieceType;

import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.GObject;
import no.geosoft.cc.graphics.GSegment;
import no.geosoft.cc.graphics.GStyle;

class GUIAntBoard extends GObject {
	private GSegment board;
	private GSegment[] grid;
	private ArrayList<GSegment> pieces;
	private GStyle[] pieceStyle;
	private AntBoardManager antBoardManager;

	public GUIAntBoard(AntBoardManager boardManager) {
		antBoardManager = boardManager;
		board = new GSegment();
		GStyle boardStyle = new GStyle();
		boardStyle.setBackgroundColor(new Color(102, 204, 51));
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
		for (GSegment s : pieces){
			removeSegment(s);
		}
		
		PieceType[] state = antBoardManager.getState();
		for (int i = 0; i < state.length; i++) {
			if (state[i] != PieceType.NOTHING) {
				double x = i % size + 1.084;
				double y = i / size + 1.9;

				int[] xy = getTransformer().worldToDevice(x, y);

				GSegment piece;
				/*if (j < pieces.size())
					piece = (GSegment) pieces.get(j);
				else {
					piece = new GSegment();
					pieces.add(piece);
					addSegment(piece);
				}*/

				piece = new GSegment();
				pieces.add(piece);
				addSegment(piece);
			
				piece.setStyle(pieceStyle[state[i].id - 1]);					
				piece.setGeometry(Geometry.createRectangle(xy[0], xy[1], 21, 16));
			}
		}
	}
}