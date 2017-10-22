/**
 * 登录界面的主函数，实现账号和密码的填写
 */
package Client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Info.Usr_Info;
import MainPage.MainFace;

/**
 *
 * @author zhuqihao Allrights reservered;
 */
public class LoginClient extends JFrame implements ActionListener {
	/**
	 * 界面部件的声明
	 */
	private JPanel background;
	private JPanel fun;
	private JLabel labelaccount;
	private JLabel newaccount;
	private JButton login;
	private JLabel labelpassword;
	private JComboBox account;
	private JPasswordField password;
	private JCheckBox remember;
	private JCheckBox autologin;
	private JLabel imagetitle;
	private String font = new String("Xingkai SC");
	private int fontsize = 16;
	/**
	 * 用来存储从本地读取的需要记住密码的用户名和密码
	 */
	ArrayList<String> oldaccount;

	/**
	 * 界面部件的初始化和摆放位置以及监视器的添加
	 */
	public LoginClient() {
		oldaccount = new ArrayList<String>();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(300, 160, 800, 500); // 确定界面的大小为800*500
		this.setLayout(null); // 确定布局方式为null
		initjlabel();
		initground();
		setVisible(true);
		try {
			File file = new File("src/usr_info/usr.txt"); // 读取记住密码的用户信息
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedReader cin = new BufferedReader(new FileReader(file));
			String account = new String();
			String account_password = new String();
			account = cin.readLine();
			while (account != null) {
				oldaccount.add(new String(account));
				account = cin.readLine();
			}
			cin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (oldaccount.size() != 0) {
			JTextField tf = (JTextField) (account.getEditor().getEditorComponent());
			tf.setText(oldaccount.get(0));
			password.setText("unknown"); // 密码框内默认显示的是unknown，这样就不知道原来的密码是几位数的
			imagetitle.setIcon(new ImageIcon("src/usr_image/" + oldaccount.get(0))); // 设置记住密码用户的头像
		}

	}

	// 设置登录界面的主背景函数
	public void setbackground() {
		ImageIcon img = new ImageIcon("./src/image/loginbackground.jpg");
		JLabel background = new JLabel(img);
		this.background.add(background);
		background.setBounds(this.getX(), this.getY(), img.getIconWidth(), img.getIconHeight());
		return;
	}

	// 初始化整个大Panel的函数，包括它添加部件等
	public void initground() {
		background = new JPanel();
		background.setOpaque(true);
		background.setBounds(0, 0, this.getWidth(), this.getHeight());
		setbackground();
		this.add(background);
	}

	public void initjlabel() {
		// 初始化所有的label里的文字内容
		labelaccount = new JLabel("账号:");
		labelpassword = new JLabel("密码:");
		remember = new JCheckBox("记住密码");
		password = new JPasswordField();
		account = new JComboBox(new comboboxmodel());
		autologin = new JCheckBox("自动登录");
		login = new JButton("进入悠品");
		newaccount = new JLabel("注册账户");
		imagetitle = new JLabel();
		// 初始化圆框Panel,即登录界面的白框
		fun = new RoundRectPanel();// new JPanel();
		this.add(fun);
		fun.setBounds(20, 50, 370, 250);
		fun.setBackground(Color.WHITE);
		fun.setOpaque(true);
		// 设置label字体颜色
		labelaccount.setForeground(Color.gray);
		labelaccount.setBounds(120, 50, 50, 40);
		labelaccount.setFont(new Font(font, 1, fontsize));
		// 填写账户信息的列表框，需要设置大小位置以及添加事件监听器，点击切换用户时实时更新头像
		account.setBounds(170, 58, 150, 24);
		account.setOpaque(true);
		account.setEditable(true);
		account.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				System.out.println(e.getItem().toString());
				imagetitle.setIcon(new ImageIcon("./src/usr_image/" + e.getItem().toString()));
			}
		});
		// 密码label初始化位置
		labelpassword.setForeground(Color.gray);
		labelpassword.setBounds(120, 100, 50, 40);
		labelpassword.setFont(new Font(font, 1, fontsize));
		// 密码框设置填充字符
		password.setBounds(170, 108, 150, 27);
		password.setEchoChar('*');
		// 两个复选框的设置
		remember.setBounds(150, 150, 100, 40);
		remember.setFont(new Font(font, 1, fontsize));
		remember.setOpaque(false);
		remember.setForeground(Color.gray);

		autologin.setForeground(Color.GRAY);
		autologin.setBounds(250, 150, 100, 40);
		autologin.setFont(new Font(font, 1, fontsize));
		autologin.setOpaque(false);
		// 登录按钮的设置
		login.setBackground(new Color(57, 111, 57));
		login.setBorderPainted(false);
		login.setForeground(Color.white);
		login.setBounds(120, 200, 150, 40);
		login.setFont(new Font(font, 1, fontsize));
		login.addActionListener(this);
		login.setOpaque(true);
		// 注册新用户了label，添加鼠标点击，移入，移出事件，改变颜色和鼠标的形状
		newaccount.setBounds(295, 205, 150, 40);
		newaccount.setForeground(Color.gray);
		newaccount.setFont(new Font(font, 1, fontsize - 2));
		newaccount.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				// 将鼠标设置为手型
				newaccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				// 将鼠标设置为默认形状
				newaccount.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			public void mouseClicked(MouseEvent e) {
				dispose();
				new RegisterClient();
			}
		});
		// 显示头像的label
		imagetitle.setBounds(12, 45, 100, 100);
		imagetitle.setBorder(BorderFactory.createRaisedBevelBorder());
		// 添加部件
		fun.add(imagetitle);
		fun.add(newaccount);
		fun.add(login);
		fun.add(autologin);
		fun.add(remember);
		fun.add(password);
		fun.setLayout(null);
		fun.add(labelaccount);
		fun.add(labelpassword);
		fun.add(account);
	}

	// 整个类继承了ActionListener接口实现鼠标点击事件的处理
	public void actionPerformed(ActionEvent e) {
		// 点击注册账号，进入注册界面
		if (e.getSource() == newaccount) {
			dispose();
			new RegisterClient();
		}
		// 点击登录按钮，与服务器进行通信，验证密码正确
		else if (e.getSource() == login) {
			String usrid = account.getSelectedItem().toString();
			connection con = new connection(usrid);
			// 未连接上服务器
			if (!con.active) {
				JOptionPane.showMessageDialog(null, "与服务器连接异常，请稍后再试!", "警告", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// 读取记住密码的用户密码
			try {
				for (int i = 0; i < oldaccount.size(); ++i) {
					if (oldaccount.get(i).equals(usrid)) {
						String pass = new String();
						DataInputStream out = new DataInputStream(
								new FileInputStream("src/usr_info/" + usrid + ".txt"));
						password.setText(out.readUTF());
						break;
					}
				}
			} catch (IOException ev) {
				ev.printStackTrace();
			}
			// 验证密码是否正确
			String usrpassword = password.getText();
			if (!con.sentusr_password(usrid, usrpassword).equals("matched")) {
				JOptionPane.showMessageDialog(null, "不存在该用户或密码错误，请重试", "警告", JOptionPane.ERROR_MESSAGE);
				con.close();
				password.setText("unknown");
				return;
			}
			// 关闭与服务器的连接
			con.close();
			// 是否记住密码，记录在src/usr_info/文件夹下
			if (remember.isSelected()) {
				try {
					boolean exist = false;
					File f = new File("src/usr_info/" + usrid + ".txt");
					if (f.exists()) {
						DataOutputStream out = new DataOutputStream(
								new FileOutputStream("src/usr_info/" + usrid + ".txt"));
						out.writeUTF(usrpassword);
					} else {
						f.createNewFile();
						DataOutputStream out = new DataOutputStream(
								new FileOutputStream("src/usr_info/" + usrid + ".txt"));
						out.writeUTF(usrpassword);
						FileWriter fout = new FileWriter("src/usr_info/usr.txt", true);
						fout.write(usrid + '\n');
						fout.close();
					}
				} catch (IOException ev) {
					ev.printStackTrace();
				}

			}
			dispose();
			// 初始化登录账户，并传给Mainface主界面类
			Usr_Info user = new Usr_Info("非正常登陆", "");
			con = new connection(usrid);
			try {
				user = con.getUsrFromServer(usrid);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
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
			/*
			 * File[] list = Picture.listFiles(); Random r = new Random();
			 * Picture = list[r.nextInt(list.length)]; Picture = list[1];
			 */
			User = new File("./YoPin/User");
			if (!User.exists() || !User.isDirectory()) {
				User.mkdir();
			}
			try {
				new MainFace(Root, Picture, User, user, con);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return;
	}

	// 实现内部类使得JCombobox可以获得选择的用户号码
	class comboboxmodel extends AbstractListModel implements ComboBoxModel {

		String item;

		public Object getElementAt(int index) {
			return oldaccount.get(index++);
		}

		public int getSize() {
			return oldaccount.size();
		}

		public void setSelectedItem(Object anItem) {
			item = (String) anItem;
		}

		public Object getSelectedItem() {
			return item;
		}
	}
}
