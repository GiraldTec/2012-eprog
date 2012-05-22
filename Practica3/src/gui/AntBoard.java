package gui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.GObject;
import no.geosoft.cc.graphics.GPosition;
import no.geosoft.cc.graphics.GSegment;
import no.geosoft.cc.graphics.GStyle;
import no.geosoft.cc.graphics.GText;
import practica3.AntBoardManager;
import practica3.AntBoardManager.PieceType;

public class AntBoard extends GObject {
	private GSegment board;
	private GSegment[] grid;
	private GSegment title;
	private ArrayList<GSegment> pieces;
	private GStyle[] pieceStyle;
	private AntBoardManager antBoardManager;
	private GText foodText;

	public AntBoard(AntBoardManager boardManager) {
		antBoardManager = boardManager;
		antBoardManager.setBoardRef(this);
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
		
		title = new GSegment();
		foodText = new GText (" - Eaten food: 0 - ", GPosition.CENTER);
		GStyle textStyle = new GStyle();
		textStyle.setForegroundColor (new Color (255, 204, 51));
		textStyle.setBackgroundColor(new Color (255, 102, 51));
		textStyle.setFont (new Font ("Dialog", Font.BOLD, 23));
		foodText.setStyle (textStyle);
		title.setText(foodText);
		addSegment(title);
				
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
				piece = new GSegment();
				pieces.add(piece);
				addSegment(piece);
			
				piece.setStyle(pieceStyle[state[i].id - 1]);					
				piece.setGeometry(Geometry.createRectangle(xy[0], xy[1], 21, 16));	
				
				if (state[i] == PieceType.ANT){
					int[] geom = Geometry.createRectangle(xy[0], xy[1], 21, 16);
					int[] arrow = Geometry.createArrow(xy[0], xy[1]+5, xy[0]+20, xy[1]+5, 10, 0.5, 0.7);;
					switch (antBoardManager.getCurrentAntRot()){
					
						case RIGHT:
							arrow = Geometry.createArrow(xy[0], xy[1]+5, xy[0]+20, xy[1]+5, 10, 0.5, 0.7);
							break;
						case LEFT:
							arrow = Geometry.createArrow(xy[0]+20, xy[1]+5, xy[0], xy[1]+5, 10, 0.5, 0.7);
							break;
						case UP:
							arrow = Geometry.createArrow(xy[0]+10, xy[1]+20, xy[0]+10, xy[1], 10, 0.5, 0.7);
							break;
						case DOWN:
							arrow = Geometry.createArrow(xy[0]+10, xy[1]-4, xy[0]+10, xy[1]+12, 10, 0.5, 0.7);
							break;
						default:
							System.err.println("Error dibujando hormiga");
					}
					int[] both = new int[geom.length + arrow.length];
				    System.arraycopy(arrow,0,both,0,arrow.length);
				    System.arraycopy(geom,0,both,arrow.length,geom.length);
					piece.setGeometry(both);
				}
			}
		}
		
		title.setGeometry (360, 15);
	}
	
	public void setFoodText(int ammount){
		foodText.setText("- Eaten food: "+ammount+" -");
	}
}