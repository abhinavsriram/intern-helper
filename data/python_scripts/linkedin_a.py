from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

import time
import csv

##This file is similar to other files that start with linkedin_
##Using selenium, scraping LinkedIn for all internship data

#set up the driver
driver = webdriver.Chrome(ChromeDriverManager().install())

#get the link of the platform to pull from: linkedin
driver.get("https://www.linkedin.com/jobs")

# creates the csv file with unique suffix
with open('linkedin_a.csv', 'w', newline='') as x:
    writer = csv.writer(x)
    writer.writerow(['title','company','location', 'requirements', 'link'])

#finding the search bar and typing the role, in this case Accounting
driver.find_element_by_xpath('//*[@id="JOBS"]/section[2]/button').click()
search = driver.find_element_by_name("keywords")
search.send_keys("Accounting Intern") #varies depending on role searching for
search.send_keys(Keys.RETURN)

#try catch to load the next page for the results
try:
    #get the table where all the results lie
    table = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.CLASS_NAME, "results__list"))
    )

    # Get scroll height
    last_height = driver.execute_script("return document.body.scrollHeight")

    #Loop to continue scrolling the page until there are no more results
    while True:
        # Scroll down to bottom
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        # Wait to load page
        time.sleep(1)
        # Show more jobs button pressed for more results
        try:
            button = driver.find_element_by_xpath('//*[@id="main-content"]/div/section[2]/button').click()
        except:
            pass
        # Calculate new scroll height and compare with last scroll height
        new_height = driver.execute_script("return document.body.scrollHeight")
        if new_height == last_height:
            break
        last_height = new_height
    #Finding all the results in the list 
    results = table.find_elements_by_xpath('/html/body/main/div/section[2]/ul/li')
    #storing element that stores all data of a job
    tab = driver.find_element_by_xpath('//*[@id="main-content"]/section/div[2]')
    #loop through all the results found
    for result in results:
        #sleep for a second and then click the result to see data
        try:
            time.sleep(1)
            result.click()
            time.sleep(2)
        except:
            pass
        #get the title from the tab element
        try:
            title = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[1]/div[1]/div[1]/a/h2')
        except:
            pass
        #check to see if title has intern in it 
        if ('intern' in title.text.lower()):
            #get company name
            try:
                company = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[1]/div[1]/div[1]/h3[1]/span[1]/a')
            except:
                pass
            #get location
            try:
                location = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[1]/div[1]/div[1]/h3[1]/span[2]')
            except:
                pass
            #sleep for a second and then get link
            try:
                time.sleep(2)
                link = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[1]/div[1]/div[2]/a').get_attribute('href')
            except:
                pass
            #find the show more button
            try:
                button = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[2]/div/section/button[1]')
            except:
                pass
            #click the show more button
            try:
                button.click()
            except:
                pass
            #get all the requirement data
            try:
                requirements = driver.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[2]/div/section/div')
                driver.back()
            except:
                pass
        else:
            pass
        #write the result into the csv file
        try:
            with open('linkedin_a.csv', 'a+', newline='') as x:
                writer = csv.writer(x)
                writer.writerow([title.text, company.text, location.text, requirements.text, link])
        except:
            pass
#quit when done
finally:
    driver.quit()