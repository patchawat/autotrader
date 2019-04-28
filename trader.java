package autotrader;
import java.io.File;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
import java.io.IOException;
/*import java.util.concurrent.ExecutorService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.Executors;
*/
//import java.io.RandomAccessFile;




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
		
		ChartCtrl chart = new ChartCtrl();
		chart.AnalyseMinuteData();
		
		//chart.setDatetimeGraphData(args[0], args[1]);
		//chart.getGraphData();

	}

}
