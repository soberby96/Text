package tetris;

import java.awt.Color;
import java.awt.Graphics;

/**
 * ������
 * @author Leslie Leung
 */
public class Cell {
	public static final int CELL_SIZE = 25;		//һ������Ĵ�С

	/* ���ӵ�������ɫ  */
	public static final int COLOR_CYAN = 0;
	public static final int COLOR_BLUE = 1;
	public static final int COLOR_GREEN = 2;
	public static final int COLOR_YELLOW = 3;
	public static final int COLOR_ORANGE = 4;
	public static final int COLOR_RED = 5;
	public static final int COLOR_PINK = 6;

	private int color;	//���ӵ���ɫ
	private int x;	//������
	private int y;	//������

	/**
	 * ���췽��
	 * @param x ������
	 * @param y ������
	 * @param style ���ӵ���ʽ��ͨ����ɫ��ָ��
	 */
	public Cell(int x, int y, int style) {
		/* ���ݴ���������ʽ�������ӵ���ɫ */
		switch(style) {
			case 0: color = COLOR_CYAN; break;
			case 1: color = COLOR_BLUE; break;
			case 2: color = COLOR_GREEN; break;
			case 3: color = COLOR_YELLOW; break;
			case 4: color = COLOR_ORANGE; break;
			case 5: color = COLOR_RED; break;
			case 6: color = COLOR_PINK; break;
		}

		this.x = x;
		this.y = y;
	}

	/**
	 * ���øø��ӵĺ�����
	 * @param newX �µĺ�����
	 */
	public void setX(int newX) {
		x = newX;
	}

	/**
	 * ���øø��ӵ�������
	 * @param newY �µ�������
	 */
	public void setY(int newY) {
		y = newY;
	}

	/**
	 * ��ȡ��Cell�ĺ�����
	 * @return ������
	 */
	public int getX() {
		return x;
	}

	/**
	 * ��ȡ��Cell��������
	 * @return ������
	 */
	public int getY() {
		return y;
	}

	/**
	 * ��ͼ����
	 * @param g Graphics����
	 */
	public void paintCell(Graphics g) {
		switch(color) {
			case COLOR_CYAN: g.setColor(Color.CYAN);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_BLUE: g.setColor(Color.BLUE);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_GREEN: g.setColor(Color.GREEN);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_YELLOW: g.setColor(Color.YELLOW);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_ORANGE: g.setColor(Color.ORANGE);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_RED: g.setColor(Color.RED);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_PINK: g.setColor(Color.PINK);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
		}
	}
}
