package com.uf.stock.sniffer.alarm.email;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class MailSender {

	public boolean sendTextMail(String mailContent) {

		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setFromAddress("zzzjjss@163.com");
		mailInfo.setToAddress("18588207247@163.com");
		mailInfo.setSubject("subject");
		mailInfo.setContent("context");
		Properties p = new Properties();
		p.put("mail.smtp.host", "smtp.163.com");
		p.put("mail.smtp.port", "25");
		p.put("mail.smtp.auth", "true");
		Session sendMailSession = Session.getDefaultInstance(p, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("zzzjjss@163.com", "zjs521541");
			}
		});
		try {
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mailInfo.getFromAddress());
			mailMessage.setFrom(from);
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			mailMessage.setSubject(mailInfo.getSubject());
			mailMessage.setSentDate(new Date());
			mailMessage.setText(mailContent);
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
