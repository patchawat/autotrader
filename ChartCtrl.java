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
	private final int X = 20;
	
	
	
	
	ChartCtrl()
	{
		
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
	
	private void updatevol1(Integer vol1)
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
	private void updatevol2(Integer vol2)
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
	private void updateHigh(Double high)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
		       
		
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("high"))
		    	config.addProperty("high", high);
		    else
		    	config.setProperty("high", high);
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
	private Double getHigh()
	{
		
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
			
		    Configuration config = builder.getConfiguration();
		    Double high = config.getDouble("high");
		    return high;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
	private void updateLow(Double low)
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
		       
		
		    Configuration config = builder.getConfiguration();
		    if(!config.containsKey("low"))
		    	config.addProperty("low", low);
		    else
		    	config.setProperty("low", low);
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
	private Double getLow()
	{
		
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\trend_status.properties")));
			
		    Configuration config = builder.getConfiguration();
		    Double low = config.getDouble("low");
		    return low;		
		}
		catch(Exception cex)
		{
		    return null;
		} 
	}
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
			
			p.setFilename(new File(".").getCanonicalPath().concat("\\img\\120min.jpg"));
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
				
				//data.reset();
				
				data.open_price = RegexNumeric(str," id=\"huOpen\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.close_price = RegexNumeric(str," id=\"huClose\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.high_price = RegexNumeric(str," id=\"huHigh\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.low_price = RegexNumeric(str," id=\"huLow\" class=\"huField\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				data.vol = RegexNumeric(str," id=\"huVolume\" class=\"huField hu_V\">[-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				
				data.rsi = RegexNumeric(str," RSI RSI \\(14\\) = [-]*[0-9]*[,]*[0-9]*[.]*[0-9]*");
				//<li><span id="huPeriod" class="huField">20/07/2018 15:00:00</span></li>
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
			
			analyse(data);
		
		
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
	void analyse(Data data_3min)
	{
		String stat = updateStatus(data_3min);
		if(stat.equalsIgnoreCase("L"))
			OpenL("Open L " + data_3min.DateTime,String.format("current price: %s", data_3min.close_price),data_3min.close_price);
		else if(stat.equalsIgnoreCase("S"))
			OpenS("Open S " + data_3min.DateTime,String.format("current price: %s", data_3min.close_price),data_3min.close_price);
		else if(stat.equalsIgnoreCase("CL"))
			CloseAll(data_3min.DateTime,String.format("current price: %s trade price: %s", data_3min.close_price,ReadProperty("trade_status.properties","tradedprice")),data_3min.close_price);
		else if(stat.equalsIgnoreCase("CS"))
			CloseAll(data_3min.DateTime,String.format("current price: %s trade price: %s", data_3min.close_price,ReadProperty("trade_status.properties","tradedprice")),data_3min.close_price);
		
		
	}
	private String updateStatus(Data data_3min)
	{
		String c_trade = ReadProperty("trade_status.properties","traded");
		
		
		
		double c_close_price_3min = new BigDecimal(data_3min.close_price).setScale(2).doubleValue();
		Double c_high_price_3min = new BigDecimal(data_3min.high_price).setScale(2).doubleValue();
		Double c_low_price_3min = new BigDecimal(data_3min.low_price).setScale(2).doubleValue();
		
		Integer c_vol_3min =   new BigDecimal(data_3min.vol).intValue();
		Integer p_vol1_3min = getVol1();
		Integer p_vol2_3min = getVol2();
		
		if(c_vol_3min < p_vol2_3min)
		{
			updateHigh(c_high_price_3min);
			updateLow(c_low_price_3min);
			updatevol1(p_vol2_3min);
			updatevol2(c_vol_3min);
			return "";
		}
		else
		{
			updatevol2(c_vol_3min);
			
		}
				
		if(c_vol_3min < p_vol1_3min)
			return "";
		
		Double p_high_price_3min = getHigh();
		Double p_low_price_3min = getLow();
		
		String res = "";
		
		if(c_close_price_3min > p_high_price_3min)
		{
			if(c_trade == null || !c_trade.equalsIgnoreCase("L") )
				res= "L";
		}
		else if(c_close_price_3min < p_low_price_3min)
		{
			if(c_trade == null || !c_trade.equalsIgnoreCase("S") )
				res= "S";
		}
		
			return res;
		
		
		
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
