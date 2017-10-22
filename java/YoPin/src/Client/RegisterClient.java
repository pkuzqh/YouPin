/*
 * 注册账号界面，作为JPanel添加到上级容器里
 */
package Client;

/**
 *
 * @author zhuqihao
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Info.Usr_Info;
import MainPage.MainFace;

public class RegisterClient extends JFrame implements ActionListener {
	// 部件的声明包括 邮箱地址 用户名 密码 确认密码 头像 验证码
	private JPanel background;
	private JPanel fun;
	private JLabel labelemail;
	private JTextField email;
	private JLabel labelnickname;
	private JTextField nickname;
	private JLabel labelpassword;
	private JPasswordField password;
	private JLabel labelconfirmpassword;
	private JPasswordField confirmpassword;
	private JLabel labelpass;
	private JTextField pass;
	private JLabel passimage;
	private JLabel newpassimage;
	private JButton register;
	private JLabel back;
	private JLabel nickpicture;
	private JButton selectnickpicture;
	private JButton eyes;
	private String passans;
	int randomnum = 0;
	// 声明字体
	private String font = new String("Xingkai SC");
	private int fontsize = 18;
	// 头像图片，如果没有设置就是默认
	private BufferedImage usr_image;
	private String usr_image_name = new String();

	// 初始化整个panel并在上级容器里面添加这个panel
	public RegisterClient() {
		setBounds(300, 160, 800, 500);
		this.setLayout(null);
		initlabel();
		initground();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	// 设置界面背景图片
	public void setbackground() {
		ImageIcon img = new ImageIcon("src/image/registerbackground.jpg");
		JLabel background = new JLabel(img);
		this.background.add(background);
		background.setBounds(this.getX(), this.getY(), img.getIconWidth(), img.getIconHeight());
		return;
	}

	public void initground() {
		background = new JPanel();
		background.setOpaque(true);
		background.setBounds(0, 0, this.getWidth(), this.getHeight());
		setbackground();
		this.add(background);
		// this.getContentPane().add(background);
	}

	public void initlabel() {
		fun = new RoundRectPanel();
		fun.setBounds(150, 55, 440, 310);
		fun.setOpaque(true);
		fun.setBackground(Color.WHITE);
		fun.setFocusable(true);
		fun.setLayout(null);
		this.add(fun);
		// 邮箱的label
		labelemail = new JLabel("邮箱地址:");
		labelemail.setBounds(20, 0, 100, 40);
		labelemail.setFont(new Font(font, 1, fontsize));
		// 邮箱地址的textfield部件
		email = new JTextField();
		email.setBounds(100, 5, 160, 30);
		// 昵称的label
		labelnickname = new JLabel("昵称:");
		labelnickname.setBounds(40, 40, 100, 40);
		labelnickname.setFont(new Font(font, 1, fontsize));
		// 填写昵称的textfield
		nickname = new JTextField();
		nickname.setBounds(100, 45, 160, 30);
		// 密码的label
		labelpassword = new JLabel("密码:");
		labelpassword.setBounds(40, 80, 100, 40);
		labelpassword.setFont(new Font(font, 1, fontsize));
		// 填写密码的JpasswordField，设置掩盖字符
		password = new JPasswordField();
		password.setText("请输入6-15的英文、数字密码");
		password.setEchoChar((char) 0);
		password.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				password.setText("");
				password.setEchoChar('*');
			}
		});
		password.setBounds(100, 85, 160, 30);
		// 确认密码的label
		labelconfirmpassword = new JLabel("确认密码:");
		labelconfirmpassword.setBounds(20, 120, 100, 40);
		labelconfirmpassword.setFont(new Font(font, 1, fontsize));
		// 确认密码的密码框JPasswordField
		confirmpassword = new JPasswordField();
		confirmpassword.setBounds(100, 125, 160, 30);
		confirmpassword.setEchoChar('*');
		// 验证码的label
		labelpass = new JLabel("验证码:");
		labelpass.setBounds(30, 160, 100, 40);
		labelpass.setFont(new Font(font, 1, fontsize));
		// 验证码的填写区域
		pass = new JTextField();
		pass.setBounds(100, 165, 60, 30);
		// 验证码的图片
		passimage = new JLabel(new ImageIcon(newpass()));
		passimage.setBounds(170, 165, 80, 30);
		// 更换验证码的label，点击重新生成一张验证码图片
		newpassimage = new JLabel("<html><u>看不清？换一张</u></html>");
		newpassimage.setForeground(Color.red);
		newpassimage.setBounds(255, 180, 100, 15);
		newpassimage.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				// 设置鼠标移入的时候设置鼠标形状为手型
				newpassimage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				newpassimage.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		newpassimage.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// 生成新的验证码图片
				passimage.setIcon(new ImageIcon(newpass()));
			}
		});
		// 返回登录界面的label
		back = new JLabel("<html><u>已有账号？点击登录</u></html>");
		back.setBounds(230, 280, 180, 20);
		back.setForeground(Color.red);
		back.setFont(new Font("华文仿宋", 0, fontsize - 2));
		back.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				back.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			public void mouseClicked(MouseEvent e) {
				dispose();
				new LoginClient();
			}
		});
		// 注册按钮
		register = new JButton("立即注册");
		register.setBounds(100, 250, 120, 50);
		register.addActionListener(this);
		register.setFont(new Font(font, 1, fontsize + 2));
		// 设置头像的文件选择框
		nickpicture = new JLabel();
		nickpicture.setBounds(320, 10, 100, 100);
		nickpicture.setOpaque(true);
		nickpicture.setIcon(new ImageIcon("src/usr_image/default.jpg"));
		fun.add(nickpicture);
		/// 选择头像的label
		selectnickpicture = new JButton("本地选择头像");
		selectnickpicture.setFont(new Font("宋体", 0, 10));
		selectnickpicture.setBounds(320, 120, 100, 30);
		selectnickpicture.addActionListener(this);
		fun.add(selectnickpicture);
		// 查看密码的按钮
		ImageIcon img = new ImageIcon(("src/image/eyes.png"));
		eyes = new JButton(img);
		eyes.setBounds(260, 90, 30, 20);
		eyes.addActionListener(this);

		fun.add(eyes);
		fun.add(back);
		fun.add(register);
		fun.add(newpassimage);
		fun.add(passimage);
		fun.add(pass);
		fun.add(labelpass);
		fun.add(confirmpassword);
		fun.add(labelconfirmpassword);
		fun.add(password);
		fun.add(labelpassword);
		fun.add(nickname);
		fun.add(labelnickname);
		fun.add(email);
		fun.add(labelemail);

	}

	// 获得随机的颜色，来画验证码
	Color getRandColor() {
		Random random = new Random();
		int r = random.nextInt(2 * randomnum + 100) % 256;
		int g = random.nextInt(10 * randomnum + 122) % 256;
		int b = random.nextInt(30 * randomnum + 299) % 256;
		randomnum += 10;
		return new Color(r, g, b);
	}

	// 生成验证码的函数
	public BufferedImage newpass() {
		int width = 80, height = 30;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 声明一个画图对象
		Graphics g = image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		// 再图上先画出想要的四位验证码
		g.setFont(new Font("Times New Roman", Font.BOLD, 18));
		g.setColor(new Color(0, 0, 0));
		g.drawRect(0, 0, width - 1, height - 1);
		((Graphics2D) g).setStroke(new BasicStroke(1.5f));
		Random random = new Random();
		// 在图上随机找两个点，然后在两个点之间随机画线，以此达到干扰效果
		for (int i = 0; i < 50; i++) {
			g.setColor(getRandColor());
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		String sRand = "";
		// 随机选取数字和字母进行填充验证码
		for (int i = 0; i < 4; i++) {
			String rand = null;
			if (random.nextInt(10) > 5) {
				rand = String.valueOf((char) (random.nextInt(10) + 48));
			} else {
				rand = String.valueOf((char) (random.nextInt(26) + 65));
			}
			sRand += rand;
			g.setColor(getRandColor());
			g.drawString(rand, 15 * i + 10, 16);
		}
		g.dispose();
		passans = new String(sRand);
		return image;
	}

	// 判断填写的字符串是否是一个合法的邮箱地址
	static boolean isemailaddress(String s) {
		int atloc = -1;
		// 判断是否有@符号
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) == '@') {
				if (atloc == -1) {
					atloc = i;
				} else {
					return false;
				}
			}
		}
		if (atloc == 0 || atloc == s.length() - 1) {
			return false;
		}
		// 判断是否有合理域名
		if (s.charAt(0) == '.' || s.charAt(s.length() - 1) == '.') {
			return false;
		}
		for (int i = atloc + 1; i < s.length(); ++i) {
			if (s.charAt(i) == '.') {
				if (i == atloc + 1) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	// 取得图片的后缀名，以此来保存图片
	public String suffix(String image_name) {
		StringBuilder suffix = new StringBuilder();
		for (int i = image_name.length() - 1; i >= 0; --i) {
			if (image_name.charAt(i) == '.')
				break;
			suffix.append(image_name.charAt(i));
		}
		suffix.reverse();
		return suffix.toString();
	}

	// 处理panle中部件中的各种点击事件和移入事件
	public void actionPerformed(ActionEvent e) {
		// 点击选择头像，弹出文件选择框，选择合适的图片
		if (e.getSource() == selectnickpicture) {
			JFileChooser select = new JFileChooser();
			select.showDialog(select, "选择头像");
			File file = select.getSelectedFile();
			// 将图片进行resize，label的大小固定为100*100，因此需要将图片进行改变大小
			if (file.exists()) {
				try {
					Image img = ImageIO.read(file);
					BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
					Graphics g = image.getGraphics();
					g.drawImage(img, 0, 0, 100, 100, Color.LIGHT_GRAY, null);
					g.dispose();
					usr_image = image;
					nickpicture.setIcon(new ImageIcon(image));
					usr_image_name = file.getName();
				} catch (IOException event) {
					JOptionPane.showMessageDialog(null, "图片读取错误，请重新选择", "警告", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "不存在该文件，请重新选择", "警告", JOptionPane.ERROR_MESSAGE);
			}
		}
		// 点击注册按钮，和服务器进行连接，并且检查各项的注册输入是否符合规则
		if (e.getSource() == register) {
			if (!isemailaddress(email.getText())) {
				email.setText("");
				JOptionPane.showMessageDialog(null, "非法邮箱地址,请重新输入", "警告", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!password.getText().equals(confirmpassword.getText())) {
				confirmpassword.setText("");
				JOptionPane.showMessageDialog(null, "两次输入的密码不同,请重新输入", "警告", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (password.getText().length() < 6 || password.getText().length() > 15) {
				JOptionPane.showMessageDialog(null, "输入的密码不符合要求，请重新输入", "警告", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!passans.equals(pass.getText())) {
				pass.setText("");
				JOptionPane.showMessageDialog(null, "验证码错误,请重新输入", "警告", JOptionPane.ERROR_MESSAGE);
				return;
			}
			connection con = new connection(nickname.getText());
			if (!con.active) {
				JOptionPane.showMessageDialog(null, "与服务器连接异常，请稍后再试!", "警告", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// 向用户的邮箱地址发送说明邮件
			new sendemail("1500012943@pku.edu.cn", email.getText(), "欢迎使用悠品软件，开始享受您的优质生活。");
			// 服务器返回注册的生成的用户号码
			String usr_id = con.new_usr(nickname.getText(), password.getText(), email.getText());
			JOptionPane.showMessageDialog(null, "注册成功，自动登录中...您的博客号码为" + usr_id);
			try {
				// 向本地缓存添加头像，如果没有选择。那么使用默认头像
				if (usr_image == null) {
					BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
					File f = new File("src/usr_image/default.jpg");
					Graphics g = image.getGraphics();
					g.drawImage(ImageIO.read(f), 0, 0, 100, 100, Color.LIGHT_GRAY, null);
					g.dispose();
					usr_image = image;
					usr_image_name = "default.jpg";
				}
				ImageIO.write(usr_image, suffix(usr_image_name), new File("src/usr_image/" + usr_id));
			} catch (Exception ev) {
				ev.printStackTrace();
			}
			dispose();
			con.close();
			con = new connection(usr_id);
			Usr_Info user = new Usr_Info("非正常进入账号", "");
			// 传递新建的、和从服务器上读取的用户信息
			try {
				user = con.getUsrFromServer(usr_id);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			File Root, Picture, User;
			Root = new File("./YoPin");
			if (!Root.exists() || !Root.isDirectory()) {
				Root.mkdir();
			}
			Picture = new File("./YoPin/Pictures/YoPin1");
			if (!Picture.exists() || !Picture.isDirectory()) {
				Picture.mkdir();
			}
			User = new File("./YoPin/User");
			if (!User.exists() || !User.isDirectory()) {
				User.mkdir();
			}
			try {
				new MainFace(Root, Picture, User, user, con);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == eyes) {
			if (password.getEchoChar() != 0) {
				password.setEchoChar((char) 0);
			} else {
				password.setEchoChar('*');
			}
		}
	}
}
