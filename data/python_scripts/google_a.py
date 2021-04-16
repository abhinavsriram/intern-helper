from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

import time
import csv

##This file is similar to other files that start with google_
##Using selenium, scraping Google Jobs for all internship data

#set up the driver
driver = webdriver.Chrome(ChromeDriverManager().install())

# creates the csv file with unique suffix
with open('google_a.csv', 'w', newline='') as x:
    writer = csv.writer(x)
    writer.writerow(['title','company','location', 'requirements', 'link'])

#get the link of the platform to pull from: google
driver.get("https://www.google.com/")

#finding the search bar and typing the role, in this case Accounting
search = driver.find_element_by_name("q")
search.send_keys("Accounting Intern") #varies depending on role searching for
search.send_keys(Keys.RETURN)

#wait for three seconds before going to next page
time.sleep(3)

#try catch to load the next page for the results
try:
	#find on the first result which presents all the jobs
	jobs = WebDriverWait(driver, 15).until(
		EC.presence_of_element_located((By.XPATH, "/html/body/div[8]/div/div[9]/div[1]/div/div[2]/div[2]/div/div/div[1]/div/div/g-card/div/div/div[1]/div/g-link/a/g-tray-header"))
		)
	#click on the jobs element and get the table with all results
	jobs.click()
	table = driver.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[1]/div[1]/div[3]")

	#Supporting scrolling on the Google Jobs page
	last_height = driver.execute_script("return document.body.scrollHeight")
	while True:
		driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
		time.sleep(1)
		new_height = driver.execute_script("return document.body.scrollHeight")
		if new_height == last_height:
			break
		last_height = new_height

	#get all the internships in the table
	internships = table.find_elements_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[1]/div[1]/div[3]/ul/li")
	#fine data element that stores all individual data
	data = driver.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div")
	#loop through each internship
	for internship in internships:
		#sleep for a second and then click the result to see data
		try:
			time.sleep(2)
			internship.click()
			time.sleep(2)
			#get the title from the tab element
			try:
				title = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[1]/div/div[1]/h2").text
			except:
				pass
			#check to see if title has intern in it 	
			if ('intern' in title.lower()):
				#get the company name
				try:
					company = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[1]/div/div[2]/div[2]/div[1]").text
				except:
					pass
				#get the location
				try: 
					location = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[1]/div/div[2]/div[2]/div[2]").text
				except:
					pass
				#get the link	
				try:
					link = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/g-scrolling-carousel/div[1]/div/div/span/a").get_attribute('href')
				except: 
					pass
				#click the show more button and ge the requirements
				try:
					show_more = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[5]/div/div/div").click()
					requirements = data.find_element_by_xpath("/html/body/div[2]/div/div[2]/div[1]/div/div/div[3]/div[2]/div/div[1]/div/div/div[5]/div/span")
				except:
					pass
		except:
			pass
		#write the result into the csv file
		try:
			with open('google_a.csv', 'a+', newline='') as x:
				writer = csv.writer(x)
				writer.writerow([title, company, location, requirements.text, link])    
		except:
			pass
#quit when finished
finally:
	driver.quit()