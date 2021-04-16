import os

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

#Master script to run all the python files
def run_script():
    #storing all the suffixes
    suffix = ['a', 'bank', 'bd', 'bs', 'c', 'ca', 'camp', 'cc', 'cd', 'cnc', 'cn', 'corstrat', 'cre', 'da', 'de', 'ds',
              'dt', 'e', 'ei', 'eq', 'fi', 'ga', 'gd', 'gr', 'hr', 'hcc', 'hcr', 'hcra', 'ia', 'ib', 'la', 'l', 'lob',
              'm', 'mm', 'md', 'ne', 'o', 'oure', 'p', 'pe', 'ph', 'pm', 'po', 'pr', 'ps', 'qae', 'r', 'ra', 'red',
              'ro', 'repm', 'reo', 's', 'sa', 'sc', 'se', 'sm', 'ss', 'swe', 'sw', 'ta', 'tf', 'ux', 'wd', 'wm'];
    #run all linkedin files by individual suffix
    for s in suffix:
        filename = 'linkedin_' + s + '.py'
        print(f"{bcolors.HEADER}----------------------------EXECUTING: running {filename}----------------------------{bcolors.ENDC}")
        os.system('python3 ' + filename)
    #run all google files by individual suffix
    for g in suffix:
        filename = 'google_' + g + '.py'
        print(f"{bcolors.HEADER}----------------------------EXECUTING: running {filename}----------------------------{bcolors.ENDC}")
        os.system('python3 ' + filename)
    #run the python scripts to combine all to csv and then to sqlite
    os.system('python3 combine_all_csv.py')
    os.system('python3 csv_to_sqlite.py')

if __name__ == '__main__':
    #running the script
    try:
        run_script()
    except KeyboardInterrupt:
        print(f"\n{bcolors.FAIL}ERROR: KeyboardInterrupt{bcolors.ENDC}")
        print(f"{bcolors.FAIL}SCRIPT ABORTED: entire script aborted{bcolors.ENDC}")
        sys.exit(0)
