# README: Maps

## Partner Division of Labor

## Known Bugs

## Design Details
### High Level Design
#### Backend

#### Automated Scripts
##### run
standard python script/unix executable provided by cs32 which now accepts an
additional argument --pagerank to run a Java script that computes the pagerank
for every job and stores the values in csv files

##### deployment
unix executable (coded in python) which when run will create a production build of the
react app, push to github, then push to heroku for deployment

##### scrape_data
unix executable (coded in python) which when run, will run 132 python web scraper files,
combine the 132 produced csv files paired by job title to create 66 csv files, and then
add the data from these 66 csv files into a single sqlite3 database with 66 tables
each named by the role they represent

#### Frontend
##### High Level Overview
- approx. 4000 lines of code split across 24 files
- in the components directory, all files are React functional components - these
components are designed to be extensible and reusable
- in the screens directory, all files are React class components - these are less
extensible due to our use of firebase which requires very specific API calls and
function structures
- in App.js, React Router is used to map the different classes in /screens to their
corresponding routes - this should give a good high level overview of the structure
of the frontend
- all files are commented extensively with function header comments and inline comments
to improve readability

## Running Tests

## Testing

## Building and Running Program
#### Building
mvn package in root directory

npm install in /frontend

#### Running: 2 Terminals
##### Terminal 1
./run --gui

##### Terminal 2
npm start in /frontend

## Browser Used For Testing
Chrome
