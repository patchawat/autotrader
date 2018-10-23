import pandas as pd


data_path = "feature.csv"
img_path = "img\\regression.png"
trade_status_path = "trade_status.ini"
basic_conf_path = "conf\\basic.ini"


rsi_max = 70
rsi_max_c = rsi_max - 10
rsi_min = 100 - rsi_max
rsi_min_c = rsi_min + 10


ticks = 83

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


def median_linear_regression(df):
	from sklearn import linear_model
	import matplotlib.pyplot as plt
	
	
	y_previous = df['price'][len(df)-(ticks*2):len(df)-ticks]
	y_current = df['price'][len(df)-ticks:]
	

	x_previous = pd.DataFrame(data={'series':y_previous.index})
	x_current = pd.DataFrame(data={'series':y_current.index})	
	
	y_overbuy = df[len(df)-(ticks*2):][df['rsi'] > rsi_max]['price']
	y_oversell = df[len(df)-(ticks*2):][df['rsi'] < rsi_min]['price']
	
	x_overbuy = pd.DataFrame(data={'series':y_overbuy.index})
	x_oversell = pd.DataFrame(data={'series':y_oversell.index})

	lm = linear_model.LinearRegression()
	
	model_previous = lm.fit(x_previous, y_previous)
	regression_previous = lm.predict(x_previous)
	
	
	model_current = lm.fit(x_current, y_current)
	regression_current = lm.predict(x_current)
	
	plt.scatter(x_previous, y_previous, color='#0000FF', label='history price')
	plt.scatter(x_current, y_current, color='#000055', label='actual price')
	
	plt.scatter(x_overbuy, y_overbuy, color='#00FF00', label='overbuy point')
	plt.scatter(x_oversell, y_oversell, color='#FF0000', label='oversell point')
	
	plt.plot(x_previous, regression_previous, color='#00FFFF', lw=2, label='regression history')
	plt.plot(x_current, regression_current, color='#005555', lw=2, label='regression current')

	plt.xlabel('series')
	plt.ylabel('price')
	plt.title('Linear Regression')
	plt.legend()
	# plt.show()
	plt.savefig(img_path)
	
	return regression_current[len(regression_current)-1]-regression_current[0] , regression_previous[len(regression_previous)-1]-regression_previous[0] 
	
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



df = pd.read_csv(data_path)


d = {
	'price':df['price'],
	'vol': df['vol'],
	'rsi': df['rsi']
	}


df = pd.DataFrame(data=d)

c_reg,h_reg = median_linear_regression(df)
# print(c_reg,h_reg)



if(get_trade_position()== "L" and df['rsi'][len(df)-2] > rsi_max and df['rsi'][len(df)-1] < rsi_max):
	close_position(df)

elif(get_trade_position()== "S" and df['rsi'][len(df)-2] < rsi_min and df['rsi'][len(df)-1] > rsi_min):
	close_position(df)
	

elif(c_reg > 0 and  h_reg > 0 and df['rsi'][len(df)-2] < rsi_min_c and df['rsi'][len(df)-1] > rsi_min_c ):
	L(df)

	

elif(c_reg < 0 and  h_reg < 0 and df['rsi'][len(df)-2] > rsi_max_c and df['rsi'][len(df)-1] < rsi_max_c ):
	S(df)
	
	
elif(((c_reg > 0 and c_reg > abs(h_reg)) or (c_reg < 0 and abs(c_reg) < h_reg)) and df['rsi'][len(df)-2] < rsi_min and df['rsi'][len(df)-1] > rsi_min ):
	L(df)
	
	
elif(((c_reg > 0 and c_reg < abs(h_reg)) or (c_reg < 0 and abs(c_reg) > h_reg)) and df['rsi'][len(df)-2] > rsi_max and df['rsi'][len(df)-1] < rsi_max ):
	S(df)







