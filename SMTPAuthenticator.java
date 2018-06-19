package autotrader;

import java.io.File;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;



public class SMTPAuthenticator extends Authenticator 
{
	//private static final String SMTP_HOST_NAME = "smtp.myserver.com";
	
	private String getEmail()
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\basic.properties")));
		
		    Configuration config = builder.getConfiguration();
		    String email = config.getString("email");
		    return email;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}
	private String getEmailPassword()
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\basic.properties")));
		
		    Configuration config = builder.getConfiguration();
		    String email_password = config.getString("email_password");
		    return email_password;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}
	public PasswordAuthentication getPasswordAuthentication() 
	{
         String username = getEmail();
         String password = getEmailPassword();
         return new PasswordAuthentication(username, password);
    }
}
