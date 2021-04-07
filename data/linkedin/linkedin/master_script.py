import os

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
        filename = 'linkedin_' + s + '.py'
        print(f"{bcolors.HEADER}EXECUTING: running {filename}{bcolors.ENDC}")
        os.system('python3 ' + filename)
    os.system('python3 combine_csv.py')
    os.system('python3 csv_to_sqlite.py')
    # for s in suffix:
    #     filename = 'linkedin_' + s + '.csv'
    #     os.remove(filename)

run_script()
