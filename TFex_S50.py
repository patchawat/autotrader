import pandas as pd
import datetime



data_path = "feature.csv"
img_path = "img\\sup_res_line.png"
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
	

	
def analyse(df):
	
	i = 1
	while i < len(df) and i > 0:
		
		
		c_vol = int(df['vol'][i])
		c_price = float(df['close_price'][i])
		
		p_vol = int(df['vol'][i-1])
		p_high_price = float(df['high_price'][i-1])
		p_low_price = float(df['low_price'][i-1])
		
		c_datetime = str(df['DateTime'][i])
		Date = c_datetime[:10]
		Time = c_datetime[11:]
		
		
		position = get_trade_position()
		
		if c_datetime.find("09:30:00") != -1 or c_datetime.find("14:00:00") != -1:
			print(c_datetime)
			i = i+1
			continue
		elif (c_datetime.find("12:00:00") != -1 or c_datetime.find("16:30:00") != -1) and (position == "L" or position == "S"):
			print(c_datetime)
			close_position(df)
			i = i+2
			continue
			
			
		start_index = df[df['DateTime'].str.contains(Date)].index
		print (to_last[0])
		# max_vol_intraday = ['vol'].max()
		# print (max_vol_intraday)
		# t = df_15['vol'].max()*7/10
		
		
		
		# #update expected_price
		# expected_price = -1
		# trade_price = -1
		
		# if position != "":
			# expected_price = get_expected_price()
			# trade_price = get_trade_price()
			# if position== "L":
				# if c_price - trade_price > 4 and c_price > expected_price:
					# expected_price = c_price
					# set_expected_price(expected_price)
					
			# elif position== "S":
				# if trade_price - c_price > 4 and (expected_price == -1 or c_price < expected_price):
					# expected_price = c_price
					# set_expected_price(expected_price)
		
		# #trade
		# if c_vol >= t:
			# if c_price > p_price :
				
				# if (position == "S" and int(c_vol) >= t2) or position == "":
				# #if position != "L":
					# print ("L: ",c_price ," at i: " ,i+16," t: ",t)
					# L(df_15,"U",c_vol)
					
				# print("Up trend at vol: ",c_vol," t2: ",t2)
				
				# if position == "L":
					# set_vol(c_vol)
					
			# elif c_price < p_price :
				
				# if (position == "L" and int(c_vol) >= t2) or position == "":
				# #if position != "S":
					# print ("S: ",c_price, " at i: " ,i+16," t: ",t)
					# S(df_15,"D",c_vol)
					
				# print("Down trend at vol: ",c_vol," t2: ",t2)
				# if position == "S":
					
					# set_vol(c_vol)
		
		# #Close
		# position = get_trade_position()
		# if position != "":
			# if position == "L" and c_price - trade_price > 0 and c_price - trade_price  <= (expected_price - trade_price)*3/5:
				# close_position(df_15)
				# print ("Close L: ",c_price, " at i: " ,i+16)
				
			# elif position == "S"  and trade_price - c_price > 0 and trade_price - c_price <= (trade_price - expected_price)*3/5:
				# close_position(df_15)
				# print ("Close S: ",c_price, " at i: " ,i+16)
			
		i = i+1
	
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
	send_mail(from_usr,from_usr_pass,to_usr,"L",str(df['close_price'][len(df)-1]))	
		
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
	send_mail(from_usr,from_usr_pass,to_usr,"S",str(df['close_price'][len(df)-1]))
	

		
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
	send_mail(from_usr,from_usr_pass,to_usr,"close",str(df['close_price'][len(df)-1]))
	


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

def keep_last_n_data(df,n=ticks*2):
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
	import matplotlib.pyplot as plt
	from sklearn import linear_model

	   

	if len(df) == 0:

		return []

				   
	
	elem_under_50rsi = df[df['rsi'] < 50]
	# elem_under_50rsi = pd.DataFrame(data={'low_price':elem_under_50rsi['low_price'],'group_idx':0,'series':elem_under_50rsi.index})
	elem_under_50rsi = pd.DataFrame(data={'rsi':elem_under_50rsi['rsi'],'group_idx':0,'series':elem_under_50rsi.index,'vol':elem_under_50rsi['vol']})

	p_row = 0
	c_row = 0

	group_index = 0

	#elem_under_50rsi = elem_under_50rsi.reset_index(drop=True)
	

	for index, row in elem_under_50rsi.iterrows():
		
		if p_row == 0:
			p_row = index
			c_row = index
			continue
		
		c_row = index
			
		if c_row - p_row == 1:
			elem_under_50rsi.at[index, 'group_idx'] = group_index

				   

		else:
			group_index = group_index + 1
			elem_under_50rsi.at[index, 'group_idx'] = group_index
		
		p_row = index
			

	support_line_group = elem_under_50rsi.groupby(['group_idx']).min()
	
	X = pd.DataFrame(data={'series':support_line_group['series']})
	# Y = support_line_group['low_price']
	Y = support_line_group['rsi']
	vol = support_line_group['vol']
	
	regr = linear_model.LinearRegression()
	regr.fit(X, Y)
	support_line = regr.predict(X)
	return X,Y,vol,support_line

	

		

def get_resistance_line(df):
	import matplotlib.pyplot as plt
	from sklearn import linear_model

	   

	if len(df) == 0:

		return []

				   
	
	elem_over_50rsi = df[df['rsi'] > 50]
	
	
	# elem_over_50rsi = pd.DataFrame(data={'high_price':elem_over_50rsi['high_price'],'group_idx':0,'series':elem_over_50rsi.index})
	elem_over_50rsi = pd.DataFrame(data={'rsi':elem_over_50rsi['rsi'],'group_idx':0,'series':elem_over_50rsi.index,'vol':elem_over_50rsi['vol']})
	

	p_row = 0
	c_row = 0

	group_index = 0

	#elem_over_50rsi = elem_over_50rsi.reset_index(drop=True)
	

	for index, row in elem_over_50rsi.iterrows():
		
		if p_row == 0:
			p_row = index
			c_row = index
			continue
		
		c_row = index
			
		if c_row - p_row == 1:
			elem_over_50rsi.at[index, 'group_idx'] = group_index

				   

		else:
			group_index = group_index + 1
			elem_over_50rsi.at[index, 'group_idx'] = group_index
		
		p_row = index
			
	
	resistance_line_group = elem_over_50rsi.groupby(['group_idx']).max()
	 
	
	
	X = pd.DataFrame(data={'series':resistance_line_group['series']})
	# Y = resistance_line_group['high_price']
	Y = resistance_line_group['rsi']
	vol = resistance_line_group['vol']
	regr = linear_model.LinearRegression()
	regr.fit(X, Y)
	resistance_line = regr.predict(X)
	return X,Y,vol,resistance_line



def plot(X1,Y1,vol1,line1,X2,Y2,vol2,line2):

	import matplotlib.pyplot as plt 
	import numpy as np
	
	plt.subplot(2, 1, 1)
	plt.scatter(X1, Y1,  color='green')
	plt.plot(X1, line1, color='cyan', linewidth=2)
	
	plt.scatter(X2, Y2,  color='red')
	plt.plot(X2, line2, color='magenta', linewidth=2)	

	plt.xlabel('Series')
	plt.ylabel('RSI')
	
	plt.subplot(2, 1, 2)
	ind1 = np.arange(len(X1))
	ind2 = np.arange(len(X2))
	
	
	plt.bar(np.array(X1.to_numpy()).flatten(), vol1, color='green',width = 5)
	plt.bar(np.array(X2.to_numpy()).flatten(), vol2, color='red',width = 5)
	plt.xlabel('Series')
	plt.ylabel('Volume')

	plt.savefig(img_path)

df = pd.read_csv(data_path)

X1,Y1,vol1,resistance_line = get_resistance_line(df)
X2,Y2,vol2,support_line = get_support_line(df)



plot(X1,Y1,vol1,resistance_line,X2,Y2,vol2,support_line)





# #df = keep_last_n_data(df)

# d = {
	# 'high_price':int(df['high_price']),
	# 'low_price':df['low_price'],
	# 'open_price':df['open_price'],
	# 'close_price':df['close_price'],
	# 'vol': df['vol'],
	# 'rsi': df['rsi']
	# }


# # df = pd.DataFrame(data=d)


# # analyse(df)

# now = datetime.datetime.now()



# c_vol = int(df['vol'][len(df)-1])
# # c_price = float(df['close_price'][len(df)-1])

# # p_vol = int(df['vol'][len(df)-2])
# # p_high_price = float(df['high_price'][len(df)-2])
# # p_low_price = float(df['low_price'][len(df)-2])

# c_datetime = str(df['DateTime'][len(df)-1])
# Date = c_datetime[:10]
# Time = c_datetime[11:]


# position = get_trade_position()

# #update expected_price
# expected_price = -1
# trade_price = -1

# if position != "":
	# expected_price = get_expected_price()
	# trade_price = get_trade_price()
	# if position== "L":
		# if c_price - trade_price > 4 and c_price > expected_price:
			# expected_price = c_price
			# set_expected_price(expected_price)
			
	# elif position== "S":
		# if trade_price - c_price > 4 and (expected_price == -1 or c_price < expected_price):
			# expected_price = c_price
			# set_expected_price(expected_price)
			

# # if c_datetime.find("09:30:00") != -1 or c_datetime.find("14:00:00") != -1:
	# # exit()
# # elif (int(now.hour) == 12 and int(now.minute) >= 25 ) or (int(now.hour) == 16 and int(now.minute) >= 50 ) :
	# # if position == "L" or position == "S":
		# # close_position(df)
	# # exit()

# if (int(now.hour) == 12 and int(now.minute) >= 25 ) or (int(now.hour) == 16 and int(now.minute) >= 50 ) :
	# if position == "L" or position == "S":
		# close_position(df)
	# exit()

# start_index = 0
# end_index = df.index[-1] + 1



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


# # if p_high_price - c_price < c_price - p_low_price and position != 'L':
	# # L(df,"U",c_vol)
# # elif  p_high_price - c_price > c_price - p_low_price and position != 'S':
	# # S(df,"D",c_vol)


# # #Close
# # if position != "":
	# # if position == "L" and c_price - trade_price > 0 and c_price - trade_price  <= (expected_price - trade_price)*3/5:
		# # close_position(df)
		
	# # elif position == "S"  and trade_price - c_price > 0 and trade_price - c_price <= (trade_price - expected_price)*3/5:
		# # close_position(df)
	

	
	


	
	






