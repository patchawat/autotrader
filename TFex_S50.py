import pandas as pd
import datetime



data_path_5min = "feature_5min.csv"
data_path_1min = "feature_1min.csv"
img_path = "img\\sup_res_line.png"
trade_status_path = "trade_status.ini"
trade_status_test_path = "trade_status_test.ini"
basic_conf_path = "conf\\basic.ini"


rsi_max = 70
rsi_max_c = rsi_max - 10
rsi_min = 100 - rsi_max
rsi_min_c = rsi_min + 10
min_distance = 3
fit_distance = 5
global_min_volume = 2500
global_min_volume_test = 500

elem = 200
elem_test = 90

minimum_profit = 6
maximum_loss = 3


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
	
def send_mail(from_usr,from_usr_pass,to_usr,subject="test",text="regression test"):
	import smtplib,ssl,os
	from email.mime.text import MIMEText
	from email.mime.multipart import MIMEMultipart
	
	gmail_user = from_usr  
	gmail_password = from_usr_pass
	to = to_usr  
	
	
	msg = MIMEMultipart()
	msg['Subject'] = subject
	msg['From'] = gmail_user
	msg['To'] = to

	text = MIMEText(text)
	msg.attach(text)

	s = smtplib.SMTP_SSL('smtp.gmail.com', 465)
	s.ehlo()
	#s.starttls()
	#s.ehlo()
	s.login(gmail_user, gmail_password)
	s.sendmail(gmail_user, to, msg.as_string())
	s.quit()
	
	
def send_mail_to_me(subject="test",text="regression test"):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(basic_conf_path)
	from_usr = config['email']['email_from']
	from_usr_pass = config['email']['email_password']
	to_usr = config['email']['email_to']
	send_mail(from_usr,from_usr_pass,to_usr,subject,text)	
	


def L(df,trend,vol):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	
	config['trade'] = {'position': 'L','trade_price': df['close_price'][len(df)-1],'trend': trend,'expected_price': df['close_price'][len(df)-1],'volume':vol}
	with open(trade_status_path, 'w') as configfile:
		config.write(configfile)
	config.read(basic_conf_path)
	from_usr = config['email']['email_from']
	from_usr_pass = config['email']['email_password']
	to_usr = config['email']['email_to']
	send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"L",str(df['close_price'][len(df)-1]))
	
def L_test(df,trend,vol):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_test_path)
	
	config['trade'] = {'position': 'L','trade_price': df['close_price'][len(df)-1],'trend': trend,'expected_price': df['close_price'][len(df)-1],'volume':vol}
	with open(trade_status_test_path, 'w') as configfile:
		config.write(configfile)
	config.read(basic_conf_path)
	from_usr = config['email']['email_from']
	from_usr_pass = config['email']['email_password']
	to_usr = config['email']['email_to']
	print("L",str(df['close_price'][len(df)-1]),"Date:",str(df['DateTime'][len(df)-1]))
		
def S(df,trend,vol):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	
	config['trade'] = {'position': 'S','trade_price': df['close_price'][len(df)-1],'trend': trend,'expected_price': df['close_price'][len(df)-1],'volume':vol}
	with open(trade_status_path, 'w') as configfile:
		config.write(configfile)
	config.read(basic_conf_path)
	from_usr = config['email']['email_from']
	from_usr_pass = config['email']['email_password']
	to_usr = config['email']['email_to']
	send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"S",str(df['close_price'][len(df)-1]))
	
def S_test(df,trend,vol):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_test_path)
	
	config['trade'] = {'position': 'S','trade_price': df['close_price'][len(df)-1],'trend': trend,'expected_price': df['close_price'][len(df)-1],'volume':vol}
	with open(trade_status_test_path, 'w') as configfile:
		config.write(configfile)
	config.read(basic_conf_path)
	from_usr = config['email']['email_from']
	from_usr_pass = config['email']['email_password']
	to_usr = config['email']['email_to']
	print("S",str(df['close_price'][len(df)-1]),"Date:",str(df['DateTime'][len(df)-1]))
	

		
def close_position(df):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
			
	config['trade'] = {'position': '','trade_price': '','trend': '','expected_price': '','volume':''}
	with open(trade_status_path, 'w') as configfile:
		config.write(configfile)
	config.read(basic_conf_path)
	from_usr = config['email']['email_from']
	from_usr_pass = config['email']['email_password']
	to_usr = config['email']['email_to']
	send_mail_img(from_usr,from_usr_pass,to_usr,img_path,"close",str(df['close_price'][len(df)-1]))
	
def close_position_test(df):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_test_path)
			
	config['trade'] = {'position': '','trade_price': '','trend': '','expected_price': '','volume':''}
	with open(trade_status_test_path, 'w') as configfile:
		config.write(configfile)
	config.read(basic_conf_path)
	from_usr = config['email']['email_from']
	from_usr_pass = config['email']['email_password']
	to_usr = config['email']['email_to']
	print("close",str(df['close_price'][len(df)-1]),"Date:",str(df['DateTime'][len(df)-1]))
	


def set_expected_price(price):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		if(config['trade']['position'] == "S" or config['trade']['position'] == "L" ):
			position = config['trade']['position']
			trade_price = config['trade']['trade_price']
			trend = config['trade']['trend']
			vol = config['trade']['volume']
			
			config['trade'] = {'position': position,'trade_price': trade_price,'trend': trend,'expected_price': price,'volume':vol}
			with open(trade_status_path, 'w') as configfile:
				config.write(configfile)
			
	except:
		print("except in set_expected_price")
		position = config['trade']['position']
		trade_price = config['trade']['trade_price']
		trend = config['trade']['trend']
		vol = config['trade']['volume']
		
		config['trade'] = {'position': position,'trade_price': trade_price,'trend': trend,'expected_price': price,'volume':vol}
		with open(trade_status_path, 'w') as configfile:
			config.write(configfile)
			
def set_expected_price_test(price):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_test_path)
	try:
		if(config['trade']['position'] == "S" or config['trade']['position'] == "L" ):
			position = config['trade']['position']
			trade_price = config['trade']['trade_price']
			trend = config['trade']['trend']
			vol = config['trade']['volume']
			
			config['trade'] = {'position': position,'trade_price': trade_price,'trend': trend,'expected_price': price,'volume':vol}
			with open(trade_status_test_path, 'w') as configfile:
				config.write(configfile)
			
	except:
		print("except in set_expected_price")
		position = config['trade']['position']
		trade_price = config['trade']['trade_price']
		trend = config['trade']['trend']
		vol = config['trade']['volume']
		
		config['trade'] = {'position': position,'trade_price': trade_price,'trend': trend,'expected_price': price,'volume':vol}
		with open(trade_status_test_path, 'w') as configfile:
			config.write(configfile)

def set_vol(vol):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		if(config['trade']['position'] == "S" or config['trade']['position'] == "L" ):
			position = config['trade']['position']
			trade_price = config['trade']['trade_price']
			trend = config['trade']['trend']
			expected_price = config['trade']['expected_price']
			
			
			config['trade'] = {'position': position,'trade_price': trade_price,'trend': trend,'expected_price': expected_price,'volume':vol}
			with open(trade_status_path, 'w') as configfile:
				config.write(configfile)
			
	except:
		print("except in set_vol")
		position = config['trade']['position']
		trade_price = config['trade']['trade_price']
		trend = config['trade']['trend']
		expected_price = config['trade']['expected_price']
		
		
		config['trade'] = {'position': position,'trade_price': trade_price,'trend': trend,'expected_price': expected_price,'volume':vol}
		with open(trade_status_path, 'w') as configfile:
			config.write(configfile)
			
def get_expected_price():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		return float(config['trade']['expected_price'])
	except:
		return -1
		
def get_expected_price_test():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_test_path)
	try:
		return float(config['trade']['expected_price'])
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
		
def get_trade_position_test():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_test_path)
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
		
def get_trade_price_test():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_test_path)
	try:
		return float(config['trade']['trade_price'])
	except:
		return -1

def get_trade_trend():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		return str(config['trade']['trend'])
	except:
		return ""
		
def get_profit(df):
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		position = get_trade_position()
		profit =  df['close_price'][len(df)-1] - float(config['trade']['trade_price'])
		if position == "S" : 
			profit = -profit
		return float(profit)
		
	except:
		return 0

def keep_last_n_data(df,data_path,n=ticks*2):
	try:
		if(len(df) > n*2):
			df = df[len(df)-n:]
			df.to_csv(data_path,index=False)
		return df
	except:
		return df

def get_trade_volume():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(trade_status_path)
	try:
		return int(config['trade']['volume'])
	except:
		return -1
		


def get_support_line(df):
	import numpy as np
	from sklearn import linear_model

	   

	if len(df) == 0:

		return []

				   
	
	elem_under_rsi_min = df[df['rsi'] < rsi_min]
	elem_under_rsi_min = pd.DataFrame(data={
									'open_price':elem_under_rsi_min['open_price'],
									'close_price':elem_under_rsi_min['close_price'],
									'high_price':elem_under_rsi_min['high_price'],
									'low_price':elem_under_rsi_min['low_price'],
									'vol':elem_under_rsi_min['vol'],
									'rsi':elem_under_rsi_min['rsi'],
									'group_idx':0,
									'series':elem_under_rsi_min.index
									})

	p_row = 0
	c_row = 0

	group_index = 0

	

	for index, row in elem_under_rsi_min.iterrows():
		
		if p_row == 0:
			p_row = index
			c_row = index
			continue
		
		c_row = index
			
		if c_row - p_row == 1:
			elem_under_rsi_min.at[index, 'group_idx'] = group_index

				   

		else:
			group_index = group_index + 1
			elem_under_rsi_min.at[index, 'group_idx'] = group_index
		
		p_row = index
			
	min_volume = elem_under_rsi_min.groupby(['group_idx'])['vol'].max() /2
	min_volume = np.array(min_volume.to_numpy()).flatten()
	
	elem_under_rsi_min = elem_under_rsi_min.assign(min_vol=lambda x: min_volume[x.group_idx] )
	support_line_group = elem_under_rsi_min[elem_under_rsi_min['vol'] >= elem_under_rsi_min['min_vol']]
	# print(support_line_group)
	
	if len(support_line_group) < 2:
		return support_line_group,[]
	
	X = pd.DataFrame(data={'series':support_line_group['series']})
	Y = support_line_group['rsi']
	vol = support_line_group['vol']
	prices = support_line_group['low_price']
	
	regr = linear_model.LinearRegression()
	regr.fit(X, Y)
	support_line = regr.predict(X)
	return support_line_group,support_line

	

		

def get_resistance_line(df):
	import numpy as np
	from sklearn import linear_model

	   

	if len(df) == 0:

		return []

				   
	
	elem_over_rsi_max = df[df['rsi'] > rsi_max]
	
	
	elem_over_rsi_max = pd.DataFrame(data={
									'open_price':elem_over_rsi_max['open_price'],
									'close_price':elem_over_rsi_max['close_price'],
									'high_price':elem_over_rsi_max['high_price'],
									'low_price':elem_over_rsi_max['low_price'],
									'vol':elem_over_rsi_max['vol'],
									'rsi':elem_over_rsi_max['rsi'],
									'group_idx':0,
									'series':elem_over_rsi_max.index
									})
	

	p_row = 0
	c_row = 0

	group_index = 0

	

	for index, row in elem_over_rsi_max.iterrows():
		
		if p_row == 0:
			p_row = index
			c_row = index
			continue
		
		c_row = index
			
		if c_row - p_row == 1:
			elem_over_rsi_max.at[index, 'group_idx'] = group_index

				   

		else:
			group_index = group_index + 1
			elem_over_rsi_max.at[index, 'group_idx'] = group_index
		
		p_row = index
			
	
	min_volume = elem_over_rsi_max.groupby(['group_idx'])['vol'].max() /2
	min_volume = np.array(min_volume.to_numpy()).flatten()
	
	elem_over_rsi_max = elem_over_rsi_max.assign(min_vol=lambda x: min_volume[x.group_idx] )
	resistance_line_group = elem_over_rsi_max[elem_over_rsi_max['vol'] >= elem_over_rsi_max['min_vol']]
	# print(resistance_line_group)
	
	if len(resistance_line_group) < 2:
		return resistance_line_group,[]
	
	X = pd.DataFrame(data={'series':resistance_line_group['series']})
	Y = resistance_line_group['rsi']
	vol = resistance_line_group['vol']
	prices = resistance_line_group['high_price']
	regr = linear_model.LinearRegression()
	regr.fit(X, Y)
	resistance_line = regr.predict(X)
	return resistance_line_group,resistance_line



def plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = img_path):

	import matplotlib.pyplot as plt 
	from matplotlib.ticker import StrMethodFormatter
	import numpy as np
	
	X1 = pd.DataFrame(data={'series':resistance_line_group['series']})
	X2 = pd.DataFrame(data={'series':support_line_group['series']})
	
	X11 = pd.DataFrame(data={'series':resistance_line_group[resistance_line_group['open_price'] <= resistance_line_group['close_price']]['series']})
	X12 = pd.DataFrame(data={'series':resistance_line_group[resistance_line_group['open_price'] > resistance_line_group['close_price']]['series']})
	Y11 = resistance_line_group[resistance_line_group['open_price'] <= resistance_line_group['close_price']]['rsi']
	Y12 = resistance_line_group[resistance_line_group['open_price'] > resistance_line_group['close_price']]['rsi']
	
	X21 = pd.DataFrame(data={'series':support_line_group[support_line_group['open_price'] >= support_line_group['close_price']]['series']})
	X22 = pd.DataFrame(data={'series':support_line_group[support_line_group['open_price'] < support_line_group['close_price']]['series']})
	Y21 = support_line_group[support_line_group['open_price'] >= support_line_group['close_price']]['rsi']
	Y22 = support_line_group[support_line_group['open_price'] < support_line_group['close_price']]['rsi']
	
	vol11 = resistance_line_group[resistance_line_group['open_price'] <= resistance_line_group['close_price']]['vol']
	vol12 = resistance_line_group[resistance_line_group['open_price'] > resistance_line_group['close_price']]['vol']
	vol21 = support_line_group[support_line_group['open_price'] >= support_line_group['close_price']]['vol']
	vol22 = support_line_group[support_line_group['open_price'] < support_line_group['close_price']]['vol']
	
	prices11 = resistance_line_group[resistance_line_group['open_price'] <= resistance_line_group['close_price']]['high_price']
	prices12 = resistance_line_group[resistance_line_group['open_price'] > resistance_line_group['close_price']]['high_price']
	prices21 = support_line_group[support_line_group['open_price'] >= support_line_group['close_price']]['low_price']
	prices22 = support_line_group[support_line_group['open_price'] < support_line_group['close_price']]['low_price']
	
	
	#prices
	plt.subplot(3, 1, 1)
	if len(resistance_line_group) > 1:
		if len(X11) > 0 :
			plt.scatter(X11, prices11,  color='green',marker = '.')
		if len(X12) > 0 :
			plt.scatter(X12, prices12,  color='red',marker = 'v')
	if len(support_line_group) > 1 :
		if len(X21) > 0 :
			plt.scatter(X21, prices21,  color='red',marker = '.')
		if len(X22) > 0 :
			plt.scatter(X22, prices22,  color='green',marker = '^')
	plt.xlabel('Series')
	plt.ylabel('prices')
	
	#RSI
	plt.subplot(3, 1, 2)
	if len(resistance_line_group) > 1:
		plt.scatter(X11, Y11,  color='green',marker = '.')
		plt.scatter(X12, Y12,  color='red',marker = 'v')
	if len(X1) == len(resistance_line):
		plt.plot(X1, resistance_line, color='cyan', linewidth=1)
	if len(support_line_group) > 1 :
		plt.scatter(X21, Y21,  color='red',marker = '.')
		plt.scatter(X22, Y22,  color='green',marker = '^')
	if len(X2) == len(support_line):
		plt.plot(X2, support_line, color='magenta', linewidth=1)	

	plt.xlabel('Series')
	plt.ylabel('RSI')
	
	#volume
	plt.subplot(3, 1, 3)
	if len(resistance_line_group) > 1:
		plt.bar(np.array(X11.to_numpy()).flatten(), vol11, color='green',width = 1)
		plt.bar(np.array(X12.to_numpy()).flatten(), vol12, color='red',width = 1)
	if len(support_line_group) > 1 :
		plt.bar(np.array(X21.to_numpy()).flatten(), vol21, color='red',width = 1)
		plt.bar(np.array(X22.to_numpy()).flatten(), vol22, color='green',width = 1)
	plt.xlabel('Series')
	plt.ylabel('Volume')


	
	plt.savefig(save_path)
	plt.clf()



	
def test(df):
	
	i = elem_test
	
	while i < len(df):
		
		
		df2 = df[i-elem_test:i]
		

		df2 = df2.reset_index(drop=True)


		resistance_line_group,resistance_line = get_resistance_line(df2)
		support_line_group,support_line = get_support_line(df2)
		
		
		
		c_vol = int(df2['vol'][len(df2)-1])
		c_rsi = float(df2['rsi'][len(df2)-1])
		p_rsi = float(df2['rsi'][len(df2)-2])
		c_price = float(df2['close_price'][len(df2)-1])
		c_open_price = float(df2['open_price'][len(df2)-1])
		c_idx = int(df2.index[len(df2)-1])		

		position = get_trade_position_test()
		

		#update expected_price
		expected_price = -1
		trade_price = -1

		if position != "":
			expected_price = get_expected_price_test()
			trade_price = get_trade_price_test()
			if position== "L":
				if c_price - trade_price > minimum_profit and c_price > expected_price:
					expected_price = c_price
					set_expected_price_test(expected_price)
					
			elif position== "S":
				if trade_price - c_price > minimum_profit and (expected_price == -1 or c_price < expected_price):
					expected_price = c_price
					set_expected_price_test(expected_price)
					




			

		if len(resistance_line) < 2 and len(support_line) < 2:
			i = i+1
			continue
		#Has only support line
		if len(resistance_line) < 2:
			last_index_support = support_line_group.index[-1]
			trend_support_line = support_line[-1] - support_line[0]
			is_up_trend =  trend_support_line > 0
			is_down_trend = trend_support_line < 0

			if (c_rsi > rsi_max and p_rsi < rsi_min) or (c_rsi < rsi_min and p_rsi > rsi_max) or c_idx != last_index_support or (c_vol < global_min_volume_test):
				i = i+1
				continue
			
			if is_up_trend == True and c_open_price < c_price and position != 'L':
				L_test(df2,"U",c_vol)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			elif is_down_trend == True and c_open_price > c_price and position != 'S':
				S_test(df2,"D",c_vol)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			
		#Has only resistance line	
		elif len(support_line) < 2:
			last_index_resistance = resistance_line_group.index[-1]
			trend_resistance_line = resistance_line[-1] - resistance_line[0]
			is_up_trend =  trend_resistance_line > 0
			is_down_trend = trend_resistance_line < 0	
			if (c_rsi > rsi_max and p_rsi < rsi_min) or (c_rsi < rsi_min and p_rsi > rsi_max) or c_idx != last_index_resistance  or (c_vol < global_min_volume_test):
				i = i+1
				continue
			
			if is_up_trend == True and c_open_price < c_price and position != 'L':
				L_test(df2,"U",c_vol)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			elif is_down_trend == True and c_open_price > c_price and position != 'S':
				S_test(df2,"D",c_vol)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
		#Has both lines
		else:
			last_index_resistance = resistance_line_group.index[-1]
			last_index_support = support_line_group.index[-1]
			trend_resistance_line = resistance_line[-1] - resistance_line[0]
			trend_support_line = support_line[-1] - support_line[0]
			is_up_trend = trend_resistance_line > 0 and trend_support_line > 0
			is_down_trend = trend_resistance_line < 0 and trend_support_line < 0

			if (c_rsi > rsi_max and p_rsi < rsi_min) or (c_rsi < rsi_min and p_rsi > rsi_max) or ((c_idx != last_index_resistance) and (c_idx != last_index_support)) or (c_vol < global_min_volume_test):
				i = i+1
				continue
				
			if c_rsi < rsi_min and is_up_trend == True and c_price > c_open_price and position != 'L':
				L_test(df2,"U",c_vol)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			elif c_rsi < rsi_min and c_rsi < support_line[-1] and is_down_trend == False and c_price > c_open_price and position != 'L':
				L_test(df2,"U",c_vol)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			elif  c_rsi > rsi_max and is_down_trend == True and c_price < c_open_price and position != 'S':
				S_test(df2,"D",c_vol)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			elif  c_rsi > rsi_max and c_rsi > resistance_line[-1] and is_up_trend == False and c_price < c_open_price and position != 'S':
				S_test(df2,"D",c_vol)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			elif  (c_rsi > rsi_max or is_down_trend == True) and c_rsi < resistance_line[-1] and is_up_trend == False and c_price < c_open_price and position == 'L':
				close_position_test(df2)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			elif  (c_rsi < rsi_min or is_up_trend == True) and c_rsi > support_line[-1] and is_down_trend == False and c_price > c_open_price and position == 'S':
				close_position_test(df2)
				plot(resistance_line_group,resistance_line,support_line_group,support_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			
			
		# position = get_trade_position_test()
		# expected_price = get_expected_price_test()
		# trade_price = get_trade_price_test()

		# #Close
		# if position != "":
			# if position == "L" and c_price - trade_price > 0 and c_price - trade_price  <= (expected_price - trade_price)*3/5:
				# close_position_test(df2)
				
			# elif position == "L" and trade_price - c_price > maximum_loss:
				# close_position_test(df2)
				
			# elif position == "S"  and trade_price - c_price > 0 and trade_price - c_price <= (trade_price - expected_price)*3/5:
				# close_position_test(df2)
				
			# elif position == "S"  and c_price - trade_price > maximum_loss :
				# close_position_test(df2)
			
		i = i+1
	print("end loop while")
df1min = pd.read_csv(data_path_1min)	
df5min = pd.read_csv(data_path_5min)
test(df1min)

#df = pd.read_csv(data_path)
# df = df.tail(elem)

# df = df.reset_index(drop=True)


# resistance_line_group,resistance_line = get_resistance_line(df)
# support_line_group,support_line = get_support_line(df)




# #df = keep_last_n_data(df)

# # d = {
	# # 'high_price':int(df['high_price']),
	# # 'low_price':df['low_price'],
	# # 'open_price':df['open_price'],
	# # 'close_price':df['close_price'],
	# # 'vol': df['vol'],
	# # 'rsi': df['rsi']
	# # }


# # df = pd.DataFrame(data=d)




# # now = datetime.datetime.now()



# c_vol = int(df['vol'][len(df)-1])
# c_rsi = float(df['rsi'][len(df)-1])
# p_rsi = float(df['rsi'][len(df)-2])
# c_price = float(df['close_price'][len(df)-1])
# c_open_price = float(df['open_price'][len(df)-1])
# c_idx = int(df.index[len(df)-1])

# last_index_resistance = resistance_line_group.index[-1]
# last_index_support = support_line_group.index[-1]
# trend_resistance_line = resistance_line[-1] - resistance_line[0]
# trend_support_line = support_line[-1] - support_line[0]

# # p_vol = int(df['vol'][len(df)-2])
# # p_high_price = float(df['high_price'][len(df)-2])
# # p_low_price = float(df['low_price'][len(df)-2])

# # c_datetime = str(df['DateTime'][len(df)-1])
# # Date = c_datetime[:10]
# # Time = c_datetime[11:]


# position = get_trade_position()

# #update expected_price
# expected_price = -1
# trade_price = -1

# if position != "":
	# expected_price = get_expected_price()
	# trade_price = get_trade_price()
	# if position== "L":
		# if c_price - trade_price > minimum_profit and c_price > expected_price:
			# expected_price = c_price
			# set_expected_price(expected_price)
			
	# elif position== "S":
		# if trade_price - c_price > minimum_profit and (expected_price == -1 or c_price < expected_price):
			# expected_price = c_price
			# set_expected_price(expected_price)
			

# # if c_datetime.find("09:30:00") != -1 or c_datetime.find("14:00:00") != -1:
	# # exit()
# # elif (int(now.hour) == 12 and int(now.minute) >= 25 ) or (int(now.hour) == 16 and int(now.minute) >= 50 ) :
	# # if position == "L" or position == "S":
		# # close_position(df)
	# # exit()

# # if (int(now.hour) == 12 and int(now.minute) >= 25 ) or (int(now.hour) == 16 and int(now.minute) >= 50 ) :
	# # if position == "L" or position == "S":
		# # close_position(df)
	# # exit()

# # start_index = 0
# # end_index = df.index[-1] + 1



# # if(int(now.hour) > 12):
	# # date_time = Date + " 14:00:00"
	# # start_index = df[df['DateTime'].str.contains(date_time)].index[0]
	
# # else:
	# # date_time = Date + " 09:30:00"
	# # start_index = df[df['DateTime'].str.contains(date_time)].index[0]
 
# # df2 = df[start_index:end_index]
 
# # minimum_vol = df2['vol'].max()/2 



# # if c_vol < minimum_vol:
	# # exit()

# if (c_rsi > rsi_max and p_rsi < rsi_min) or (c_rsi < rsi_min and p_rsi > rsi_max) or ((c_idx != last_index_resistance) and (c_idx != last_index_support)):
	# exit()
	
# is_up_trend = trend_resistance_line > 0 and trend_support_line > 0
# is_down_trend = trend_resistance_line < 0 and trend_support_line < 0

# print(c_idx,last_index_resistance,last_index_support,resistance_line[-1],support_line[-1],trend_resistance_line,trend_support_line,is_up_trend)

# if c_rsi < rsi_min and is_up_trend == True and c_price > c_open_price and position != 'L':
	# L(df,"U",c_vol)
	# plot(resistance_line_group,resistance_line,support_line_group,support_line)
	# exit()
	
# elif c_rsi < rsi_min and c_rsi < support_line[-1] and is_down_trend == False and c_price > c_open_price and position != 'L':
	# L(df,"U",c_vol)
	# plot(resistance_line_group,resistance_line,support_line_group,support_line)
	# exit()

# elif  c_rsi > rsi_max and is_down_trend == True and c_price < c_open_price and position != 'S':
	# S(df,"D",c_vol)
	# plot(resistance_line_group,resistance_line,support_line_group,support_line)
	# exit()
	
# elif  c_rsi > rsi_max and c_rsi > resistance_line[-1] and is_up_trend == False and c_price < c_open_price and position != 'S':
	# S(df,"D",c_vol)
	# plot(resistance_line_group,resistance_line,support_line_group,support_line)
	# exit()
	
# elif  c_rsi > rsi_max and c_rsi < resistance_line[-1] and is_up_trend == False and c_price < c_open_price and position == 'L':
	# close_position(df)
	# plot(resistance_line_group,resistance_line,support_line_group,support_line)
	# exit()
	
# elif  c_rsi < rsi_min and c_rsi > support_line[-1] and is_down_trend == False and c_price > c_open_price and position == 'S':
	# close_position(df)
	# plot(resistance_line_group,resistance_line,support_line_group,support_line)
	# exit()

# position = get_trade_position()
# expected_price = get_expected_price()
# trade_price = get_trade_price()

# #Close
# if position != "":
	# if position == "L" and c_price - trade_price > 0 and c_price - trade_price  <= (expected_price - trade_price)*3/5:
		# close_position(df)
	# elif position == "L"  and trade_price - c_price  > maximum_loss :
		# close_position(df)
	# elif position == "S"  and trade_price - c_price > 0 and trade_price - c_price <= (trade_price - expected_price)*3/5:
		# close_position(df)
	# elif position == "S"  and c_price - trade_price > maximum_loss :
		# close_position(df)
	

	
	


	
	






