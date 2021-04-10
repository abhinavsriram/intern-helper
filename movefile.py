import shutil
import os

cwd = os.getcwd()
original = cwd + "/node_modules"
target = cwd + "/frontend"

shutil.move(original,target)
