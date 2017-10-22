/*
 * 社区主题软件（悠品）
 * version 1.0
 * 主界面
 * 2017-5-23
 * 最近修改 2017-6-23
 * 修改内容：改变界面样貌和组件
 * 源代码编码：UTF-8
 * 作者：郑思泽  zhengsz@pku.edu.cn
 * 
*/
package MainPage;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Client.connection;
import Community.Community;
import Info.Usr_Info;

/*
 * 主界面主要类
 */
public class MainFace extends JFrame {
	private static final long serialVersionUID = 1L;
	File Root, Picture, User;// 文件目录
	File img;// 临时图片
	int imgHeight, imgWidth;// 临时图片大小
	File jf_mem;// 存储最近一次打开目录
	Usr_Info user_info;// 用户信息
	connection con;// 与服务器连接
	JFrame pthis = this;
	/*
	 * 界面组件
	 */
	public JPanel community, newblog, showblog;
	public JLabel lbl_LeftInfo, lbl_MidInfo, lbl_RightInfo;
	JLabel lbl_LuserImage, lbl_Litems[], lbl_Msearch, lbl_Mitems[], lbl_Mimages[], lbl_Ritems[], lbl_Rnull;
	final int Litems_num = 3, Mitems_num = 8, Ritems_num = 0;// 各个label所含的子label数目
	JTextField txt_MSearch;
	JButton btn_MSearch;
	JLabel lbl_warning;
	String Mstring[] = { "悠品新闻", "悠品娱乐", "悠品贴吧", "悠品运动", "悠品美食", "悠品轻奢", "悠品美图", "悠品生活" };
	SelfPage selfPage;
	Community comPage;

	Color ColorOpt[] = { Color.red, Color.GREEN, Color.pink, Color.blue, Color.CYAN, Color.darkGray, Color.green,
			Color.magenta, Color.ORANGE, Color.YELLOW, Color.lightGray };
	Random r = new Random();

	/*
	 * 初始化函数
	 */
	public void init() throws IOException {
		init_LayBoard();// 初始化底层面板
		init_SelfPage();// 初始化个人修改主页

		this.setBounds(200, 150, 900, 500);
		this.setLayout(null);
		this.addWindowListener(new WindowAdapter() {// 关闭窗口时发送信息给服务端
			public void windowClosing(WindowEvent e) {
				con.close();
			}
		});
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	/*
	 * 底层面板初始化函数
	 */

	private void init_LayBoard() throws IOException {
		/*
		 * 界面初始化部分 分为左中右三部分为三个label
		 */
		lbl_LeftInfo = new JLabel();
		lbl_LeftInfo.setBounds(0, 0, 60, 500);
		lbl_LeftInfo.setOpaque(true);
		lbl_LeftInfo.setBackground(Color.BLACK);

		lbl_LuserImage = new JLabel(); // 用户头像
		lbl_LuserImage.setBounds(5, 5, 50, 50);
		img = new File(Picture.getPath() + "/user_image0.jpg");
		if (!img.exists())
			img = new File(Picture.getPath() + "/default_image.jpg");
		System.out.println(Picture);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(img.getPath()));
		Image bi = ImageIO.read(in);
		imgHeight = bi.getHeight(null);
		imgWidth = bi.getWidth(null);
		if (imgHeight != 50 || imgWidth != 50) {
			MyImageTools.resizePicture(bi, 50, 50, img); // 将图片变为指定大小
		}
		in.close();
		lbl_LuserImage.setIcon(new ImageIcon(img.getPath()));

		lbl_LeftInfo.add(lbl_LuserImage);

		lbl_Litems = new JLabel[Litems_num];
		for (int i = 0; i < Litems_num; ++i) { // 左侧面板控制组件初始化
			lbl_Litems[i] = new JLabel();
			lbl_Litems[i].setBounds(5, 60 * (i + 1) + 5, 50, 50);

			img = new File(Picture.getPath() + "/L_image" + i + ".jpg");
			if (!img.exists())
				img = new File(Picture.getPath() + "/default_image.jpg");

			in = new BufferedInputStream(new FileInputStream(img.getPath()));
			bi = ImageIO.read(in);
			imgHeight = bi.getHeight(null);
			imgWidth = bi.getWidth(null);
			if (imgHeight != 50 || imgWidth != 50) {
				MyImageTools.resizePicture(bi, 50, 50, img);
			}
			in.close();
			lbl_Litems[i].setIcon(new ImageIcon(img.getPath()));
			lbl_Litems[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lbl_LeftInfo.add(lbl_Litems[i]);
		}
		// 为组件添加事件响应
		lbl_Litems[0].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) { // 该按钮响应新闻界面打开事件
				lbl_Litems[0].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) { // 新闻界面初始化
				lbl_Litems[0].setBorder(null);
			}

			public void mouseClicked(MouseEvent e) { // 该按钮响应个人主页打开事件
				lbl_Rnull.setVisible(false);
				selfPage.setVisible(false);
				comPage = new Community(user_info, con, pthis);
				comPage.setBounds(0, 0, 640, 500);
				comPage.setVisible(true);
				lbl_RightInfo.add(comPage);

			}
		});

		lbl_Litems[1].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Litems[1].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Litems[1].setBorder(null);
			}

			public void mouseClicked(MouseEvent e) {
				lbl_Rnull.setVisible(false);
				comPage.setVisible(false);
				selfPage.refresh();
				selfPage.setVisible(true);
			}
		});

		lbl_Litems[2].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Litems[2].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Litems[2].setBorder(null);
			}

			public void mouseClicked(MouseEvent e) {
				lbl_Rnull.setVisible(true);
				selfPage.setVisible(false);
			}
		});

		lbl_MidInfo = new JLabel();
		lbl_MidInfo.setBounds(60, 0, 200, 500);
		lbl_MidInfo.setOpaque(true);
		lbl_MidInfo.setBackground(Color.LIGHT_GRAY);

		lbl_Msearch = new JLabel();
		lbl_Msearch.setBounds(0, 0, 200, 50);
		txt_MSearch = new JTextField();
		txt_MSearch.setBounds(5, 5, 140, 40);
		lbl_Msearch.add(txt_MSearch);

		btn_MSearch = new JButton();
		btn_MSearch.setBounds(155, 5, 40, 40);
		img = new File(Picture.getPath() + "/search_image.jpg");
		if (!img.exists())
			img = new File(Picture.getPath() + "/default_image.jpg");

		in = new BufferedInputStream(new FileInputStream(img.getPath()));
		bi = ImageIO.read(in);
		imgHeight = bi.getHeight(null);
		imgWidth = bi.getWidth(null);
		if (imgHeight != 40 || imgWidth != 40) {
			MyImageTools.resizePicture(bi, 40, 40, img);
		}
		in.close();
		btn_MSearch.setIcon(new ImageIcon(img.getPath()));
		lbl_Msearch.add(btn_MSearch);

		lbl_MidInfo.add(lbl_Msearch);

		lbl_Mitems = new JLabel[Mitems_num];
		lbl_Mimages = new JLabel[Mitems_num];
		for (int i = 0; i < Mitems_num; ++i) { // 中间组件初始化
			lbl_Mitems[i] = new JLabel();
			lbl_Mimages[i] = new JLabel();
			lbl_Mitems[i].setBounds(0, 50 * (i + 1), 200, 50);
			lbl_Mimages[i].setBounds(5, 5, 40, 40);
			img = new File(Picture.getPath() + "/M_image" + i + ".jpg");
			if (!img.exists())
				img = new File(Picture.getPath() + "/default_image.jpg");

			in = new BufferedInputStream(new FileInputStream(img.getPath()));
			bi = ImageIO.read(in);
			int height = bi.getHeight(null);
			int width = bi.getWidth(null);
			if (height != 40 || width != 40) {
				MyImageTools.resizePicture(bi, 40, 40, img);
			}
			in.close();
			lbl_Mimages[i].setIcon(new ImageIcon(img.getPath()));
			lbl_Mitems[i].add(lbl_Mimages[i]);
			lbl_Mitems[i].setFont(new Font("宋体", Font.BOLD, 18));
			lbl_Mitems[i].setText("        " + Mstring[i]);
			lbl_Mitems[i].setBorder(BorderFactory.createEtchedBorder());
			lbl_Mitems[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lbl_MidInfo.add(lbl_Mitems[i]);
		}
		// 为中间组件添加事件响应
		lbl_Mitems[0].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Mitems[0].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Mitems[0].setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseClicked(MouseEvent e) {
				// 功能定义，具体尚未实现
			}
		});

		lbl_Mitems[1].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Mitems[1].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Mitems[1].setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseClicked(MouseEvent e) {
				// 功能定义，具体尚未实现
			}
		});

		lbl_Mitems[2].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Mitems[2].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Mitems[2].setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseClicked(MouseEvent e) {
				// 功能定义，具体尚未实现
			}
		});

		lbl_Mitems[3].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Mitems[3].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Mitems[3].setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseClicked(MouseEvent e) {
				// 功能定义，具体尚未实现
			}
		});

		lbl_Mitems[4].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Mitems[4].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Mitems[4].setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseClicked(MouseEvent e) {
				// 功能定义，具体尚未实现
			}
		});

		lbl_Mitems[5].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Mitems[5].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Mitems[5].setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseClicked(MouseEvent e) {
				// 功能定义，具体尚未实现
			}
		});

		lbl_Mitems[6].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Mitems[6].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Mitems[6].setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseClicked(MouseEvent e) {
				// 功能定义，具体尚未实现
			}
		});

		lbl_Mitems[7].addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lbl_Mitems[7].setBorder(BorderFactory.createLineBorder(ColorOpt[r.nextInt(ColorOpt.length)]));
			}

			public void mouseExited(MouseEvent e) {
				lbl_Mitems[7].setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseClicked(MouseEvent e) {
				// 功能定义，具体尚未实现
			}
		});
		// 右侧部分初始化
		lbl_RightInfo = new JLabel();
		lbl_RightInfo.setBounds(260, 0, 640, 500);
		lbl_RightInfo.setOpaque(true);
		// 初始化设置界面
		lbl_Rnull = new JLabel();
		lbl_Rnull.setBounds(0, 0, 640, 500);
		img = new File(Picture.getPath() + "/logo_image.jpeg");
		if (!img.exists())
			img = new File(Picture.getPath() + "/default_image.jpg");

		in = new BufferedInputStream(new FileInputStream(img.getPath()));
		bi = ImageIO.read(in);
		int height = bi.getHeight(null);
		int width = bi.getWidth(null);
		if (height != 500 || width != 640) {
			MyImageTools.resizePicture(bi, 640, 500, img);
		}
		in.close();
		lbl_Rnull.setIcon(new ImageIcon(img.getPath()));
		lbl_RightInfo.add(lbl_Rnull);
		// 用于提醒重要信息的窗口
		lbl_warning = new JLabel();
		lbl_warning.setBounds(200, 0, 200, 20);
		lbl_warning.setOpaque(true);
		lbl_warning.setBackground(Color.red);
		lbl_warning.setForeground(Color.black);
		lbl_warning.setFont(new Font("华文新魏", Font.PLAIN, 18));
		lbl_warning.setVisible(false);
		// 将三部分界面添加到窗口容器中
		lbl_RightInfo.add(lbl_warning);

		this.add(lbl_LeftInfo);
		this.add(lbl_MidInfo);
		this.add(lbl_RightInfo);

	}

	/*
	 * 初始化个人信息主页
	 */
	private void init_SelfPage() throws IOException {
		selfPage = new SelfPage();
		selfPage.setBounds(0, -5, 640, 500);
		selfPage.setVisible(false);

		lbl_RightInfo.add(selfPage);
	}

	/*
	 * 构造函数 参数1：界面访问信息所用根目录 参数2：界面访问 图片所用目录 参数3：界面访问用户资料所用目录 参数4：用户信息 参数5：与服务器链接
	 * 
	 * 抛出异常：IOException
	 */
	public MainFace(File Root_, File Picture_, File User_, Usr_Info user_info_, connection con_) throws IOException {
		super("悠品");
		Root = Root_;
		Picture = Picture_;
		User = User_;
		user_info = user_info_;
		con = con_;
		init();
		System.out.println("client" + user_info.getName());
	}

	/*
	 * 内部类，个人主页 JPanel子类
	 */
	class SelfPage extends JPanel {
		private static final long serialVersionUID = 1L;
		JLabel lbl_change_back; // 用于充当背景，存放其他组件
		JTextField txt_change[]; // 信息内容
		JLabel lbl_image[]; // 属性图像与用户头像
		JLabel lbl_image_ins; // 提示信息
		JLabel lbl_id, lbl_level;
		JButton btn_save; // 保存按钮
		/*
		 * 初始化函数 初始化界面组件
		 */

		void init() throws IOException {
			lbl_change_back = new JLabel();
			// 设置背景图片
			img = new File(Picture.getPath() + "/change_back_image.jpg");
			if (!img.exists())
				img = new File(Picture.getPath() + "/default_image.jpeg");

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(img.getPath()));
			Image bi = ImageIO.read(in);
			imgHeight = bi.getHeight(null);
			imgWidth = bi.getWidth(null);
			if (imgHeight != 500 || imgWidth != 640) {
				MyImageTools.resizePicture(bi, 640, 500, img);// 修改成指定大小
			}
			in.close();
			lbl_change_back.setIcon(new ImageIcon(img.getPath()));

			lbl_image = new JLabel[8];
			for (int i = 0; i < 7; ++i) { // 个人信息属性组件
				lbl_image[i] = new JLabel();
				lbl_image[i].setBounds(70, 80 + i * 50, 100, 40);
				img = new File(Picture.getPath() + "/change_image" + i + ".jpg");
				if (!img.exists())
					img.createNewFile();

				in = new BufferedInputStream(new FileInputStream(img.getPath()));
				bi = ImageIO.read(in);
				imgHeight = bi.getHeight(null);
				imgWidth = bi.getWidth(null);
				if (imgHeight != 40 || imgWidth != 100) {
					MyImageTools.resizePicture(bi, 100, 40, img);
				}
				in.close();
				lbl_image[i].setIcon(new ImageIcon(img.getPath()));
				JLabel tmp = lbl_image[i];
				lbl_image[i].addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent e) {
						tmp.setBorder(BorderFactory.createRaisedBevelBorder());
					}

					public void mouseExited(MouseEvent e) {
						tmp.setBorder(null);
					}
				});

				lbl_change_back.add(lbl_image[i]);
			}
			lbl_image[7] = new JLabel(); // 用户头像加载
			lbl_image[7].setBounds(400, 80, 90, 90);
			lbl_image[7].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			img = new File(Picture.getPath() + "/user_image1.jpg");
			if (!img.exists())
				img.createNewFile();

			in = new BufferedInputStream(new FileInputStream(img.getPath()));
			bi = ImageIO.read(in);
			imgHeight = bi.getHeight(null);
			imgWidth = bi.getWidth(null);
			if (imgHeight != 90 || imgWidth != 90) {
				MyImageTools.resizePicture(bi, 90, 90, img);
			}
			in.close();
			lbl_image[7].setIcon(new ImageIcon(img.getPath()));
			lbl_image[7].addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					lbl_image_ins.setVisible(true);
					lbl_image[7].setBorder(BorderFactory.createLineBorder(Color.blue));
				}

				public void mouseExited(MouseEvent e) {
					lbl_image_ins.setVisible(false);
					lbl_image[7].setBorder(null);
				}

				public void mouseClicked(MouseEvent e) { // 响应头像更改事件
					JFileChooser jf = new JFileChooser();
					jf.setDialogTitle("选取头像");
					jf.setAcceptAllFileFilterUsed(false);
					if (jf_mem != null)
						jf.setCurrentDirectory(jf_mem);
					// 设置文件筛选
					jf.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

						@Override
						public boolean accept(File f) {
							String s = f.getName().toLowerCase();
							if (s.endsWith(".jpg") || f.isDirectory())
								return true;
							return false;
						}

						// 描述内容为*.JPG
						@Override
						public String getDescription() {
							// TODO Auto-generated method stub
							return new String("*.JPG");
						}

					});
					;
					jf.showOpenDialog(new JLabel());
					img = jf.getSelectedFile();
					if (img != null && img.exists()) {
						// 头像更改涉及两部分图片改变，fp是个人界面头像更改，fpp是主界面头像更改
						File fp = new File(Picture.getPath() + "/user_image1.jpg");
						File fpp = new File(Picture.getPath() + "/user_image0.jpg");
						try {

							BufferedInputStream in = new BufferedInputStream(new FileInputStream(img.getPath()));
							Image bi = ImageIO.read(in);
							MyImageTools.resizePicture(bi, 90, 90, fp);
							MyImageTools.resizePicture(bi, 40, 40, fpp);
							String s = img.getParent();
							jf_mem = new File(s);
							// 采用随机数作为文件名，避免因路径的一致导致更新头像失败
							File tmp = new File(s + "/" + r.nextInt() + "YoPin_tmp.jpg");
							if (!tmp.exists())
								tmp.createNewFile();
							MyImageTools.resizePicture(bi, 90, 90, tmp);
							lbl_image[7].setIcon(new ImageIcon(tmp.getAbsolutePath()));

							in = new BufferedInputStream(new FileInputStream(img.getPath()));
							bi = ImageIO.read(in);
							MyImageTools.resizePicture(bi, 90, 90, fp);
							MyImageTools.resizePicture(bi, 40, 40, fpp);
							s = img.getParent();
							jf_mem = new File(s);
							// 采用随机数作为文件名，避免因路径的一致导致更新头像失败
							tmp = new File(s + "/" + r.nextInt() + "YoPin_tmp.jpg");
							if (!tmp.exists())
								tmp.createNewFile();
							MyImageTools.resizePicture(bi, 40, 40, tmp);
							lbl_LuserImage.setIcon(new ImageIcon(tmp.getAbsolutePath()));
							tmp.delete();
							in.close();

						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			});
			lbl_change_back.add(lbl_image[7]);
			// 初始化提醒信息窗口
			lbl_image_ins = new JLabel();
			lbl_image_ins.setBounds(500, 30, 90, 20);
			lbl_image_ins.setText("点击修改头像");
			lbl_image_ins.setVisible(false);
			lbl_image_ins.setOpaque(true);
			lbl_image_ins.setBackground(Color.white);
			lbl_image_ins.setForeground(Color.BLACK);
			lbl_image_ins.setFont(new Font("华文新魏", Font.PLAIN, 15));
			lbl_change_back.add(lbl_image_ins);

			btn_save = new JButton();
			btn_save.setOpaque(true);
			btn_save.setBackground(Color.WHITE);
			btn_save.setBounds(500, 400, 100, 25);
			btn_save.setText("保存");
			btn_save.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					// 进入事件
				}

				public void mouseExited(MouseEvent e) {
					// 离开事件
				}

				public void mouseClicked(MouseEvent e) {// 响应点击事件
					boolean ok = true;
					int a = txt_change[4].getText().indexOf("@");
					int len = txt_change[4].getText().length();
					if (a <= 0 || a >= len - 1 || len < 3) {
						ok = false;
						lbl_warning.setSize(200, 20);
						lbl_warning.setText("邮箱地址格式不正确");
						lbl_warning.setVisible(true);
					}
					if (txt_change[0].getText().length() == 0) {
						ok = false;
						lbl_warning.setSize(130, 20);
						lbl_warning.setText("不能有空昵称");
						lbl_warning.setVisible(true);
					}
					if (ok) {// 合理信息，进行保存
						lbl_warning.setVisible(false);
						user_info.setName(txt_change[0].getText());
						user_info.setHobby(txt_change[2].getText());
						user_info.setAge(txt_change[3].getText());
						user_info.setMail(txt_change[4].getText());
						user_info.setStar(txt_change[5].getText());
						user_info.setJob(txt_change[6].getText());
						// 发送给服务器
						con.usr_info_change(user_info);
					}
				}
			});
			// 信息填写框初始化
			txt_change = new JTextField[7];
			for (int i = 0; i < 7; ++i) {
				txt_change[i] = new JTextField();
				txt_change[i].setBounds(180, 80 + 50 * i, 200, 40);
				txt_change[i].setOpaque(true);
				txt_change[i].setBackground(Color.white);
				txt_change[i].setForeground(Color.BLACK);
				txt_change[i].setFont(new Font("黑体", Font.PLAIN, 15));
				lbl_change_back.add(txt_change[i]);
			}
			txt_change[1].setVisible(false);
			lbl_id = new JLabel();
			lbl_id.setBounds(180, 130, 200, 40);
			lbl_id.setOpaque(true);
			lbl_id.setForeground(Color.black);
			lbl_id.setBackground(Color.WHITE);
			lbl_id.setFont(new Font("黑体", Font.PLAIN, 15));
			lbl_change_back.add(lbl_id);

			lbl_change_back.add(btn_save);

			this.add(lbl_change_back);
			this.setBounds(0, 0, 640, 500);
		}

		/*
		 * 用于刷新用户信息
		 */
		public void refresh() {
			txt_change[0].setText(user_info.getName());
			lbl_id.setText(user_info.getID());
			txt_change[2].setText(user_info.getHobby());
			txt_change[3].setText(user_info.getAge());
			txt_change[4].setText(user_info.getMail());
			txt_change[5].setText(user_info.getStar());
			txt_change[6].setText(user_info.getJob());
		}

		SelfPage() throws IOException {
			init();
		}

	}

	/*
	 * 内部类，设置界面 JPanel子类
	 */
	class SetPage extends JPanel {
		JLabel lbl_back, lbl_return, lbl_return_ins, lbl_msg;

		public void init() throws IOException {
			lbl_back = new JLabel();
			lbl_back.setBounds(0, 0, 700, 500);

			img = new File(Picture.getPath() + "/set_image.jpg");
			if (!img.exists())
				img = new File(Picture.getPath() + "/default_image.jpg");

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(img.getPath()));
			Image bi = ImageIO.read(in);
			int height = bi.getHeight(null);
			int width = bi.getWidth(null);
			if (height != 500 || width != 700) {
				MyImageTools.resizePicture(bi, 700, 500, img);
			}
			in.close();
			lbl_back.setIcon(new ImageIcon(img.getPath()));

			lbl_return = new JLabel();
			lbl_return.setBounds(670, 0, 30, 500);
			lbl_return.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lbl_return.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					lbl_return_ins.setVisible(false);
				}

				public void mouseEntered(MouseEvent e) {
					lbl_return_ins.setVisible(true);
				}

				public void mouseExited(MouseEvent e) {
					lbl_return_ins.setVisible(false);
				}
			});
			lbl_back.add(lbl_return);

			lbl_return_ins = new JLabel();
			lbl_return_ins.setBounds(700, 0, 100, 20);
			lbl_return_ins.setText("点击返回");
			lbl_return_ins.setVisible(false);
			lbl_return_ins.setOpaque(true);
			lbl_return_ins.setForeground(Color.black);
			lbl_return_ins.setBackground(Color.white);
			lbl_return_ins.setFont(new Font("华文新魏", Font.PLAIN, 18));
			// lbl_board.add(lbl_return_ins);

			lbl_msg = new JLabel();
			lbl_msg.setBounds(50, 50, 260, 20);
			lbl_msg.setText("软件仍在建设中，感谢支持！");
			lbl_msg.setFont(new Font("华文新魏", Font.PLAIN, 20));
			lbl_msg.setOpaque(true);
			lbl_msg.setBackground(Color.white);
			lbl_msg.setForeground(Color.black);
			lbl_back.add(lbl_msg);

			this.add(lbl_back);
		}
		/*
		 * 构造函数
		 */

		SetPage() throws IOException {
			init();
		}
	}

}

/*
 * 图像调整辅助类
 */
class MyImageTools {
	/*
	 * 用于改变图像大小并存储 参数1：原图像 参数2：目标图像宽 参数3：目标图像高 参数4：目标图像 抛出异常：IOException
	 */
	public static void resizePicture(Image in, int width, int height, File outfile) throws IOException {
		// 创建指定大小缓存
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 存入图片
		tag.getGraphics().drawImage(in, 0, 0, width, height, null);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outfile.getPath()));
		// 输出图像
		ImageIO.write(tag, "JPG", out);
		out.close();
	}
}
