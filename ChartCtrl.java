package autotrader;




import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import java.util.stream.Collectors;

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
	
	private double MINOR_MAX_RSI = 70;
	private double MINOR_MIN_RSI = 100-MINOR_MAX_RSI;
	private double MAJOR_MAX_RSI = 60;
	private double MAJOR_MIN_RSI = 100-MAJOR_MAX_RSI;
	
	
	
	ChartCtrl()
	{
		
	}
	private String getEmail()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("basic.properties"));
		try
		{
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
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("basic.properties"));
		try
		{
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
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("basic.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    String email_receiver = config.getString("email_receiver");
		    return email_receiver;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}
	
	private String ReadProperty(String filename,String property)
	{
		String res;
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName(filename));
		try
		{
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
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName(filename));
		try
		{
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
		return true;
		
	}
	private void resetConf(String filename)
	{
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName(filename));
		try
		{
		    Configuration config = builder.getConfiguration();
		    config.clear();
		    
		    builder.save();
		}
		catch(Exception cex)
		{
			System.out.println(cex.getMessage() );
		} 
	}
	
	private void updatePriceList(Double[] price)
	{
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("pricelist"))
		    	config.addProperty("pricelist", price);
		    else
		    	config.setProperty("pricelist", price);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
	}
	private Double[] getPriceList()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    //String[] prices = config.getStringArray("pricelist");
		    List<Object> list= config.getList("pricelist").stream().map(w-> Double.valueOf((String) w)).collect(Collectors.toList());
		    Double[] prices = list.toArray(new Double[list.size()]);
		    return prices;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	private void updateRSI(String rsi)
	{
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("rsi"))
		    	config.addProperty("rsi", rsi);
		    else
		    	config.setProperty("rsi", rsi);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
	}
	
	private Double getRSI()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    Double rsi = config.getDouble("rsi");
		    return rsi;		
		}
		catch(Exception cex)
		{
		    return (Double) null;
		} 
	}
	
	private Double[] getFilterPosPrices()
	{
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    List<Object> list = config.getList("pricelist").stream().map(w-> Double.valueOf((String)w)).filter(w -> w > 0).collect(Collectors.toList());
		    Double[] prices = list.toArray(new Double[list.size()]);
		    
		    return prices;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	private Double[] getFilterNegPrices()
	{
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    List<Object> list = config.getList("pricelist").stream().map(w-> Double.valueOf((String)w) ).filter(w -> w < 0).collect(Collectors.toList());
		    Double[] prices = list.toArray(new Double[list.size()]);
		    
		    return prices;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	/*
	private void updateTrendTop(Double top)
	{
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trade_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("top"))
		    	config.addProperty("top", top);
		    else
		    	config.setProperty("top", top);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
	}
	private Double getTrendTop()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trade_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    Double top = config.getDouble("top");
		    return top;		
		}
		catch(Exception cex)
		{
		    return (Double) null;
		} 
	}
	private void updateTrendBottom(Double bottom)
	{
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trade_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("bottom"))
		    	config.addProperty("bottom", bottom);
		    else
		    	config.setProperty("bottom", bottom);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
	}
	private Double getTrendBottom()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trade_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    Double bottom = config.getDouble("bottom");
		    return bottom;		
		}
		catch(Exception cex)
		{
		    return (Double) null;
		} 
	}
	private void removeTrend()
	{
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trade_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    config.clearProperty("top");
		    config.clearProperty("bottom");
		    
		    builder.save();
		}
		catch(Exception cex)
		{
			System.out.println(cex.getMessage() );
		} 
	}
	private Double getTop()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    Double price= config.getList("pricelist").stream().map(w-> Math.abs(Double.valueOf((String) w))).max(Double::compare).get();
		    
		    return price;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	private Double getBottom()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    Double price= config.getList("pricelist").stream().map(w-> Math.abs(Double.valueOf((String) w))).min(Double::compare).get();
		    
		    return price;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	
	private void updateTrend(String trend_status)
	{
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("trend_status"))
		    	config.addProperty("trend_status", trend_status);
		    else
		    	config.setProperty("trend_status", trend_status);
		    builder.save();
		}
		catch(ConfigurationException cex)
		{
		    
		} 
	}
	private String getTrend()
	{
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties()
		        .setFileName("trend_status.properties"));
		try
		{
		    Configuration config = builder.getConfiguration();
		    String trend_status = config.getString("trend_status");
		    return trend_status;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}*/
	public void AnalyseMinuteData()throws IOException
	{
		Calendar now = new GregorianCalendar(GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.YEAR),GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.MONTH),
				GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.DAY_OF_MONTH),GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.HOUR_OF_DAY),
				GregorianCalendar.getInstance(Locale.ENGLISH).get(GregorianCalendar.MINUTE));
		
		
		SimpleDateFormat ft = new SimpleDateFormat ("MM-dd HH:mm");
		
		
		
		String t_str = ft.format(now.getTime());
		
		
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
			String fail = ReadProperty("trade_status.properties","fail");
			if(fail == null)
				WriteProperty("trade_status.properties","fail","1");
			int fail_t = Integer.valueOf(fail) + 1;
			WriteProperty("trade_status.properties","fail",String.valueOf(fail_t));
			//Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
			if(fail_t > 5)
			{
				sendMail("Minute collector period fail", "Shutting down");
				Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
			}
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
				
				
				
				//data.reset();
				
				data.open_price = RegexNumeric(str," id=\"huOpen\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.close_price = RegexNumeric(str," id=\"huClose\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.high_price = RegexNumeric(str," id=\"huHigh\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.low_price = RegexNumeric(str," id=\"huLow\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.vol = RegexNumeric(str," id=\"huVolume\" class=\"huField hu_V\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				
				data.rsi = RegexNumeric(str," RSI RSI \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				
				data.DateTime = t_str;
				
				
			}
			catch(Exception e)
			{
				//sendMail("Minute collector fail", "Cannot find HTML attributes");
				//Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
				takeSS();
				sendMailwithAttachment("3 min HTML fail", "Here is SS",new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
				String fail = ReadProperty("trade_status.properties","fail");
				if(fail == null)
					WriteProperty("trade_status.properties","fail","1");
				int fail_t = Integer.valueOf(fail) + 1;
				WriteProperty("trade_status.properties","fail",String.valueOf(fail_t));
				if(fail_t > 5)
				{
					sendMail("Minute collector HTML fail", "Shutting down");
					Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
				}
				Runtime.getRuntime().exec("taskkill /F /IM efinTradePlus.exe");
				Runtime.getRuntime().exec(new File(".").getCanonicalPath().concat("\\login.bat"));
			}
			
			//240min
			/*
			try
			{
				
				
				p.setFilename(new File(".").getCanonicalPath().concat("\\img\\period.jpg"));
				m1 = s.find(p);
				s.click();
				
				p.setFilename(new File(".").getCanonicalPath().concat("\\img\\240min.jpg"));
				s.wait(p,WAITNUM);
				s.click();
				
			}
			catch(Exception e)
			{
				//sendMail("Minute collector fail", "Cannot find period and 240 minute data");
				takeSS();
				sendMailwithAttachment("240min img detect fail", "Here is SS",new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
				String fail = ReadProperty("trade_status.properties","fail");
				if(fail == null)
					WriteProperty("trade_status.properties","fail","1");
				int fail_t = Integer.valueOf(fail) + 1;
				WriteProperty("trade_status.properties","fail",String.valueOf(fail_t));
				//Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
				if(fail_t > 5)
				{
					sendMail("Minute collector period fail", "Shutting down");
					Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
				}
				return;
				
			}
			
			
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
					
					
					
					//data.reset();
					
					data2.open_price = RegexNumeric(str," id=\"huOpen\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
					data2.close_price = RegexNumeric(str," id=\"huClose\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
					data2.high_price = RegexNumeric(str," id=\"huHigh\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
					data2.low_price = RegexNumeric(str," id=\"huLow\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
					data2.vol = RegexNumeric(str," id=\"huVolume\" class=\"huField hu_V\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
					
					data2.rsi = RegexNumeric(str," RSI RSI \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
					
					data2.DateTime = t_str;
					
					
				}
				catch(Exception e)
				{
					//sendMail("Minute collector fail", "Cannot find HTML attributes");
					//Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
					takeSS();
					sendMailwithAttachment("240min HTML fail", "Here is SS",new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
					String fail = ReadProperty("trade_status.properties","fail");
					if(fail == null)
						WriteProperty("trade_status.properties","fail","1");
					int fail_t = Integer.valueOf(fail) + 1;
					WriteProperty("trade_status.properties","fail",String.valueOf(fail_t));
					if(fail_t > 5)
					{
						sendMail("Minute collector HTML fail", "Shutting down");
						Runtime.getRuntime().exec("shutdown.exe /s /f /t 00");
					}
				}
				*/
			analyse(data/*,data2*/);
		
		
	}
	
	
	private String RegexNumeric(String str,String patternStr)
	{
	    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(str);
	    if(!matcher.find())
	    {
	    	return "N";
	    }
	    String res = str.substring(matcher.start(),matcher.end());
	    patternStr = patternStr.replace("[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*","");
	    res = res.replaceAll(patternStr, "");
	    return res.trim().equalsIgnoreCase("")?"N":res.replace(",", "");
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
	void analyse(Data data_3min/*,Data data_240min*/)
	{
		String stat = updateStatus(data_3min/*,data_240min*/);
		if(stat.equalsIgnoreCase("L"))
			OpenL("Open L " + data_3min.DateTime,String.format("current price: %s", data_3min.close_price),data_3min.close_price);
		else if(stat.equalsIgnoreCase("S"))
			OpenS("Open S " + data_3min.DateTime,String.format("current price: %s", data_3min.close_price),data_3min.close_price);
		else if(stat.equalsIgnoreCase("CL"))
			CloseAll(data_3min.DateTime,String.format("current price: %s trade price: %s", data_3min.close_price,ReadProperty("trade_status.properties","tradedprice")),data_3min.close_price);
		else if(stat.equalsIgnoreCase("CS"))
			CloseAll(data_3min.DateTime,String.format("current price: %s trade price: %s", data_3min.close_price,ReadProperty("trade_status.properties","tradedprice")),data_3min.close_price);
		
		//if(!validate())
			//sendMail("price list invalid",data.DateTime);
	}
	private String updateStatus(Data data_3min/*,Data data_240min*/)
	{
		String c_trade = ReadProperty("trade_status.properties","traded");
		
		Double p_rsi_3min = getRSI();
		updateRSI(data_3min.rsi);
		
		double c_rsi_3min = new BigDecimal(data_3min.rsi).setScale(4).doubleValue();
		//double c_rsi_240min = new BigDecimal(data_240min.rsi).setScale(4).doubleValue();
		//String trend = getTrend();
		Double[] prices = getPriceList();
		double c_high_price_3min = new BigDecimal(data_3min.high_price).setScale(2).doubleValue();
		double c_low_price_3min = new BigDecimal(data_3min.low_price).setScale(2).doubleValue();
		
		String res = "";
		
		
		prices = update3minPrice( c_rsi_3min, c_high_price_3min, c_low_price_3min,prices);
		 
		
		//new open status
		/*if(c_rsi_3min > MINOR_MIN_RSI && p_rsi_3min <= MINOR_MIN_RSI 
		&& Math.abs(prices[prices.length-1]) > Math.abs(prices[prices.length-3]))
		{
			if(c_trade == null || !c_trade.equalsIgnoreCase("L") )
				res= "L";
		}
		else if(c_rsi_3min < MINOR_MAX_RSI && p_rsi_3min >= MINOR_MAX_RSI 
		&& Math.abs(prices[prices.length-1]) < Math.abs(prices[prices.length-3]))
		{
			if(c_trade == null || !c_trade.equalsIgnoreCase("S") )
				res= "S";
		}*/
		Double[] overbuy_peaks = getFilterPosPrices();
		Double[] oversell_peaks = getFilterNegPrices();
		if(c_rsi_3min < MINOR_MAX_RSI && p_rsi_3min >= MINOR_MAX_RSI && overbuy_peaks.length > 1)
		{
			if(overbuy_peaks[overbuy_peaks.length-1]>overbuy_peaks[overbuy_peaks.length-2])
			{
				if(c_trade == null || !c_trade.equalsIgnoreCase("L") )
					res= "L";
			}
			else if(overbuy_peaks[overbuy_peaks.length-1]<overbuy_peaks[overbuy_peaks.length-2])
			{
				if(c_trade == null || !c_trade.equalsIgnoreCase("S") )
					res= "S";
			}
			
		}
		else if(c_rsi_3min > MINOR_MIN_RSI && p_rsi_3min <= MINOR_MIN_RSI && oversell_peaks.length > 1 )
		{
			if(oversell_peaks[oversell_peaks.length-1]<oversell_peaks[oversell_peaks.length-2])
			{
				if(c_trade == null || !c_trade.equalsIgnoreCase("L") )
					res= "L";
			}
			else if(oversell_peaks[oversell_peaks.length-1]>oversell_peaks[oversell_peaks.length-2])
			{
				if(c_trade == null || !c_trade.equalsIgnoreCase("S") )
					res= "S";
			}
		}
		
		/*
		if(c_rsi_240min >= MAJOR_MAX_RSI || c_rsi_240min <= MAJOR_MIN_RSI)//OB or OS
		{
			
			if(c_rsi_3min > MINOR_MIN_RSI && p_rsi_3min <= MINOR_MIN_RSI 
			&& Math.abs(prices[prices.length-1]) > Math.abs(prices[prices.length-3]) && Math.abs(prices[prices.length-2]) > Math.abs(prices[0]))
			{
				if(c_trade == null || !c_trade.equalsIgnoreCase("L") )
					res= "L";
			}
			else if(c_rsi_3min < MINOR_MAX_RSI && p_rsi_3min >= MINOR_MAX_RSI 
			&& Math.abs(prices[prices.length-1]) < Math.abs(prices[prices.length-3]) && Math.abs(prices[prices.length-2]) < Math.abs(prices[0]))
			{
				if(c_trade == null || !c_trade.equalsIgnoreCase("S") )
					res= "S";
			}
			else if(c_rsi_3min < MINOR_MAX_RSI && p_rsi_3min >= MINOR_MAX_RSI)
			{
				if(c_trade != null && c_trade.equalsIgnoreCase("L"))
					res= "CL";
			}
			else if(c_rsi_3min > MINOR_MIN_RSI && p_rsi_3min <= MINOR_MIN_RSI)
			{
				if(c_trade != null && c_trade.equalsIgnoreCase("S"))
					res= "CS";
			}
			
		}
		else
		{
			
			if(c_rsi_3min < MINOR_MAX_RSI && p_rsi_3min >= MINOR_MAX_RSI && Math.abs(prices[prices.length-1]) < Math.abs(prices[prices.length-3])) 
			{
				if(c_trade == null || !c_trade.equalsIgnoreCase("L") )
					res= "L";
			}
			else if(c_rsi_3min > MINOR_MIN_RSI && p_rsi_3min <= MINOR_MIN_RSI && Math.abs(prices[prices.length-1]) > Math.abs(prices[prices.length-3]))
			{	
				if(c_trade == null || !c_trade.equalsIgnoreCase("S") )
					res= "S";
			}
			else if((Math.abs(prices[prices.length-1]) > Math.abs(prices[prices.length-3]))
			||(c_rsi_3min > MINOR_MIN_RSI && p_rsi_3min <= MINOR_MIN_RSI)) 
			{
				if(c_trade != null && c_trade.equalsIgnoreCase("S") )
					res= "CS";
			}
			
			else if( (Math.abs(prices[prices.length-1]) < Math.abs(prices[prices.length-3]))
			||(c_rsi_3min < MINOR_MAX_RSI && p_rsi_3min >= MINOR_MAX_RSI))
			{	
				if(c_trade != null && c_trade.equalsIgnoreCase("L") )
					res= "CL";
			}
		}
		*/
		
			return res;
		
		
		
	}
	private Double[] update3minPrice(Double c_rsi_3min,Double c_high_price_3min,Double c_low_price_3min,Double[] prices)
	{
		if(c_rsi_3min > MINOR_MAX_RSI)
		{
			if((prices[prices.length-1] > 0  && c_high_price_3min > Math.abs(prices[prices.length-1]))
			|| (prices[prices.length-1] == 0.0))
			{
				prices[prices.length-1] = c_high_price_3min;
				updatePriceList(prices);
			}
			else if(prices[prices.length-1] < 0)
			{
				List<Double> tmp= Arrays.asList(prices);
				Collections.rotate(tmp,-1);
				prices = tmp.toArray(new Double[tmp.size()]);
				prices[prices.length-1] = c_high_price_3min;
				updatePriceList(prices);
			}
		}
		else if(c_rsi_3min < MINOR_MIN_RSI)
		{
			if((prices[prices.length-1] < 0  && c_low_price_3min < Math.abs(prices[prices.length-1]))
			||(prices[prices.length-1] == 0.0))
			{
				prices[prices.length-1] = -c_low_price_3min;
				updatePriceList(prices);
			}
			else if(prices[prices.length-1] > 0)
			{
				List<Double> tmp= Arrays.asList(prices);
				Collections.rotate(tmp,-1);
				prices = tmp.toArray(new Double[tmp.size()]);
				prices[prices.length-1] = -c_low_price_3min;
				updatePriceList(prices);
			}
		}
		else if((prices[prices.length-1] > 0 && c_rsi_3min <= MAJOR_MAX_RSI)
		||(prices[prices.length-1] < 0 && c_rsi_3min >= MAJOR_MIN_RSI))
		{
			List<Double> tmp= Arrays.asList(prices);
			Collections.rotate(tmp,-1);
			prices = tmp.toArray(new Double[tmp.size()]);
			prices[prices.length-1] = 0.0;
			updatePriceList(prices);
		}
		return prices;
	}
	private void OpenS(String title,String content,String price)
	{
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
				sendMail("Profit L "+title, String.format("trade:%s current:%s profit:%f", c_trade,price,dif));
			else if(dif < 0)
				sendMail("Cut L "+title, String.format("trade:%s current:%s loss:%f", c_trade,price,dif));
			resetConf("trade_status.properties");
			WriteProperty("trade_status.properties","traded","CL");
		}
		else if(c_trade.equalsIgnoreCase("S"))
		{
			double traded_price = new BigDecimal(ReadProperty("trade_status.properties","tradedprice")).setScale(2).doubleValue();
			double dif = traded_price - c_price - 1;
			if(dif > 0)
				sendMail("Profit S "+title, String.format("trade:%s current:%s profit:%f", c_trade,price,dif));
			else if(dif < 0)
				sendMail("Cut S "+title, String.format("trade:%s current:%s loss:%f", c_trade,price,dif));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (AWTException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Test()
	{
		//case1
		
		 try 
		 {
			sendMailwithAttachment("Test", "Here is testing",new File(".").getCanonicalPath().concat("\\img\\SS.jpg"));
		 } 
		 catch (IOException e)
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//case2
		//System.out.println(getMedian());
	}
	
}
