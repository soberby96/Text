package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * ����˹������Ϸ������
 * @author Leslie Leung
 */
public class TetrisPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6391677703154264004L;
	public static final int ROWS = 20;	//��������������
	public static final int COLUMNS = 16;	//��������������
	
	/* ��ʾ7�ֲ�ͬ���ĸ񷽿� */
	public static final int I_SHAPED = 0;
	public static final int S_SHAPED = 1;
	public static final int T_SHAPED = 2;
	public static final int Z_SHAPED = 3;
	public static final int L_SHAPED = 4;
	public static final int O_SHAPED = 5;
	public static final int J_SHAPED = 6;
	
	public static final int KIND = 7;	//��ʾ�ĸ񷽿���7������
	public static final int INIT_SPEED = 1000;	//��ʾ����ĳ�ʼ�ٶ�
	
	private static int randomNum = 0;	//��ʾ�����ɵĶ���˹�������Ŀ
	
	private Random random;
	private Tetromino currentTetromino;	//��ʾ��ǰ���ĸ񷽿�
	private Cell[][] wall;		//��ʾǽ��null��ʾ������û����
	private Timer autoDrop;		//ʵ���Զ�����ļ�ʱ��
	private KeyControl keyListener;	//��ʾ�����¼���ر���
	
	/**
	 * ���췽��
	 */
	public TetrisPane() {
		setPreferredSize(new Dimension(COLUMNS * Cell.CELL_SIZE, ROWS * Cell.CELL_SIZE));
		
		random = new Random();
		wall = new Cell[ROWS][COLUMNS];
		autoDrop = new Timer();
		keyListener = new KeyControl();
		
		randomOne();
		
		autoDrop.schedule(new DropExecution(), (long)interval(), (long)interval());
	}
	
	/**
	 * �������һ���ĸ񷽿�
	 */
	public void randomOne() {
		Tetromino tetromino = null;
		
		/* �������7���ĸ񷽿������һ�� */
		switch(random.nextInt(KIND)) {
			case I_SHAPED: 
				tetromino = new IShaped();
				break;
			case S_SHAPED: 
				tetromino = new SShaped();
			   	break;
			case T_SHAPED: 
				tetromino = new TShaped();
			    break;
			case Z_SHAPED: 
				tetromino = new ZShaped();
			    break;
			case L_SHAPED: 
				tetromino = new LShaped();
			    break;
			case O_SHAPED: 
				tetromino = new OShaped();
			    break;
			case J_SHAPED: 
				tetromino = new JShaped();
			    break;
		}
		currentTetromino = tetromino;	//��ǰ���ĸ񷽿�Ϊ���ɵ��ĸ񷽿�
		randomNum ++;
	}
	
	/**
	 * �ж�����Ƿ�����
	 * @return true�����ˣ�false��û��
	 */
	public boolean isGameOver() {
		int x, y;	//��ǰ����˹������ӵĺ������������
		for(int i = 0; i < getCurrentCells().length; i ++) {
			x = getCurrentCells()[i].getX();
			y = getCurrentCells()[i].getY();
			
			if(isContain(x, y)) {//��������ɵ�λ���Ƿ��Ѵ��ڷ�����󣬴��ڵĻ�����ʾ����
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * ÿ����һ������˹���飬ͨ���ı�TimerTask��ʱ�������ӿ������ٶ�
	 * @return ʱ����
	 */
	public double interval() {
		return INIT_SPEED * Math.pow((double)39 / 38, 0 - randomNum);
	}
	
	/**
	 * ����KeyControl���ʵ��
	 * @return KeyControl��ʵ��
	 */
	public KeyControl getInnerInstanceOfKeyControl() {
		return keyListener;
	}
	
	/**
	 * �ڲ��࣬����ʵ�ֶ���˹������Զ�����
	 * @author Leslie Leung
	 */
	private class DropExecution extends TimerTask {	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			if(isGameOver()) {//�������
				JOptionPane.showMessageDialog(null, "������");
				autoDrop.cancel();
				removeKeyListener(keyListener);
				return;
			}
			
			if(!isReachBottomEdge()) {
				currentTetromino.softDrop();
			} else {
				landIntoWall();		//�Ѷ���˹������ӵ�ǽ��
				removeRows();	//�����У�ɾ����
				randomOne();	//�����½�һ������˹����
				
				autoDrop.cancel();
				autoDrop = new Timer();
				autoDrop.schedule(new DropExecution(), (long)interval(), (long)interval());
			}
			
			repaint();
		}	
	}
	
	/**
	 * �Ѷ���˹������ӵ�ǽ��
	 */
	public void landIntoWall() {
		int x, y;	//�������˹���鲻���ƶ���ĺ������������
		
		for(int i = 0; i < getCurrentCells().length; i ++) {
			x = getCurrentCells()[i].getX();
			y = getCurrentCells()[i].getY();
			
			wall[y][x] = getCurrentCells()[i];	//��ӵ�ǽ��
		}
	}
	
	/**
	 * �ڲ��࣬����ʵ�ּ��̵��¼�����
	 * @author Leslie Leung
	 */
	private class KeyControl extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT: //�����ƶ�
					
					if(!isReachLeftEdge()) {//������˹����û������߽�ʱ�������ƶ�
						currentTetromino.moveLeft();
						repaint();
					}					
					break;
					
				case KeyEvent.VK_RIGHT:	//�����ƶ�
					
					if(!isReachRightEdge()) {//������˹����û�����ұ߽�ʱ�������ƶ�
						currentTetromino.moveRight();
						repaint();
					}
					break;
				
				case KeyEvent.VK_DOWN:	//�����ƶ�
					
					if(!isReachBottomEdge()) {//������˹����û�����±߽�ʱ�������ƶ�
						currentTetromino.softDrop();
						repaint();
					}
					
					break;
					
				case KeyEvent.VK_SPACE:	//Ӳ����
					
					hardDrop();	//Ӳ����
					landIntoWall();		//��ӵ�ǽ��
					removeRows();	//�����У�ɾ����
					
					randomOne();
					autoDrop.cancel();
					autoDrop = new Timer();
					autoDrop.schedule(new DropExecution(), (long)interval(), (long)interval());
					
					repaint();
					break;
					
				case KeyEvent.VK_D:	//˳ʱ��ת
					
					if(!clockwiseRotateIsOutOfBounds() && !(currentTetromino instanceof OShaped)) {//����˹����ûԽ�����䲻ΪO��ʱ����ת
						currentTetromino.clockwiseRotate(getAxis(), getRotateCells());
						repaint();
					}
					break;
					
				case KeyEvent.VK_A:	//��ʱ��ת
					
					if(!anticlockwiseRotateIsOutOfBounds() && !(currentTetromino instanceof OShaped)) {//����˹����ûԽ�����䲻ΪO��ʱ����ת
						currentTetromino.anticlockwiseRotate(getAxis(), getRotateCells());
						repaint();
					}
					break;
			}
		}
	}
	
	/**
	 * �ڲ��࣬I�ε��ĸ񷽿飬�̳���Tetromino��
	 * @author Leslie Leung
	 */
	private class IShaped extends Tetromino {
		/**
		 * ���췽��
		 */
		public IShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_CYAN);
			cells[0] = new Cell(4, 0, Cell.COLOR_CYAN);
			cells[2] = new Cell(5, 0, Cell.COLOR_CYAN);
			cells[3] = new Cell(6, 0, Cell.COLOR_CYAN);
			
			/* ������ת���Ҫ��ת�ĸ��� */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * �ڲ��࣬S�ε��ĸ񷽿飬�̳���Tetromino��
	 * @author Leslie Leung
	 */
	private class SShaped extends Tetromino {
		/**
		 * ���췽��
		 */
		public SShaped() {
			cells = new Cell[4];
			
			cells[0] = new Cell(4, 0, Cell.COLOR_BLUE);
			cells[1] = new Cell(5, 0, Cell.COLOR_BLUE);
			cells[2] = new Cell(3, 1, Cell.COLOR_BLUE);
			cells[3] = new Cell(4, 1, Cell.COLOR_BLUE);
			
			/* ������ת���Ҫ��ת�ĸ��� */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * �ڲ��࣬T�ε��ĸ񷽿飬�̳���Tetromino��
	 * @author Leslie Leung
	 */
	private class TShaped extends Tetromino {
		/**
		 * ���췽��
		 */
		public TShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_GREEN);
			cells[0] = new Cell(4, 0, Cell.COLOR_GREEN);
			cells[2] = new Cell(5, 0, Cell.COLOR_GREEN);
			cells[3] = new Cell(4, 1, Cell.COLOR_GREEN);
			
			/* ������ת���Ҫ��ת�ĸ��� */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * �ڲ��࣬Z�ε��ĸ񷽿飬�̳���Tetromino��
	 * @author Leslie Leung
	 */
	private class ZShaped extends Tetromino {
		/**
		 * ���췽��
		 */
		public ZShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_ORANGE);
			cells[2] = new Cell(4, 0, Cell.COLOR_ORANGE);
			cells[0] = new Cell(4, 1, Cell.COLOR_ORANGE);
			cells[3] = new Cell(5, 1, Cell.COLOR_ORANGE);
			
			/* ������ת���Ҫ��ת�ĸ��� */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * �ڲ��࣬L�ε��ĸ񷽿飬�̳���Tetromino��
	 * @author Leslie Leung
	 */
	private class LShaped extends Tetromino {
		/**
		 * ���췽��
		 */
		public LShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_PINK);
			cells[0] = new Cell(4, 0, Cell.COLOR_PINK);
			cells[2] = new Cell(5, 0, Cell.COLOR_PINK);
			cells[3] = new Cell(3, 1, Cell.COLOR_PINK);
			
			/* ������ת���Ҫ��ת�ĸ��� */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * �ڲ��࣬O�ε��ĸ񷽿飬�̳���Tetromino��
	 * @author Leslie Leung
	 */
	private class OShaped extends Tetromino {
		/**
		 * ���췽��
		 */
		public OShaped() {
			cells = new Cell[4];
			
			cells[0] = new Cell(4, 0, Cell.COLOR_RED);
			cells[1] = new Cell(5, 0, Cell.COLOR_RED);
			cells[2] = new Cell(4, 1, Cell.COLOR_RED);
			cells[3] = new Cell(5, 1, Cell.COLOR_RED);
			
			/* ������ת���Ҫ��ת�ĸ��� */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * �ڲ��࣬J�ε��ĸ񷽿飬�̳���Tetromino��
	 * @author Leslie Leung
	 */
	private class JShaped extends Tetromino {
		/**
		 * ���췽��
		 */
		public JShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_YELLOW);
			cells[0] = new Cell(4, 0, Cell.COLOR_YELLOW);
			cells[2] = new Cell(5, 0, Cell.COLOR_YELLOW);
			cells[3] = new Cell(5, 1, Cell.COLOR_YELLOW);
			
			/* ������ת���Ҫ��ת�ĸ��� */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * ɾ��������
	 */
	public void removeRows() {
		for(int i = 0; i < getCurrentCells().length; i ++) {
			removeRow(getCurrentCells()[i].getY());
		}
	}
	
	/**
	 * ��ȡ��ת��
	 * @return ��ת��
	 */
	public Cell getAxis() {
		return currentTetromino.getAxis();
	}
	
	/**
	 * ��ȡ��Ҫ��ת�ĸ���
	 * @return ��Ҫ��ת�ĸ���
	 */
	public Cell[] getRotateCells() {
		return currentTetromino.getRotateCells();
	}
	
	/**
	 * ��ȡ��ǰ����˹��������и���
	 * @return ��ǰ����˹��������и���
	 */
	public Cell[] getCurrentCells() {
		return currentTetromino.getCells();
	}
	
	/**
	 * �ж϶���˹�����Ƿ񵽴�ײ�(�����Ƿ񵽴����ײ�����һλ���Ƿ��з���)
	 * @return true�����false��û����
	 */
	public boolean isReachBottomEdge() {
		int oldY, newY, oldX;		//������Ӿɵ������ꡢ�µ�������;ɵĺ�����
		
		for(int i = 0; i < getCurrentCells().length; i ++) {
			oldY = getCurrentCells()[i].getY();
			newY = oldY + 1;
			oldX = getCurrentCells()[i].getX();
			
			if(oldY == ROWS - 1) {//�������ײ�
				return true;
			}
			
			if(isContain(oldX, newY)) {//��һλ���з���
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �ж϶���˹�����Ƿ񵽴���߽�(�����Ƿ񳬳������߽����һλ���Ƿ����������������ײ)
	 * @return true���ѵ��false��û����
	 */
	public boolean isReachLeftEdge() {
		int oldX, newX, oldY;		//������Ӿɵĺ����ꡢ�µĺ�����;ɵ�������
		
		for(int i = 0; i < getCurrentCells().length; i ++) {
			oldX = getCurrentCells()[i].getX();
			newX = oldX - 1;
			oldY = getCurrentCells()[i].getY();
			
			if(oldX == 0 || isContain(newX, oldY)) {//������߽�
				return true;
			}
			
			if(isContain(newX, oldY)) {//��һλ���з���
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �ж϶���˹�����Ƿ񵽴��ұ߽�(�����Ƿ񳬳�����ұ߽����һλ���Ƿ����������������ײ)
	 * @return true���ѵ��false��û����
	 */
	public boolean isReachRightEdge() {
		int oldX, newX, oldY;		//������Ӿɵĺ����ꡢ�µĺ�����;ɵ�������
		
		for(int i = 0; i < getCurrentCells().length; i ++) {
			oldX = getCurrentCells()[i].getX();
			newX = oldX + 1;
			oldY = getCurrentCells()[i].getY();
			
			if(oldX == COLUMNS - 1 || isContain(newX, oldY)) {//�����ұ߽�
				return true;
			}
			
			if(isContain(newX, oldY)) {//��һλ���з���
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �ж϶���˹����˳ʱ��ת���Ƿ񳬳��߽�(�����Ƿ񳬳����߽����һλ���Ƿ��з���)
	 * @return true�������߽磻false��û���߽�
	 */
	public boolean clockwiseRotateIsOutOfBounds() {
		int oldX;	//rotateCell�ĺ�����
		int oldY;	//rotateCell��������
		int newX;	//rotateCell��ת��ĺ�����
		int newY;	//rotateCell��ת���������
		
		for(int i = 0; i < 3; i ++) {
			oldX = getRotateCells()[i].getX();
			oldY = getRotateCells()[i].getY();
			
			newX = getAxis().getX() - oldY + getAxis().getY();	//�º���������㷨
			newY = getAxis().getY() + oldX - getAxis().getX();	//������������㷨
			
			if(newX < 0 || newY < 0 || newX > COLUMNS - 1 || newY > ROWS - 1) {//���Խ�磬����true
				return true;
			}
			
			if(isContain(newX, newY)) {//���Խ�磬����true
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * �ж϶���˹������ʱ��ת���Ƿ񳬳��߽�(�����Ƿ񳬳����߽����һλ���Ƿ��з���)
	 * @return true�������߽磻false��û���߽�
	 */
	public boolean anticlockwiseRotateIsOutOfBounds() {
		int oldX;	//rotateCell�ĺ�����
		int oldY;	//rotateCell��������
		int newX;	//rotateCell��ת��ĺ�����
		int newY;	//rotateCell��ת���������
		
		for(int i = 0; i < 3; i ++) {
			oldX = getRotateCells()[i].getX();
			oldY = getRotateCells()[i].getY();
			
			newX = getAxis().getX() - getAxis().getY() + oldY;	//�º���������㷨
			newY = getAxis().getY() + getAxis().getX() - oldX;	//������������㷨
			
			if(newX < 0 || newY < 0 || newX > COLUMNS - 1 || newY > ROWS - 1) {//���Խ�磬����true
				return true;
			}
			
			if(isContain(newX, newY)) {//���Խ�磬����true
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * �ж�ĳ�������Ƿ���ڷ������
	 * @param x ������
	 * @param y ������
	 * @return true�����ڶ���false�������ڶ���
	 */
	public boolean isContain(int x, int y) {
		if(wall[y][x] == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * ʵ�ֶ���˹�����Ӳ����
	 */
	public void hardDrop() {
		while(!isReachBottomEdge()) {
			currentTetromino.softDrop();
		}
	}
	
	/**
	 * ��������
	 * @param i �е��±�
	 */
	public void removeRow(int i) {
		int oldY, newY;	
		
		for(int j = 0; j < COLUMNS; j ++) {
			if(wall[i][j] == null) {//�������һ������û��������return
				return;
			}
		}
		
		/* �����в��Ѹ�������ķ��������� */
		for(int k = i; k >= 1; k --){
			System.arraycopy(wall[k - 1], 0, wall[k], 0, COLUMNS);
			
			for(int m = 0; m < COLUMNS; m ++) {
				if(wall[k][m] != null) {//���ڲ��ǿյĶ���Ҫ������������
					oldY = wall[k][m].getY();
					newY = oldY + 1;
					wall[k][m].setY(newY);				
				}
			}
			
		}
		Arrays.fill(wall[0], null);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		
		/* ��ǽ */
		for(int i = 0; i < ROWS; i ++) {
			for(int j = 0; j < COLUMNS; j ++) {
				if(wall[i][j] == null) {//ĳ��ķ���Ϊ��ʱ
					g.setColor(Color.WHITE);
					g.fillRect(j * Cell.CELL_SIZE + 1, i * Cell.CELL_SIZE + 1, Cell.CELL_SIZE - 2, Cell.CELL_SIZE - 2);
				} else {//�����鲻Ϊ��ʱ
					wall[i][j].paintCell(g);
				}
			}
		}
		
		/* ����ǰ����˹���� */
		for(int i = 0; i < getCurrentCells().length; i ++) {
			getCurrentCells()[i].paintCell(g);
		}
		
	}
}
