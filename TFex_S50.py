import pandas as pd


data_path = "..\\..\\..\\..\\feature.csv"
img_path = "..\\..\\..\\..\\img\\regression.png"
trade_status_path = "..\\..\\..\\..\\trade_status.ini"
basic_conf_path = "..\\..\\..\\..\\conf\\basic.ini"
rsi_mid = 50

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


def median_vol(df,ticks=-1):
	if ticks < 0:
		return df['vol'].median()
	df = df[len(df)-ticks:]
	return df['vol'].median()

def ticks(day=1,tickperday=65):
	return day*tickperday





def median_linear_regression(df):
	from sklearn import linear_model
	import matplotlib.pyplot as plt
	
	
	y5day = df[len(df)-ticks(2,16):][df['vol'] < median_vol(df,ticks(2,16))]['price']
	y1day = df[len(df)-ticks(1,16):][df['vol'] < median_vol(df,ticks(1,16))]['price']
	
	y5dayh = df[len(df)-ticks(2,16):][df['vol'] >= median_vol(df,ticks(2,16))]['price']

	x5day = pd.DataFrame(data={'series':y5day.index})
	x5dayh = pd.DataFrame(data={'series':y5dayh.index})
	x1day = pd.DataFrame(data={'series':y1day.index})	
	


	lm = linear_model.LinearRegression()
	
	model5day = lm.fit(x5day, y5day)
	median5day = lm.predict(x5day)
	
	
	model1day = lm.fit(x1day, y1day)
	median1day = lm.predict(x1day)
	
	plt.scatter(x5day, y5day, color='cornflowerblue', label='actual price')
	plt.scatter(x5dayh, y5dayh, color='darkorange', label='high vol actual price')
	plt.plot(x5day, median5day, color='navy', lw=2, label='regression x2 ticks')
	plt.plot(x1day, median1day, color='c', lw=2, label='regression x ticks')
	# plt.scatter(X, y, color='darkorange', label='data')
	# plt.plot(X, y_rbf, color='navy', lw=lw, label='RBF model')
	# plt.plot(X, y_lin, color='c', lw=lw, label='Linear model')
	# plt.plot(X, y_poly, color='cornflowerblue', lw=lw, label='Polynomial model')
	plt.xlabel('series')
	plt.ylabel('price')
	plt.title('Linear Regression')
	plt.legend()
	# plt.show()
	plt.savefig(img_path)
	
	return median1day[len(median1day)-1]-median1day[0] > median5day[len(median5day)-1]-median5day[0] 
	
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

if(median_linear_regression(df) == True and df['vol'][len(df)-1] > median_vol(df,ticks(1,16)) and df['rsi'][len(df)-1] < rsi_mid ):
	L(df)

	

elif(median_linear_regression(df) == False and df['vol'][len(df)-1] > median_vol(df,ticks(1,16)) and df['rsi'][len(df)-1] > rsi_mid ):
	S(df)
	





