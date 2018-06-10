package autotrader;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.sikuli.script.Key;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

public class LoginCtrl 
{
	private final int WAITNUM = 1000;
	private final float SIMILARITY_SCORE = (float) 0.9;
	LoginCtrl()
	{
		
	}
	private String getUsername()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("basic.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    String username = config.getString("username");
		    return username;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}
	private String getPassword()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("basic.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    String password = config.getString("password");
		    return password;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}
	public Boolean login() throws IOException
	{
		Runtime.getRuntime().exec("taskkill /F /IM efinTradePlus.exe");
		Screen s = new Screen();
		Pattern p = new Pattern(new File(".").getCanonicalPath().concat("\\img\\efin.jpg"));
		p.similar(new Float(SIMILARITY_SCORE));
		//p.exact();
		
		try
		{
			s.wait(p,WAITNUM);
			s.click();
			
			/*try
			{
				p.setFilename(new File(".").getCanonicalPath().concat("\\img\\username1.jpg"));
				s.wait(p,WAITNUM);
				
			}
			catch(Exception e)
			{
				p.setFilename(new File(".").getCanonicalPath().concat("\\img\\username2.jpg"));
				s.click(p);
				s.type("11773895");
				
			}*/
			String password = getPassword();
			p.setFilename(new File(".").getCanonicalPath().concat("\\img\\password.jpg"));
			s.click(p);
			s.type(password);
			
			
			/*p.setFilename(new File(".").getCanonicalPath().concat("\\img\\sup_tfex.jpg"));
		    s.click(p);*/
		    
		    p.setFilename(new File(".").getCanonicalPath().concat("\\img\\ok.jpg"));
		    s.click(p);
		    
		    Thread.sleep(WAITNUM);
		    try
		    {
		    	 p.setFilename(new File(".").getCanonicalPath().concat("\\img\\graph.jpg"));
				 s.wait(p,WAITNUM);
				 s.click();
		    }
		    catch(Exception e)
			{
				System.out.println("Cannot find graph icon");
				
			}
		    
		    /*try
		    {
		    	p.setFilename(new File(".").getCanonicalPath().concat("\\img\\last_update.jpg"));
				s.wait(p,WAITNUM);
			    s.click();
		    }
		    catch(Exception e)
			{
				System.out.println("The price is last up to date");
				
			}*/
		    
			
			
		}
		catch(Exception e)
		{
			return false;
			
		}
		
		return true;
	}
}
