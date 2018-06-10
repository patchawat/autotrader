package autotrader;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;



public class SMTPAuthenticator extends Authenticator 
{
	private static final String SMTP_HOST_NAME = "smtp.myserver.com";
	private static final String SMTP_AUTH_USER = "patchawat.trader@gmail.com";
	private static final String SMTP_AUTH_PWD  = "He11_Master";
	public PasswordAuthentication getPasswordAuthentication() 
	{
         String username = SMTP_AUTH_USER;
         String password = SMTP_AUTH_PWD;
         return new PasswordAuthentication(username, password);
    }
}
