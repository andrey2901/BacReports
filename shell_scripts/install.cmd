call settings.cmd

md %BAC_REPORT%
md %REPORTS_FOLDER%
md %DERBY_FOLDER%
md %BACKUP_FOLDER%

java -Dderby.system.home=%DERBY_FOLDER% -Dlocal.fonts=%FONT_FOLDER% -Dreport.folder=%REPORTS_FOLDER% -cp BacReports.jar ua.com.hedgehogsoft.bacreports.BacReports --install