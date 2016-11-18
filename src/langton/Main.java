package langton;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

public class Main {
	
	private static DrawPanel p;
	private static JFrame f;
	private static Board board;
	private static Ant ant;
	private static int frameHeight = 1300;
	private static int frameWidth = 1300;
 	private static int boardSize = 120;
	private static int tick = 1; // milliseconds: 20 ms = 50 fps
	private static int k = 0;
	private static int steps = 12000;
	private static int[] origin = {0,0};
	private static int[] newTranslate = {0,0};
	private static int[] translate = {-1600,-1200};
	private static int[] mousePos = {0,0};
	private static boolean moving = false;
	private static float zoom = 3;
	private static int dir = 0;
	
	public static void main(String [] args) {
		
		Main m = new Main();
		m.start();
		
	}
	
	public void start() {
			
		board = new Board(boardSize);
		ant = new Ant(boardSize/2+10, boardSize/2, 0);
		
		f = new JFrame("Langton's Ant");
		
		f.setSize(frameWidth, frameHeight+45);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.addMouseListener(new MouseListener() {
			
			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {
				moving = true;
				origin[0] = mousePos[0];
				origin[1] = mousePos[1];
			}

			public void mouseReleased(MouseEvent e) {
				moving = false;
				translate[0] += newTranslate[0];
				translate[1] += newTranslate[1];
				newTranslate[0] = 0;
				newTranslate[1] = 0;
			}

		});
		
		f.addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				
				double[] percents = getMousePercent();
				double oldCellSize = zoom * frameWidth/boardSize;
				zoom -= (float)e.getWheelRotation()/10;
				double newCellSize = zoom * frameWidth/boardSize;
				
				translate[0] -= percents[0]*boardSize*(newCellSize-oldCellSize);
				translate[1] -= percents[1]*boardSize*(newCellSize-oldCellSize);
			}
			
		});
		
		f.setVisible(true);
		
		p = new DrawPanel();
		
		f.getContentPane().add(p);
		
		try {
			Thread.sleep(tick);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		int t = 0;
		
		while(true) {
			
			updateFrame(f);
			
			p.repaint();
			
			if(moving) {
				newTranslate[0] = mousePos[0] - origin[0];
				newTranslate[1] = mousePos[1] - origin[1];
			}
			
			if (Math.floorMod(t, tick) == 0) {
			
				if(k == 0) dir = 1;
				else if(k == steps)	dir = -1;
			
				if(dir == 1) {
					
					ant.turn(board.getCellState(ant.getX(), ant.getY()));
					board.invertCell(ant.getX(), ant.getY(), 1);
					ant.move();
										
				}
				
				else if(dir == -1) {
					
					ant.reverse();
					ant.turn(board.getCellState(ant.getX(), ant.getY()));
					board.invertCell(ant.getX(), ant.getY(), -1);
					
				}
				
				k+=dir;
				
			}
				
			t++;
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}
	
	private void updateFrame(JFrame f) {
		if(getTrueWidth(f) > (getTrueHeight(f))) frameHeight = getTrueHeight(f);
		else frameWidth = f.getWidth();
		
		frameWidth = getTrueWidth(f);
		frameHeight = getTrueHeight(f);
	}
	
	private int[] getMousePos() {
		int x = MouseInfo.getPointerInfo().getLocation().x - f.getX() - 13;
		int y = MouseInfo.getPointerInfo().getLocation().y - f.getY() - 13;
		int[] pos = {x,y};
		return pos;
	}
	
	private double[] getMousePercent() {
		int[] mousePos = getMousePos();
		double[] percents = {0,0};
		percents[0] = (double) mousePos[0]/frameWidth;
		percents[1] = (double) mousePos[1]/frameHeight;
		
		for(int i = 0; i < 2; i++) {
			if(percents[i] < 0) percents[i] = 0;
			if(percents[i] > 1) percents[i] = 1;
		}
		
		return percents;
	}
	
	private int getTrueHeight(JFrame f) {
		return f.getHeight()-45;
	}
	
	private int getTrueWidth(JFrame f) {
		return f.getWidth();
	}

	class DrawPanel extends JPanel {
	
		private static final long serialVersionUID = 1L;
		
			public DrawPanel() {};
			
			public void paintComponent(Graphics g) {
				
				int buffer = 50;
				int edge = 25;
				int shadowOffset = 8;
				int cellSize = 0;
				
				if(frameWidth > frameHeight) cellSize = (int) (zoom*frameHeight/boardSize);
				else cellSize = (int) (zoom*frameWidth/boardSize);
								
				int antXLoc = (buffer+cellSize*ant.getX()+translate[0]+newTranslate[0]);
				int antYLoc = (buffer+cellSize*ant.getY()+translate[1]+newTranslate[1]);
				
				g.setColor(new Color(230, 230, 230));
				g.fillRect(0, 0, 2*frameWidth, 2*frameHeight);
				

				g.setColor(Color.WHITE);
				g.fillRect(0, 0, 2*frameWidth, 2*frameHeight);
				
				for(int i = 0; i < boardSize; i++) {
					for(int j = 0; j < boardSize; j++) {
						
						
						g.setColor(new Color(255-board.getCellCount(i, j)*6,255-board.getCellCount(i, j)*6,255-board.getCellCount(i, j)*6));
						g.fillRect(buffer+cellSize*i+translate[0]+newTranslate[0],buffer+cellSize*j+translate[1]+newTranslate[1],cellSize,cellSize);
						
						if(board.getCellState(i,j)) g.setColor(Color.WHITE);
						else g.setColor(Color.BLACK);
						g.fillRect(buffer+cellSize*i+translate[0]+newTranslate[0]+cellSize/4,buffer+cellSize*j+translate[1]+newTranslate[1]+cellSize/4,cellSize/2,cellSize/2);
								
						if(i == 0 || j == 0 || i == boardSize-1 || j == boardSize-1) {
							g.setColor(Color.GRAY);
							g.fillRect(buffer+cellSize*i+translate[0]+newTranslate[0]+cellSize/8, buffer+cellSize*j+translate[1]+newTranslate[1]+cellSize/8, cellSize-cellSize/4, cellSize-cellSize/4);
		
						}
						
					}
				}
				
				int[] xpoints = new int[5];
				int[] ypoints = new int[5];
				
				switch(ant.getDir()) {
				
					case 0:
						xpoints = new int[]{antXLoc, antXLoc+cellSize/2, antXLoc+cellSize};
						ypoints = new int[]{antYLoc+cellSize, antYLoc, antYLoc+cellSize};
						break;
					case 1:
						xpoints = new int[]{antXLoc, antXLoc+cellSize, antXLoc};
						ypoints = new int[]{antYLoc, antYLoc+cellSize/2, antYLoc+cellSize};
						break;
					case 2:
						xpoints = new int[]{antXLoc, antXLoc+cellSize/2, antXLoc+cellSize};
						ypoints = new int[]{antYLoc, antYLoc+cellSize, antYLoc};
						break;
					case 3:
						xpoints = new int[]{antXLoc+cellSize, antXLoc, antXLoc+cellSize};
						ypoints = new int[]{antYLoc, antYLoc+cellSize/2, antYLoc+cellSize};
						break;
				}
				
				g.setColor(Color.RED);
				g.fillPolygon(xpoints, ypoints, 3);
								
				g.setColor(new Color(230, 230, 230));
				g.fillRect(0, 0, buffer, 2*frameHeight);
				g.fillRect(0, 0, 2*frameHeight, buffer);
				g.fillRect(frameWidth-buffer-edge, 0, frameWidth, 2*frameHeight);
				g.fillRect(0, frameHeight-buffer-edge, 2*frameWidth, frameHeight);
								
				g.setColor(Color.GRAY);
				g.fillRect(frameWidth-buffer-edge, buffer+shadowOffset, shadowOffset, frameHeight-2*buffer-edge);
				g.fillRect(buffer+shadowOffset, frameHeight-buffer-edge, frameWidth-2*buffer-edge, shadowOffset);
				
				g.setColor(Color.BLACK);
				g.drawRect(buffer, buffer, frameWidth-2*buffer-edge, frameHeight-2*buffer-edge);
				
				mousePos = getMousePos();
				double[] percents = getMousePercent();
				g.setColor(Color.GRAY);
				g.drawString("Step: "+k+" of "+steps+" Zoom: "+(double)Math.round(zoom*10)/10+" Mouse: "+mousePos[0]+" "+mousePos[1]+" Translate: "+newTranslate[0]+" "+newTranslate[1]+" "+translate[0]+" "+translate[1], buffer+10, buffer+20);
				g.drawString("Window: "+frameWidth+" "+frameHeight, buffer+10, buffer+40);
				g.drawString("Scaling: "+(double)Math.round(percents[0]*100)/100+" "+(double)Math.round(percents[1]*100)/100,buffer+10, buffer+60);
				g.drawString("Zoom and pan!", frameWidth-buffer-edge-90, buffer+20);
			}

	}
}
