call settings.cmd

java -Dderby.system.home=%DERBY_FOLDER% -Dlocal.fonts=%FONT_FOLDER% -Dreport.folder=%REPORTS_FOLDER% -cp BacReports.jar ua.com.hedgehogsoft.bacreports.BacReports