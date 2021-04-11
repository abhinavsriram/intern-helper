import os
import sys
import sqlite3
import pandas as pd

class bcolors:
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
    print(f"{bcolors.HEADER}EXECUTING: running csv_to_sqlite.py{bcolors.ENDC}")
    if os.path.exists("combined.csv"):
        if os.path.exists("combined.sqlite3"):
            text = input(f"{bcolors.OKCYAN}FILE EXISTS: the file combined.sqlite3 already exists, do you wish to delete the file? y/N \n{bcolors.ENDC}")
            if text == "y":
                os.remove("combined.sqlite3")
                print(f"{bcolors.WARNING}DELETE: deleting combined.sqlite3{bcolors.ENDC}")
                # throws error if file already exists
                try:
                    open('combined.sqlite3', 'x')
                    # load data
                    data = pd.read_csv('combined.csv')
                    # strip whitespace from headers
                    data.columns = data.columns.str.strip()
                    # establish connection to database
                    conn = sqlite3.connect("combined.sqlite3")
                    # drop data into database
                    data.to_sql("data", conn)
                    # close database connection
                    conn.close()
                    print(f"{bcolors.OKGREEN}SUCCESS: created sqlite3 file named combined.sqlite3{bcolors.ENDC}")
                except:
                    print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")
            elif text == "N":
                print(f"{bcolors.FAIL}ABORTING PROCESS: script aborted{bcolors.ENDC}")
        else:
            # throws error if file already exists
            try:
                open('combined.sqlite3', 'x')
                # load data
                data = pd.read_csv('combined.csv')
                # strip whitespace from headers
                data.columns = data.columns.str.strip()
                # establish connection to database
                conn = sqlite3.connect("combined.sqlite3")
                # drop data into database
                data.to_sql("data", conn)
                # close database connection
                conn.close()
                print(f"{bcolors.OKGREEN}SUCCESS: created sqlite3 file named combined.sqlite3{bcolors.ENDC}")
            except:
                print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")
    else:
        print(f"{bcolors.FAIL}ERROR: first run combine_csv.py to create combined.csv{bcolors.ENDC}")
        print(f"{bcolors.FAIL}ABORTING PROCESS: script aborted{bcolors.ENDC}")

run_script()
