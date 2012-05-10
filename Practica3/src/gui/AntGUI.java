package gui;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.*;

import javax.swing.*;

import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.*;

public class AntGUI extends JFrame implements GInteraction {
	private static final long serialVersionUID = 5565888368147220570L;
	static int boardSize = 32;
	
	private Reversi reversi_;

	public AntGUI(int boardSize) {
		super("G Graphics Library - Demo 13");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create the GUI
		JPanel topLevel = new JPanel();
		topLevel.setLayout(new BorderLayout());
		getContentPane().add(topLevel);

		// Create the graphic canvas
		GWindow window = new GWindow(new Color(0, 220, 220));
		topLevel.add(window.getCanvas(), BorderLayout.CENTER);

		// Create scene
		GScene scene = new GScene(window);
		double w0[] = { 0.0, 0.0, 0.0 };
		double w1[] = { boardSize + 2.0, 0.0, 0.0 };
		double w2[] = { 0.0, boardSize + 2.0, 0.0 };
		scene.setWorldExtent(w0, w1, w2);

		// Create the Reversi game and graphics representation
		reversi_ = new Reversi(boardSize);
		GObject reversiBoard = new ReversiBoard();
		scene.add(reversiBoard);
		
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

		if (i < 0 || i >= reversi_.getSize() || j < 0
				|| j >= reversi_.getSize())
			return;

		switch (event) {
		case GWindow.MOTION:
			if (reversi_.isLegalMove(i, j)) {
				GSegment highlight = new GSegment();
				GStyle highlightStyle = new GStyle();
				highlightStyle.setBackgroundColor(new Color(1.0f, 1.0f, 1.0f,
						0.7f));
				highlight.setStyle(highlightStyle);
				interaction.addSegment(highlight);

				highlight.setGeometryXy(new double[] { j + 1.0, i + 1.0,
						j + 2.0, i + 1.0, j + 2.0, i + 2.0, j + 1.0, i + 2.0,
						j + 1.0, i + 1.0 });
			}
			break;

		case GWindow.BUTTON1_UP:
			if (reversi_.isLegalMove(i, j)) {
				reversi_.move(i, j);
				interaction.removeSegments();
				scene.redraw();
			}
		}

		scene.refresh();
	}

	class ReversiBoard extends GObject {
		private GSegment board_;
		private GSegment[] grid_;
		private ArrayList<GSegment> pieces_; // og GSegment
		private GStyle[] pieceStyle_;

		public ReversiBoard() {
			board_ = new GSegment();
			GStyle boardStyle = new GStyle();
			boardStyle.setBackgroundColor(new Color(0, 200, 0));
			board_.setStyle(boardStyle);
			addSegment(board_);

			GStyle gridStyle = new GStyle();
			gridStyle.setForegroundColor(new Color(0, 0, 0));
			gridStyle.setLineWidth(2);
			grid_ = new GSegment[(reversi_.getSize() + 1) * 2];

			for (int i = 0; i < grid_.length; i++) {
				grid_[i] = new GSegment();
				grid_[i].setStyle(gridStyle);
				addSegment(grid_[i]);
			}

			pieceStyle_ = new GStyle[2];
			pieceStyle_[0] = new GStyle();
			pieceStyle_[0].setForegroundColor(new Color(255, 255, 255));
			pieceStyle_[0].setBackgroundColor(new Color(255, 255, 255));

			pieceStyle_[1] = new GStyle();
			pieceStyle_[1].setForegroundColor(new Color(0, 0, 0));
			pieceStyle_[1].setBackgroundColor(new Color(0, 0, 0));

			pieces_ = new ArrayList<GSegment>();
		}

		public void draw() {
			int size = reversi_.getSize();

			// Board
			board_.setGeometryXy(new double[] { 1.0, 1.0, size + 1.0, 1.0,
					size + 1.0, size + 1.0, 1.0, size + 1.0, 1.0, 1.0 });

			// Grid lines
			for (int i = 0; i <= size; i++) {
				grid_[i * 2 + 0].setGeometry(1.0, i + 1.0, size + 1.0, i + 1.0);
				grid_[i * 2 + 1].setGeometry(i + 1.0, 1.0, i + 1.0, size + 1.0);
			}

			// Pieces
			int[] state = reversi_.getState();
			int j = 0;
			for (int i = 0; i < state.length; i++) {
				if (state[i] != 0) {
					double y = i / size + 1.5;
					double x = i % size + 1.5;

					int[] xy = getTransformer().worldToDevice(x, y);

					GSegment piece;
					if (j < pieces_.size())
						piece = (GSegment) pieces_.get(j);
					else {
						piece = new GSegment();
						pieces_.add(piece);
						addSegment(piece);
					}

					j++;

					piece.setStyle(pieceStyle_[state[i] - 1]);
					piece.setGeometry(Geometry.createCircle(xy[0], xy[1], 15 - boardSize/4));// createRectangle(xy[0], xy[0]+5, xy[1], xy[1]+5));
				}
			}
		}
	}

	class Reversi {
		private int size_;
		private int[] state_;
		private int player_;

		public Reversi(int size) {
			size_ = size;

			state_ = new int[size_ * size_];
			for (int i = 0; i < state_.length; i++)
				state_[i] = 0;

			state_[(size_ / 2 - 1) * size_ + size_ / 2 - 1] = 1;
			state_[(size_ / 2 - 1) * size_ + size_ / 2] = 2;
			state_[(size_ / 2) * size_ + size_ / 2 - 1] = 2;
			state_[(size_ / 2) * size_ + size_ / 2] = 1;

			player_ = 1;
		}

		public int getSize() {
			return size_;
		}

		public int[] getState() {
			return state_;
		}

		public boolean isLegalMove(int i, int j) {
			return state_[i * size_ + j] == 0
					&& (win(i, j, player_)).length > 0;
		}

		public void move(int i, int j) {
			int[] win = win(i, j, player_);
			for (int s = 0; s < win.length; s++)
				state_[win[s]] = player_;
			state_[i * size_ + j] = player_;

			player_ = player_ == 1 ? 2 : 1;
		}

		private int[] win(int i, int j, int color) {
			ArrayList<Integer> win = new ArrayList<Integer>();
			win.addAll(win(i, j, color, +1, 0));
			win.addAll(win(i, j, color, +1, +1));
			win.addAll(win(i, j, color, 0, +1));
			win.addAll(win(i, j, color, -1, +1));
			win.addAll(win(i, j, color, -1, 0));
			win.addAll(win(i, j, color, -1, -1));
			win.addAll(win(i, j, color, 0, -1));
			win.addAll(win(i, j, color, +1, -1));

			int[] a = new int[win.size()];
			int s = 0;
			for (Iterator<Integer> t = win.iterator(); t.hasNext(); s++)
				a[s] = ((Integer) t.next()).intValue();

			return a;
		}

		private ArrayList<Integer> win(int i, int j, int color, int dx, int dy) {
			ArrayList<Integer> win = new ArrayList<Integer>();

			while (true) {
				i += dx;
				j += dy;

				if (i < 0 || i == size_ || j < 0 || j == size_
						|| state_[i * size_ + j] == 0)
					return new ArrayList<Integer>();

				else if (state_[i * size_ + j] == color)
					return win;
				else if (state_[i * size_ + j] != color)
					win.add(new Integer(i * size_ + j));
			}
		}
	}

	public static void main(String[] args) {
		new AntGUI(boardSize);
	}
}
