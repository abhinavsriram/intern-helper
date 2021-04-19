# README: Maps

## Partner Division of Labor
Meera: 
Abhinav: 
Kunj:
Riya: One of the backend developers. Responsible for writing pagerank algorithm as well as developing
naive method for calculating similarity between text. Also, helped with frontend-backend integration-wrote
handlers in Main class in order to receive and send data from the front to the back and vice versa.

## Known Bugs
While we did not identify any particular bugs with the application, we noticed that the recommendations
and results that the user sees may not always be the most accurate or highest of quality. We believe this
is attributed to a few things: 1) our mechanism for comparing a user's resume, and an internship opportunity
is dependent on a very naive text similarity "algorithm" 2) scraped data from Linkedin and Google Jobs but
due to website blocking issues and various formatting variations it was hard to capture a really quality
dataset 3) used a linear combination of pagerank + resume similarity as a way to calculate the overall similarity
score for a particular internship opportunity and user, but it is possible that there is a more nuanced
formula that may work better

## Design Details
#### Backend
There are 17 classes in the backend. Here is how each of the classes are working together:
    1)  CachePageRanks Class: We have a database of 66 internship roles across 8 different industries in
        data/python_scripts/internships.sqlite3. This class has a method that essentially iterates through all
        the tables within the database, builds a directed graph for each of these tables in which the internship
        opportunities within a particular table represents the vertices on the graph, runs pagerank on this graph,
        and then outputs the pagerank results to a corresponding csv. Therefore, the pagerank calculation is not
        happening in real-time when a user is interacting with the application, but rather it is calculated ahead
        of time and then the ranks are used for real-time calculations.
    2)  CSVWriter Class: This class takes in a hashmap in which the keys are of type Job and the values are doubles
        which correspond to the pagerank of that particular opportunity. This class has a method that essentially
        iterates through the map and writes to a csv in which the headers are the internship opportunity's
        id, title, company, location, required qualifications, link, and associated pagerank.
    3) JobGraphBuilder Class: This class is where the meat of the functionality takes place. It is responsible
       for reading data from a particular table within the internships' database, calculating the similarity between
       every internship opportunity and every other internship opportunity of its type, building a graph based on
       those similarity scores (essentially where the more similar a particular opportunity is to another the more
       incoming edges it gets and vice versa), run page rank on the graph, calculate similarity between resume
       attributes and internship opportunity, and then combine page rank and resume similarity score for the final
       set of results the user gets to see.
    4) PageRank Class: This class is in charge of running the page rank algorithm on a directed graph. It handles sinks
       which are vertices with no outgoing edges so that their page rank isn't automatically weighted higher. 
    5) TextSimilarity Class: This class is responsible for removing the stop words from a piece of text (list was
       derived from stop words NLTK corpus: https://gist.github.com/larsyencken/1440509) and creating a set of common
       words between two texts (words that are found in both). 
    6) Job, User, Resume, Experience Classes: These Java classes contain different data points we collect from a user
       in order to output the list of results. Job class contains the information for a particular internship
       opportunity (company, title, location, link, required qualifications). User class contains all information on
       a particular user (name, gpa, email, coursework, skills, resume). Resume class represents all prior experiences
       of a user. Experience class represents different attributes of a particular experience (title, company, 
       start date, end date, description).
    7) DirectedGraph, Edge, Vertex Classes: These are the graphical components for this project. The DirectedGraph class
       is made up of the Vertex and Edge class. Vertices and edges can actually be added in this class and is what
       is used to be passed into PagRank algorithm. 
    8) MyFireBase Class: This is responsible for connecting to Google Firebase database in which we are storing all
       user data and then being able to take that data and convert them to Java objects that can be referenced
       throughout.

Algorithm Overview:
Takes in user resume (skills, coursework, prior experiences)
Calculate similarity between prior experience titles and types of internship roles in our database and displays
roles with highest similarity
Build a graph of all internship opportunities we have available in database for a particular role selected by user
Graph is built by calculating the similarity between a particular internship opportunity and every other internship
opportunity
Run page rank on that graph and store each internship opportunity’s rank score
Calculate similarity between resume and internship opportunity. There are three main components of the resume and that
is skills, coursework, and prior experiences. As a team decided that if a particular user's skills match with an
internship opportunity description that should be weighted more than any other factor so that is weighted at 0.4
Followed by coursework which is weighted at 0.35 and prior experiences at 0.25
Combine page rank + resume similarity score for final results

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
  
## Testing
#### System Tests
Since we did not have a REPL, we couldn't figure out an easy way in order to write out system tests, so 
we clicked around on the application as well as asked three additional people in our social networks to play with
the application. We tested different types of resumes (tech-related, finance-related, humanities-related),
different types of scenarios such as the event that there no roles appear to be a good fit for the user or that
the suggestions our application makes does not align with the user's interests, and we also ensured that we did not
guide the user on how to use our application so that we could minimize bias and interference. We also hand simulated
results with our data/small_intern_data.sqlite3 database which is a curated sample size of 15.

#### JUnit Tests
We wrote 10 JUnit Tests and achieved roughly 65% Jacaco coverage: 
    -CSVTest makes sure that writing and reading from a CSV is being handled correctly.
    -EdgeTest makes sure that different attributes of an edge can be set and then retrieved
    -ExperienceTest makes sure that different attributes of an experience can be set and then retrieved
    -GraphTest makes sure that adding of vertices and edges between vertices are being done correctly
    -JobGraphBuilderTest tests the crux of most of the functionality within our project. It makes sure that
     the correct data is being read, graph is being built correctly, page ranks ordering and resume similarity
     scores makes sense, and the final output results align with what we think they should be.
    -JobTest makes sure that different attributes of a job can be set and then retrieved
    -ResumeTest makes sure that different attributes of a resume can be set and then retrieved
    -SQLDatabaseTest makes sure that connection to SQLDatabase is established, deals with null input/non-existent
     database, ability to query, etc. 
    -TextSimilarityTest takes small sets of phrases and then removes stop words and makes sure the common words are
     actually being identified correctly
    -UserTest makes sure that different attributes of a user can be set and then retrieved

## Building and Running Program
#### Building
mvn package in root directory

npm install in /frontend

#### Running: 2 Terminals
##### Terminal 1
In the instance that you want to run the page rank calculations yourself, then you can do ./run -pagerank. That
will store all the pagerank results in the data directory and then you would  ./run --gui after. 

In the instance that you just want to ue the existing page rank calculations, then you can just directly do
./run --gui

##### Terminal 2
npm start in /frontend

## Browser Used For Testing
Chrome

## Checkstyle Errors
We do have some Checkstyle Errors for naming conventions. The reasoning for those errors is because Firebase has
this cool feature where if we follow the nomenclature of different fields as specified in Firestore then when we
read data from it rather than creating an instance of a java class and then using setters to set the values of that 
instance  based on the data from firestore, we can just call on instance.toObject(instance.class) and it will
automatically fill in all the relevant information. Thus we thought that is better coding style as it avoids
redudancy and is more efficient so we did it that way.