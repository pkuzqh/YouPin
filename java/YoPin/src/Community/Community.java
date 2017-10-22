package Community;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * make changes by ZSZ
 * add listener to btnMypage
 * change address(not absolute)
 */
/**
 *
 * @author zqh
 */
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Client.connection;
import Info.Usr_Info;
import MainPage.MainFace;

public class Community extends JPanel {
	// 部件
	static final String Kind[] = { "娱乐", "科技", "汽车", "体育", "财经", "军事" };
	private connection con;
	int ID;
	JPanel thisPanel;
	private String usrdir = "./src/usr/";
	private Usr_Info user;
	JLabel background;
	JScrollPane s;
	JPanel mains;
	public JButton btnMyPage;
	ShowBlog blPage;
	JPanel title;
	int textkind = 1;
	JFrame frm_main;
	JLabel Ytitle;
	JPanel kindBack;
	JPanel kind;
	JLabel[] Tkind = new JLabel[6];
	
	// Label内文字自动换行，用HTML实现
	void JlabelSetText(JLabel jLabel, String longString) {
		StringBuilder builder = new StringBuilder("<html>");
		char[] chars = longString.toCharArray();
		FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
		int start = 0;
		int len = 0;
		while (start + len < longString.length()) {
			while (true) {
				len++;
				if (start + len > longString.length())
					break;
				if (fontMetrics.charsWidth(chars, start, len) > jLabel.getWidth()) {
					break;
				}
			}
			builder.append(chars, start, len - 1).append("<br>");
			start = start + len - 1;
			len = 0;
		}
		builder.append(chars, start, longString.length() - start);
		builder.append("</html>");
		jLabel.setText(builder.toString());
	}
	// 初始化帖子摘要界面
	private void initBlogPane(int blogID, int id) {
		blogAbstract blog = con.getBlogText(textkind, blogID);
		blog.id = blogID;

		JPanel content = new JPanel();
		content.setLayout(null);
		// 设置帖子的图片
		if (blog.filename != null) {
			ImageIcon img = new ImageIcon(blog.filename);
			img.setImage(img.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
			JLabel lblIcon = new JLabel();
			lblIcon.setIcon(img);
			lblIcon.setBounds(0, 0, 100, 100);
			content.add(lblIcon);
		}

		// 输出帖子的摘要，时间、作者、内容等
		JLabel lblText = new JLabel();
		lblText.setFont(new Font("华文仿宋", 0, 18));
		lblText.setBounds(110, 0, 500, 60);
		JLabel lblAuthor = new JLabel("作者：" + blog.authorName);
		lblAuthor.setBounds(365, 60, 150, 20);
		lblAuthor.setForeground(Color.GRAY);
		lblAuthor.setFont(new Font("", 0, 13));
		JLabel lblTime = new JLabel("发布时间：" + blog.time);
		lblTime.setForeground(Color.GRAY);
		lblTime.setFont(new Font("", 0, 13));
		lblTime.setBounds(365, 80, 250, 20);
		JlabelSetText(lblText, blog.title);
		content.add("Center", lblText);
		content.add("South", lblTime);
		content.add(lblAuthor);
		// 点帖子的话，进入帖子的详细内容
		content.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				blog.kind = textkind;
				blPage = new ShowBlog(blog, user, con);
				blPage.setBounds(0, 0, 640, 500);
				setVisible(false);
				blPage.setVisible(true);
				blPage.btnBack.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						blPage.setVisible(false);
						setVisible(true);
					}

				});
				((MainFace) frm_main).lbl_RightInfo.add(blPage);
			}
		});
		// 将新的帖子摘要加入主界面
		content.setBackground(Color.white);
		mains.add(content);
	}

	// 显示帖子摘要的主界面
	public Community(Usr_Info user_, connection con_, JFrame frm_main_) {
		this.setOpaque(true);

		this.setForeground(Color.black);
		this.setBackground(Color.WHITE);
		thisPanel = this;
		user = user_;
		con = con_;
		frm_main = frm_main_;
		((MainFace) frm_main_).community = thisPanel;
		this.setFont(new Font("Xingkai SC", Font.BOLD, 18));
		// 放多个帖子摘要的部件
		s = new JScrollPane();
		s.getVerticalScrollBar().setUnitIncrement(20);
		s.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.setLayout(null);
		this.setBounds(0, 0, 640, 500);
		this.add(s);
		s.setBounds(20, 80, 620, 420);

		s.getViewport().setLayout(null);

		s.setBorder(null);
		// 界面上的题目、种类
		title = new JPanel();
		title.setLayout(null);
		Ytitle = new JLabel("悠品");
		Ytitle.setForeground(Color.WHITE);
		Ytitle.setFont(new Font("华文仿宋", 1, 30));
		Ytitle.setBounds(20, 15, 100, 25);
		title.add(Ytitle);
		title.setBackground(new Color(183, 39, 18));
		title.setBounds(0, 0, 640, 50);
		
		// 提供选择不同种类帖子的部件
		kindBack = new JPanel();
		kindBack.setBounds(0, 50, 640, 30);
		kindBack.setLayout(null);
		kind = new JPanel();
		kind.setLayout(new GridLayout(1, 6));
		kind.setBounds(20, 0, 600, 30);
		for (int i = 0; i < 6; ++i) {
			Tkind[i] = new JLabel();
			Tkind[i].setFont(new Font("", 0, 20));
			Tkind[i].setText(Kind[i]);
			Tkind[i].setHorizontalAlignment(JLabel.CENTER);
			Tkind[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					for (int j = 0; j < 6; ++j) {
						if (e.getSource() == Tkind[j]) {
							Tkind[j].setForeground(Color.red);
							textkind = j + 1;
						} else
							Tkind[j].setForeground(Color.BLACK);
					}
				}
			});
			kind.add(Tkind[i]);
		}
		Tkind[0].setForeground(Color.red);
		kindBack.add(kind);
		this.add(kindBack);
		this.add(title);

		// 放多个帖子摘要的部件
		mains = new JPanel();
		mains.setPreferredSize(new Dimension(760, 2000));

		mains.setBounds(0, 0, 660, 2000);
		mains.setLayout(new GridLayout(20, 1, 0, 2));

			
		// 从服务器读取若干帖子摘要，将其显示
		blogAbstract.latestID = con.blognumber(textkind);
		ID = blogAbstract.latestID;
		int maxnum = ID;
		if (ID > 19)
			maxnum = 19;
		// 每次创建一个新的帖子摘要，加入主界面
		for (int i = 0; i < maxnum; ++i) {
			initBlogPane(ID - i, i);
		}
		JPanel content = new JPanel();
		String str = "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;More <html/>";
		content.setLayout(new BorderLayout());
		// More按钮，点击可以刷新显示的帖子
		JLabel btnMore = new JLabel(str);
		btnMore.setHorizontalAlignment(JLabel.CENTER);
		btnMore.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (ID < 19)
					return;
				ID -= 19;
				mains = new JPanel();
				mains.setPreferredSize(new Dimension(760, 2000));
				mains.setBounds(0, 0, 760, 2000);
				mains.setLayout(new GridLayout(20, 1, 2, 0));
				int max_num = ID;
				if (ID >= 19)
					max_num = 19;
				for (int i = 0; i < max_num; ++i) {
					initBlogPane(ID - i, i);
				}
				mains.add(content);
				s.setViewportView(mains);
			}

		});
		content.add("Center", btnMore);
		mains.add(content);
		s.setViewportView(mains);
		this.add(s);
		
		// 创建博客按钮，点击可以创建新的博客
		JButton btnBlog = new JButton("创建博客");
		btnBlog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				blog b;
				try {
					b = new blog(frm_main_, user_, con);
					((MainFace) frm_main_).lbl_RightInfo.add(b);
					((MainFace) frm_main_).community.setVisible(false);
					b.setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnBlog.setBounds(540, 10, 100, 34);
		title.add(btnBlog);

		// 标题
		btnMyPage = new JButton("\u6211\u7684\u4E3B\u9875");
		btnMyPage.setBounds(447, 417, 113, 34);
		this.add(btnMyPage);

		this.setVisible(true);

	}
}
