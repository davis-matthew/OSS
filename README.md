# Open Source Spellchecker (OSS)
An Open Source Code Comment Spellchecker 

### Installation:
To install oss...
### How to Use
to Use OSS...
From github, download OSS, and run.
Main Menu displays 4 buttons:
    1. Download Github Repos
    2. Generate Comments File
    3. 
    
I.	Test “Download GitHub Repo” 		
A.	Run OSS from Eclipse
B.	Click on “Download GitHub Repo” button.
C.	Enter a github url: https://github.com/exeloar/OSSTest.git
i.	If repo does not exist, a repo will be downloaded. Verify repo 		
was created in user’s repos directory (i.e. C:\Users\Janet\eclipse-workspace\OSS/repos/OSSTest).

ii.	If the repo already exists, a warning message will appear “Repo Already Exists” Select option to proceed:
a.	Select “Cancel” – Program returns to OSS Main Menu. 
b.	Select “git reset-hard (restore to what is on github)” 
A warning message appears: “Directory may be overwritten. Are you sure?”								
Select “No” – program returns to OSS Main Menu.
Select “Yes” – the repo will be downloaded into the user’s repos directory.(i.e.  C:\Users\Janet\eclipse-workspace\OSS/repos/).
(Verify the repos folder has the current date/time.)

c.	Select “git pull-pull changes and keep other edits”: 

A warning message appears: “Directory may be overwritten. Are you sure?”								
Select “No” – program returns to OSS Main Menu.
Select “Yes” – the repo will be downloaded into the user’s repos directory.(i.e.  C:\Users\Janet\eclipse-workspace\OSS/repos/).
(Verify the repos folder has the current date/time.)


II.	Test “Analyze/Generate Comments”

A.	Run OSS from Eclipse
B.	Click on “Analyze/Generate Comments” button.
Click “Cancel” – programs returns to OSS Main Menu.
C.	Click on “Analyze/Generate Comments” button. 
Verify a comment file (i.e. OSSTest_comments.txt) has been generated with latest date/time. 
D.	Click on “Analyze/Generate Comments” button. 
If a comment  file already exists, a warning message should appear:
“The Comment file <filename> already exists. Regenerating will delete it.”
Click Cancel button. User should return to main menu.
Click OK button.  A new comments file is generated.
Verify the new comment file (i.e. OSSTest_comments.txt) has been generated with latest date/time. 


III.	Test “Apply Spelling Corrections”

A.	Run OSS from Eclipse
B.	Click on “Apply Spelling Corrections” button.
A list of existing repositories is displayed.

Click “Cancel” button – program returns to OSS Main Menu.
C.	Click on “Apply Spelling Corrections” button.
Select a repository from the selection list.
Click OK.
The OSS tool will generate files with all the corrected comments from the repository.

D.	Delete the Comments file created in Step II. 
Click on “Apply Spelling Corrections” button.
Select a repository from the selection list.
Click OK.
An error message should appear  “Comments file is missing, please use generate comments file button “ to generate a comments file. Click ”OK”, and repeat Step II. 


E.	The developer signs on to the powershell and does a  “git-push” to push the the corrected files back to the github respository. The corrected files will now reside in github. 





IV.	Test “Exit OSS”
A.	Run OSS from Eclipse
B.	Click on “Exit OSS” button.
OSS Main Menu disappears.




### Contact Us
to contact us...
