import shutil
import os

try:
    cwd = os.getcwd()
    original = cwd + "/node_modules"
    target = cwd + "/frontend"
    shutil.move(original,target)
    input("prompt: ")
except:
    input("prompt: ")
