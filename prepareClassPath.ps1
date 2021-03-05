WRITE-HOST $env:userprofile\.m2\repository
if (-not (Test-Path env:MAVEN_HOME))
 { 
 WRITE-HOST "MAVEN_HOME DID NOT EXIST, CREATING AT " $env:userprofile\.m2
 [System.Environment]::SetEnvironmentVariable('MAVEN_HOME',$env:userprofile+'\.m2',[System.EnvironmentVariableTarget]::Machine)
 $env:MAVEN_HOME = [System.Environment]::GetEnvironmentVariable("MAVEN_HOME","Machine")
 WRITE-HOST SET $env:MAVEN_HOME\repository
 }
 else
 {
 WRITE-HOST MAVEN REPO PATH: $env:MAVEN_HOME\repository
 }


$classPath = ""
$folders = (Get-ChildItem -Directory -Recurse $env:MAVEN_HOME\repository).FullName
WRITE-HOST $folders
foreach ($folder in $folders)
{
    $fileBaseNames = (Get-ChildItem $folder\*.jar).FullName
    if(![string]::IsNullOrEmpty($fileBaseNames))
    {
  $classPath = $classPath + ";" + $fileBaseNames
    }
	WRITE-HOST $fileBaseNames

}
	WRITE-HOST $classPath

[System.Environment]::SetEnvironmentVariable('buildClassPath',$classPath,[System.EnvironmentVariableTarget]::Machine)