import os
import sys
import glob
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
    print(f"{bcolors.HEADER}EXECUTING: running combine_csv.py{bcolors.ENDC}")
    if os.path.exists("linkedin_combined.csv"):
        text = input(f"{bcolors.OKCYAN}FILE EXISTS: the file linkedin_combined.csv already exists, do you wish to delete the file? y/N \n{bcolors.ENDC}")
        if text == "y":
            os.remove("linkedin_combined.csv")
            print(f"{bcolors.WARNING}DELETE: deleting linkedin_combined.csv{bcolors.ENDC}")
            try:
                # get working directory
                os.getcwd()
                # find all csv files in the folder
                # use glob pattern matching -> extension = 'csv'
                # save result in list -> all_filenames
                extension = 'csv'
                all_filenames = [i for i in glob.glob('*.{}'.format(extension))]
                # combine all files in the list
                combined_csv = pd.concat([pd.read_csv(f) for f in all_filenames])
                # export to csv
                combined_csv.to_csv("linkedin_combined.csv", index=False, encoding='utf-8-sig')
                print(f"{bcolors.OKGREEN}SUCCESS: created combined csv file named linkedin_combined.csv{bcolors.ENDC}")
            except:
                print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")
        elif text == "N":
            print(f"{bcolors.FAIL}ABORTING PROCESS: script aborted{bcolors.ENDC}")
    else:
        try:
            # get working directory
            os.getcwd()
            # find all csv files in the folder
            # use glob pattern matching -> extension = 'csv'
            # save result in list -> all_filenames
            extension = 'csv'
            all_filenames = [i for i in glob.glob('*.{}'.format(extension))]
            # combine all files in the list
            combined_csv = pd.concat([pd.read_csv(f) for f in all_filenames])
            # export to csv
            combined_csv.to_csv("linkedin_combined.csv", index=False, encoding='utf-8-sig')
            print(f"{bcolors.OKGREEN}SUCCESS: created combined csv file named linkedin_combined.csv{bcolors.ENDC}")
        except:
            print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")

run_script()
