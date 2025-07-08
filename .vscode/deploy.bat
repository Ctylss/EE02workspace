@echo off
echo [1/3] 清除與打包專案...
call mvn clean package -DskipTests

echo [2/3] 複製 war 到 Tomcat...
xcopy /Y /Q target\mywebapp.war "D:\Download\importnant\apache-tomcat-10.1.42\webapps\"

echo [3/3] 已部署，等待 Tomcat 自動載入
