package newServer;

/*
 * Community App Version 0.0
 * 
 * User information class
 * 
 * By ZSZ: 188124@pku.edu.cn 
 * 
 * Original edit : 2017-3-23 
 * Last edit : 2017-3-23
 * 
 * */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class Usr_Info implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String usr_name;
	private String usr_star;
	private String usr_age;
	private String usr_job;
	private String usr_hobby;
	private String usr_keyword;
	private String usr_id;
	private String usr_mail;

	// constructor
	public Usr_Info(String usr_id_, String usr_keyword_) {
		usr_id = usr_id_;
		usr_keyword = Encryption.Encrypt(usr_keyword_);
		usr_name = "unknown";
		usr_star = "unknown";
		usr_age = "unknown";
		usr_job = "unknown";
		usr_hobby = "unknown";
		usr_mail = "unknown";
	}

	public Usr_Info(String usr_id_, String usr_keyword_, String usr_mail_, String usrname) {
		usr_id = usr_id_;
		usr_keyword = Encryption.Encrypt(usr_keyword_);
		usr_name = new String(usrname);
		usr_star = "unknown";
		usr_age = "unknown";
		usr_job = "unknown";
		usr_hobby = "unknown";
		usr_mail = usr_mail_;
	}
	// get information

	public String getID() {
		return new String(usr_id);
	}

	private void setID(String newID) {
		usr_id = newID;
	}

	public String getKeyword() {
		return new String(usr_keyword);
	}

	private void setKeyword(String newKeyword) {
		usr_keyword = Encryption.Encrypt(newKeyword);
	}

	public String getName() {
		return new String(usr_name);
	}

	public void setName(String newName) {
		usr_name = newName;
	}

	public String getStar() {
		return new String(usr_star);
	}

	public void setStar(String newStar) {
		usr_star = newStar;
	}

	public String getAge() {
		return new String(usr_age);
	}

	public void setAge(String newAge) {
		usr_age = newAge;
	}

	public String getJob() {
		return new String(usr_job);
	}

	public void setJob(String newJob) {
		usr_job = newJob;
	}

	public String getHobby() {
		return new String(usr_hobby);
	}

	public void setHobby(String newHobby) {
		usr_hobby = newHobby;
	}

	public String getMail() {
		return new String(usr_mail);
	}

	public void setMail(String newMail) {
		usr_mail = newMail;
	}

	public static void writeUser(Usr_Info thisUsr) throws IOException {
		String filename = "src/usr/" + thisUsr.getID() + ".txt";
		File f = new File(filename);
		if (!f.exists()) {
			f.createNewFile();
		}
		FileWriter out = new FileWriter(filename);
		out.write(thisUsr.getName() + '\n');
		out.write(thisUsr.getID() + '\n');
		out.write(thisUsr.getKeyword() + '\n');
		out.write(thisUsr.getStar() + '\n');
		out.write(thisUsr.getAge() + '\n');
		out.write(thisUsr.getJob() + '\n');
		out.write(thisUsr.getHobby() + '\n');
		out.write(thisUsr.getMail() + '\n');
		out.close();
	}

	public static Usr_Info readUser(String filename) throws IOException {
		Usr_Info tmp;
		String name, id, keyword, star, age, job, hobby, mail;
		FileInputStream file_in = new FileInputStream(filename);
		InputStreamReader file_r = new InputStreamReader(file_in);
		BufferedReader fbuf_r = new BufferedReader(file_r);

		name = fbuf_r.readLine();
		id = fbuf_r.readLine();
		keyword = fbuf_r.readLine();
		star = fbuf_r.readLine();
		age = fbuf_r.readLine();
		job = fbuf_r.readLine();
		hobby = fbuf_r.readLine();
		mail = fbuf_r.readLine();
		fbuf_r.close();

		tmp = new Usr_Info(id, keyword);
		tmp.usr_keyword = new String(keyword);
		tmp.setName(name);
		tmp.setAge(age);
		tmp.setStar(star);
		tmp.setJob(job);
		tmp.setHobby(hobby);
		tmp.setMail(mail);
		return tmp;
	}
}

class Encryption {

	public static String Encrypt(String org_code) {
		String new_code = new String();
		// do something with the code
		for (int i = 0; i < org_code.length(); ++i) {
			new_code += (char) (org_code.charAt(i) + 3);
		}
		return new_code;
	}
}
