import os
import sys
import glob
import pandas as pd


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

#script to combine all the csv files for a specific internship position
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
      "mm": "Multimedia Intern",
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
    #loop through all data/suffixes
    for s in suffix:
        #create name for combiend csv file
        csvname = 'combined_' + s + '.csv'
        text = ""
        print(f"{bcolors.HEADER}----------------------------RUNNING: creating {csvname}----------------------------{bcolors.ENDC}")
        #check if the file exists
        if os.path.exists(csvname):
            text = input(
                f"{bcolors.OKCYAN}FILE EXISTS: the file {csvname} already exists, do you wish to delete the file? y/N \n{bcolors.ENDC}")
            #if want to delete, then remove file 
            if text == "y":
                os.remove(csvname)
                print(f"{bcolors.WARNING}DELETE: deleting {csvname}{bcolors.ENDC}")
                #now updating new csv combined file
                try:
                    # get working directory
                    os.getcwd()
                    # find all csv files in the folder of format google_s.csv and linkedin_s.csv
                    # use glob pattern matching -> extension = 'csv'
                    # save result in list -> all_filenames
                    extension = 'csv'
                    all_filenames = [i for i in glob.glob('*_' + s + '.{}'.format(extension))]
                    if len(all_filenames) != 2:
                        #getting the google and linkedin files
                        google = 'google_' + s + '.csv'
                        linkedin = 'linkedin_' + s + '.csv'
                        #check if both google and linkedin files exist
                        if os.path.exists(google):
                            continue
                        else:
                            print(f"{bcolors.FAIL}ERROR: {google} does not exist{bcolors.ENDC}")
                        if os.path.exists(linkedin):
                            continue
                        else:
                            print(f"{bcolors.FAIL}ERROR: {linkedin} does not exist{bcolors.ENDC}")
                        #if there are more than 2 files, there are too many and fail running
                        if len(all_filenames) > 2:
                            print(f"{bcolors.FAIL}ERROR: too many csv files exist for {suffix_expansion[s]}{bcolors.ENDC}")
                    #if both files exist, now combine
                    else:
                        # combine all files in the list
                        csvname_csv = pd.concat([pd.read_csv(f) for f in all_filenames])
                        # export to csv
                        csvname_csv.to_csv(csvname, index=False, encoding='utf-8-sig')
                        print(f"{bcolors.OKGREEN}SUCCESS: created {csvname}{bcolors.ENDC}")
                #error checking
                except:
                    print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")
            #error checking: if select no then do not continue script
            elif text == "N":
                print(f"{bcolors.FAIL}SCRIPT ABORTED: script aborted for {csvname}{bcolors.ENDC}")
            #error checking
            else:
                print(f"{bcolors.FAIL}ERROR: illegal input{bcolors.ENDC}")
                print(f"{bcolors.FAIL}SCRIPT ABORTED: entire script aborted{bcolors.ENDC}")
                sys.exit(0)
        #when a combined file does not exist
        else:
            if text == "":
                try:
                    # get working directory
                    os.getcwd()
                    # find all csv files in the folder of format google_s.csv and linkedin_s.csv
                    # use glob pattern matching -> extension = 'csv'
                    # save result in list -> all_filenames
                    extension = 'csv'
                    all_filenames = [i for i in glob.glob('*_' + s + '.{}'.format(extension))]
                    if len(all_filenames) != 2:
                        # error checking both google and linkedin csv files(same as above)
                        google = 'google_' + s + '.csv'
                        linkedin = 'linkedin_' + s + '.csv'
                        if os.path.exists(google):
                            continue
                        else:
                            print(f"{bcolors.FAIL}ERROR: {google} does not exist{bcolors.ENDC}")
                        if os.path.exists(linkedin):
                            continue
                        else:
                            print(f"{bcolors.FAIL}ERROR: {linkedin} does not exist{bcolors.ENDC}")
                        if len(all_filenames) > 2:
                            print(f"{bcolors.FAIL}ERROR: too many csv files exist{bcolors.ENDC}")
                    #if both files exist, now combine
                    else:
                        # combine all files in the list
                        csvname_csv = pd.concat([pd.read_csv(f) for f in all_filenames])
                        # export to csv
                        csvname_csv.to_csv(csvname, index=False, encoding='utf-8-sig')
                        print(f"{bcolors.OKGREEN}SUCCESS: created {csvname}{bcolors.ENDC}")
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
