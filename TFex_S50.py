import pandas as pd
import datetime



data_path_5min = "feature_5min.csv"
data_path_1min = "feature_1min.csv"
img_path = "img\\sup_res_line.png"
trade_status_path = "trade_status.ini"
trade_status_test_path = "trade_status_test.ini"
basic_conf_path = "conf\\basic.ini"


rsi_max = 60
rsi_overbuy = 75
rsi_min = 100 - rsi_max
rsi_oversell = 100 - rsi_overbuy

min_distance = 2
fit_distance = 20

global_min_volume = 2500
global_min_volume_test = 2500

elem = 200
elem_test = 330

step_test = 30

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
			
	
	# min_volume = elem_over_rsi_max.groupby(['group_idx'])['vol'].max() /2
	# min_volume = np.array(min_volume.to_numpy()).flatten()
	
	# elem_over_rsi_max = elem_over_rsi_max.assign(min_vol=lambda x: min_volume[x.group_idx] )
	# resistance_line_group = elem_over_rsi_max[elem_over_rsi_max['vol'] >= elem_over_rsi_max['min_vol']]
	resistance_line_group = elem_over_rsi_max
	# print(resistance_line_group)
	
	if len(resistance_line_group) < 3:
		return resistance_line_group,[]
	
	X = pd.DataFrame(data={'series':resistance_line_group['series']})
	Y = resistance_line_group['close_price']
	vol = resistance_line_group['vol']
	prices = resistance_line_group['high_price']
	regr = linear_model.LinearRegression()
	regr.fit(X, Y)
	resistance_line = regr.predict(X)
	return resistance_line_group,resistance_line

def get_mid_line(df):
	import numpy as np
	from sklearn import linear_model

	   

	if len(df) == 0:

		return []

				   
	
	elem_mid = df[(df['rsi'] > rsi_min)&(df['rsi'] < rsi_max)]
	elem_mid = pd.DataFrame(data={
									'open_price':elem_mid['open_price'],
									'close_price':elem_mid['close_price'],
									'high_price':elem_mid['high_price'],
									'low_price':elem_mid['low_price'],
									'vol':elem_mid['vol'],
									'rsi':elem_mid['rsi'],
									'group_idx':0,
									'series':elem_mid.index
									})

	p_row = 0
	c_row = 0

	group_index = 0

	

	for index, row in elem_mid.iterrows():
		
		if p_row == 0:
			p_row = index
			c_row = index
			continue
		
		c_row = index
			
		if c_row - p_row == 1:
			elem_mid.at[index, 'group_idx'] = group_index

				   

		else:
			group_index = group_index + 1
			elem_mid.at[index, 'group_idx'] = group_index
		
		p_row = index
			
	# min_volume = elem_mid.groupby(['group_idx'])['vol'].max() /2
	# min_volume = np.array(min_volume.to_numpy()).flatten()
	
	# elem_mid = elem_mid.assign(min_vol=lambda x: min_volume[x.group_idx] )
	# mid_line_group = elem_mid[elem_mid['vol'] >= elem_mid['min_vol']]
	mid_line_group = elem_mid
	
	
	if len(mid_line_group) < 3:
		return mid_line_group,[]
	
	X = pd.DataFrame(data={'series':mid_line_group['series']})
	Y = mid_line_group['close_price']
	vol = mid_line_group['vol']
	prices = mid_line_group['low_price']
	
	regr = linear_model.LinearRegression()
	regr.fit(X, Y)
	mid_line = regr.predict(X)
	return mid_line_group,mid_line

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
			
	# min_volume = elem_under_rsi_min.groupby(['group_idx'])['vol'].max() /2
	# min_volume = np.array(min_volume.to_numpy()).flatten()
	
	# elem_under_rsi_min = elem_under_rsi_min.assign(min_vol=lambda x: min_volume[x.group_idx] )
	# support_line_group = elem_under_rsi_min[elem_under_rsi_min['vol'] >= elem_under_rsi_min['min_vol']]
	support_line_group = elem_under_rsi_min
	
	
	if len(support_line_group) < 3:
		return support_line_group,[]
	
	X = pd.DataFrame(data={'series':support_line_group['series']})
	Y = support_line_group['close_price']
	vol = support_line_group['vol']
	prices = support_line_group['low_price']
	
	regr = linear_model.LinearRegression()
	regr.fit(X, Y)
	support_line = regr.predict(X)
	return support_line_group,support_line
	
def get_regression_line(df):
	import numpy as np
	from sklearn import linear_model

	   

	if len(df) == 0:

		return []

				   
	
	elem = df[(df['rsi'] < rsi_oversell) | (df['rsi'] > rsi_overbuy)]

	array_idx = -1
	idx = elem.index[array_idx]
	
	while(idx != elem.index[0]):
		
		array_idx = array_idx - 1
		p_idx = elem.index[array_idx]
		
		# print(elem.index[0],p_idx,idx)
		
		if idx - p_idx == 1:
			idx = p_idx
		else:
			break
			
	elem = df[idx:]

	
	
	series = pd.DataFrame(data={'series':elem.index})
	X = series
	Y = elem['close_price']
	
	# print(X,Y)
	
	regr = linear_model.LinearRegression()
	regr.fit(X, Y)
	reg_line = regr.predict(X)
	return elem,reg_line

def get_modes(df,n=5):
	import numpy as np
	i = 0
	modes = []
	
	df = df.astype({'close_price': 'int64'})
	df1 = df['close_price']
	
	
	while i < n :
		mode = df1.mode().to_numpy()[0]
		modes.append(mode)
		df_drop = df[df['close_price'] == mode].index
		df1 = df1.drop(np.array(df_drop.to_numpy()).flatten())
		i = i + 1
	modes.sort(reverse = True)
	return modes
	

def plot(resistance_line_group,resistance_line,mid_line_group,mid_line,support_line_group,support_line,save_path = img_path):

	import matplotlib.pyplot as plt 
	from matplotlib.ticker import StrMethodFormatter
	import numpy as np
	
	#price trend line
	X1 = pd.DataFrame(data={'series':resistance_line_group['series']})
	X2 = pd.DataFrame(data={'series':support_line_group['series']})
	X3 = pd.DataFrame(data={'series':mid_line_group['series']})
	
	#X Y rsi scatters and bar
	X11 = pd.DataFrame(data={'series':resistance_line_group[resistance_line_group['open_price'] <= resistance_line_group['close_price']]['series']})
	X12 = pd.DataFrame(data={'series':resistance_line_group[resistance_line_group['open_price'] > resistance_line_group['close_price']]['series']})
	Y11 = resistance_line_group[resistance_line_group['open_price'] <= resistance_line_group['close_price']]['rsi']
	Y12 = resistance_line_group[resistance_line_group['open_price'] > resistance_line_group['close_price']]['rsi']
	
	X21 = pd.DataFrame(data={'series':support_line_group[support_line_group['open_price'] >= support_line_group['close_price']]['series']})
	X22 = pd.DataFrame(data={'series':support_line_group[support_line_group['open_price'] < support_line_group['close_price']]['series']})
	Y21 = support_line_group[support_line_group['open_price'] >= support_line_group['close_price']]['rsi']
	Y22 = support_line_group[support_line_group['open_price'] < support_line_group['close_price']]['rsi']
	
	X31 = pd.DataFrame(data={'series':mid_line_group[mid_line_group['open_price'] > mid_line_group['close_price']]['series']})
	X32 = pd.DataFrame(data={'series':mid_line_group[mid_line_group['open_price'] < mid_line_group['close_price']]['series']})
	X33 = pd.DataFrame(data={'series':mid_line_group[mid_line_group['open_price'] == mid_line_group['close_price']]['series']})
	Y31 = mid_line_group[mid_line_group['open_price'] > mid_line_group['close_price']]['rsi']
	Y32 = mid_line_group[mid_line_group['open_price'] < mid_line_group['close_price']]['rsi']
	Y33 = mid_line_group[mid_line_group['open_price'] == mid_line_group['close_price']]['rsi']
	
	#volume bar
	vol11 = resistance_line_group[resistance_line_group['open_price'] <= resistance_line_group['close_price']]['vol']
	vol12 = resistance_line_group[resistance_line_group['open_price'] > resistance_line_group['close_price']]['vol']
	vol21 = support_line_group[support_line_group['open_price'] >= support_line_group['close_price']]['vol']
	vol22 = support_line_group[support_line_group['open_price'] < support_line_group['close_price']]['vol']
	vol31 = mid_line_group[mid_line_group['open_price'] > mid_line_group['close_price']]['vol']
	vol32 = mid_line_group[mid_line_group['open_price'] < mid_line_group['close_price']]['vol']
	vol33 = mid_line_group[mid_line_group['open_price'] == mid_line_group['close_price']]['vol']
	
	#Y price scatters
	prices11 = resistance_line_group[resistance_line_group['open_price'] <= resistance_line_group['close_price']]['close_price']
	prices12 = resistance_line_group[resistance_line_group['open_price'] > resistance_line_group['close_price']]['close_price']
	prices21 = support_line_group[support_line_group['open_price'] >= support_line_group['close_price']]['close_price']
	prices22 = support_line_group[support_line_group['open_price'] < support_line_group['close_price']]['close_price']
	prices31 = mid_line_group[mid_line_group['open_price'] > mid_line_group['close_price']]['close_price']
	prices32 = mid_line_group[mid_line_group['open_price'] < mid_line_group['close_price']]['close_price']
	prices33 = mid_line_group[mid_line_group['open_price'] == mid_line_group['close_price']]['close_price']	
	
	#prices
	plt.subplot(3, 1, 1)
	if len(resistance_line_group) > 2:
		if len(X11) > 0 :
			plt.scatter(X11, prices11,s = 6 , color='green',marker = '.')
		if len(X12) > 0 :
			plt.scatter(X12, prices12, s = 6 , color='red',marker = 'v')
	if len(X1) == len(resistance_line):
		plt.plot(X1, resistance_line, color='cyan', linewidth=1)
		
	if len(support_line_group) > 2 :
		if len(X21) > 0 :
			plt.scatter(X21, prices21,s = 6 ,  color='red',marker = '.')
		if len(X22) > 0 :
			plt.scatter(X22, prices22, s = 6 , color='green',marker = '^')
	if len(X2) == len(support_line):
		plt.plot(X2, support_line, color='magenta', linewidth=1)
		
	if len(mid_line_group) > 2 :
		if len(X31) > 0 :
			plt.scatter(X31, prices31, s = 6 , color='blue',marker = 'v')
		if len(X32) > 0 :
			plt.scatter(X32, prices32, s = 6 , color='blue',marker = '^')
		if len(X33) > 0 :
			plt.scatter(X33, prices33, s = 6 , color='blue',marker = '.')
	if len(X3) == len(mid_line):
		plt.plot(X3, mid_line, color='orange', linewidth=1)
		
	plt.xlabel('Series')
	plt.ylabel('prices')
	
	#RSI
	plt.subplot(3, 1, 2)
	if len(resistance_line_group) > 2:
		plt.scatter(X11, Y11, s = 6 , color='green',marker = '.')
		plt.scatter(X12, Y12, s = 6 , color='red',marker = 'v')

		
	if len(support_line_group) > 2 :
		plt.scatter(X21, Y21, s = 6 , color='red',marker = '.')
		plt.scatter(X22, Y22, s = 6 , color='green',marker = '^')

		
	if len(mid_line_group) > 2 :
		plt.scatter(X31, Y31, s = 6 , color='blue',marker = 'v')
		plt.scatter(X32, Y32, s = 6 , color='blue',marker = '^')
		plt.scatter(X33, Y33, s = 6 ,  color='blue',marker = '.')


	plt.xlabel('Series')
	plt.ylabel('RSI')
	
	#volume
	plt.subplot(3, 1, 3)
	if len(resistance_line_group) > 2:
		plt.bar(np.array(X11.to_numpy()).flatten(), vol11, color='green',width = 1)
		plt.bar(np.array(X12.to_numpy()).flatten(), vol12, color='red',width = 1)
	if len(support_line_group) > 2 :
		plt.bar(np.array(X21.to_numpy()).flatten(), vol21, color='red',width = 1)
		plt.bar(np.array(X22.to_numpy()).flatten(), vol22, color='green',width = 1)
	if len(mid_line_group) > 2 :
		plt.bar(np.array(X31.to_numpy()).flatten(), vol31, color='red',width = 1)
		plt.bar(np.array(X32.to_numpy()).flatten(), vol32, color='green',width = 1)
		plt.bar(np.array(X33.to_numpy()).flatten(), vol33, color='blue',width = 1)
	plt.xlabel('Series')
	plt.ylabel('Volume')


	
	plt.savefig(save_path)
	plt.clf()

def plot2(elem,reg_line,save_path = img_path):

	import matplotlib.pyplot as plt 
	from matplotlib.ticker import StrMethodFormatter
	import numpy as np
	
	#price trend line
	X = pd.DataFrame(data={'series':elem.index})
	
	#X Y rsi scatters and bar
	rsi = elem['rsi']
	#volume bar
	vol = elem['vol']
	
	#Y price scatters
	prices = elem['close_price']
	
	#prices
	plt.subplot(3, 1, 1)
	plt.scatter(X, prices,s = 6 , color='green',marker = '.')
	plt.plot(X, reg_line, color='cyan', linewidth=1)
		
		
	plt.xlabel('Series')
	plt.ylabel('prices')
	
	#RSI
	plt.subplot(3, 1, 2)
	plt.scatter(X, rsi, s = 6 , color='green',marker = '.')


	plt.xlabel('Series')
	plt.ylabel('RSI')
	
	#volume
	plt.subplot(3, 1, 3)
	
	plt.bar(np.array(X.to_numpy()).flatten(), vol, color='green',width = 1)
	
	plt.xlabel('Series')
	plt.ylabel('Volume')


	
	plt.savefig(save_path)
	plt.clf()

	
def plot_all(df,save_path = img_path):

	import matplotlib.pyplot as plt 
	from matplotlib.ticker import StrMethodFormatter
	import numpy as np
	
	#overbuy,middle,oversell
	df1 = df[df['rsi'] >= rsi_overbuy]
	df2 = df[(df['rsi'] < rsi_overbuy) & (df['rsi'] > rsi_oversell)]
	df3 = df[df['rsi'] <= rsi_oversell]
	
	#price trend line
	X1 = pd.DataFrame(data={'series':df1.index})
	X2 = pd.DataFrame(data={'series':df2.index})
	X3 = pd.DataFrame(data={'series':df3.index})
	
	#X Y rsi scatters and bar
	rsi1 = df1['rsi']
	rsi2 = df2['rsi']
	rsi3 = df3['rsi']
	
	#volume bar
	vol1 = df1['vol']
	vol2 = df2['vol']
	vol3 = df3['vol']
	
	#Y price scatters
	prices1 = df1['close_price']
	prices2 = df2['close_price']
	prices3 = df3['close_price']
	
	#prices
	plt.subplot(3, 1, 1)
	plt.scatter(X1, prices1 , color='green',marker = '.')
	plt.scatter(X2, prices2, color='blue',marker = '.')
	plt.scatter(X3, prices3, color='red',marker = '.')
		
		
	plt.xlabel('Series')
	plt.ylabel('prices')
	
	#RSI
	plt.subplot(3, 1, 2)
	plt.scatter(X1, rsi1 , color='green',marker = '.')
	plt.scatter(X2, rsi2 , color='blue',marker = '.')
	plt.scatter(X3, rsi3 , color='red',marker = '.')


	plt.xlabel('Series')
	plt.ylabel('RSI')
	
	#volume
	plt.subplot(3, 1, 3)
	
	plt.bar(np.array(X1.to_numpy()).flatten(), vol1, color='green')
	plt.bar(np.array(X2.to_numpy()).flatten(), vol2, color='blue')
	plt.bar(np.array(X3.to_numpy()).flatten(), vol3, color='red')

	
	plt.xlabel('Series')
	plt.ylabel('Volume')


	
	plt.savefig(save_path)
	plt.clf()

def test_plot(df):
	start_plot = 500
	step_plot = 500
	i = start_plot
	round = 1
	while i < len(df):
		
		
		df2 = df[i-start_plot:i]
		df2 = df2.reset_index(drop=True)
		
		
		plot_all(df2,"img\\test\\graph"+str(round)+".png")
		round = round + 1
			

		i = i+step_plot
	
def test(df):


	i = elem_test + step_test
	while i < len(df):
		
		df1 = df[i-(elem_test + step_test):i-step_test]
		df1 = df1.reset_index(drop=True)
		
		df2 = df[i-elem_test:i]
		df2 = df2.reset_index(drop=True)
		
		
		modes1 = get_modes(df1)
		modes2 = get_modes(df2)
		
		c_vol = int(df2['vol'][len(df2)-1])
		c_rsi = float(df2['rsi'][len(df2)-1])
		# p_rsi = float(df2['rsi'][len(df2)-2])
		c_price = float(df2['close_price'][len(df2)-1])
		p_price = float(df2['close_price'][len(df2)-2])
		# c_high_price = float(df2['high_price'][len(df2)-1])
		# c_low_price = float(df2['low_price'][len(df2)-1])
		# c_open_price = float(df2['open_price'][len(df2)-1])
		# c_idx = int(df2.index[len(df2)-1])		

		position = get_trade_position_test()
		
	
			
		dif = []
		dif.append(modes2[0] - modes2[1])
		dif.append(modes2[1] - modes2[2])
		dif.append(modes2[2] - modes2[3])
		dif.append(modes2[3] - modes2[4])
		m = max(dif)
		split_indexes = [index for index, j in enumerate(dif) if j == m]
		
		# print(m)
		# print (split_indexes)	
		# print(modes2)
		
		up2 = len(modes2[:split_indexes[0]+1])
		down2 = len(modes2[split_indexes[-1]+1:])
		
		is_up_trend = True if (modes1[0] < modes2[0] and modes1[4] <= modes2[4]) or (modes1[0] <= modes2[0] and modes1[4] < modes2[4]) else False
		is_down_trend = True if (modes1[0] > modes2[0] and modes1[4] >= modes2[4]) or (modes1[0] >= modes2[0] and modes1[4] > modes2[4]) else False
		
	
		# if position != 'L' and is_up_trend and up2 < 4 and up2 != down2 and c_price > modes2[0] :
			# # plot2(elem,reg_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			# L_test(df2,"U",c_vol)
			# print(modes1,modes2,up2,down2)	
		# elif position != 'S' and is_down_trend and down2 < 4 and up2 != down2 and c_price < modes2[4] :
			# # plot2(elem,reg_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			# S_test(df2,"U",c_vol)
			# print(modes1,modes2,up2,down2)			
		# elif position != 'L' and is_down_trend and (down2 >= 4 or up2 == down2) and c_price > modes2[4] and c_price < modes2[2] :
			# # plot2(elem,reg_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			# L_test(df2,"U",c_vol)
			# print(modes1,modes2,up2,down2)			
		# elif position != 'S' and is_up_trend and (up2 >= 4 or up2 != down2) and c_price < modes2[0] and c_price > modes2[2] :
			# # plot2(elem,reg_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			# S_test(df2,"D",c_vol)
			# print(modes1,modes2,up2,down2)
			
			
		if position != 'L' and is_up_trend:
			# plot2(elem,reg_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			L_test(df2,"U",c_vol)
			print(modes1,modes2,up2,down2)	
		elif position != 'S' and is_down_trend:
			# plot2(elem,reg_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			S_test(df2,"U",c_vol)
			print(modes1,modes2,up2,down2)			
		elif position != 'L' and is_down_trend == False and modes1 != modes2 and up2 == down2 and c_rsi < rsi_oversell:
			# plot2(elem,reg_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			L_test(df2,"U",c_vol)
			print(modes1,modes2,up2,down2)			
		elif position != 'S' and is_up_trend == False and modes1 != modes2 and up2 == down2 and c_rsi > rsi_overbuy :
			# plot2(elem,reg_line,save_path = '{0}{1}{2}'.format("img\\test\\graph",i,".png"))
			S_test(df2,"D",c_vol)
			print(modes1,modes2,up2,down2)
		
			

		i = i+step_test
	
df1min = pd.read_csv(data_path_1min)	
df5min = pd.read_csv(data_path_5min)
test_plot(df1min)
#test(df1min)

	
	


	
	






