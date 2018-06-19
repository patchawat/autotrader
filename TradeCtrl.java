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
	
	private WebDriver driver;
	
	TradeCtrl()
	{
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
		
		driver.navigate().to("http://www.maybank-ke.co.th/");

		driver.findElement(By.xpath("//*[@id=\"user\"]")).clear();
		driver.findElement(By.xpath("//*[@id=\"user\"]")).sendKeys(username);

		driver.findElement(By.xpath("//*[@id=\"txtPassword\"]")).clear();
		driver.findElement(By.xpath("//*[@id=\"txtPassword\"]")).sendKeys(password);

		driver.findElement(By.xpath("//*[@id=\"11650\"]/form/input[4]")).click();
		
		
		
		driver.findElement(By.xpath("//*[@id=\"Table_01\"]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td[3]/table/tbody/tr[2]/td[2]/form/table/tbody/tr/td/p[5]/font/input")).click();
		driver.findElement(By.xpath("//*[@id=\"Table_01\"]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td[3]/table/tbody/tr[2]/td[2]/form/table/tbody/tr/td/p[6]/font/input[2]")).clear();
		driver.findElement(By.xpath("//*[@id=\"Table_01\"]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td[3]/table/tbody/tr[2]/td[2]/form/table/tbody/tr/td/p[7]/input")).click();
		
		
		WebDriverWait wait = new WebDriverWait(driver,10);
		
		//pre streaming1
		driver.switchTo().frame(1);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"cell_Settrade\"]")));
		 
		driver.findElement(By.xpath("//*[@id=\"cell_Settrade\"]")).click();
		
		for(String i :driver.getWindowHandles())
		{
			driver.switchTo().window(i);
			
		}
		
		
		//pre streaming2
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/table[3]/tbody/tr/td[1]/table[2]/tbody/tr[3]/td[2]/a/img")));
		
		driver.findElement(By.xpath("/html/body/table[3]/tbody/tr/td[1]/table[2]/tbody/tr[3]/td[2]/a/img")).click();
		
		for(String i :driver.getWindowHandles())
		{
			driver.switchTo().window(i);
			//System.out.println(i);
			
		}
		
		
		//login to streaming
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")));
		
		driver.findElement(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")).clear();
		driver.findElement(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")).sendKeys(serie);
		
		driver.findElement(By.xpath("//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")).sendKeys(Keys.RETURN);
		
		//dropdown click
		driver.findElement(By.xpath("//*[@id=\"dLabel\"]/span\"]")).click();
		driver.findElement(By.xpath("//*[@id=\"account-dropdown-2\"]/a/span")).click();
		
		
		
		//radio button click
		//*[@id="buy-btn"]
		//*[@id="sell-btn"]
		
		//dropdown click
		//*[@id="order-position-dropdown-0"]
		//*[@id="order-position-dropdown-1"]
		
		//fillbox
		//*[@id="place-order-volume"]/div/volume-input/input
		
		//fillbox
		//*[@id="place-order-pin"]/div/input
		
		//button click
		//*[@id="place-order-price"]/div/a/div/i[1]
		//*[@id="place-order-price"]/div/a/div/i[2]
		
		//button click
		//*[@id="place-order-price"]/div/div/div[2]
		
		//button click
		//*[@id="place-order-submit"]
		
		/*button click
		/html/body/modal-layer/div/div/div/form/div[2]/div[1]/button
		*/
		
		//order status text value 
		/*
		/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[15]
		/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[3]/ul/li[15]
		*/
		
		
	}
	public void open(String LS)
	{
		
	}
	public void close()
	{
		try {
			login();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//open,close?
		String pos = driver.findElement(By.xpath("/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[5]")).getText();
		
		//L,S?
		String status = driver.findElement(By.xpath("/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[7]")).getText();
		//match?
		String match = driver.findElement(By.xpath("/html/body/app-controller/div/ul/li[3]/order/div[2]/order-status/div/div/div/ul/normal-derivatives-order-status-row[2]/ul/li[15]")).getText();
		
		
		
		
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
