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
    print(f"{bcolors.OKCYAN}EXECUTING: npm start as bg process{bcolors.ENDC}")
    print(f"{bcolors.WARNING}CHANGE DIRECTORY: os.chdir('./frontend'): in frontend directory{bcolors.ENDC}")
    os.chdir('./frontend')
    os.system('npm start &')
    print(f"{bcolors.WARNING}CHANGE DIRECTORY: os.chdir('../'): in project root directory{bcolors.ENDC}")
    os.chdir('../')
    print(f"{bcolors.OKCYAN}EXECUTING: mvn package{bcolors.ENDC}")
    os.system('mvn package')
    print(f"{bcolors.OKCYAN}EXECUTING: ./run{bcolors.ENDC}")
    os.system('./run --gui')
    print(f"\n{bcolors.FAIL}TERMINATED: all processes terminated{bcolors.ENDC}")

run_script()
