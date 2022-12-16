# README
Intern Helper is a web app that allows users to submit their resume, find job descriptions that best match their resume and to tailor their resume to a specific job application.

## Automated Scripts
### `run`
python script/unix executable which will run the backend of the project and it accepts an additional argument --pagerank to run a Java script that computes the PageRank for every job in the database and stores the values in csv files. This will take roughly around 5-10 minutes to run for all the roles.

### `deployment`
unix executable (coded in python) which when run will create a production build of the react app, push to github, then push to heroku for deployment.

### `scrape_data`
unix executable (coded in python) which when run, will run 132 python web scraper files, combine the 132 produced csv files paired by job title to create 66 csv files, and then add the data from these 66 csv files into a single sqlite3 database with 66 tables each, named by the role they represent.

## Building and Running Program
### Building
`mvn package` in root directory

`npm install` in [/frontend](/frontend)

### Running
##### Terminal 1
In the instance that you want to run the page rank calculations yourself, then you can do `./run --pagerank`. This process will take approximately 5-10 minutes. That will store all the PageRank results in the data directory and then you would `./run --gui` after.

In the instance that you just want to use the existing page rank calculations, then you can just directly do `./run --gui`.

##### Terminal 2
`npm start` in [/frontend](/frontend)
