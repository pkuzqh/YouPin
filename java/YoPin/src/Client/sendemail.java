/*
 * 给指定邮箱地址发送邮件
 */
package Client;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class sendemail {
	private static final String account = "";
	private static final String password = "";

	public sendemail(String fromemail, String tomail, String body) {
		try {
			Properties props = new Properties();
			props.put("mail.trasport.protocol", "smtp");
			props.put("mail.smtp.host", "smtp.pku.edu.cn");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smto.port", "25"); // 邮件发送的端口为25
			Session session = Session.getInstance(props);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromemail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(tomail));
			message.setText(body);
			message.setSentDate(new Date());
			// message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(account, password); // 发送邮件的账号和密码
			transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有已设好的收件人地址
			transport.close();
		} catch (Exception e) {
			System.out.print(e);
		}
	}
}
