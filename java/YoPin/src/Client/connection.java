/*
 *和服务器进行沟通的connection类，实现各种交互功能
 */
package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 *
 * @author zqh
 */
import java.net.Socket;

import Community.blogAbstract;
import Community.blogComment;
import Info.Usr_Info;

public class connection {

	private String serverip;
	private int port;
	private Socket client_socket;
	private DataOutputStream cout;
	private DataInputStream cin;
	public boolean active;
	private String usrId;

	public connection(String usrid) {
		usrId = usrid;
		// 向port8888和服务器建立连接
		port = 8888;
		// 本地测试
		serverip = new String("127.0.0.1");
		// 启用服务器
		// serverip=new String("120.77.203.54");
		active = true;
		// 向套接字发出连接请求
		try {
			client_socket = new Socket(serverip, port);
			cout = new DataOutputStream(client_socket.getOutputStream());
			cin = new DataInputStream(client_socket.getInputStream());
			cout.writeUTF(usrid);
		} catch (Exception e) {
			active = false;
			e.printStackTrace();
		}
	}

	// 用户登录请求
	public String sentusr_password(String usr_id, String usr_password) {
		String result = "";
		try {
			cout.writeUTF("usr_password");
			cout.writeUTF(usr_id);
			cout.writeUTF(usr_password);
			result = cin.readUTF();
		} catch (Exception e) {
			System.out.print(e);
		}
		return result;
	}

	// 申请新用户
	public String new_usr(String usr_id, String usr_password, String usr_email) {
		String result = "";
		try {
			cout.writeUTF("usr_newusr");
			cout.writeUTF(usr_id);
			cout.writeUTF(usr_password);
			cout.writeUTF(usr_email);
			result = cin.readUTF();
		} catch (IOException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 修改用户信息
	public void usr_info_change(Usr_Info new_usr) {
		try {
			cout.writeUTF("change_usr_info");
			cout.writeUTF(new_usr.getName());
			cout.writeUTF(new_usr.getID());
			cout.writeUTF(new_usr.getKeyword());
			cout.writeUTF(new_usr.getStar());
			cout.writeUTF(new_usr.getAge());
			cout.writeUTF(new_usr.getJob());
			cout.writeUTF(new_usr.getHobby());
			cout.writeUTF(new_usr.getMail());

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 获取帖子标题内容
	public blogAbstract getBlogText(int kind, int id) {
		blogAbstract result = new blogAbstract();
		try {
			cout.writeUTF("getblogtext");
			cout.writeInt(kind);
			cout.writeInt(id);
			String title = cin.readUTF();

			String authorname = cin.readUTF();
			String time = cin.readUTF();
			String commentid = cin.readUTF();
			byte buf[] = new byte[8000];
			String name = cin.readUTF();
			long flength = cin.readLong();
			String filename = "src/blog_icon/" + id + name;
			DataOutputStream fout = new DataOutputStream(new FileOutputStream(filename));
			long rlen = 0;
			while (true) {
				int read = 0;
				read = cin.read(buf);
				rlen += read;
				fout.write(buf, 0, read);
				if (rlen == flength)
					break;
			}
			fout.flush();
			result.authorName = new String(authorname);
			result.commentID = Integer.parseInt(commentid);
			result.time = new String(time);
			result.title = new String(title);
			result.filename = new String(filename);
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	// 获取贴子主要内容
	public void getblogmaintext(int kind, int blogID, String filepath) {
		try {
			cout.writeUTF("getblogmaintext");
			cout.writeInt(kind);
			cout.writeInt(blogID);
			int filenum = cin.readInt();
			for (int i = 0; i < filenum; ++i) {
				String filename = cin.readUTF();
				long flength = cin.readLong();
				File file = new File(filepath + "/" + filename);
				file.createNewFile();
				DataOutputStream fout = new DataOutputStream(new FileOutputStream(file));
				byte buf[] = new byte[8000];
				long rlen = 0;
				while (true) {
					int read = 0;
					read = cin.read(buf);
					rlen += read;
					fout.write(buf, 0, read);
					if (rlen == flength)
						break;
				}
				fout.flush();
				fout.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	// 获得帖子的已有评论
	public blogComment getblogcomment(int kind, int blogID, int commentID) {
		blogComment buf = new blogComment();
		try {
			cout.writeUTF("getblogcomment");
			cout.writeInt(kind);
			cout.writeInt(blogID);
			cout.writeInt(commentID);
			buf.authorName = cin.readUTF();
			buf.time = cin.readUTF();
			buf.commentText = cin.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf;
	}

	// 新建帖子
	public void createblog(blogAbstract abstractblog, String filename, File file) {
		try {
			cout.writeUTF("createblog");
			cout.writeInt(abstractblog.kind);
			cout.writeUTF(abstractblog.title);
			cout.writeUTF(abstractblog.authorName);
			cout.writeUTF(abstractblog.time);
			cout.writeUTF(String.valueOf(abstractblog.commentID));
			DataInputStream fin = new DataInputStream(new FileInputStream(file));
			cout.writeUTF(file.getName());
			cout.writeLong(file.length());
			byte buf[] = new byte[8000];
			while (true) {
				int read = 0;
				read = fin.read(buf);
				if (read == -1) {

					break;
				}
				cout.write(buf, 0, read);
			}
			Thread.sleep(10);
			File Allfile = new File(filename);
			File[] files = Allfile.listFiles();
			cout.writeInt(files.length);
			for (int i = 0; i < files.length; ++i) {
				// 重要，需要休眠10ms
				Thread.sleep(10);
				fin = new DataInputStream(new FileInputStream(files[i]));
				cout.writeUTF(files[i].getName());
				cout.writeLong(files[i].length());
				buf = new byte[8000];
				while (true) {
					int read = 0;
					read = fin.read(buf);
					System.out.println(read);
					if (read == -1) {
						break;
					}
					cout.write(buf, 0, read);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 添加评论
	public void addcomment(int kind, int blogid, blogComment comment) {
		try {
			cout.writeUTF("addcomment");
			cout.writeInt(kind);
			cout.writeInt(blogid);
			cout.writeUTF(comment.authorName);
			cout.writeUTF(comment.time);
			cout.writeUTF(comment.commentText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获得当前已有帖子数目
	public int blognumber(int kind) {
		int ans = 0;
		try {
			cout.writeUTF("getblognumber");
			cout.writeInt(kind);
			ans = cin.readInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}

	// 从服务器获取用户信息
	public Usr_Info getUsrFromServer(String usrName) throws IOException {
		cout.writeUTF("getUsr");
		cout.writeUTF(usrName);
		Usr_Info tmp;
		String name, id, star, age, job, hobby, mail;
		name = cin.readUTF();
		id = cin.readUTF();
		star = cin.readUTF();
		age = cin.readUTF();
		job = cin.readUTF();
		hobby = cin.readUTF();
		mail = cin.readUTF();

		tmp = new Usr_Info(id, "test");
		tmp.setName(name);
		tmp.setAge(age);
		tmp.setStar(star);
		tmp.setJob(job);
		tmp.setHobby(hobby);
		tmp.setMail(mail);
		return tmp;
	}

	// 关闭套接字连接
	public void close() {
		try {
			cout.writeUTF("bye");
			client_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
