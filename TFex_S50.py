import pandas as pd

def copy2newfile(df,destination_filename):
	#df = pd.read_csv(source_filename)
	#df = df[df['b_vol'] + df['s_vol'] > df['vol'].median()]
	#df.to_csv(destination_filename, columns = columns,index=False)
	#d = {'price':df['close_price'],'vol': df['b_vol'] + df['s_vol'], 'rsi':df['rsi'],'DateTime': df['DateTime']}
	#df = pd.DataFrame(data=d)
	df.to_csv(destination_filename,index=False)
	

def plot_rsi_vol_data(df):
	import matplotlib.pyplot as plt
	# df = pd.read_csv(source_filename,usecols = columns)
	
	# s_high_price = df[(df['rsi'] > 70)& (df['vol'] >= df['vol'].median())]['price']
	
	# ax = plt.gca()
	# s_high_price.plot(y='price',style='go',ms = 3, ax=ax)
	# s_low_price.plot(y='price',style='ro', ms = 3, ax=ax)
	# s_middle_price_lessvol.plot(y='price',style='yo', ms = 3, ax=ax)
	# s_middle_price_greatvol.plot(y='price',style='bo', ms = 3, ax=ax)
	# s_price.plot(y='price',style='y-',ms = 3, ax=ax)
	# plt.show()
	
	
	up_5_day = df[(df['rsi']>50) & (df['vol']>=median_vol(df,ticks(5)))]['price']
	up_1_day = df[(df['rsi']>50) & (df['vol']>=median_vol(df,ticks()))]['price']
	down_5_day = df[(df['rsi']<50) & (df['vol']>=median_vol(df,ticks(5)))]['price']
	down_1_day = df[(df['rsi']<50) & (df['vol']>=median_vol(df,ticks()))]['price']
	
	#plt.scatter(y_test.index, y_test, color='darkorange', label='data')
	#plt.plot(up_5_day.index, up_5_day, color='navy', lw=2, label='up 5 days')
	# plt.plot(up_1_day.index, up_1_day, color='darkorange', lw=2, label='up 1 days')
	plt.scatter(up_1_day.index, up_1_day, color='darkorange', label='up 1 days')
	#plt.plot(down_5_day.index, down_5_day, color='c', lw=2, label='down 5 days')
	# plt.plot(down_1_day.index, down_1_day, color='cornflowerblue', lw=2, label='down 1 days')
	plt.scatter(down_1_day.index, down_1_day, color='cornflowerblue', label='down 1 days')
	# plt.scatter(X, y, color='darkorange', label='data')
	# plt.plot(X, y_rbf, color='navy', lw=lw, label='RBF model')
	# plt.plot(X, y_lin, color='c', lw=lw, label='Linear model')
	# plt.plot(X, y_poly, color='cornflowerblue', lw=lw, label='Polynomial model')
	
	plt.xlabel('time')
	plt.ylabel('rsi')
	plt.title('overbuy - oversell with medain vol from 1 and 5 days')
	plt.legend()
	plt.show()
	
	
	
	
def normalized_data(df):

	#Nornalized data 0 - 1
	#normalized_df=(df-df.mean())/df.std()
	normalized_df=(df-df.min())/(df.max()-df.min())
	return normalized_df



def sort_data(df):

	df = df.sort_values(['geohash','timestamp'])
	return df
	
def send_email():
	import smtplib

	gmail_user = 'patchawat.trader@gmail.com'  
	gmail_password = 'He11_Master'

	sent_from = gmail_user  
	to = ['teexaou@gmail.com', 'pat.visit@gmail.com']  
	subject = 'OMG Super Important Message'  
	body = 'Hallo'

	email_text = "\r\n".join([
	"From: {}".format(gmail_user),
	"To: {}".format(to),
	"Subject: {}".format(subject),
	"",
	"{}".format(body)])

	

	try:  
		server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
		server.ehlo()
		server.login(gmail_user, gmail_password)
		server.sendmail(sent_from, to, email_text)
		server.close()

		print ('Email sent!')
	except:  
		print ('Something went wrong...') 

def send_mail_img(img_filepath):
	import smtplib,ssl,os
	from email.mime.text import MIMEText
	from email.mime.image import MIMEImage
	from email.mime.multipart import MIMEMultipart
	
	gmail_user = 'patchawat.trader@gmail.com'  
	gmail_password = 'He11_Master'
	to = 'teexaou@gmail.com'  
	
	img_data = open(img_filepath, 'rb').read()
	msg = MIMEMultipart()
	msg['Subject'] = 'Test img'
	msg['From'] = gmail_user
	msg['To'] = to

	text = MIMEText("test")
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
	
def max_vol(df):
	return df['vol'].max()
	
def min_vol(df):
	return df['vol'].min()

def mean_vol(df):
	return df['vol'].mean()

def median_vol(df,ticks=-1):
	if ticks < 0:
		return df['vol'].median()
	df = df[len(df)-ticks+1:]
	return df['vol'].median()

def ticks(day=1,tickperday=65):
	return day*tickperday

	
def linear_regression(df_features,df_target):
	from sklearn import linear_model
	import matplotlib.pyplot as plt
	X_train = df_features[:int(len(df_features)/2)]
	# X_test = df_features[int(len(df_features)/2):]
	X_test = df_features
	y_train = df_target[:int(len(df_target)/2)]
	# y_test = df_target[int(len(df_target)/2):]
	y_test = df_target
	lm = linear_model.LinearRegression()
	# model = lm.fit(X_train, y_train)
	model = lm.fit(X_test, y_test)
	# predictions = lm.predict(X_test)
	predictions = lm.predict(X_test)
	print(predictions[0:5])
	print(lm.score(X_train,y_train))
	print(lm.coef_)
	print(lm.score(X_test,y_test))
	print(lm.coef_)
	print(lm.intercept_)
	
	plt.scatter(X_test, y_test, color='darkorange', label='data')
	plt.plot(X_test, predictions, color='navy', lw=2, label='Linear model')
	# plt.scatter(X, y, color='darkorange', label='data')
	# plt.plot(X, y_rbf, color='navy', lw=lw, label='RBF model')
	# plt.plot(X, y_lin, color='c', lw=lw, label='Linear model')
	# plt.plot(X, y_poly, color='cornflowerblue', lw=lw, label='Polynomial model')
	plt.xlabel('time')
	plt.ylabel('target')
	plt.title('Linear Regression')
	plt.legend()
	plt.show()

def nonlinear_regression(df_features,df_target):
	from sklearn.svm import SVR
	import matplotlib.pyplot as plt
	X_train = df_features[:int(len(df_features)/2)]
	# X_test = df_features[int(len(df_features)/2):]
	X_test = df_features
	y_train = df_target[:int(len(df_target)/2)]
	# y_test = df_target[int(len(df_target)/2):]
	y_test = df_target
	
	svr_rbf = SVR(kernel='rbf', C=1e3, gamma=0.1)
	# y_rbf = svr_rbf.fit(X_train, y_train).predict(X_test)
	y_rbf = svr_rbf.fit(X_test, y_test).predict(X_test)
	
	#print(y_rbf[0:5])
	print(svr_rbf.score(X_test,y_test))
	#print(svr_rbf.coef_)
	print(svr_rbf.intercept_)
	
	plt.scatter(X_test, y_test, color='darkorange', label='data')
	plt.plot(X_test, y_rbf, color='navy', lw=2, label='RBF model')
	# plt.scatter(X, y, color='darkorange', label='data')
	# plt.plot(X, y_rbf, color='navy', lw=lw, label='RBF model')
	# plt.plot(X, y_lin, color='c', lw=lw, label='Linear model')
	# plt.plot(X, y_poly, color='cornflowerblue', lw=lw, label='Polynomial model')
	plt.xlabel('time')
	plt.ylabel('target')
	plt.title('Support Vector Regression')
	plt.legend()
	plt.show()

	
def example():
	from sklearn import linear_model
	from sklearn import datasets
	data = datasets.load_boston()
	df = pd.DataFrame(data.data, columns=data.feature_names)
	target = pd.DataFrame(data.target, columns=["MEDV"])
	print(df)
	print(target)

def median_linear_regression(df):
	from sklearn import linear_model
	import matplotlib.pyplot as plt
	
	
	y5day = df[len(df)-ticks(2,16)+1:][df['vol'] < median_vol(df,ticks(2,16))]['price']
	y1day = df[len(df)-ticks(1,16)+1:][df['vol'] < median_vol(df,ticks(1,16))]['price']
	
	y5dayh = df[len(df)-ticks(2,16)+1:][df['vol'] >= median_vol(df,ticks(2,16))]['price']

	x5day = pd.DataFrame(data={'series':y5day.index})
	x5dayh = pd.DataFrame(data={'series':y5dayh.index})
	x1day = pd.DataFrame(data={'series':y1day.index})	
	


	lm = linear_model.LinearRegression()
	
	model5day = lm.fit(x5day, y5day)
	median5day = lm.predict(x5day)
	
	# print(median5day)
	
	model1day = lm.fit(x1day, y1day)
	median1day = lm.predict(x1day)
	
	# print(median1day)
	
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
	plt.show()
	

	
	
	
df = pd.read_csv("..\\..\\..\\..\\feature.csv")

#-----------------------------------------------------------------Regression

# d_features = {
	# 'series':df.index
	# # 'vol': df['vol'], 
	# # 'rsi':df['rsi'],
	# #'adx': df['adx'],
	# #'atr':df['atr'],
	# # 'cci':df['cci'],
	# #'macd':df['macd'],
	# #'mfi':df['mfi'],
	# #'mom':df['mom'],
	# # 'obv':df['obv'],
	# #'rocr':df['rocr'],
	# #'s_macd':df['s_macd'],
	# #'std':df['std'],
	# # 'swing':df['swing'],
	# #'trix':df['trix'],
	# # 'tsf':df['tsf'],
	# # 'willr':df['willr']
	# }


# df_features = pd.DataFrame(data=d_features)

# # df_features = normalized_data(df_features)

# d_target = {'price':df['price']}

# df_target = pd.DataFrame(data=d_target)


# nonlinear_regression(df_features,df_target)

#-------------------------------------------------------------------------Median Regrssion

d = {
	'price':df['price'],
	'vol': df['vol']
	}


df_features = pd.DataFrame(data=d)

median_linear_regression(df)





