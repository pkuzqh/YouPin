//实现圆角panel
package Client;

import java.awt.Graphics;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author zhuqihao
 */
import javax.swing.JPanel;

public class RoundRectPanel extends JPanel {

	RoundRectPanel() {
		setBounds(0, 0, 400, 600);
	}

	// 重写panel的paint函数
	public void paint(Graphics g) {
		RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20);
		g.setClip(rect);
		super.paint(g);
	}

}
