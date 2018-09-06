package autotrader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
/*import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;*/
import java.util.concurrent.Executors;





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
		//chart.setDatetimeGraphData("09-05 09:45", "09-05 16:54");
		//chart.getGraphData();

		
	}

}
