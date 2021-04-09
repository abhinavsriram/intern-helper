#!/usr/bin/env python3

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
    print(f"{bcolors.WARNING}CHANGE DIRECTORY: os.chdir('./frontend'): in frontend directory{bcolors.ENDC}")
    os.chdir('./frontend')
    # https://stackoverflow.com/questions/1196074/how-to-start-a-background-process-in-python
    # os.spawnl(os.P_DETACH, 'npm start')
    # os.spawnl(os.P_NOWAIT, 'npm start')
    # runs npm start as bg process - may only work on unix based systems
    print(f"{bcolors.OKCYAN}EXECUTING: yarn install{bcolors.ENDC}")
    os.system('yarn install')
    print(f"{bcolors.OKCYAN}EXECUTING: yarn start as bg process{bcolors.ENDC}")
    os.system('yarn start &')
    # print(f"{bcolors.WARNING}CHANGE DIRECTORY: os.chdir('../'): in project root directory{bcolors.ENDC}")
    # os.chdir('../')
    # print(f"{bcolors.OKCYAN}EXECUTING: mvn package{bcolors.ENDC}")
    # os.system('mvn package')
    # print(f"{bcolors.OKCYAN}EXECUTING: ./run{bcolors.ENDC}")
    # os.system('./run')
    print(f"\n{bcolors.FAIL}TERMINATED: all processes terminated{bcolors.ENDC}")

# def kill_processes():
#     subprocess = subprocess.Popen(['ps', '-A'], stdout=subprocess.PIPE)
#     output, error = subprocess.communicate()
#     print(output)
#     target_process = "python"
#     for line in output.splitlines():
#         if target_process in str(line):
#             pid = int(line.split(None, 1)[0])
#             os.kill(pid, 9)

run_script()
