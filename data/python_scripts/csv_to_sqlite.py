import os
import sys
import sqlite3
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


def run_script():
    suffix = ['a', 'bank', 'bd', 'bs', 'c', 'ca', 'camp', 'cc', 'cd', 'cnc', 'cn', 'corstrat', 'cre', 'da', 'de', 'ds',
              'dt', 'e', 'ei', 'eq', 'fi', 'ga', 'gd', 'gr', 'hr', 'hcc', 'hcr', 'hcra', 'ib', 'ia', 'la', 'l', 'lob',
              'm', 'mm', 'md', 'ne', 'o', 'oure', 'p', 'pe', 'ph', 'pm', 'po', 'pr', 'ps', 'qae', 'r', 'ra', 'red',
              'ro', 'repm', 'reo', 's', 'se', 'sa', 'sc', 'se', 'sm', 'ss', 'swe', 'sw', 'ta', 'tf', 'ux', 'wd', 'wm'];
    suffix_expansion = {
      "hcr": "Healthcare Research Intern",
      "hcra": "Healthcare Administration Intern",
      "la": "Lab Assistant Intern",
      "hcc": "Healthcare Consultant Intern",
      "cc": "Clinical Coordinator Intern",
      "r": "Research Intern",
      "ph": "Pharmacy Intern",
      "ss": "Support Specialist Intern",
      "ds": "Data Science Intern",
      "ux": "UI/UX Intern",
      "swe": "Software Engineering Intern",
      "wd": "Web Developer Intern",
      "qae": "Quality Assurance Engineer Intern",
      "de": "Data Engineer Intern",
      "ne": "Network Engineer Intern",
      "se": "Systems Engineer Intern",
      "pm": "Product Management Intern",
      "s": "Security Intern",
      "da": "Data Analysis Intern",
      "reo": "Real Estate Operations Intern",
      "repm": "Real Estate Property Management Intern",
      "cre": "Commercial Real Estate Intern",
      "ps": "Property Strategy Intern",
      "red": "Real Estate Development Intern",
      "md": "Merchandising Intern",
      "c": "Communications Intern",
      "m": "Marketing Intern",
      "sc": "Store Clerk Intern",
      "dt": "Distribution Intern",
      "sa": "Sales Associate Intern",
      "ro": "Retail Operations Intern",
      "cnc": "Content Creator Intern",
      "bs": "Brand Strategist Intern",
      "mm": "Multimdeia Intern",
      "sm": "Social Media Intern",
      "gd": "Graphic Design Intern",
      "tf": "Teaching Fellow Intern",
      "ta": "Teaching Assistant Intern",
      "cd": "Curriculum Development Intern",
      "ei": "Education Intern",
      "sw": "Social Work Intern",
      "ga": "Government Affairs Intern",
      "gr": "Government Relations Intern",
      "ca": "Community Affairs Intern",
      "pe": "Public Engagement Intern",
      "oure": "Outreach Intern",
      "lob": "Lobbying Intern",
      "camp": "Campaign Intern",
      "po": "Political Operations Intern",
      "e": "Editorial Intern",
      "l": "Legal Intern",
      "p": "Policy Intern",
      "fi": "Fixed Income Intern",
      "eq": "Equities Intern",
      "a": "Accounting Intern",
      "ib": "Investment Banking Intern",
      "ra": "Risk Advisory Intern",
      "wm": "Wealth Management Intern",
      "bank": "Banking Intern",
      "ia": "Investment Analyst Intern",
      "corstrat": "Corporate Strategy Intern",
      "bd": "Business Development Intern",
      "cn": "Consulting Intern",
      "o": "Operations Intern",
      "pr": "Public Relations Intern",
      "hr": "Human Resources Intern"
    }
    missing_counter = 0
    for s in suffix:
        csvname = 'combined_' + s + '.csv'
        if os.path.exists(csvname):
            continue
        else:
            missing_counter += 1
            print(f"{bcolors.WARNING}MISSING FILE: {csvname} does not exist{bcolors.ENDC}")
    if missing_counter != 0:
        print(f"{bcolors.FAIL}ERROR: missing {missing_counter} required files{bcolors.ENDC}")
        print(f"{bcolors.FAIL}SCRIPT ABORTED: entire script aborted{bcolors.ENDC}")
        sys.exit(0)
    if os.path.exists('internships.sqlite3'):
        text = input(f"{bcolors.OKCYAN}FILE EXISTS: the file internships.sqlite3 already exists, do you wish to delete the file? y/N \n{bcolors.ENDC}")
        if text == "y":
            os.remove('internships.sqlite3')
            print(f"{bcolors.WARNING}DELETE: deleting internships.sqlite3{bcolors.ENDC}")
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
                    text = input(f"{bcolors.OKCYAN}INSERTING: adding {suffix_expansion[s]} listings to database{bcolors.ENDC}")
                    data.to_sql(suffix_expansion[s], conn)
                    # close database connection
                conn.close()
                print(f"{bcolors.OKGREEN}SUCCESS: created sqlite3 database named internships.sqlite3{bcolors.ENDC}")
            except:
                print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")
        elif text == "N":
            print(f"{bcolors.FAIL}ABORTING PROCESS: script aborted{bcolors.ENDC}")
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
                text = input(f"{bcolors.OKCYAN}INSERTING: adding {suffix_expansion[s]} listings to database{bcolors.ENDC}")
                data.to_sql(suffix_expansion[s], conn)
                # close database connection
            conn.close()
            print(f"{bcolors.OKGREEN}SUCCESS: created sqlite3 database named internships.sqlite3{bcolors.ENDC}")
        except:
            print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")


if __name__ == '__main__':
    try:
        run_script()
    except KeyboardInterrupt:
        print(f"\n{bcolors.FAIL}ERROR: KeyboardInterrupt{bcolors.ENDC}")
        print(f"{bcolors.FAIL}SCRIPT ABORTED: entire script aborted{bcolors.ENDC}")
        sys.exit(0)
