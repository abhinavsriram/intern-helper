import os
import sys
import sqlite3
import pandas as pd

#for the messages printed in terminal
class bcolors:
    def __init__(self):
        pass

    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKCYAN = '\033[96m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


#script to combine all the csv files into the database as individual tables
def run_script():
    #storing all the suffixes
    suffix = ['a', 'bank', 'bd', 'bs', 'c', 'ca', 'camp', 'cc', 'cd', 'cnc', 'cn', 'corstrat', 'cre', 'da', 'de', 'ds',
              'dt', 'e', 'ei', 'eq', 'fi', 'ga', 'gd', 'gr', 'hr', 'hcc', 'hcr', 'hcra', 'ia', 'ib', 'la', 'l', 'lob',
              'm', 'mm', 'md', 'ne', 'o', 'oure', 'p', 'pe', 'ph', 'pm', 'po', 'pr', 'ps', 'qae', 'r', 'ra', 'red',
              'ro', 'repm', 'reo', 's', 'sa', 'sc', 'se', 'sm', 'ss', 'swe', 'sw', 'ta', 'tf', 'ux', 'wd', 'wm'];
    #expanding the name of each suffix
    suffix_expansion = {
      "a": "Accounting Intern",
      "bank": "Banking Intern",
      "bd": "Business Development Intern",
      "bs": "Brand Strategist Intern",
      "c": "Communications Intern",
      "ca": "Community Affairs Intern",
      "camp": "Campaign Intern",
      "cc": "Clinical Coordinator Intern",
      "cd": "Curriculum Development Intern",
      "cnc": "Content Creator Intern",
      "cn": "Consulting Intern",
      "corstrat": "Corporate Strategy Intern",
      "cre": "Commercial Real Estate Intern",
      "da": "Data Analysis Intern",
      "de": "Data Engineer Intern",
      "ds": "Data Science Intern",
      "dt": "Distribution Intern",
      "e": "Editorial Intern",
      "ei": "Education Intern",
      "eq": "Equities Intern",
      "fi": "Fixed Income Intern",
      "ga": "Government Affairs Intern",
      "gd": "Graphic Design Intern",
      "gr": "Government Relations Intern",
      "hr": "Human Resources Intern",
      "hcc": "Healthcare Consultant Intern",
      "hcr": "Healthcare Research Intern",
      "hcra": "Healthcare Administration Intern",
      "ia": "Investment Analyst Intern",
      "ib": "Investment Banking Intern",
      "la": "Lab Assistant Intern",
      "l": "Legal Intern",
      "lob": "Lobbying Intern",
      "m": "Marketing Intern",
      "mm": "Multimdeia Intern",
      "md": "Merchandising Intern",
      "ne": "Network Engineer Intern",
      "o": "Operations Intern",
      "oure": "Outreach Intern",
      "p": "Policy Intern",
      "pe": "Public Engagement Intern",
      "ph": "Pharmacy Intern",
      "pm": "Product Management Intern",
      "po": "Political Operations Intern",
      "pr": "Public Relations Intern",
      "ps": "Property Strategy Intern",
      "qae": "Quality Assurance Engineer Intern",
      "r": "Research Intern",
      "ra": "Risk Advisory Intern",
      "red": "Real Estate Development Intern",
      "ro": "Retail Operations Intern",
      "repm": "Real Estate Property Management Intern",
      "reo": "Real Estate Operations Intern",
      "s": "Security Intern",
      "sa": "Sales Associate Intern",
      "sc": "Store Clerk Intern",
      "se": "Systems Engineer Intern",
      "sm": "Social Media Intern",
      "ss": "Support Specialist Intern",
      "swe": "Software Engineering Intern",
      "sw": "Social Work Intern",
      "ta": "Teaching Assistant Intern",
      "tf": "Teaching Fellow Intern",
      "ux": "UI/UX Intern",
      "wd": "Web Developer Intern",
      "wm": "Wealth Management Intern"
    }
    missing_counter = 0
    #loop through each suffix
    for s in suffix:
        #find csv with suffix and see if exists
        csvname = 'combined_' + s + '.csv'
        if os.path.exists(csvname):
            continue
        #error check: print if missing a csv file
        else:
            missing_counter += 1
            print(f"{bcolors.WARNING}MISSING FILE: {csvname} does not exist{bcolors.ENDC}")
    #if missing csv file, exit script
    if missing_counter != 0:
        print(f"{bcolors.FAIL}ERROR: missing {missing_counter} required files{bcolors.ENDC}")
        print(f"{bcolors.FAIL}SCRIPT ABORTED: entire script aborted{bcolors.ENDC}")
        sys.exit(0)
    #check if sqlite database exists and if so, can remove
    if os.path.exists('internships.sqlite3'):
        text = input(f"{bcolors.OKCYAN}FILE EXISTS: the file internships.sqlite3 already exists, do you wish to delete the file? y/N \n{bcolors.ENDC}")
        if text == "y":
            #removing database
            os.remove('internships.sqlite3')
            print(f"{bcolors.WARNING}DELETE: deleting internships.sqlite3{bcolors.ENDC}")
            # throws error if file already exists
            try:
                #creating a new database
                open('internships.sqlite3', 'x')
                # load data and loop suffixes
                for s in suffix:
                    csvname = 'combined_' + s + '.csv'
                    data = pd.read_csv(csvname)
                    # strip whitespace from headers
                    data.columns = data.columns.str.strip()
                    # establish connection to database
                    conn = sqlite3.connect('internships.sqlite3')
                    # drop data into database
                    print(f"{bcolors.OKCYAN}INSERTING: adding {suffix_expansion[s]} listings to database{bcolors.ENDC}")
                    data.to_sql(suffix_expansion[s], conn)
                    # close database connection
                conn.close()
                print(f"{bcolors.OKGREEN}SUCCESS: created sqlite3 database named internships.sqlite3{bcolors.ENDC}")
            except:
                print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")
        #error checking: if select no then do not continue script
        elif text == "N":
            print(f"{bcolors.FAIL}ABORTING PROCESS: script aborted{bcolors.ENDC}")
        #error checking
        else:
            print(f"{bcolors.FAIL}ERROR: illegal input{bcolors.ENDC}")
            print(f"{bcolors.FAIL}SCRIPT ABORTED: entire script aborted{bcolors.ENDC}")
            sys.exit(0)    
    else:
        # throws error if file already exists
        try:
            open('internships.sqlite3', 'x')
            # load data
            for s in suffix:
                csvname = 'combined_' + s + '.csv'
                data = pd.read_csv(csvname)
                # strip whitespace from headers
                data.columns = data.columns.str.strip()
                # establish connection to database
                conn = sqlite3.connect('internships.sqlite3')
                # drop data into database
                print(f"{bcolors.OKCYAN}INSERTING: adding {suffix_expansion[s]} listings to database{bcolors.ENDC}")
                data.to_sql(suffix_expansion[s], conn)
                # close database connection
            conn.close()
            print(f"{bcolors.OKGREEN}SUCCESS: created sqlite3 database named internships.sqlite3{bcolors.ENDC}")
        #error checking
        except:
            print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")


if __name__ == '__main__':
    #running the script
    try:
        run_script()
    except KeyboardInterrupt:
        print(f"\n{bcolors.FAIL}ERROR: KeyboardInterrupt{bcolors.ENDC}")
        print(f"{bcolors.FAIL}SCRIPT ABORTED: entire script aborted{bcolors.ENDC}")
        sys.exit(0)
