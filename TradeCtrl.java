package autotrader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;




public class TradeCtrl  
{

	private String getUsername()
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\basic.properties")));
		
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
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\basic.properties")));
		
		    Configuration config = builder.getConfiguration();
		    String password = config.getString("password");
		    return password;		
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
	private String getPin()
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\basic.properties")));
		
		    Configuration config = builder.getConfiguration();
		    String pin = config.getString("pin");
		    return pin;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}
	private String getlimited_volume()
	{
		try
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
		    .configure(params.properties().setPath(new File(".").getCanonicalPath().concat("\\conf\\basic.properties")));
		
		    Configuration config = builder.getConfiguration();
		    String pin = config.getString("limited_volume");
		    return pin;		
		}
		catch(Exception cex)
		{
		    return (String) null;
		} 
	}
	
	private WebDriver driver;
	
	TradeCtrl()
	{
		try 
		{
			killIns();
		} catch (IOException e) 
		{
			
			e.printStackTrace();
		}
		driver = new ChromeDriver();
	}

	public static void killIns() throws IOException
	{
		Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
	}
	
	public void login() throws IOException
	{
		
		System.setProperty("webdriver.chrome.driver", new File(".").getCanonicalPath().concat("\\chromedriver.exe"));
		
		String username = getUsername();
		String password = getPassword();
		String serie = getSerie();
		
		driver.navigate().to("https://streaming.settrade.com/realtime/streaming-login/login.jsp?noPopUp=true");

		
		driver.findElement(By.xpath("//*[@id=\"txtLoginBrokerId\"]/ng-select/div/div/div[2]/input")).clear();
		driver.findElement(By.xpath("//*[@id=\"txtLoginBrokerId\"]/ng-select/div/div/div[2]/input")).sendKeys("MAYBANK KIMENG");
		driver.findElement(By.xpath("//*[@id=\"txtLoginBrokerId\"]/ng-select/div/div/div[2]/input")).sendKeys(Keys.RETURN);
		
		
		driver.findElement(By.xpath("//*[@id=\"txtLogin\"]")).clear();
		driver.findElement(By.xpath("//*[@id=\"txtLogin\"]")).sendKeys(username);
		
		driver.findElement(By.xpath("//*[@id=\"txtPassword\"]")).clear();
		driver.findElement(By.xpath("//*[@id=\"txtPassword\"]")).sendKeys(password);
		
		
		driver.findElement(By.xpath("//*[@id=\"submitBtn\"]")).click();
		
		/*---------------------------------------*/
		
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"open-streaming-btn\"]")));
		
		driver.findElement(By.xpath("//*[@id=\"open-streaming-btn\"]")).click();
		
		
		/*-------------------------------------*/
		
		for(String i :driver.getWindowHandles())
		{
			driver.switchTo().window(i);
			
		}
		
		
		
		
		//login to streaming
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")));
		
		driver.findElement(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")).clear();
		driver.findElement(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")).sendKeys(serie);
		driver.findElement(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")).sendKeys(Keys.RETURN);
		
		driver.findElement(By.xpath("//*[@id=\"menu\"]/menu-renderer/div[3]")).click();
		
		
		
		
	}
	public void open(String LS)
	{
		try 
		{
			login();
			close();
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		//vol
		driver.findElement(By.xpath("//*[@id=\"place-order-volume\"]/div/volume-input/input")).sendKeys(getlimited_volume());
		
		driver.findElement(By.xpath("//*[@id=\"place-order-pin\"]/div/input")).sendKeys(getPin());
		
		//S,L?
		if(LS.equalsIgnoreCase("L"))
		{
			driver.findElement(By.xpath("//*[@id=\"buy-btn\"]")).click();
		}
		else if(LS.equalsIgnoreCase("S"))
		{
			driver.findElement(By.xpath("//*[@id=\"sell-btn\"]")).click();
		}
		
		//price
		driver.findElement(By.xpath("//*[@id=\"place-order-price\"]/div/a")).click();
		driver.findElement(By.xpath("//*[@id=\"place-order-price\"]/div/div/div[2]")).click();
		
		//submit
		driver.findElement(By.xpath("//*[@id=\"place-order-submit\"]/span")).click();
		
		driver.findElement(By.xpath("/html/body/modal-layer/div/div/div/form/div[2]/div[1]/button")).click();
		
		
		//cancel button
		/*
		/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[18]/a
	    */
	}
	public void close() throws IOException
	{
		try 
		{
			login();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String pos = "",status = "",match = "",aval_vol = "";
		
		try
		{
			//open,close?
			pos = driver.findElement(By.xpath("/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[5]")).getText();
			
			//L,S?
			status = driver.findElement(By.xpath("/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[7]")).getText();
			//match?
			match = driver.findElement(By.xpath("/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[15]")).getText();
			
			//available volumes
			aval_vol = driver.findElement(By.xpath("/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[9]")).getText();
		}
		catch(Exception e)
		{
			
		}
		//System.out.println("pos="+pos+" status="+status+" match="+match);
		
		if(match.contains("Pending"))
		{
			//cancel
			driver.findElement(By.xpath("/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[18]/a")).click();
			driver.findElement(By.xpath("//*[@id=\"cancel-order-pin\"]/div/input")).sendKeys(aval_vol);
			driver.findElement(By.xpath("/html/body/modal-layer/div/div/div/div/div[2]/div[2]/button[1]")).click();
			
			
			return;
		}
		else if(!pos.equalsIgnoreCase("O") || !match.contains("Matched"))
			return;
		
		//close
		driver.findElement(By.xpath("/html/body/app-controller/div/ul/li[3]/order/div[1]/derivative-place-order/div/div/index-dropdown")).click();
		driver.findElement(By.xpath("//*[@id=\"order-position-dropdown-1\"]/a/span")).click();
		
		//vol
		driver.findElement(By.xpath("//*[@id=\"place-order-volume\"]/div/volume-input/input")).sendKeys(aval_vol);
		
		driver.findElement(By.xpath("//*[@id=\"place-order-pin\"]/div/input")).sendKeys(getPin());
		
		//S,L?
		if(status.equalsIgnoreCase("Long"))
		{
			driver.findElement(By.xpath("//*[@id=\"sell-btn\"]")).click();
		}
		else if(status.equalsIgnoreCase("Short"))
		{
			driver.findElement(By.xpath("//*[@id=\"buy-btn\"]")).click();
		}
		
		//price
		driver.findElement(By.xpath("//*[@id=\"place-order-price\"]/div/a")).click();
		driver.findElement(By.xpath("//*[@id=\"place-order-price\"]/div/div/div[2]")).click();
				
		//submit
		driver.findElement(By.xpath("//*[@id=\"place-order-submit\"]/span")).click();
		
		driver.findElement(By.xpath("/html/body/modal-layer/div/div/div/form/div[2]/div[1]/button")).click();
		
		
		
		
		
		
		//cancel button
		/*
		/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[18]/a
	    */
	}
	public void endProcess() throws IOException
	{
		driver.close();
		driver.quit();
		
		killIns();
	}
	public void Test() throws IOException
	{
		System.setProperty("webdriver.chrome.driver", new File(".").getCanonicalPath().concat("\\chromedriver.exe"));
		driver.switchTo().window("CDwindow-(A4CCF040C6704C469CAC7780A3261196)");
		
		String serie = getSerie();
		driver.findElement(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")).clear();
		driver.findElement(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")).sendKeys(serie);
		
		driver.findElement(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")).sendKeys(Keys.RETURN);
		
	}
}
