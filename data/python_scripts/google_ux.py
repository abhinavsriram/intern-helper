from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

import time
import csv

driver = webdriver.Chrome(ChromeDriverManager().install())

with open('google_ux.csv', 'w', newline='') as x:
    writer = csv.writer(x)
    writer.writerow(['title','company','location', 'requirements', 'link'])

driver.get("https://www.google.com/")

search = driver.find_element_by_name("q")
search.send_keys("UI/UX Design Intern")
search.send_keys(Keys.RETURN)

time.sleep(3)

try:
	jobs = WebDriverWait(driver, 15).until(
		EC.presence_of_element_located((By.XPATH, "/html/body/div[8]/div/div[9]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div/g-card/div/div/div[1]/div/g-link/a/g-tray-header"))
		)
	jobs.click()
	table = driver.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[1]/div[1]/div[3]")

	#.send_keys(Keys.PAGE_DOWN);

	last_height = driver.execute_script("return document.body.scrollHeight")
	while True:
		driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
		time.sleep(1)
		new_height = driver.execute_script("return document.body.scrollHeight")
		if new_height == last_height:
			break
		last_height = new_height


	internships = table.find_elements_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[1]/div[1]/div[3]/ul/li")
	data = driver.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div")
	for internship in internships:
		try:
			time.sleep(2)
			internship.click()
			time.sleep(2)
			try:
				title = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[1]/div/div[1]/h2").text
			except:
				pass
			if ('intern' in title.lower()):
				try:
					company = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[1]/div/div[2]/div[2]/div[1]").text
				except:
					pass
				try: 
					location = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[1]/div/div[2]/div[2]/div[2]").text
				except:
					pass
				try:
					link = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/g-scrolling-carousel/div[1]/div/div/span/a").get_attribute('href')
				except: 
					pass
				try:
					show_more = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[5]/div/div/div").click()
					requirements = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[5]/div/span")
					#print(requirements.text)
				except:
					pass
		except:
			pass
		try:
			with open('google_ux.csv', 'a+', newline='') as x:
				writer = csv.writer(x)
				writer.writerow([title, company, location, requirements.text, link])    
		except:
			print("pass")
			pass
finally:
	driver.quit()

