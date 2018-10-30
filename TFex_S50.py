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

	plt.plot(x_current, regression_current, color=color, lw=2, label=name)

	plt.xlabel('series')
	plt.ylabel('price')
	plt.title('Linear Regression')
	plt.legend()

def save_img(save_to = img_path):
	import matplotlib.pyplot as plt
	plt.savefig(save_to)
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

regression_current_5 ,  x_current_5,y_current_5,x_overbuy_5,y_overbuy_5,x_oversell_5,y_oversell_5 = linear_regression_by_period(df[len(df)-5:])
regression_current_15 ,  x_current_15,y_current_15,x_overbuy_15,y_overbuy_15,x_oversell_15,y_oversell_15 = linear_regression_by_period(df[len(df)-15:])
regression_current_30 ,  x_current_30,y_current_30,x_overbuy_30,y_overbuy_30,x_oversell_30,y_oversell_30 = linear_regression_by_period(df[len(df)-30:])
regression_current_60 ,  x_current_60,y_current_60,x_overbuy_60,y_overbuy_60,x_oversell_60,y_oversell_60 = linear_regression_by_period(df[len(df)-60:])
regression_current_120 ,  x_current_120,y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120 = linear_regression_by_period(df[len(df)-120:])




idx = [regression_current_5[-1],regression_current_15[-1],regression_current_30[-1],regression_current_60[-1],regression_current_120[-1],df['price'][len(df)-1]]
idx.sort()
pos = idx.index(df['price'][len(df)-1])

distance =  abs(idx[-1] - idx[0])

#trade logic

if distance < min_distance:
	exit()
	
elif pos == 5 and get_trade_position()!= "L" :
	plot_period(regression_current_5 ,  x_current_5 , '5 min regression', '#777700')
	plot_period(regression_current_15 ,  x_current_15, '15 min regression', '#007777')
	plot_period(regression_current_30 ,  x_current_30, '30 min regression', '#770077')
	plot_period(regression_current_60 ,  x_current_60, '60 min regression', '#222200')
	plot_period(regression_current_120 ,  x_current_120 , '120 min regression', '#220022',y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120)

	save_img()
	L(df)

elif pos ==0 and get_trade_position()!= "S":
	plot_period(regression_current_5 ,  x_current_5 , '5 min regression', '#777700')
	plot_period(regression_current_15 ,  x_current_15, '15 min regression', '#007777')
	plot_period(regression_current_30 ,  x_current_30, '30 min regression', '#770077')
	plot_period(regression_current_60 ,  x_current_60, '60 min regression', '#222200')
	plot_period(regression_current_120 ,  x_current_120 , '120 min regression', '#220022',y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120)

	save_img()
	S(df)

elif df['rsi'][len(df)-2] >  rsi_max and df['rsi'][len(df)-1] <  rsi_max and get_trade_position()!= "S":
	plot_period(regression_current_5 ,  x_current_5 , '5 min regression', '#777700')
	plot_period(regression_current_15 ,  x_current_15, '15 min regression', '#007777')
	plot_period(regression_current_30 ,  x_current_30, '30 min regression', '#770077')
	plot_period(regression_current_60 ,  x_current_60, '60 min regression', '#222200')
	plot_period(regression_current_120 ,  x_current_120 , '120 min regression', '#220022',y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120)

	save_img()
	S(df)
	
elif df['rsi'][len(df)-2] <  rsi_min and df['rsi'][len(df)-1] >  rsi_min and get_trade_position()!= "L":
	plot_period(regression_current_5 ,  x_current_5 , '5 min regression', '#777700')
	plot_period(regression_current_15 ,  x_current_15, '15 min regression', '#007777')
	plot_period(regression_current_30 ,  x_current_30, '30 min regression', '#770077')
	plot_period(regression_current_60 ,  x_current_60, '60 min regression', '#222200')
	plot_period(regression_current_120 ,  x_current_120 , '120 min regression', '#220022',y_current_120,x_overbuy_120,y_overbuy_120,x_oversell_120,y_oversell_120)

	save_img()
	L(df)
	
	






