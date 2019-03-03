package autotrader;


//git checkout -b vol
//git branch 
//git add *.java
//git commit -m "something here"(not 1st time)
//git push origin vol
//git pull origin vol

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.GregorianCalendar;


import java.util.Properties;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.sikuli.script.*;
import org.sikuli.script.Pattern;

import java.util.regex.*;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ChartCtrl 
{
	private final int WAITNUM = 10;
	private final float SIMILARITY_SCORE = (float) 0.9;
	private final int Y = 20;
	private final int X = 20;
	
	private final int MAX_RSI = 70;
	private final int MIN_RSI = 100-MAX_RSI;
	
	private final int MIN_VOL = 5000;
	private final int FEASIBLE_PRICE = 5;
	
	private final int STEP = 7;
	private ArrayList <Data>prc;
	
	private String start_str;
	private String end_str;
	
	
	
	ChartCtrl()
	{
		prc = new ArrayList <Data>();
		
	}
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
	private String getEmailReceiver()
	{
		
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\basic.properties")));
			
		    Configuration config = builder.getConfiguration();
		    String email_receiver = config.getString("email_receiver");
		    return email_receiver;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}
	private String getSerie()
	{
		
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\basic.properties")));
			
		    Configuration config = builder.getConfiguration();
		    String serie = config.getString("serie");
		    return serie;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}
	private String ReadProperty(String filename,String property)
	{
		String res;
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\"+filename)));
		
		    Configuration config = builder.getConfiguration();
		    res= config.getString(property);
		}
		catch(Exception cex)
		{
		    return null;
		} 
		return res;
		
	}
	private Boolean WriteProperty(String filename,String property,String value)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\"+filename)));
		
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey(property))
		    	config.addProperty(property, value);
		    else
		    	config.setProperty(property, value);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    System.out.println(cex.getMessage());
		    return false;
		} 
		catch(Exception cex)
		{
		    return null;
		} 
		return true;
		
	}
	private void resetConf(String filename)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\"+filename)));
			
		    Configuration config = builder.getConfiguration();
		    config.clear();
		    
		    builder.save();
		}
		catch(Exception cex)
		{
			System.out.println(cex.getMessage() );
		} 
	}
	private void updateRSI(String rsi)
	{
		try
		{
			
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
				new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
			    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
		
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("rsi"))
		    	config.addProperty("rsi", rsi);
		    else
		    	config.setProperty("rsi", rsi);
		    builder.save();
		}
		catch(Exception cex)
		{
		    
		} 
	}
	
	private Double getRSI()
	{
		try
		{
		
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
				new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
			    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
		
		    Configuration config = builder.getConfiguration();
		    Double rsi = config.getDouble("rsi");
		    return rsi;		
		}
		catch(Exception cex)
		{
		    return (Double) null;
		} 
	}
	private void updateVol(Integer vol)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
		       
		
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("vol"))
		    	config.addProperty("vol", vol);
		    else
		    	config.setProperty("vol", vol);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
		catch(Exception cex)
		{
			System.out.println(cex.getMessage() );
		} 
	}
	private Integer getVol()
	{
		
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
			
		    Configuration config = builder.getConfiguration();
		    Integer vol = config.getInt("vol");
		    return vol;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	private void updateVol1(Integer vol1)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
		       
		
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("vol1"))
		    	config.addProperty("vol1", vol1);
		    else
		    	config.setProperty("vol1", vol1);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
		catch(Exception cex)
		{
			System.out.println(cex.getMessage() );
		} 
	}
	private Integer getVol1()
	{
		
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
			
		    Configuration config = builder.getConfiguration();
		    Integer vol1 = config.getInt("vol1");
		    return vol1;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	private void updateVol2(Integer vol2)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
		       
		
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("vol2"))
		    	config.addProperty("vol2", vol2);
		    else
		    	config.setProperty("vol2", vol2);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
		catch(Exception cex)
		{
			System.out.println(cex.getMessage() );
		} 
	}
	private Integer getVol2()
	{
		
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
			
		    Configuration config = builder.getConfiguration();
		    Integer vol2 = config.getInt("vol2");
		    return vol2;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	private void updatestate(Integer state)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
		       
		
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("state"))
		    	config.addProperty("state", state);
		    else
		    	config.setProperty("state", state);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
		catch(Exception cex)
		{
			System.out.println(cex.getMessage() );
		} 
	}
	private Integer getstate()
	{
		
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
			
		    Configuration config = builder.getConfiguration();
		    Integer state = config.getInt("state");
		    return state;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	
	private void updateLastDateTime(String lastdatetime)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
		       
		
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("lastdatetime"))
		    	config.addProperty("lastdatetime", lastdatetime);
		    else
		    	config.setProperty("lastdatetime", lastdatetime);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
		catch(Exception cex)
		{
			System.out.println(cex.getMessage() );
		} 
	}
	private String getLastDateTime()
	{
		
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
			
		    Configuration config = builder.getConfiguration();
		    String lastdatetime = config.getString("lastdatetime");
		    return lastdatetime;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	
	
	
	
	
	
	
	public void AnalyseMinuteData()throws IOException
	{
		/*Calendar now = new GregorianCalendar(GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.YEAR),GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.MONTH),
				GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.DAY_OF_MONTH),GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.HOUR_OF_DAY),
				GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.MINUTE));
		*/
		
		//SimpleDateFormat ft = new SimpleDateFormat ("MM-dd HH:mm");
		
		
		
		//String t_str = ft.format(now.getTime());
		
		
		/*------------------------------------------Start collect graph data------------------------------------------------------*/
		Data data = new Data();
		//Data data2 = new Data();
		
		
		Screen s = new Screen();
		Pattern p = new Pattern(new File(".").getCanonicalPath().concat("\\img\\efin.jpg"));
		p.similar(new Float(SIMILARITY_SCORE));
		
		
		
		
		//go to chart	
		Match m1 = null;
		try
		{
			s.wait(p,WAITNUM);
			s.click();
			
			
			
			p.setFilename(new File(".").getCanonicalPath().concat("\\img\\period.jpg"));
			m1 = s.find(p);
			s.click();
			
			p.setFilename(new File(".").getCanonicalPath().concat("\\img\\3min.jpg"));
			s.wait(p,WAITNUM);
			s.click();
			
		}
		catch(Exception e)
		{
			//sendMail("Minute collector fail", "Cannot find period and 5 minute data");
			takeSS();
			sendMailwithAttachment("3min img detect fail", "Here is SS",new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
			/*String fail = ReadProperty("trade_status.properties","fail");
			if(fail == null)
				WriteProperty("trade_status.properties","fail","1");
			int fail_t = Integer.valueOf(fail) + 1;
			WriteProperty("trade_status.properties","fail",String.valueOf(fail_t));
			//Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
			if(fail_t > 5)
			{
				sendMail("Minute collector period fail", "Shutting down");
				Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
			}*/
			Runtime.getRuntime().exec("taskkill /F /IM efinTradePlus.exe");
			Runtime.getRuntime().exec(new File(".").getCanonicalPath().concat("\\login.bat"));
			return;
			
		}
			Location c_loc = new Location(m1.getBottomLeft().x,m1.getBottomLeft().y - Y);
		
		
			try
			{
				
				s.rightClick(c_loc.x,c_loc.y);
				p.setFilename(new File(".").getCanonicalPath().concat("\\img\\viewsource.jpg"));
				s.click(p);
				
				p.setFilename(new File(".").getCanonicalPath().concat("\\img\\notepad.jpg"));
				s.mouseMove(p);
				s.mouseMove(0, Y);
				
				Thread.sleep(200);
				
				s.keyDown(Key.CTRL);
				Thread.sleep(200);
				s.keyDown(KeyEvent.VK_A);
				Thread.sleep(200);
				s.keyUp(KeyEvent.VK_A);
				
				Thread.sleep(200);
				
				s.keyDown(KeyEvent.VK_C);
				Thread.sleep(200);
				s.keyUp(KeyEvent.VK_C);
				Thread.sleep(200);
				s.keyUp(Key.CTRL);
				
				Thread.sleep(200);
				
				s.keyDown(Key.ALT);
				Thread.sleep(200);
				s.keyDown(Key.F4);
				
				Thread.sleep(200);
				
				s.keyUp(Key.ALT);
				Thread.sleep(200);
				s.keyUp(Key.F4);
				
				Thread.sleep(200);
				String str = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				
				String serie = getSerie();
				String match = Regex(str," id=\"huSymbol\" class=\"huField hu_symbol\">"+serie);
				if(match == null)
				{
					p.setFilename(new File(".").getCanonicalPath().concat("\\img\\serie.jpg"));
					Location serie_location = new Location(s.find(p).getBottomRight().x + X,s.find(p).getBottomRight().y);
					s.click(serie_location);
					s.type(serie);
					Thread.sleep(200);
					s.keyDown(Key.ENTER);
					s.keyUp(Key.ENTER);
					Thread.sleep(200);
					p.setFilename(new File(".").getCanonicalPath().concat("\\img\\efin.jpg"));
					
					s.click(p);
					AnalyseMinuteData();
				}
				data.open_price = RegexNumeric(str," id=\"huOpen\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.close_price = RegexNumeric(str," id=\"huClose\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.high_price = RegexNumeric(str," id=\"huHigh\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.low_price = RegexNumeric(str," id=\"huLow\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.rsi = RegexNumeric(str," RSI RSI \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.vol = RegexNumeric(str," id=\"huVolume\" class=\"huField hu_V\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				//data.tsf = RegexNumeric(str," LINFCST = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				
				//data.b_vol = RegexNumeric(str," BUYVOL = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				//data.s_vol = RegexNumeric(str," SELLVOL = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				/*data.cci = RegexNumeric(str," RESULT CCI \\(20\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.trix = RegexNumeric(str," RESULT TRIX \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.mom = RegexNumeric(str," RESULT MOMENTUM \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				
				data.atr = RegexNumeric(str," RESULT TRUE RANGE = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.macd = RegexNumeric(str," MACD \\(12,26,9\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.s_macd = RegexNumeric(str," SIGNAL MACD \\(12,26,9\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.mfi = RegexNumeric(str," RESULT M FL \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.rocr = RegexNumeric(str," PROC PRICE ROC \\(12\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.swing = RegexNumeric(str," RESULT SWING \\(0.5\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.adx = RegexNumeric(str," ADX DIRECTIONAL \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.obv = RegexNumeric(str," RESULT OBV \\(C\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.std = RegexNumeric(str," RESULT STD DEV \\(14,C,2,SMAV\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				
				data.willr = RegexNumeric(str," RESULT WILLIAMS %R \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");*/
				
				//data.DateTime = RegexNumericDate(str,"[0-9][0-9][-][0-9][0-9][ ][0-9][0-9][:][0-9][0-9]"); // 09-21 16:00
				data.DateTime = Regex(str,"\\d*[/]\\d*[/]\\d*\\s*\\d*[:]\\d*[:]\\d*");//<li><span id="huPeriod" class="huField">20/07/2018 15:00:00</span></li>
				//
			}
			catch(Exception e)
			{
				//sendMail("Minute collector fail", "Cannot find HTML attributes");
				//Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
				takeSS();
				sendMailwithAttachment("3 min HTML fail", "Here is SS",new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
				/*String fail = ReadProperty("trade_status.properties","fail");
				if(fail == null)
					WriteProperty("trade_status.properties","fail","1");
				int fail_t = Integer.valueOf(fail) + 1;
				WriteProperty("trade_status.properties","fail",String.valueOf(fail_t));
				if(fail_t > 5)
				{
					sendMail("Minute collector HTML fail", "Shutting down");
					Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
				}*/
				Runtime.getRuntime().exec("taskkill /F /IM efinTradePlus.exe");
				Runtime.getRuntime().exec(new File(".").getCanonicalPath().concat("\\login.bat"));
			}
			
			//analyse(data);
			saveData(data);
		
	}
	
	
	private String RegexNumeric(String str,String patternStr)
	{
	    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(str);
	    if(!matcher.find())
	    {
	    	return null;
	    }
	    String res = str.substring(matcher.start(),matcher.end());
	    patternStr = patternStr.replace("[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*","");
	    res = res.replaceAll(patternStr, "");
	    return res.trim().equalsIgnoreCase("")?null:res.replace(",", "");
	}

	private String Regex(String str,String patternStr)
	{
	    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(str);
	    if(!matcher.find())
	    {
	    	return null;
	    }
	    String res = str.substring(matcher.start(),matcher.end());
	    return res.trim().equalsIgnoreCase("")?null:res.replace(",", "");
	}
	
	
	
	
	public void sendMail(String subj, String msg)
	{
		
		String[] To = {getEmailReceiver()};
		sendFromGMail(getEmail(),getEmailPassword(),To,subj,msg);
	}
	public void sendMailwithAttachment(String subj, String msg, String filename)
	{
		String[] To = {getEmailReceiver()};
		sendFromGMailwithAttachment(getEmail(),getEmailPassword(),To,subj,msg,filename);
	}
	public static void sendFromGMail(String from, String pass, String[] to, String subject, String body) 
    {
    	
        String d_host = "smtp.gmail.com";
        String d_port  = "465";
        Properties props = new Properties();
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", d_port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
        session.setDebug(true);

        MimeMessage msg = new MimeMessage(session);
     
        try 
        {
            msg.setSubject(subject);
            
            msg.setContent(body,"text/plain");
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[0]));
            
            
            
            Transport transport = session.getTransport("smtps");
            transport.connect(d_host, Integer.valueOf(d_port), from, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

        } 
        catch (AddressException e) 
        {
                e.printStackTrace();
        } 
        catch (MessagingException e)
        {
                e.printStackTrace();
        }
    }
	public static void sendFromGMailwithAttachment(String from, String pass, String[] to, String subject, String body,String filename) 
    {
    	
        String d_host = "smtp.gmail.com";
        String d_port  = "465";
        Properties props = new Properties();
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", d_port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
        session.setDebug(true);

        MimeMessage msg = new MimeMessage(session);
     
        BodyPart messageBodyPart = new MimeBodyPart();
        try 
        {
            msg.setSubject(subject);
            
            //msg.setContent(body,"text/plain");
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[0]));
            
            messageBodyPart.setText(body);
            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            
            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            //String filename = "/home/manisha/file.txt";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            
            // Send the complete message parts
            msg.setContent(multipart);
            
            
            Transport transport = session.getTransport("smtps");
            transport.connect(d_host, Integer.valueOf(d_port), from, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

        } 
        catch (AddressException e) 
        {
                e.printStackTrace();
        } 
        catch (MessagingException e)
        {
                e.printStackTrace();
        }
    }
	void analyse(Data _data)
	{
		String stat = updateStatus(_data);
		if(stat.equalsIgnoreCase("L"))
			OpenL("Open L " + _data.DateTime,String.format("current price: %s", _data.close_price),_data.close_price);
		else if(stat.equalsIgnoreCase("S"))
			OpenS("Open S " + _data.DateTime,String.format("current price: %s", _data.close_price),_data.close_price);
		else if(stat.equalsIgnoreCase("CL"))
			CloseAll(_data.DateTime,String.format("current price: %s trade price: %s", _data.close_price,ReadProperty("trade_status.properties","tradedprice")),_data.close_price);
		else if(stat.equalsIgnoreCase("CS"))
			CloseAll(_data.DateTime,String.format("current price: %s trade price: %s", _data.close_price,ReadProperty("trade_status.properties","tradedprice")),_data.close_price);
		
		
	}
	private String updateStatus(Data _data)
	{
		String c_trade = ReadProperty("trade_status.properties","traded");
		
		
		
		Double c_close_price = new BigDecimal(_data.close_price).setScale(2).doubleValue();
		//Double c_open_price = new BigDecimal(_data.open_price).setScale(2).doubleValue();
		//Double c_high_price = new BigDecimal(_data.high_price).setScale(2).doubleValue();
		//Double c_low_price = new BigDecimal(_data.low_price).setScale(2).doubleValue();
		
		Integer c_vol =   new BigDecimal(_data.vol).intValue();
		
		Double c_rsi = new BigDecimal(_data.rsi).setScale(4).doubleValue();
		Double p_rsi = getRSI()==null?50:getRSI();
		
		
		String c_datetime = _data.DateTime;
		String p_datetime = getLastDateTime()==null?c_datetime:getLastDateTime(); 
		
		
		updateLastDateTime(c_datetime);
		
		Integer p_vol = getVol()==null ?0:getVol();
		Integer p_vol1 = getVol1()==null ?0:getVol1();
		Integer p_vol2 = getVol2()==null ?c_vol:getVol2();
		updateVol2(c_vol);
		
		Integer state = getstate()==null?0:getstate();
		
		String lasttime = c_datetime.substring(11);
		final String i_lasttime = "16:50:00";
		//final String i_starttime = "09:45:00";
		
		
		String res = "";
		
		
		//update vol
		Integer vol;
		if(c_rsi > MAX_RSI || c_rsi < MIN_RSI)
		{
			updateRSI(_data.rsi);
			if(c_datetime.equalsIgnoreCase(p_datetime))
			{
				
				updateVol2(c_vol);
				vol =  c_vol + p_vol1 ;
			}
			else
			{
				updateVol2(c_vol);
				if(p_vol2 == 0)
				{
					updateVol1(0);
				}
				else
				{
					updateVol1(p_vol1 + p_vol2);
				}
				vol =  c_vol + p_vol1 + p_vol2;
			}
			
				
			updatestate(0);
			
			
		}
		else
		{
			if(c_datetime.equalsIgnoreCase(p_datetime))
			{
				if(p_vol2 == 0)
				{
					updateVol2(0);
				}
				vol =  p_vol1 + p_vol2;
				
			}
			else
			{
				updateVol2(0);
				if(p_vol2 == 0)
				{
					updateVol1(0);
				}
				else
				{
					updateVol1(p_vol1 + p_vol2);
				}
				vol =  p_vol1 + p_vol2;
				
			}
			p_vol = vol==0?p_vol:vol;
			updateVol(p_vol);
		}
		
		//determine feasible price
		/*String f_price = ReadProperty("trade_status.properties","feasible_price");
		if(c_trade != null && (c_trade.equalsIgnoreCase("L") || c_trade.equalsIgnoreCase("S")))
		{
			double trade_price = new BigDecimal(ReadProperty("trade_status.properties","tradedprice")).setScale(2).doubleValue();
			
			if(c_trade.equalsIgnoreCase("L"))
			{
				if(c_high_price - trade_price >= FEASIBLE_PRICE)
				{
					if(f_price == null || (new BigDecimal(f_price).setScale(2).doubleValue() < c_high_price))
					{
						WriteProperty("trade_status.properties","feasible_price",_data.high_price);
						//sendMail("Feasible price L "+_data.DateTime,_data.high_price);
					}
					
				}
			}
			else if(c_trade.equalsIgnoreCase("S"))
			{
				if(trade_price - c_low_price >= FEASIBLE_PRICE)
				{
					if(f_price == null || (new BigDecimal(f_price).setScale(2).doubleValue() > c_low_price))
					{
						WriteProperty("trade_status.properties","feasible_price",_data.low_price);
						//sendMail("Feasible price S "+_data.DateTime,_data.low_price);
					}
					
				}
			}
			
		}*/
		//close if possible
		if(c_trade != null && c_trade.equalsIgnoreCase("S"))
		{
			double trade_price = new BigDecimal(ReadProperty("trade_status.properties","tradedprice")).setScale(2).doubleValue();
			if(c_close_price - trade_price >= FEASIBLE_PRICE || lasttime.equalsIgnoreCase(i_lasttime))
				return res = "CS";
			else if( trade_price - c_close_price >= 1 && lasttime.equalsIgnoreCase(i_lasttime))
			{
				return res = "CS";
			}
			/*else if(f_price == null || f_price.equalsIgnoreCase(""))
			{
				
			}
			else
			{
				double feasible_price = new BigDecimal(f_price).setScale(2).doubleValue();
				double dif1 = trade_price - c_close_price;
				double dif2 = (trade_price - feasible_price)*2/3;
				if(dif1 < dif2)
				{
					res = "CS";
				}
				
			}*/
		}
		else if(c_trade != null && c_trade.equalsIgnoreCase("L"))
		{
			double trade_price = new BigDecimal(ReadProperty("trade_status.properties","tradedprice")).setScale(2).doubleValue();
			if(trade_price - c_close_price   >= FEASIBLE_PRICE  )
				return res = "CL";
			else if(c_close_price - trade_price >= 1 && lasttime.equalsIgnoreCase(i_lasttime))
			{
				return res = "CL";
			}
			/*else if(f_price == null || f_price.equalsIgnoreCase(""))
			{
				
			}
			else
			{
				double feasible_price = new BigDecimal(f_price).setScale(2).doubleValue();
				double dif1 = c_close_price - trade_price;
				double dif2 = (feasible_price - trade_price)*2/3;
				if(dif1 < dif2 )
				{
					res = "CL";
				}
			}*/
		}
	
		if(!c_datetime.equalsIgnoreCase(p_datetime) && !lasttime.equalsIgnoreCase(i_lasttime) /*&& !lasttime.equalsIgnoreCase(i_starttime)*/)
		{
			if(c_rsi < MAX_RSI && c_rsi > MIN_RSI && p_rsi > MAX_RSI && p_vol2 == 0)
			{
				if(p_vol < MIN_VOL)
				{
					if(c_trade == null || !c_trade.equalsIgnoreCase("S"))
					{
						res = "S";
						
					}
				}
				else
				{
					if(c_rsi < 50 && state == 0)
						updatestate(++state);
					else if(c_rsi > 50 && state > 0)
					{
						if(c_trade == null || !c_trade.equalsIgnoreCase("L"))
						{
							res = "L";
							
						}
						
					}
				}
			}
			else if(c_rsi < MAX_RSI && c_rsi > MIN_RSI && p_rsi < MIN_RSI && p_vol2 == 0)
			{
				if(p_vol < MIN_VOL)
				{
					if(c_trade == null || !c_trade.equalsIgnoreCase("L"))
					{
						res = "L";
						
					}
				}
				else
				{
					if(c_rsi > 50 && state == 0)
						updatestate(++state);
					else if(c_rsi < 50 && state > 0)
					{
						if(c_trade == null || !c_trade.equalsIgnoreCase("S"))
						{
							res = "S";
							
						}
						
					}
				}
			}
		}
		
			
		
			return res;
		
	}
	private void OpenS(String title,String content,String price)
	{
		//create trade session
		
		
		
		//
		String c_trade = ReadProperty("trade_status.properties","traded");
		if(c_trade != null&& c_trade.equalsIgnoreCase("L"))
		{
			double traded_price = new BigDecimal(ReadProperty("trade_status.properties","tradedprice")).setScale(2).doubleValue();
			double c_price = new BigDecimal(price).setScale(2).doubleValue();
			double dif = c_price- traded_price - 1;
			if(dif > 0)
				sendMail("Profit L before " + title, String.format("trade:%s current:%s profit:%f", c_trade,price,dif));
			else if(dif < 0)
				sendMail("Cut L before "  + title, String.format("trade:%s current:%s loss:%f", c_trade,price,dif));
		}
		resetConf("trade_status.properties");
		
		/*temporaly used*/
		WriteProperty("trade_status.properties","traded","S");
		WriteProperty("trade_status.properties","tradedprice",price);
		try 
		{
			takeSS();
			sendMailwithAttachment(title, content, new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		/*temporaly used*/
	}
	private void OpenL(String title,String content,String price)
	{
		//create trade session
		
		
		
		//
		String c_trade = ReadProperty("trade_status.properties","traded");
		if(c_trade != null && c_trade.equalsIgnoreCase("S"))
		{
			double traded_price = new BigDecimal(ReadProperty("trade_status.properties","tradedprice")).setScale(2).doubleValue();
			double c_price = new BigDecimal(price).setScale(2).doubleValue();
			double dif = traded_price - c_price - 1;
			if(dif > 0)
				sendMail("Profit S before "  + title, String.format("trade:%s current:%s profit:%f", c_trade,price,dif));
			else if(dif < 0)
				sendMail("Cut S before " + title, String.format("trade:%s current:%s loss:%f", c_trade,price,dif));
		}
		resetConf("trade_status.properties");
		/*temporaly used*/
		WriteProperty("trade_status.properties","traded","L");
		WriteProperty("trade_status.properties","tradedprice",price);
		try 
		{
			takeSS();
			sendMailwithAttachment(title, content, new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		/*temporaly used*/
	}
	private void CloseAll(String title,String content,String price)
	{
		//create trade session
		
		
		
		//
		String c_trade = ReadProperty("trade_status.properties","traded");
		double c_price = new BigDecimal(price).setScale(2).doubleValue();
		if(c_trade == null)
		{
			
		}
		else if(c_trade.equalsIgnoreCase("L"))
		{
			double traded_price = new BigDecimal(ReadProperty("trade_status.properties","tradedprice")).setScale(2).doubleValue();
			
			double dif = c_price -  traded_price - 1;
			if(dif > 0)
			{
				try 
				{
					takeSS();
					sendMailwithAttachment("Profit L "+title, String.format("trade:%s current:%s profit:%f", c_trade,price,dif), new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
				
			else if(dif < 0)
			{
				try 
				{
					takeSS();
					sendMailwithAttachment("Cut L "+title, String.format("trade:%s current:%s loss:%f", c_trade,price,dif), new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			resetConf("trade_status.properties");
			WriteProperty("trade_status.properties","traded","CL");
		}
		else if(c_trade.equalsIgnoreCase("S"))
		{
			double traded_price = new BigDecimal(ReadProperty("trade_status.properties","tradedprice")).setScale(2).doubleValue();
			double dif = traded_price - c_price - 1;
			if(dif > 0)
			{
				try 
				{
					takeSS();
					sendMailwithAttachment("Profit S "+title, String.format("trade:%s current:%s profit:%f", c_trade,price,dif), new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
				
			else if(dif < 0)
			{
				try 
				{
					takeSS();
					sendMailwithAttachment("Cut S "+title, String.format("trade:%s current:%s loss:%f", c_trade,price,dif), new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			resetConf("trade_status.properties");
			WriteProperty("trade_status.properties","traded","CS");
		}
		
		return;
	}
	private void takeSS()
	{
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		try 
		{
			
			BufferedImage capture = new Robot().createScreenCapture(screenRect);

			ImageIO.write(capture, "jpg", new File(new File(".").getCanonicalPath().concat("\\img\\SS.jpg")));
		} 
		catch (IOException e) 
		{
			
			e.printStackTrace();
		}
		catch (AWTException e) 
		{
			
			e.printStackTrace();
		}
	}
	public void setDatetimeGraphData(String start,String end)
	{
		start_str = start;
		end_str = end;
	}
	public void getGraphData() throws IOException
	{
		/*------------------------------------------Pre condition------------------------------------------------------*/
		
		
		Calendar starttime1 = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Integer.valueOf(start_str.substring(0, 2)),
				Integer.valueOf(start_str.substring(3, 5)),Integer.valueOf(start_str.substring(6, 8)),Integer.valueOf(start_str.substring(9)));
		
		Calendar finishtime1 = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Integer.valueOf(end_str.substring(0, 2)),
				Integer.valueOf(end_str.substring(3, 5)),Integer.valueOf(end_str.substring(6, 8)),Integer.valueOf(end_str.substring(9)));
		
		
	
		
		//if(!((now.getTimeInMillis() >= starttime1.getTimeInMillis() && now.getTimeInMillis() <= finishtime1.getTimeInMillis())||(now.getTimeInMillis() >= starttime2.getTimeInMillis() && now.getTimeInMillis() <= finishtime2.getTimeInMillis())))
			//return;
		
		
		//SimpleDateFormat ft = new SimpleDateFormat ("MM-dd HH:mm");
		
		
		
		//String starttime_str = ft.format(now.getTime());
		
		/*--------------------------------------------------------------------------------------------------------------------------*/
		Data data = null;
		
		Screen s = new Screen();
		Pattern p = new Pattern(new File(".").getCanonicalPath().concat("\\img\\efin.jpg"));
		p.similar(new Float(SIMILARITY_SCORE));
		
		//stream
		try
		{
			s.wait(p,WAITNUM);
			s.click();
			
			p.setFilename(new File(".").getCanonicalPath().concat("\\img\\draw.jpg"));
			s.click(p);
			
			
			p.setFilename(new File(".").getCanonicalPath().concat("\\img\\crosshair.jpg"));
			s.click(p);
		}
		catch(Exception e)
		{
			LogData(e.getMessage());
			return;
			
		}
			//go to chart	
			Match m1 = null;
			Match m2 = null;
		try
		{
			p.setFilename(new File(".").getCanonicalPath().concat("\\img\\period.jpg"));
			m1 = s.find(p);
			
			p.setFilename(new File(".").getCanonicalPath().concat("\\img\\limitleft.jpg"));
			m2 = s.find(p);
		}
		catch(Exception e)
		{
			LogData(e.getMessage());
			return;
			
		}
			Location c_loc = new Location(m1.getBottomLeft().x,m2.getTopLeft().y - Y);
			Location right_limit = new Location(m1.getBottomLeft().x,m2.getTopLeft().y - Y);
			Location left_limit = new Location(m2.getBottomLeft().x,m2.getTopLeft().y - Y);
		
		
		
		while(true)
		{
			//System.out.println(String.format("Before process c_loc %d %d", c_loc.x, c_loc.y));
			//System.out.println(String.format("right_limit %d %d", right_limit.x, right_limit.y));
			//System.out.println(String.format("left_limit %d %d", left_limit.x, left_limit.y));
			try
			{
				if(c_loc.x < left_limit.x )
				{
					c_loc.setLocation(right_limit.x,right_limit.y);
					s.mouseDown(Button.LEFT);
					s.mouseMove(c_loc);
					s.mouseUp(Button.LEFT);
				}
				s.rightClick(c_loc.x,c_loc.y);
				
				//System.out.println("Mouse click "+c_loc.x+" "+c_loc.y);
				p.setFilename(new File(".").getCanonicalPath().concat("\\img\\viewsource.jpg"));
				
				s.click(p);
				
				
				p.setFilename(new File(".").getCanonicalPath().concat("\\img\\notepad.jpg"));
				s.mouseMove(p);
				s.mouseMove(0, Y);
				s.keyDown(Key.CTRL);
				s.keyDown(KeyEvent.VK_A);
				s.keyUp(KeyEvent.VK_A);
				s.keyDown(KeyEvent.VK_C);
				s.keyUp(KeyEvent.VK_C);
				s.keyUp(Key.CTRL);
				
				s.keyDown(Key.ALT);
				s.keyDown(Key.F4);
				s.keyUp(Key.ALT);
				s.keyUp(Key.F4);
				
				Thread.sleep(500);
				String str = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				
				
				
				data = new Data();
				
				data.open_price = RegexNumeric(str," id=\"huOpen\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.close_price = RegexNumeric(str," id=\"huClose\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.high_price = RegexNumeric(str," id=\"huHigh\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.low_price = RegexNumeric(str," id=\"huLow\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.rsi = RegexNumeric(str," RSI RSI \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.vol = RegexNumeric(str," id=\"huVolume\" class=\"huField hu_V\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				//data.tsf = RegexNumeric(str," LINFCST = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				
				//data.b_vol = RegexNumeric(str," BUYVOL = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				//data.s_vol = RegexNumeric(str," SELLVOL = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				/*data.cci = RegexNumeric(str," RESULT CCI \\(20\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.trix = RegexNumeric(str," RESULT TRIX \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.mom = RegexNumeric(str," RESULT MOMENTUM \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				
				data.atr = RegexNumeric(str," RESULT TRUE RANGE = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.macd = RegexNumeric(str," MACD \\(12,26,9\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.s_macd = RegexNumeric(str," SIGNAL MACD \\(12,26,9\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.mfi = RegexNumeric(str," RESULT M FL \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.rocr = RegexNumeric(str," PROC PRICE ROC \\(12\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.swing = RegexNumeric(str," RESULT SWING \\(0.5\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.adx = RegexNumeric(str," ADX DIRECTIONAL \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.obv = RegexNumeric(str," RESULT OBV \\(C\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.std = RegexNumeric(str," RESULT STD DEV \\(14,C,2,SMAV\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				if(data.std.trim().equalsIgnoreCase("N"))
				{
					System.out.println("std error");
					c_loc = c_loc.setLocation(c_loc.x - STEP, c_loc.y);
				    Mouse.move(c_loc);
				    continue;
				}
				
				data.willr = RegexNumeric(str," RESULT WILLIAMS %R \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");*/
				
				data.DateTime = Regex(str,"\\d*[/]\\d*[/]\\d*\\s*\\d*[:]\\d*[:]\\d*");
			}
			catch(Exception e)
			{
				System.out.println("Data error");
				LogData(e.getMessage());
			}
		    try
		    {
			    /*if(prc.size() > 0)
			    {
			    	Data c_data = prc.get(prc.size()-1);
				    
				    if(!c_data.DateTime.trim().equalsIgnoreCase(data.DateTime))
				    	prc.add(data);
			    }
			    else if(lastData == null || !lastData.DateTime.trim().equalsIgnoreCase(data.DateTime))
			    {
			    	prc.add(data);
			    }
			    
			    //LogData("finish graph");
			    
			    if(lastData != null && (lastData.DateTime.trim().equalsIgnoreCase(data.DateTime)))
			    	break;*/
		    	Calendar c_time = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Integer.valueOf(data.DateTime.substring(3, 5)),
						Integer.valueOf(data.DateTime.substring(0, 2)),Integer.valueOf(data.DateTime.substring(11, 13)),Integer.valueOf(data.DateTime.substring(14,16)));
		    	
		    	
		    	//System.out.println(String.format("starttime1 %d finishtime1 %d c_time %d", starttime1.getTimeInMillis(), finishtime1.getTimeInMillis(),c_time.getTimeInMillis()));
		    	if(c_time.getTimeInMillis() >= starttime1.getTimeInMillis() && c_time.getTimeInMillis() <= finishtime1.getTimeInMillis() && (prc.size()==0 || !prc.get(prc.size()-1).DateTime.trim().equalsIgnoreCase(data.DateTime) ))
		    	{
		    		
		    		prc.add(data);
		    		//saveData(data);
		    		c_loc = c_loc.setLocation(c_loc.x - STEP, c_loc.y);
		    		//s.mouseMove(c_loc);
				    Mouse.move(c_loc);
				    //System.out.println("In process move "+c_loc.x+" "+c_loc.y);
				    continue;
		    	}
		    	else if(c_time.getTimeInMillis() < starttime1.getTimeInMillis())
		    	{
		    		LogData("current time:"+c_time.getTime().toString()+" start time:"+starttime1.getTime().toString());
		    		break;
		    	}
		    	
		    	
	    		c_loc = c_loc.setLocation(c_loc.x - STEP, c_loc.y);
	    		//s.mouseMove(c_loc);
			    Mouse.move(c_loc);
			    
		    }
		    catch(Exception e)
			{
		    	System.out.println(e.getMessage());
				LogData(e.getMessage());
				
			}
		}
	    
		
		saveData();
			
		
		
			
	}
	
	private void LogData(String str)
	{
	    try
	    {
	    	PrintWriter pw = new PrintWriter(new FileOutputStream(new File(new File(".").getCanonicalPath().concat("\\GetminuteLog.txt")),true )); 
		    pw.write(LocalDateTime.now().toString() + str + "\r\n");
		    pw.close();
	    }
	    catch(Exception e)
	    {
	    	System.out.println("save fail");
	    }
	}
	private void saveData()
	{
		Collections.reverse(prc);
		prc.remove(prc.size()-1);
		try
		{
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File(new File(".").getCanonicalPath().concat("\\feature.csv")), true )); 
			for(int i=0;i<prc.size();++i)
			{
				Data d = prc.get(i);
				pw.write(String.format("%s,%s,%s,%s,%s,%s,%s\r\n", 
						d.high_price,d.low_price,d.open_price,d.close_price,d.rsi,d.vol/*,d.adx,d.atr,d.cci,d.macd,d.mfi,d.mom,
						d.obv,d.rocr,d.s_macd,d.std,d.swing,d.trix,d.tsf,d.willr*/,d.DateTime));
			}
			pw.close();
		}
		catch(Exception e)
	    {
	    	System.out.println("save fail");
	    	LogData(e.getMessage());
	    }
	    
	}
	private void saveData(Data d)
	{
		try
		{
			String sCurrentLine;

			BufferedReader br = new BufferedReader(new FileReader(new File(".").getCanonicalPath().concat("\\feature.csv")));
			String lastLine = "";

		    while ((sCurrentLine = br.readLine()) != null) 
		    {
		        System.out.println(sCurrentLine);
		        lastLine = sCurrentLine;
		    }
		    String date = lastLine.substring(lastLine.lastIndexOf(',')+1);
		    
		    if(d.DateTime.equalsIgnoreCase(date))
		    {
		    
			    RandomAccessFile f = new RandomAccessFile(new File(".").getCanonicalPath().concat("\\feature.csv"), "rw");
			    long length = f.length() - 1;
			    byte b;
			    do
			    {                     
			      length -= 1;
			      f.seek(length);
			      b = f.readByte();
			    } while(b != 10);
			    
			    f.setLength(length+1);
			    f.close();
		    }
			    
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File(new File(".").getCanonicalPath().concat("\\feature.csv")), true )); 
		    
			
			
			pw.write(String.format("%s,%s,%s,%s,%s,%s,%s\r\n", 
					d.high_price,d.low_price,d.open_price,d.close_price,d.rsi,d.vol/*,d.adx,d.atr,d.cci,d.macd,d.mfi,d.mom,
					d.obv,d.rocr,d.s_macd,d.std,d.swing,d.trix,d.tsf,d.willr*/,d.DateTime));
			
			pw.close();
		}
		catch(Exception e)
	    {
	    	System.out.println("save fail");
	    	LogData(e.getMessage());
	    }
	    
	}
	private String RegexNumericDate(String str,String patternDate)
	{
	    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patternDate);
	    Matcher matcher = pattern.matcher(str);
	    if(!matcher.find())
	    {
	    	return "N";
	    }
	    String res = str.substring(matcher.start(),matcher.end());
	    return res.trim().equalsIgnoreCase("")?"N":res;
	}
	public void Test()
	{
		//case1
		Data data = new Data();
		data.high_price = "1000.00";
		data.low_price = "1000.00";
		data.open_price = "1000.00";
		data.close_price = "1000.00";
		data.rsi = "21.00";
		data.DateTime = "21/09/2018 16:20:00";
		data.vol = "30000";
		analyse(data);
		
		
		//case2
		//System.out.println(getMedian());
	}
	
}
