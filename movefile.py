import shutil
import os

try:
    cwd = os.getcwd()
    original = cwd + "/node_modules"
    target = cwd + "/frontend"
    shutil.move(original,target)
except:
    print("oops")
