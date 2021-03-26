# cs0320 Term Project 2021

## Team Members:
- Abhinav (asrira11, abhinavsriram)
- Kunj (kpadh, kunjpadh)
- Meera (mkurup1, meera-kurup)
- Riya (rdulepet, riya-dulepet)

## Team Strengths and Weaknesses:

### Abhinav:
##### Strengths:
- Prior experience with product lifecycle - user segmentation, interviews, surveys; use cases/pain points/needs; product spec sheet etc.
- Prior experience with front-end dev using React.js and development of UI wireframes/prototypes through human centered design process
##### Weaknesses:
- Dynamic and functional programming
- Algorithmic design skills
- Familiarity with AI/ML

### Kunj:
##### Strengths:
- Logic (back-end)
- Flexible to work on different project aspects
- Math
- Familiarity with AI & ML principles
##### Weaknesses:
- Design (front-end)
- Dynamic programming
- Complex Algorithms (to a degree)

### Meera:
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

### Riya:
##### Strengths:
- Very flexible
- Will take the time to learn new concepts
- Algorithmic design
- Highly organized
- Intermediate python skills
##### Weaknesses:
- Testing
- Front-end

## Project Idea:

#### Intern/Job Application Helper
##### Short Description:
A web app that auto-fills all common application questions for internships/jobs and provides data on chances for getting an internship/job.
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

**HTA Approval (dpark20):** Approved. Make sure there is good depth to the algorithms! Be sure to make your project different from what has already been done. Good ideas! Looking forward to seeing what you make.

**Mentor TA: Kanza Azhar**

## Meetings

**Specs, Mockup, and Design Meeting: Completed**

**4-Way Checkpoint:** _(Schedule for on or before April 5)_

**Adversary Checkpoint:** _(Schedule for on or before April 12 once you are assigned an adversary TA)_

## How to Build and Run
