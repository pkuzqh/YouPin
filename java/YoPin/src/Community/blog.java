package Community;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import Client.connection;
import Info.Usr_Info;
import MainPage.MainFace;

public class blog extends JPanel {

	// 设置字体的格式
	Usr_Info user;
	String[] str_Name = { "宋体", "黑体", "Dialog", "Arial" };
	String[] str_Size = { "12", "14", "18", "22", "30", "40" };
	String[] str_Style = { "常规", "斜体", "粗体", "粗斜体" };
	String[] str_Color = { "黑色", "红色", "蓝色", "黄色", "绿色" };

	File imgFile; // 保存图片
	JPanel contentPane; // 整个界面，所有的部件都放在其上
	JFrame fr_main; // 保存主页面的引用
	JTextPane textPane_title = new JTextPane(); // 输入帖子题目的部分
	JTextPane textPane = new JTextPane(); // 输入帖子的部分

	// 方便写HTML文件
	String head = ""; // 保存HTML内正文前的部分，如<p><b> 等
	String tail = "</font>"; // 保存HTML内正文后的部分，如</p></b>等
	String cont;
	File html_file; // 帖子存入的HTML文件
	BufferedWriter write; // 写帖子的流
	int pre_pos = 0; // 帖子内容是一段话一段话保存的，pre_pos存当前待保存段的起始位置
	int cur_pos = 0; // 当前待保存段的结束位子
	int img_no = 0; // 一共存了几张图

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public blog(JFrame main, Usr_Info user_, connection con) throws IOException {
		user = user_;
		fr_main = main;
		((MainFace) fr_main).newblog = this;
		// 路径需要设置,初始化html文件
		deleteDirectory("./Yopin/tmp");
		File dir = new File("./Yopin/tmp");
		dir.mkdirs();
		html_file = new File("./Yopin/tmp/blog.html");
		if (!html_file.exists()) {
			html_file.createNewFile();
		}
		write = new BufferedWriter(new FileWriter("./Yopin/tmp/blog.html"));
		write.write(
				"<script language=javascript> function hero() { var herowidth=560; var heroheight=350; window.resizeTo(herowidth,heroheight);}hero();</script><body onresize=hero();>");
		write.write("<p><font size=\"4\" face=\"arial\" color=\"black\">");
		head = "";
		tail = "";
		// 初始化html文件结束

		// 设置整体布局、大小
		setBounds(0, 0, 640, 500);
		setLayout(null);
		contentPane = new JPanel();
		contentPane.setVisible(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.add(contentPane);
		contentPane.setBounds(0, 0, 640, 500);
		contentPane.setLayout(null);
		// 帖子的标题
		JLabel lblTitle = new JLabel("新的博客");
		lblTitle.setBounds(240, 10, 200, 40);
		lblTitle.setFont(new Font("Xingkai SC", Font.ITALIC, 40));
		lblTitle.setForeground(Color.DARK_GRAY);
		contentPane.add(lblTitle);
		// 设置输入帖子标题的部件
		JLabel lblAddTitle = new JLabel("标题");
		lblAddTitle.setBounds(10, 47, 65, 50);
		contentPane.add(lblAddTitle);
		lblAddTitle.setFont(new Font("Xingkai SC", Font.ITALIC, 25));
		textPane_title.setBounds(85, 55, 300, 35);
		contentPane.add(textPane_title);
		textPane_title.setFont(new Font("Xingkai SC", Font.BOLD, 20));

		// 设置插入图片的部件
		JButton lblImg = new JButton();
		lblImg.setBounds(520, 47, 100, 50);
		lblImg.setOpaque(false);
		lblImg.setText("添加图片");
		contentPane.add(lblImg);
		// 点击添加图片从计算机内选择图片
		lblImg.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JFileChooser select = new JFileChooser();
				select.showDialog(select, "Select Photo");
				File file = select.getSelectedFile();
				if (file.exists()) {
					try {
						// 将当前段存入HTML文件内
						String txt = textPane.getText();
						cur_pos = txt.length();
						String tmp = txt.substring(pre_pos);
						for (int i = 0; i < tmp.length(); ++i) {
							if (tmp.charAt(i) == '\n') {
								write.write("<br/>");
							} else {
								write.write(tmp.charAt(i));
							}
						}
						write.write(tail);
						write.write("</p><p><img src=" + img_no + ".jpg\" width=\"550\" height=\"350\"></p><p>");
						// 将选择的图片存入缓存文件夹内，方便上传
						copyFile(file.getPath(), "./Yopin/tmp/" + img_no + ".jpg");
						txt += "\npicture " + img_no + "\n";
						img_no++;
						pre_pos = txt.length();

						write.write(head);

						textPane.setText(txt);
						// 将选择的图片显示在小框内，共用户查看
						Image img = ImageIO.read(file);
						BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
						Graphics g = image.getGraphics();
						g.drawImage(img, 10, 158, 30, 30, Color.LIGHT_GRAY, null);

						g.dispose();
						imgFile = file;
					} catch (IOException event) {
						JOptionPane.showMessageDialog(null, "1", "1", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "22", "22", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		// 选择发帖类型的部件
		JLabel lbl_kind = new JLabel("发帖类型");
		lbl_kind.setBounds(430, 47, 70, 21);
		lbl_kind.setOpaque(true);
		contentPane.add(lbl_kind);
		JComboBox Jkind = new JComboBox(Community.Kind);
		Jkind.setOpaque(false);
		Jkind.setBounds(425, 69, 90, 21);
		Jkind.setOpaque(true);
		contentPane.add(Jkind);
		// 编辑文本的部件，可滚动
		JPanel panel = new JPanel();
		panel.setBounds(10, 110, 764, 300);
		contentPane.add(panel);
		panel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 605, 371);
		scrollPane.getVerticalScrollBar().setUnitIncrement(15);
		panel.add(scrollPane);
		textPane.setBounds(0, 0, 600, 2000);
		scrollPane.setViewportView(textPane);
		// 选择字体颜色的部件
		JComboBox fontColor = new JComboBox(str_Color);
		fontColor.setBounds(10, 430, 80, 21);
		contentPane.add(fontColor);
		// 选择字体样式的部件
		JComboBox fontName = new JComboBox(str_Name);
		fontName.setBounds(97, 430, 80, 21);
		contentPane.add(fontName);
		// 选择字体大小的部件
		JComboBox fontSize = new JComboBox(str_Size);
		fontSize.setBounds(184, 430, 80, 21);
		contentPane.add(fontSize);
		// 选择是否加粗、斜的部件
		JComboBox fontStyle = new JComboBox(str_Style);
		fontStyle.setBounds(261, 430, 80, 21);
		contentPane.add(fontStyle);
		// 保存字体设置，点此按钮后，接下来的输入就会按照改后的字体格式
		Button change = new Button("保存设置");
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setFontAttrib(fontName, fontSize, fontStyle, fontColor);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		change.setBounds(363, 430, 116, 23);
		contentPane.add(change);
		// 取消发帖部件，返回上级菜单
		Button cancel = new Button("取消");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((MainFace) fr_main).newblog.setVisible(false);
				((MainFace) fr_main).community.setVisible(true);
			}
		});
		cancel.setBounds(480, 430, 57, 23);
		contentPane.add(cancel);
		// 发表部件，发表帖子。同时将内容上传服务器
		Button publish = new Button("发表");
		publish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String txt = textPane.getText(); // 将当前段落存入HTML
					cur_pos = txt.length();
					String tmp = txt.substring(pre_pos);
					for (int i = 0; i < tmp.length(); ++i) {
						if (tmp.charAt(i) == '\n') {
							write.write("<br/>");
						} else {
							write.write(tmp.charAt(i));
						}
					}
					write.write(tail);
					write.write("</p></body>");
					write.flush();
					write.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// 将帖子和摘要发送给服务器
				blogAbstract abs = new blogAbstract();
				abs.kind = Jkind.getSelectedIndex() + 1;
				blogText t = new blogText();
				Date date = new Date();
				abs.authorName = user.getName();
				abs.commentID = 0;
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				abs.time = format.format(date);
				abs.title = textPane_title.getText();
				abs.id = abs.latestID + 1;
				t.text = textPane.getText();
				try {
					if (img_no == 0) {
						copyFile("./YoPin/Pictures/YoPin0/user_image.jpg", "./Yopin/tmp/0.jpg");
					}
					con.createblog(abs, "./Yopin/tmp", new File("./Yopin/tmp/0.jpg"));
				} catch (Exception ep) {
					ep.printStackTrace();
				}
				((MainFace) fr_main).newblog.setVisible(false);
				((MainFace) fr_main).community = new Community(user_, con, fr_main);
				((MainFace) fr_main).lbl_RightInfo.add(((MainFace) fr_main).community);
				JOptionPane.showMessageDialog(null, "发表成功");
			}
		});
		publish.setBounds(555, 430, 57, 23);
		contentPane.add(publish);

	}

	// 修改字体的函数
	void setFontAttrib(JComboBox fontName, JComboBox fontSize, JComboBox fontStyle, JComboBox fontColor)
			throws IOException {
		String txt = textPane.getText();
		cur_pos = txt.length();
		String tmp = txt.substring(pre_pos);
		for (int i = 0; i < tmp.length(); ++i) {
			if (tmp.charAt(i) == '\n') {
				write.write("<br/>");
			} else {
				write.write(tmp.charAt(i));
			}
		}
		write.write(tail);
		pre_pos = cur_pos;

		System.out.println("change font");
		System.out.println(txt);

		SimpleAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setBold(attr, true);
		StyleConstants.setItalic(attr, true);
		// change font style
		String temp_style = (String) fontStyle.getSelectedItem();
		if (temp_style.equals("常规")) {
			StyleConstants.setBold(attr, false);
			StyleConstants.setItalic(attr, false);
			head = "";
			tail = "";
		} else if (temp_style.equals("黑体")) {
			StyleConstants.setBold(attr, true);
			StyleConstants.setItalic(attr, false);
			head = "<b>";
			tail = "</b>";
		} else if (temp_style.equals("斜体")) {
			StyleConstants.setBold(attr, false);
			StyleConstants.setItalic(attr, true);
			head = "<i>";
			tail = "</i>";
		} else if (temp_style.equals("黑斜体")) {
			StyleConstants.setBold(attr, true);
			StyleConstants.setItalic(attr, true);
			head = "<b><i>";
			tail = "</i></b>";
		}
		// change font size
		int size = Integer.parseInt((String) fontSize.getSelectedItem());
		StyleConstants.setFontSize(attr, size);
		size -= 8;
		head += "<font size=\"" + size + "\" ";
		// change font color
		String temp_color = (String) fontColor.getSelectedItem();
		Color color = null;
		if (temp_color.equals("黑色")) {
			color = new Color(0, 0, 0);
			head += "color=\"black\">";
		} else if (temp_color.equals("红色")) {
			color = new Color(255, 0, 0);
			head += "color=\"red\">";
		} else if (temp_color.equals("蓝色")) {
			color = new Color(0, 0, 255);
			head += "color=\"blue\">";
		} else if (temp_color.equals("黄色")) {
			color = new Color(255, 255, 0);
			head += "color=\"yellow\">";
		} else if (temp_color.equals("绿色")) {
			color = new Color(0, 255, 0);
			head += "color=\"green\">";
		}
		tail += "</font>";
		StyleConstants.setForeground(attr, color);

		// change font name
		String name = (String) fontName.getSelectedItem();
		StyleConstants.setFontFamily(attr, name);

		// save the change
		write.write(head);
		textPane.setCharacterAttributes(attr, true);
	}

	private void copyFile(String src, String dst) throws IOException {
		System.out.println(src);
		File f1 = new File(src);
		File f2 = new File(dst);
		BufferedInputStream read = null;
		BufferedOutputStream w = null;
		if (!f1.exists())
			return;
		if (!f2.exists())
			f2.createNewFile();
		try {
			read = new BufferedInputStream(new FileInputStream(src));
			w = new BufferedOutputStream(new FileOutputStream(dst));
			byte buf[] = new byte[85];
			int n = 0;
			n = read.read(buf, 0, 80);
			while (n != -1) {
				w.write(buf, 0, n);
				n = read.read(buf, 0, 80);
			}
			w.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 删除当前目录
	public boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	// 删除文件
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
}
