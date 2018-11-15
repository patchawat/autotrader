import pandas as pd


data_path = "feature.csv"
img_path = "img\\regression.png"
trade_status_path = "trade_status.ini"
basic_conf_path = "conf\\basic.ini"


rsi_max = 60
rsi_max_c = rsi_max - 10
rsi_min = 100 - rsi_max
rsi_min_c = rsi_min + 10
min_distance = 3
fit_distance = 5


ticks = 500

def send_mail_img(from_usr,from_usr_pass,to_usr,img_filepath=img_path,subject="test",text="regression test"):
	import smtplib,ssl,os
	from email.mime.text import MIMEText
	from email.mime.image import MIMEImage
	from email.mime.multipart import MIMEMultipart
	
	gmail_user = from_usr  
	gmail_password = from_usr_pass
	to = to_usr  
	
	img_data = open(img_filepath, 'rb').read()
	msg = MIMEMultipart()
	msg['Subject'] = subject
	msg['From'] = gmail_user
	msg['To'] = to

	text = MIMEText(text)
	msg.attach(text)
	image = MIMEImage(img_data, name=os.path.basename(img_filepath))
	msg.attach(image)

	s = smtplib.SMTP_SSL('smtp.gmail.com', 465)
	s.ehlo()
	#s.starttls()
	#s.ehlo()
	s.login(gmail_user, gmail_password)
	s.sendmail(gmail_user, to, msg.as_string())
	s.quit()




def linear_regression_by_period(df):
	from sklearn import linear_model

	if len(df) == 0:
		return [],{},{},{},{},{},{}
	
	y_current = df['price']
	

	
	x_current = pd.DataFrame(data={'series':y_current.index})	
	
	y_overbuy = df[df['rsi'] > rsi_max]['price']
	y_oversell = df[df['rsi'] < rsi_min]['price']
	
	x_overbuy = pd.DataFrame(data={'series':y_overbuy.index})
	x_oversell = pd.DataFrame(data={'series':y_oversell.index})

	lm = linear_model.LinearRegression()
	
	
	
	model_current = lm.fit(x_current, y_current)
	regression_current = lm.predict(x_current)
	
	
	return regression_current , x_current,y_current,x_overbuy,y_overbuy,x_oversell,y_oversell
	


def plot_period(regression_current , x_current , name, color,y_current = {},x_overbuy = {},y_overbuy = {},x_oversell = {},y_oversell = {}):
	import matplotlib.pyplot as plt
	
	try:
		plt.scatter(x_current, y_current, color='#9999FF', label='price')
		plt.scatter(x_overbuy, y_overbuy, color='#00FF00', label='overbuy point')
		plt.scatter(x_oversell, y_oversell, color='#FF0000', label='oversell point')
	except:
		{}

	try:
		plt.plot(x_current, regression_current, color=color, lw=2, label=name)
	except:
		return False

	plt.xlabel('series')
	plt.ylabel('price')
	plt.title('Linear Regression')
	plt.legend()

def save_img(save_to = img_path):
	import matplotlib.pyplot as plt
	try:
		plt.savefig(save_to)
		plt.close()
	except:
		plt.close()
	
	
def L(df,trend):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		if(config['trade']['position'] != "L"):
			config['trade'] = {'position': 'L','trade_price': df['price'][len(df)-1],'trend': trend,'expected_price': df['price'][len(df)-1]}
			with open(trade_status_path, 'w') as configfile:
				config.write(configfile)
			config.read(basic_conf_path)
			from_usr = config['email']['email_from']
			from_usr_pass = config['email']['email_password']
			to_usr = config['email']['email_to']
			send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"L "+trend,str(df['price'][len(df)-1]))
	except:
		config['trade'] = {'position': 'L','trade_price': df['price'][len(df)-1],'trend': trend,'expected_price': df['price'][len(df)-1]}
		with open(trade_status_path, 'w') as configfile:
			config.write(configfile)
		config.read(basic_conf_path)
		from_usr = config['email']['email_from']
		from_usr_pass = config['email']['email_password']
		to_usr = config['email']['email_to']
		send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"L "+trend,str(df['price'][len(df)-1]))
		
def S(df,trend):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		if(config['trade']['position'] != "S"):
			config['trade'] = {'position': 'S','trade_price': df['price'][len(df)-1],'trend': trend,'expected_price': df['price'][len(df)-1]}
			with open(trade_status_path, 'w') as configfile:
				config.write(configfile)
			config.read(basic_conf_path)
			from_usr = config['email']['email_from']
			from_usr_pass = config['email']['email_password']
			to_usr = config['email']['email_to']
			send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"S "+trend,str(df['price'][len(df)-1]))
	except:
		config['trade'] = {'position': 'S','trade_price': df['price'][len(df)-1],'trend': trend,'expected_price': df['price'][len(df)-1]}
		with open(trade_status_path, 'w') as configfile:
			config.write(configfile)
		config.read(basic_conf_path)
		from_usr = config['email']['email_from']
		from_usr_pass = config['email']['email_password']
		to_usr = config['email']['email_to']
		send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"S "+trend,str(df['price'][len(df)-1]))

		
def close_position(df):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		if(config['trade']['position'] == "S" or config['trade']['position'] == "L" ):
			position = config['trade']['position']
			trade_price = config['trade']['trade_price']
			
			config['trade'] = {'position': '','trade_price': '','trend': '','expected_price': ''}
			with open(trade_status_path, 'w') as configfile:
				config.write(configfile)
			config.read(basic_conf_path)
			from_usr = config['email']['email_from']
			from_usr_pass = config['email']['email_password']
			to_usr = config['email']['email_to']
			send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"close",position+": "+str(trade_price)+" current price: "+str(df['price'][len(df)-1]))
	except:
		config['trade'] = {'position': '','trade_price': '','trend': '','expected_price': ''}
		with open(trade_status_path, 'w') as configfile:
			config.write(configfile)
		config.read(basic_conf_path)
		from_usr = config['email']['email_from']
		from_usr_pass = config['email']['email_password']
		to_usr = config['email']['email_to']
		send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"close","no price and position")	


def set_expected_price(price):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		if(config['trade']['position'] == "S" or config['trade']['position'] == "L" ):
			position = config['trade']['position']
			trade_price = config['trade']['trade_price']
			trend = config['trade']['trend']
			
			config['trade'] = {'position': position,'trade_price': trade_price,'trend': trend,'expected_price': price}
			with open(trade_status_path, 'w') as configfile:
				config.write(configfile)
			
	except:
		config['trade'] = {'position': '','trade_price': '','trend': '','expected_price': ''}
		with open(trade_status_path, 'w') as configfile:
			config.write(configfile)

			
def get_expected_price():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		return config['trade']['expected_price']
	except:
		return -1
		
def get_trade_position():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		return config['trade']['position']
	except:
		return ""
		
def get_trade_price():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		return float(config['trade']['trade_price'])
	except:
		return -1

def get_trade_trend():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		return float(config['trade']['trend'])
	except:
		return ""
		
def get_profit(df):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		position = get_trade_position()
		profit =  df['price'][len(df)-1] - float(config['trade']['trade_price'])
		if position == "S" : 
			profit = -profit
		return profit
		
	except:
		return 0

def keep_last_n_data(df,n=ticks*2):
	try:
		if(len(df) > n*2):
			df = df[len(df)-n:]
			df.to_csv(data_path,index=False)
		return df
	except:
		return df

def analyse(df):

	i = 165
	while i < len(df):
		df20 = df[i-20:i]
		df41 = df[i-41:i]
		df82 = df[i-82:i]
		df165 = df[i-165:i]

		regression_current_20 ,  x_current_20,y_current_20,x_overbuy_20,y_overbuy_20,x_oversell_20,y_oversell_20 = linear_regression_by_period(df20)
		regression_current_41 ,  x_current_41,y_current_41,x_overbuy_41,y_overbuy_41,x_oversell_41,y_oversell_41 = linear_regression_by_period(df41)
		regression_current_82 ,  x_current_82,y_current_82,x_overbuy_82,y_overbuy_82,x_oversell_82,y_oversell_82 = linear_regression_by_period(df82)
		regression_current_165 ,  x_current_165,y_current_165,x_overbuy_165,y_overbuy_165,x_oversell_165,y_oversell_165 = linear_regression_by_period(df165)
		
		# plot_period(regression_current_20 ,  x_current_20, '20 min regression', '#229999')
		plot_period(regression_current_41 ,  x_current_41, '41 min regression', '#992299',y_current_41,x_overbuy_41,y_overbuy_41,x_oversell_41,y_oversell_41)
		# plot_period(regression_current_82 ,  x_current_82 , '82 min regression', '#999922')
		# plot_period(regression_current_165 ,  x_current_165 , '165 min regression', '#2255AA',y_current_165,x_overbuy_165,y_overbuy_165,x_oversell_165,y_oversell_165)
		save_img("C:\\Users\\USER\\Desktop\\regression result\\"+str(i)+".png")
	
		i = i+1

df = pd.read_csv(data_path)

df = keep_last_n_data(df)

d = {
	'price':df['price'],
	'vol': df['vol'],
	'rsi': df['rsi']
	}


df = pd.DataFrame(data=d)

# df = df[df['vol'] > df['vol'].median()]

# df = df.reset_index(drop=True)



# analyse(df)

df41 = df[len(df)-41:]




regression_current_41 ,  x_current_41,y_current_41,x_overbuy_41,y_overbuy_41,x_oversell_41,y_oversell_41 = linear_regression_by_period(df41)

trend = regression_current_41[-1] - regression_current_41[0]

regression_distance = abs(trend)

min_max_distance = df41['price'].max() - df41['price'].min()


#update expected_price
expected_price = get_expected_price()
trade_price = get_trade_price()
current_price = df['price'][len(df)-1]

if get_trade_position()== "L":
	if current_price - trade_price > 4 and current_price > expected_price:
		expected_price = current_price
		set_expected_price(expected_price)
		
elif get_trade_position()== "S":
	if trade_price - current_price > 4 and (expected_price == -1 or current_price < expected_price):
		expected_price = current_price
		set_expected_price(expected_price)

	

#Close
if get_trade_position()== "L" and current_price - trade_price > 0 and current_price - trade_price  <= (expected_price - trade_price)*3/5:
	plot_period(regression_current_41 ,  x_current_41, '41 min regression', '#992299',y_current_41,x_overbuy_41,y_overbuy_41,x_oversell_41,y_oversell_41)
	save_img()
	close_position(df)
	
elif get_trade_position()== "S"  and trade_price - current_price > 0 and trade_price - current_price <= (trade_price - expected_price)*3/5:
	plot_period(regression_current_41 ,  x_current_41, '41 min regression', '#992299',y_current_41,x_overbuy_41,y_overbuy_41,x_oversell_41,y_oversell_41)
	save_img()
	close_position(df)		


		
#trade logic
#Sideway
if min_max_distance <= 6 and min_max_distance >= 4 and  abs(current_price - df41['price'].max()) <= 0.5  and get_trade_position()!= "S" :
	plot_period(regression_current_41 ,  x_current_41, '41 min regression', '#992299',y_current_41,x_overbuy_41,y_overbuy_41,x_oversell_41,y_oversell_41)
	save_img()
	S(df,'S')
	
elif min_max_distance <= 6 and min_max_distance >= 4 and  abs(current_price - df41['price'].min()) <= 0.5  and get_trade_position()!= "L" :
	plot_period(regression_current_41 ,  x_current_41, '41 min regression', '#992299',y_current_41,x_overbuy_41,y_overbuy_41,x_oversell_41,y_oversell_41)
	save_img()
	L(df,'S')

#Up	
elif trend > 0 and regression_distance > 1 and get_trade_position()!= "L" :
	plot_period(regression_current_41 ,  x_current_41, '41 min regression', '#992299',y_current_41,x_overbuy_41,y_overbuy_41,x_oversell_41,y_oversell_41)
	save_img()
	L(df,'U')
	

#Down
elif trend < 0 and regression_distance > 1 and get_trade_position()!= "S" :
	plot_period(regression_current_41 ,  x_current_41, '41 min regression', '#992299',y_current_41,x_overbuy_41,y_overbuy_41,x_oversell_41,y_oversell_41)
	save_img()
	S(df,'D')


	

	
	

	
	






