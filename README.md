# Skeleton_Crossword_Project in Java

Capstone Project

Being refactored into a maven project.

Notes:

- Program uses Apache poi api and JSON api.
- Program uses https://indic-wp.thisisjava.com/api/getLogicalChars.php?string= API call. If the website is down, the program will not run correctly.
- Prefences/Settings that require attention before running the program:

WORD_LIST_FILE_NAME specifies which word list you are using on your computer.
PUZZLE_FILE_NAME will determine what the output filename will be.
UNUSED_WORDS_FILENAME specifies the filename for the unused words.
OUTPUT_FOLDER_LOCATION is the location of where the output files will be written to.
WORD_LIST_FOLDER_LOCATION is the location of where the word list is contained.
Note: PROJECT_DIRECTORY is used so that all the files are maintained inside the project folder.

PROJECT_DIRECTORY is the path of the running project. For example, if WORD_LIST_FOLDER_LOCATION = directory +"\docs\"; then the full path is Workspace\ICS499-Skeleton_Project\docs
