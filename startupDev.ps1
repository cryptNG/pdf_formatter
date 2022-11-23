.\prepareClassPath.ps1
powershell java -cp target\pdf_formatter-1.4.0.jar;external_lib\* -Dloader.main=org.springframework.boot.loader.JarLauncher org.springframework.boot.loader.PropertiesLauncher
