package autotrader;
import java.io.IOException;
/*import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;*/





public class trader 
{
	
	public static void main(String[] args) throws IOException 
	{
		/*--------------------TFEX 5.30 Hrs = 330 bars, Half day = 165 bars------------------------*/
		/*
		LoginCtrl login = new LoginCtrl();
		Boolean isLoginSuccess = login.login();
		while (!isLoginSuccess)
		{
			isLoginSuccess = login.login();
		}
		*/
		/*
		TradeCtrl.killIns();
		TradeCtrl trade = new TradeCtrl();
		trade.login();
		*/
		
		ChartCtrl chart = new ChartCtrl();
		chart.AnalyseMinuteData();
		//chart.Test();
		
		
		
		
	}

}
