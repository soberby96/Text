package tetris;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * �����
 * @author Leslie Leung
 */
public class TetrisFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3828925775219965164L;
	private TetrisPane tp;	//����˹��������Ϸ������
	private JLabel mention;		//��Ϸ����ʾ��Ϣ
	
	/**
	 * ���췽��
	 */
	public TetrisFrame() {
		setSize(550, 600);	//���ô����С
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);	//���ô�������Ļ����
		setTitle("Tetris");		//���ñ���ΪTetris
		setResizable(false);		//������������
		setLayout(new FlowLayout());	//���ò��ֹ�����
		
		tp = new TetrisPane();	//�½����������
		mention = new JLabel("��A����ʱ��ת����D˳ʱ��ת��������������������Һ����µ��˶������ո��Ӳ����");
		
		add(mention);		//�ѱ�ǩ��ӵ��������
		add(tp);		//����Ϸ�����������ӵ��������
		
		/* ע������¼� */
		addKeyListener(tp.getInnerInstanceOfKeyControl());
		tp.addKeyListener(tp.getInnerInstanceOfKeyControl());
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new TetrisFrame();
	}
}
