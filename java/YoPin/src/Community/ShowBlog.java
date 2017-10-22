package Community;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import Client.connection;
import Info.Usr_Info;

public class ShowBlog extends JPanel {

	// 用户信息
	private Usr_Info user;
	// 和服务器的连接
	private connection con;
	
	// 各种部件
	private blogAbstract blog;
	private final static int maxCommentNum = 30;	// 最多30个评论
	JPanel toptitle;
	JPanel mainScene;
	JPanel bottomScene;
	JScrollPane mainScroll;
	public JButton btnBack;
	JPanel pane_comment;
	JButton btnPublish;
	static final int textLength = 1000;

	public ShowBlog(blogAbstract b, Usr_Info user_, connection con_) {
		con = con_;
		blog = b;
		user = user_;

		// 折这各个部件的位置和大小
		setBounds(400, 400, 640, 500);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		mainScene = new JPanel();
		mainScene.setLayout(null);
		mainScroll = new JScrollPane();
		mainScroll.getVerticalScrollBar().setUnitIncrement(15);
		this.add(mainScroll);
		mainScroll.setVisible(true);
		mainScroll.setBounds(20, 35, 600, 410);
		mainScroll.setBorder(null);
		// 帖子区
		JTextPane textArea = new JTextPane();
		textArea.setBounds(0, 0, 582, textLength);
		showBlogText(textArea);
		mainScene.add(textArea);
		// 评论区
		pane_comment = new JPanel();
		mainScene.add(pane_comment);
		showComment();

		// 标题
		toptitle = new JPanel();
		toptitle.setLayout(null);
		toptitle.setBounds(0, 0, 640, 35);
		toptitle.setBackground(Color.WHITE);
		JLabel lblNewLabel = new JLabel(blog.title);
		lblNewLabel.setBounds(0, 0, 640, 40);
		lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
		lblNewLabel.setFont(new Font("Xingkai SC", Font.BOLD, 20));
		toptitle.add(lblNewLabel);
		this.add(toptitle);

		bottomScene = new JPanel();
		bottomScene.setLayout(null);
		bottomScene.setBackground(Color.WHITE);
		bottomScene.setBounds(0, 445, 640, 45);
		this.add(bottomScene);

		// 发表新评论的按钮
		JTextField comment = new JTextField("写评论...");
		comment.setBounds(130, 0, 300, 30);
		comment.setBackground(Color.GRAY);
		comment.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				comment.setText("");
			}
		});
		bottomScene.add(comment);

		btnBack = new JButton("\u8FD4\u56DE");
		btnBack.setBounds(10, 10, 93, 23);
		toptitle.add(btnBack);

		// 发表评论
		btnPublish = new JButton("\u53D1\u8868");
		btnPublish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (comment.getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "评论不能为空", "警告", JOptionPane.ERROR_MESSAGE);
					comment.setText("写评论...");
					return;
				}
				blogComment temp = new blogComment();
				temp.authorName = user.getName();

				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				temp.time = format.format(date);

				temp.commentText = comment.getText();
				System.out.println("Before comment");
				con.addcomment(blog.kind, blog.id, temp);
				comment.setText("写评论...");
				pane_comment.removeAll();
				blog.commentID++;
				showComment();
				JOptionPane.showMessageDialog(null, "发布成功", "通知", JOptionPane.ERROR_MESSAGE);
			}
		});
		btnPublish.setBounds(490, 5, 93, 23);
		bottomScene.add(btnPublish);

		mainScroll.setViewportView(mainScene);

	}

	// 将HTML文件显示出来，显示帖子的内容
	private void showBlogText(JTextPane tArea) {
		// important
		deleteDirectory("./Yopin/tmp");
		File dir = new File("./Yopin/tmp");
		dir.mkdir();
		con.getblogmaintext(blog.kind, blog.id, "./Yopin/tmp");
		tArea.setContentType("text/html");
		try {
			tArea.setPage("file:./Yopin/tmp/blog.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		tArea.setEditable(false);
	}

	// 显示帖子下的评论
	public void showComment() {
		int num = blog.commentID;
		int maxNum = num;
		if (maxNum > maxCommentNum)
			maxNum = maxCommentNum;
		mainScene.setPreferredSize(new Dimension(582, textLength + maxNum * 110));
		pane_comment.setBounds(0, textLength, 764, maxNum * 100);
		pane_comment.setLayout(new GridLayout(maxNum, 1, 5, 5));
		
		// 每一个评论初始化，包括大小、字体等等
		for (int i = 0; i < maxNum; ++i) {
			JPanel content = new JPanel();
			content.setBackground(Color.WHITE);
			JLabel comment_author = new JLabel();
			JLabel comment_text = new JLabel();
			comment_text.setFont(new Font("", 0, 20));
			comment_author.setFont(new Font("", 0, 14));
			comment_author.setForeground(Color.blue);
			JLabel comment_time = new JLabel();
			comment_time.setFont(new Font("", 0, 10));
			comment_time.setForeground(Color.LIGHT_GRAY);
			content.setLayout(null);
			comment_author.setBounds(40, 0, 520, 15);
			comment_text.setBounds(40, 20, 520, 60);
			comment_time.setBounds(40, 80, 520, 10);
			content.add(comment_author);
			content.add(comment_text);
			content.add(comment_time);
			
			
			blogComment bc = con.getblogcomment(blog.kind, blog.id, num - i);
			comment_author.setText(bc.authorName);
			comment_time.setText(bc.time);
			JlabelSetText(comment_text, bc.commentText);
			
			pane_comment.add(content);
		}
	}

	// 自动换行函数，使得JLabel内的文字能够自动换行
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