import pandas as pd


data_path = "feature.csv"
img_path = "img\\regression.png"
trade_status_path = "trade_status.ini"
basic_conf_path = "conf\\basic.ini"


rsi_max = 70
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
	except:
		plt.close()
	
	
def L(df):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		if(config['trade']['position'] != "L"):
			config['trade'] = {'position': 'L','trade_price': df['price'][len(df)-1]}
			with open(trade_status_path, 'w') as configfile:
				config.write(configfile)
			config.read(basic_conf_path)
			from_usr = config['email']['email_from']
			from_usr_pass = config['email']['email_password']
			to_usr = config['email']['email_to']
			send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"L",str(df['price'][len(df)-1]))
	except:
		config['trade'] = {'position': 'L','trade_price': df['price'][len(df)-1]}
		with open(trade_status_path, 'w') as configfile:
			config.write(configfile)
		config.read(basic_conf_path)
		from_usr = config['email']['email_from']
		from_usr_pass = config['email']['email_password']
		to_usr = config['email']['email_to']
		send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"L",str(df['price'][len(df)-1]))
		
def S(df):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		if(config['trade']['position'] != "S"):
			config['trade'] = {'position': 'S','trade_price': df['price'][len(df)-1]}
			with open(trade_status_path, 'w') as configfile:
				config.write(configfile)
			config.read(basic_conf_path)
			from_usr = config['email']['email_from']
			from_usr_pass = config['email']['email_password']
			to_usr = config['email']['email_to']
			send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"S",str(df['price'][len(df)-1]))
	except:
		config['trade'] = {'position': 'S','trade_price': df['price'][len(df)-1]}
		with open(trade_status_path, 'w') as configfile:
			config.write(configfile)
		config.read(basic_conf_path)
		from_usr = config['email']['email_from']
		from_usr_pass = config['email']['email_password']
		to_usr = config['email']['email_to']
		send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"S",str(df['price'][len(df)-1]))

		
def close_position(df):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		if(config['trade']['position'] == "S" or config['trade']['position'] == "L" ):
			position = config['trade']['position']
			trade_price = config['trade']['trade_price']
			
			config['trade'] = {'position': '','trade_price': ''}
			with open(trade_status_path, 'w') as configfile:
				config.write(configfile)
			config.read(basic_conf_path)
			from_usr = config['email']['email_from']
			from_usr_pass = config['email']['email_password']
			to_usr = config['email']['email_to']
			send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"close",position+": "+str(trade_price)+" current price: "+str(df['price'][len(df)-1]))
	except:
		config['trade'] = {'position': '','trade_price': ''}
		with open(trade_status_path, 'w') as configfile:
			config.write(configfile)
		config.read(basic_conf_path)
		from_usr = config['email']['email_from']
		from_usr_pass = config['email']['email_password']
		to_usr = config['email']['email_to']
		send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"close","no price and position")	
		
		
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

def keep_last_n_data(df,n=ticks*2):
	try:
		if(len(df) > n*2):
			df = df[len(df)-n:]
			df.to_csv(data_path,index=False)
		return df
	except:
		return df



df = pd.read_csv(data_path)

df = keep_last_n_data(df)

d = {
	'price':df['price'],
	'vol': df['vol'],
	'rsi': df['rsi']
	}


df = pd.DataFrame(data=d)

df30 = df[len(df)-30:]
df60 = df[len(df)-60:]
df120 = df[len(df)-120:]
df240 = df[len(df)-240:]



regression_current_30 ,  x_current_30,y_current_30,x_overbuy_30,y_overbuy_30,x_oversell_30,y_oversell_30 = linear_regression_by_period(df30)
regression_current_60 ,  x_current_60,y_current_60,x_overbuy_60,y_overbuy_60,x_oversell_60,y_oversell_60 = linear_regression_by_period(df60)
regression_current_120 ,  x_current_120,y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120 = linear_regression_by_period(df120)
regression_current_240 ,  x_current_240,y_current_240,x_overbuy_240,y_overbuy_240,x_oversell_240,y_oversell_240 = linear_regression_by_period(df240)


reg_trends = [regression_current_60[-1]-regression_current_60[0],regression_current_120[-1]-regression_current_120[0],regression_current_240[-1]-regression_current_240[0]]
up_reg = [x for x in reg_trends if x > 0]

#trade logic

	
if len(up_reg) >1 and df['rsi'][len(df)-2] <  50 and df['rsi'][len(df)-1] >  50 and get_trade_position()!= "L" :
	plot_period(regression_current_30 ,  x_current_30, '30 min regression', '#002222')
	plot_period(regression_current_60 ,  x_current_60, '60 min regression', '#222200')
	plot_period(regression_current_120 ,  x_current_120 , '120 min regression', '#220022',y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120)
	plot_period(regression_current_240 ,  x_current_240 , '240 min regression', '#770077',y_current_240,x_overbuy_240,y_overbuy_240,x_oversell_240,y_oversell_240)
	save_img()
	L(df)

elif len(up_reg) <= 1 and df['rsi'][len(df)-2] >  50 and df['rsi'][len(df)-1] <  50 and get_trade_position()!= "S":
	plot_period(regression_current_30 ,  x_current_30, '30 min regression', '#002222')
	plot_period(regression_current_60 ,  x_current_60, '60 min regression', '#222200')
	plot_period(regression_current_120 ,  x_current_120 , '120 min regression', '#220022',y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120)
	plot_period(regression_current_240 ,  x_current_240 , '240 min regression', '#770077',y_current_240,x_overbuy_240,y_overbuy_240,x_oversell_240,y_oversell_240)
	save_img()
	S(df)

elif get_trade_position()== "L" and regression_current_30[-1] - regression_current_30[0] < 0:
	plot_period(regression_current_30 ,  x_current_30, '30 min regression', '#002222')
	plot_period(regression_current_60 ,  x_current_60, '60 min regression', '#222200')
	plot_period(regression_current_120 ,  x_current_120 , '120 min regression', '#220022',y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120)
	plot_period(regression_current_240 ,  x_current_240 , '240 min regression', '#770077',y_current_240,x_overbuy_240,y_overbuy_240,x_oversell_240,y_oversell_240)
	save_img()
	close_position(df)
	
elif get_trade_position()== "S" and regression_current_30[-1] - regression_current_30[0] > 0:
	plot_period(regression_current_30 ,  x_current_30, '30 min regression', '#002222')
	plot_period(regression_current_60 ,  x_current_60, '60 min regression', '#222200')
	plot_period(regression_current_120 ,  x_current_120 , '120 min regression', '#220022',y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120)
	plot_period(regression_current_240 ,  x_current_240 , '240 min regression', '#770077',y_current_240,x_overbuy_240,y_overbuy_240,x_oversell_240,y_oversell_240)
	save_img()
	close_position(df)
	
	

	
	






