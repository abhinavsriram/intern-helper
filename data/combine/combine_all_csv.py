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
    suffix = ['a', 'c', 'cn', 'da', 'de', 'ds', 'e', 'gd', 'hr', 'ia', 'l', 'm', 'mm', 'o', 'p', 'ph', 'pm', 'pr', 'r', 'sm', 'swe', 'ux'];
    for s in suffix:
        filename = 'combined_' + s + '.py'
        csvname = 'combined_' + s + '.csv'
        #print(f"{bcolors.HEADER}EXECUTING: running " + filename + "{bcolors.ENDC}")
        if os.path.exists(csvname):
            text = input(f"{bcolors.OKCYAN}FILE EXISTS: the file combined.csv already exists, do you wish to delete the file? y/N \n{bcolors.ENDC}")
            if text == "y":
                os.remove(csvname)
                print(f"{bcolors.WARNING}DELETE: deleting combined.csv{bcolors.ENDC}")
                try:
                    # get working directory
                    os.getcwd()
                    # find all csv files in the folder
                    # use glob pattern matching -> extension = 'csv'
                    # save result in list -> all_filenames
                    extension = '_' + s + '.csv'
                    all_filenames = [i for i in glob.glob('*{}'.format(extension))]
                    # combine all files in the list
                    csvname_csv = pd.concat([pd.read_csv(f) for f in all_filenames])
                    # export to csv
                    csvname_csv.to_csv(csvname, index=False, encoding='utf-8-sig')
                    print(f"{bcolors.OKGREEN}SUCCESS: created combined csv file {bcolors.ENDC}")
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
                all_filenames = [i for i in glob.glob('*_' + s + '.{}'.format(extension))]
                # combine all files in the list
                csvname_csv = pd.concat([pd.read_csv(f) for f in all_filenames])
                # export to csv
                csvname_csv.to_csv(csvname, index=False, encoding='utf-8-sig')
                print(f"{bcolors.OKGREEN}SUCCESS: created combined csv file{bcolors.ENDC}")
            except:
                print(f"{bcolors.FAIL}ERROR: something went wrong, please try again{bcolors.ENDC}")

run_script()
