package autotrader;

public class Data 
{
	public String 
	low_price,
	high_price,
	open_price,
	close_price,
	rsi,
	vol,
	/*macd,
	s_macd,
	cci,
	willr,
	mfi,
	swing,
	adx,
	trix,
	obv,
	mom,
	rocr,
	tsf,
	atr,
	std,
	b_vol,
	s_vol,*/
	DateTime;
	
	Data()
	{
	}
	public void reset()
	{
		low_price = "";
		high_price = "";
		open_price = "";
		close_price = "";
		rsi= "";
		/*macd= "";
		s_macd= "";
		cci= "";
		willr= "";
		mfi= "";
		swing= "";
		adx= "";
		trix= "";
		obv= "";
		mom= "";
		rocr= "";
		tsf= "";
		atr= "";
		std= "";
		b_vol= "";
		s_vol= "";*/
		DateTime= "";
	}
}


