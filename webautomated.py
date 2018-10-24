from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys

browser = webdriver.Chrome(executable_path="chromedriver.exe")
browser.get("https://streaming.settrade.com/realtime/streaming-login/login.jsp?noPopUp=true")