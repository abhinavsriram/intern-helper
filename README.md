# cs0320 Term Project 2021

## Team Members: 
Abhinav (asrira11, abhinavsriram), Kunj (kpadh, kunjpadh), Meera (mkurup1, meera-kurup), Riya (rdulepet, riya-dulepet)

## Team Strengths and Weaknesses:
Abhinav: 
##### Strengths: 
- Prior experience with product lifecycle - user segmentation, interviews, surveys; use cases/pain points/needs; product spec sheet etc.
- Prior experience with front-end dev using React.js and development of UI wireframes/prototypes through human centered design process
##### Weaknesses: 
- Dynamic and functional programming
- Algorithmic design skills
- Familiarity with AI/ML

Kunj: 
##### Strengths:
- Logic (back-end)
- Flexible to work on different project aspects
- Math
- Familiarity with AI & ML principles
##### Weaknesses:
- Design (front-end) 
- Dynamic programming
- Complex Algorithms (to a degree) 

Meera:
##### Strengths: 
- Design (front-end) 
- Prior experience with SQL and databases 
- Note-taking and organization
- Flexible to work on different project aspects
##### Weakness: 
- Math 
- Complex Algorithms (to a degree)
- Ask too many questions
- Patience 


Riya: 
##### Strengths:
- Very flexible 
- Will take the time to learn new concepts
- Algorithmic design
- Highly organized
- Intermediate python skills
##### Weaknesses:
- Testing
- Front-end

## Project Idea(s): 

### Idea 1

#### Enhanced CAB Idea: 
##### Short Description: Web app for users to create a unique course plan for their time at Brown
##### Problem: Open curriculum is too open? Students tend to have a difficult time using CAB, specifically course selection given varying workloads between courses and pre-reqs and planning out which courses to take at Brown to make the best of their time here
##### Solution: Creating a service that allows students to create a plan for their 8 semesters at Brown, shows clear metrics when it comes to satisfying requirements for various concentrations, provides information on avg workload per semester based on CR metrics etc.
##### Critical Features: 
1. CAB and CR Functionality
- Use web scraper to get information from CAB and CR
- Present in a user friendly, integrated and appealing manner
2. Main feature(s): Personalized student course planning
- Allows students to add courses they plan to take during their time at Brown - gives a clear consolidated big picture view of what they can expect their time at Brown to look like - clean UI
- Algorithm that presents optimal course plan by balancing out hard courses with easier ones (based on CR data)
- AI/ML component that parses course plan of other students (anonymously) and uses this information when presenting optimal course plans for every concentration
3. Our Algorithm: Look at prereqs and make sample course plan
- As others add their courses: add ML which looks at other concentrators and see their selected courses, incorporating their data into the main algorithm for what courses to take
4. Better UI than current CAB
##### Challenges and Questions with the project:
We need access to data/algorithms that IT dept used to create the concentration declaration thing in ASK - otherwise it would be very hard to manually enter the concentration reqs for all concentrations (including pre-reqs)

### Idea 2

#### Intern/Job Application Helper
##### Short Description: A Chrome plug-in and separate web app that auto-fills all common application questions for internships/jobs and provides data on chances for getting an internship/job.
##### Problem: 
- It is tedious to submit applications for internships and jobs, especially ones that require submitting the same answers to the same questions each time. This includes personal information about education to demographic information. This can discourage students from applying to opportunities that are available.
- Students also never know what their odds are of getting an internship or job. Many times, students apply for thousands but only hear back from a few dozen. If they knew where their application stands among other applicants who were successful, they would know how to improve their application and chances of receiving an interview and getting the offer. 
##### Solution:  
- Firstly, it solves the issue of time as the student would only need to upload their application information once on our web app which includes a resume copy and transcript copy and the answers to common application questions. 
- Secondly, students will be able to improve their application chances with our algorithm that compares successful applicants to theirs. 
##### Critical Features: 
1. The first aspect is the web app component where user will input the relevant information
- Name, University, Address, Previous Internships/Jobs, Extracurriculars, GPA, Courses
- Demographic Questions, Veteran question, Disability questions
- Upload resume, transcript, and cover letter, all of which can be then accessed by the plug-in to submit their application information. This would require proper storage of the applicants data. 
- Challenge: How do we want to store all this data, utilizing what data structure that can be easily accessible for the plug-in to work
2. The second aspect is the plug-in
- When a new application needs to be filled, the plugin will fill in all necessary fields. This would require knowing the tags of the form to properly submit. 
- Allows the user to only press the “next” or the final “submit” button for their application. 
- Challenge: This functionality is similar to Apple’s Auto-Fill which we plan to research and understand how it works to adapt it to our application helper. 
3. The third aspect is the internship/job chance scorer (most likely part of the web app)
- Will enable users to input all the places they have applied to, upload their resume, and output a score that represents the predicted likelihood of that user’s resume getting noticed and making it to the first round of the recruitment process. 
- Essentially, we plan on using web data and/or learning from data directly in the application to compare a user’s resume to other resumes of people who have gotten through, and then giving a score based on how similar the resumes are
- Challenge: What type of web data will be available to us, how will we differentiate between different types of job roles, how will the accuracy of the score be determined. 
##### Challenges and Questions for the Project: 
- What are the metrics for comparison to figure out chances of an applicant? 
- Classifying resumes by the applicant major/concentration? 
- How does Apple’s auto-fill work?
- How do we make a plug-in that could work on all browsers?
- What if a form tag is similar but not as expected? How can we deal with this issue? 


### Idea 3
We think our idea 2 can be split off into two ideas, depending since it may be difficult to take on the entire project with the two major components. 

**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 15)_

**4-Way Checkpoint:** _(Schedule for on or before April 5)_

**Adversary Checkpoint:** _(Schedule for on or before April 12 once you are assigned an adversary TA)_

## How to Build and Run
_A necessary part of any README!_
