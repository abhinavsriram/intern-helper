from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

import time
import csv

driver = webdriver.Chrome(ChromeDriverManager().install())

driver.get("https://www.linkedin.com/jobs")

# creates the csv file
with open('linkedin_ds.csv', 'w', newline='') as x:
    writer = csv.writer(x)
    writer.writerow(['title','company','location', 'requirements', 'link'])

driver.find_element_by_xpath('//*[@id="JOBS"]/section[2]/button').click()
search = driver.find_element_by_name("keywords")
search.send_keys("Data Science Intern")
search.send_keys(Keys.RETURN)

try:
    table = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.CLASS_NAME, "results__list"))
    )
    
    # Get scroll height
    last_height = driver.execute_script("return document.body.scrollHeight")

    while True:
        # Scroll down to bottom
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        # Wait to load page
        time.sleep(1)
        # Show more jobs button pressed
        try: 
            button = driver.find_element_by_xpath('//*[@id="main-content"]/div/section[2]/button').click()
        except: 
            pass
        # Calculate new scroll height and compare with last scroll height
        new_height = driver.execute_script("return document.body.scrollHeight")
        if new_height == last_height:
            break
        last_height = new_height
    try: 
        while True:
            button = driver.find_element_by_class_name('infinite-scroller__show-more-button infinite-scroller__show-more-button--visible').click()
    except: 
        pass

    results = table.find_elements_by_xpath('/html/body/main/div/section[2]/ul/li')
    tab = driver.find_element_by_xpath('//*[@id="main-content"]/section/div[2]')
    for result in results:
        try:
            time.sleep(1)
            result.click()
            time.sleep(2)
        except:
            print("no result click")
            pass
        try:
            title = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[1]/div[1]/div[1]/a/h2')
            print(title.text)
        except: 
            print("no title")
            pass
        if ('intern' in title.text.lower()):
            try:
                company = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[1]/div[1]/div[1]/h3[1]/span[1]/a')
            except:
                print("no company")
                pass
            try:
                location = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[1]/div[1]/div[1]/h3[1]/span[2]')
            except:
                print("no location")
                pass
            try: 
                time.sleep(2)
                link = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[1]/div[1]/div[2]/a').get_attribute('href')
            except:
                print("no link") 
                pass
            try: 
                button = tab.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[2]/div/section/button[1]')
            except: 
                print("no button")
                pass
            try: 
                button.click()
            except: 
                print("no click")
                pass
            try: 
                requirements = driver.find_element_by_xpath('//*[@id="main-content"]/section/div[2]/section[2]/div/section/div')
                driver.back()
            except: 
                print("no back")
                pass
        else:
            pass
        try:
            with open('linkedin_ds.csv', 'a+', newline='') as x:
                writer = csv.writer(x)
                writer.writerow([title.text, company.text, location.text, requirements.text, link])    
        except:
            print("pass")
            pass
finally:
    driver.quit() 