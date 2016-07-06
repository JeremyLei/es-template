call rd %m2_home%\repository\com\gewara\gewara-api /q/s
call mvn eclipse:clean
call mvn eclipse:eclipse -DdownloadSources=true
@pause