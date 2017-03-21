import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOfLife {
	
	private static Field field;
	private static DrawPanel panel;
	
	private static boolean paused = true;
	private static int tick = 40; // milliseconds
	private static int board = 40;
	private static int windowSize = 700;
	private static int cellSize = (windowSize+8)/board;
	private static boolean tracers = false;
	private static boolean spiceItUp = true;
	private static int gliderRate = 10;
	private static int sparsity = 4;
	private static int texturePack = 1;
	
	private static JButton launchButton, tracerToggle, gliderToggle, repopulate, sparsityInput;
	private static JButton spiceFactor, timeButton, clearButton, spawnButton, textureSwap;
	
	public static void main(String[] args) {
		GameOfLife game = new GameOfLife();
		game.play();
	}
	
	public void play() {

		JFrame f = new JFrame("Game of Life");
		field = new Field(board, tick);
		
		f.setSize(windowSize + 15, windowSize + 38);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new DrawPanel();
		
		////////////////////////////////////////////////////////////////////////////
		
		final JFrame m = new JFrame("Settings");
		
		m.setSize(200, 300);
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		launchButton = new JButton("Start Game");
		launchButton.setPreferredSize(new Dimension(60, 40));
		launchButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				paused = !paused;
				if(!paused) launchButton.setText("Pause Game");
				else launchButton.setText("Unpause Game");
			}
			
		});
		
		tracerToggle = new JButton("Enable Tracers");
		tracerToggle.setPreferredSize(new Dimension(40, 40));
		tracerToggle.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				tracers = !tracers;
				if(tracers) tracerToggle.setText("Disable Tracers");
				else tracerToggle.setText("Enable Tracers");
			}
			
		});
		
		gliderToggle = new JButton("Disable Spawning");
		gliderToggle.setPreferredSize(new Dimension(40, 40));
		gliderToggle.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				spiceItUp = !spiceItUp;
				if(spiceItUp) gliderToggle.setText("Disable Spawning");
				else gliderToggle.setText("Enable Spawning");
			}
			
		});
		
		repopulate = new JButton("Repopulate");
		repopulate.setPreferredSize(new Dimension(40, 40));
		repopulate.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Field.populate(sparsity);
			}
			
		});
		
		sparsityInput = new JButton("Set Density (" + (12 - sparsity) + ")");
		sparsityInput.setPreferredSize(new Dimension(40, 40));
		sparsityInput.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(sparsity == 2) sparsity = 11;
				else sparsity--;
				
				sparsityInput.setText("Set Density (" + (12 - sparsity) + ")");
			}
			
		});

		spiceFactor = new JButton("Set Spice Factor (" + (12 - gliderRate) + ")");
		spiceFactor.setPreferredSize(new Dimension(40, 40));
		spiceFactor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(gliderRate == 2) gliderRate = 11;
				else gliderRate--;
				
				spiceFactor.setText("Set Spice Factor (" + (12 - gliderRate) + ")");
			}
			
		});
		
		textureSwap = new JButton("Switch Textures (1)");
		textureSwap.setPreferredSize(new Dimension(60, 40));
		textureSwap.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(texturePack == 3)
					texturePack = 1;
				else
					texturePack++;
				textureSwap.setText("Switch Textures" + "(" + texturePack + ")");
			}
			
		});

		timeButton = new JButton("Step Forward");
		timeButton.setPreferredSize(new Dimension(60, 40));
		timeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if(!paused) {
				paused = true;
				launchButton.setText("Unpause Game");
				}
				
				else {
					Field.update(tracers);
					if(spiceItUp) Field.spiceItUp(gliderRate);
					panel.repaint();
				}
			}
			
		});
		
		clearButton = new JButton("Clear Board");
		clearButton.setPreferredSize(new Dimension(60, 40));
		clearButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < board; i++) {
					for(int j = 0; j < board; j++) {
						if(Field.getCellState(i, j)){
							Field.invertCellState(i, j);
						}
					}
				}
				panel.repaint();
			}
		});
		
		spawnButton = new JButton("Spawn Player");
		spawnButton.setPreferredSize(new Dimension(60, 40));
		spawnButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Field.getBackground()[20][20] = new PlayerCell(20, 20);
			}
			
		});
		
		f.getContentPane().add(panel);
		
		GridLayout g = new GridLayout(0, 1);
		m.setLayout(g);
		
		m.add(launchButton);
		m.add(tracerToggle);
		m.add(gliderToggle);
		m.add(repopulate);
		m.add(sparsityInput);
		m.add(spiceFactor);
//		m.add(randomButton);
		m.add(textureSwap);
		m.add(timeButton);
		m.add(clearButton);
		m.add(spawnButton);
		
		//////////////////////////////////////////////////////////////////
		
		f.addMouseListener(new MouseListener() {
			
			public void mouseClicked(MouseEvent e) {
				int x = e.getX()-cellSize/3;
				int y = e.getY()-3*cellSize;
				int xIndex = 0;
				int yIndex = 0;

				while (x > cellSize) {
					x -= cellSize;
					xIndex++;
				}

				while (y > cellSize) {
					y -= cellSize;
					yIndex++;
				}

				Field.invertCellState(xIndex, yIndex);
				panel.repaint();
			}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {}

			public void mouseReleased(MouseEvent e) {}

		});

		//////////////////////////////////////////////////////////////////
		
		f.setVisible(true);
		m.setVisible(true);
		
		Field.populate(sparsity);
		panel.repaint();
		
		while(true) {
			updateBoard(f);
			if(!paused) {
				Field.update(tracers);
				if(spiceItUp) {
					Field.spiceItUp(gliderRate);
				}
			}
			panel.repaint(); 
			
			try {
				Thread.sleep(tick);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	private void updateBoard(JFrame f) {
		windowSize = f.getWidth() - 15;
		if(windowSize > f.getHeight() - 38) windowSize = f.getHeight();
		cellSize = (windowSize+8)/board;
	}

	class DrawPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		public DrawPanel() {}

		public void paintComponent(Graphics g) {
			
			switch(texturePack) {
				case 1: basic(g);
						break;
				case 2: rainbow(g);
						break;
				case 3:	numbers(g);
						break;
			
			}
		
		}

		private void basic(Graphics g) {
			
			// background paint
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, windowSize * 2, windowSize * 2);
			
			// background cell paint
			int colorIndex;
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getBackgroundCellState(i, j) > 0) {
						colorIndex = 255 - Field.getBackgroundCellState(i, j) * 10;
						g.setColor(new Color(colorIndex, colorIndex, colorIndex));
						g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
						g.setColor(Color.GRAY);
							
					}
				}
			}
				
			
			// paint cells
			g.setColor(Color.BLACK);
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getCellState(i, j)) {
						g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
					}
				}
			}
			
			// paint cell outlines
			g.setColor(Color.BLACK);
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getCellState(i, j)) {
						g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
					}
				}
			}
			
			// paint player
			g.setColor(Color.RED);
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getBackgroundCellAt(i, j).isPlayer()) {
						g.fillOval(i * cellSize-1, j * cellSize-1, cellSize+2, cellSize+2);
					}
				}
			}
			
			// paint stats
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(10, 10, 120, 40);
			g.setColor(Color.BLACK);
			g.drawString("Population: " + field.getPopulation(), 16, 24);
			g.drawString("Time: " + field.getTimeInSeconds(), 16, 44);
			
		}
		
		private void rainbow(Graphics g) {
			
			// background paint
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, windowSize * 2, windowSize * 2);
						
			// background cell paint
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getBackgroundCellState(i, j) > 0) {
						g.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
						g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
					}
				}
			}
							
			
			// paint cells
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getCellState(i, j)) {
						g.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
						g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
					}
				}
			}
						
			// paint cell outlines
			g.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getCellState(i, j)) {
						g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
					}
				}
			}
			
			// paint player
			g.setColor(Color.RED);
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getBackgroundCellAt(i, j).isPlayer()) {
						g.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
						g.fillOval(i * cellSize-1, j * cellSize-1, cellSize+2, cellSize+2);
						g.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
						g.fillOval(i * cellSize+2, j * cellSize+2, cellSize-4, cellSize-4);
					}
				}
			}
			
			// paint stats
			int r1 = (int)(Math.random()*255);
			int g1 = (int)(Math.random()*255);
			int b1 = (int)(Math.random()*255);
			int r2 = 255 - r1;
			int g2 = 255 - g1;
			int b2 = 255 - b1;
			
			g.setColor(new Color(r1, g1, b1));
			g.fillRect(10, 10, 120, 40);
			g.setColor(Color.BLACK);
			g.drawRect(10, 10, 120, 40);
			g.setColor(new Color(r2, g2, b2));
			g.drawString("Population: " + field.getPopulation(), 16, 24);
			g.drawString("Time: " + field.getTimeInSeconds(), 16, 44);
		
		}
		
		private void numbers(Graphics g) {
			
			// background paint
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, windowSize * 2, windowSize * 2);
			
			// dead cell paint
			g.setColor(Color.LIGHT_GRAY);
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(!Field.getCellState(i, j)) {
						g.drawString("0", i * cellSize + 2, j * cellSize + cellSize - 2);
					}
				}
			}
			
			// background cell paint
			int colorIndex;
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getBackgroundCellState(i, j) > 0) {
						colorIndex = 255 - Field.getBackgroundCellState(i, j) * 10;
						g.setColor(new Color(colorIndex, colorIndex, colorIndex));
						g.drawString("1", j * cellSize + 2, i * cellSize + cellSize - 2);
					}
				}
			}
			
			// paint cells
			g.setColor(Color.BLACK);
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getCellState(i, j)) {
						g.drawString("1", i * cellSize + 2, j * cellSize + cellSize - 2);
					}
				}
			}
			
			// paint cell outlines
			// no outlines for this pack
			
			// paint player
			g.setColor(Color.BLACK);
			for (int i = 0; i < board; i++) {
				for (int j = 0; j < board; j++) {
					if(Field.getBackgroundCellAt(i, j).isPlayer()) {
						g.fillRect(i * cellSize-1, j * cellSize-1, cellSize+2, cellSize+2);
					}
				}
			}
			
			// paint stats
			g.setColor(Color.WHITE);
			g.fillRect(10, 10, 120, 40);
			g.setColor(Color.BLACK);
			g.drawRect(10, 10, 120, 40);
			g.drawString("Population: " + field.getPopulation(), 16, 24);
			g.drawString("Time: " + field.getTimeInSeconds(), 16, 44);
			
		}
		
	} // end of DrawPanel
}