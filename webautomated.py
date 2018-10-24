from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

basic_conf_path = "conf\\basic.ini"

def get_user():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(basic_conf_path)
	return config['automate']['username']
		
def get_password():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(basic_conf_path)
	return config['automate']['password']
	
def get_serie():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(basic_conf_path)
	return config['automate']['serie']
	
def get_pin():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(basic_conf_path)
	return config['automate']['pin']
	
def get_volume():
	import configparser
	
	config = configparser.ConfigParser()
	config.read(basic_conf_path)
	return config['automate']['volume']
	
def kill_instance():
	import os
	os.system("taskkill /F /IM chrome.exe")
	os.system("taskkill /F /IM chromedriver.exe")

def login():
	username = get_user()
	password = get_password()
	serie = get_serie()
	print(username,password,serie)
	
	browser.get("https://streaming.settrade.com/realtime/streaming-login/login.jsp?noPopUp=true")
	
	browser.find_element_by_xpath("//*[@id=\"txtLoginBrokerId\"]/ng-select/div/div/div[2]/input").clear();
	browser.find_element_by_xpath("//*[@id=\"txtLoginBrokerId\"]/ng-select/div/div/div[2]/input").send_keys("MAYBANK KIMENG");
	browser.find_element_by_xpath("//*[@id=\"txtLoginBrokerId\"]/ng-select/div/div/div[2]/input").send_keys(Keys.RETURN);
	
	
	browser.find_element_by_xpath("//*[@id=\"txtLogin\"]").clear();
	browser.find_element_by_xpath("//*[@id=\"txtLogin\"]").send_keys(username);
	
	browser.find_element_by_xpath("//*[@id=\"txtPassword\"]").clear();
	browser.find_element_by_xpath("//*[@id=\"txtPassword\"]").send_keys(password);
	
	
	browser.find_element_by_xpath("//*[@id=\"submitBtn\"]").click();
	
	
	element = WebDriverWait(browser, 10).until(EC.presence_of_element_located((By.XPATH, "//*[@id=\"open-streaming-btn\"]")))
	# WebDriverWait wait = new WebDriverWait(driver,10);
	# wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"open-streaming-btn\"]")));
	
	element.click();
	
	
	for handle in browser.window_handles:
		browser.switch_to_window(handle)
	
	
	
	
	try:
		element = WebDriverWait(browser, 10).until(EC.presence_of_element_located((By.XPATH, "//*[@id=\"place-order-symbol\"]/auto-complete/div/input[2]")))
	finally:
		# browser.quit()
		print("error")
	
	element.clear();
	element.send_keys(serie);
	element.send_keys(Keys.RETURN);
	
	browser.find_element_by_xpath("//*[@id=\"menu\"]/menu-renderer/div[3]").click();
	print("complete")
	
kill_instance()
browser = webdriver.Chrome(executable_path="chromedriver.exe")	
login()

