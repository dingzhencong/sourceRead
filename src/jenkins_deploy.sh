#!/bin/sh
DATE=`date +%Y%m%d`
BAK_DATE=`date +%Y%m%d-%H%M%S`
DATE2=`date +%Y-%m-%d`
USER=`whoami`

# 环境变量
MODE=SIT2

TOMCAT_HOME="/usr/local/apache-tomcat-9.0.31"
APP_HOME=/home/obs/app
SRC_HOME=/home/obs/src/obs
LOG_HOME=/home/obs/logs
INSTAL_PATH=""
LIB_FROM_PATH=/home/obs/app/
LIB_TO_PATH=""
APP_NAME=""
RUN_JARNAME=""
LOGFILENAME=""

config() {
    resolveName $1
    resolveToPath $1
    resolveRunName $1
    resolveLogPath $1
}

# 服务名
resolveName() {
    APP_NAME=`echo $1 | sed 's/1/mca/g;s/2/mcm/g;s/3/eweb/g;s/4/mweb/g;s/5/router/g;s/6/access/g;s/7/esbaccess/g;s/8/batch/g;'`
}

# jar所在的绝对路径位置 /home/obs/app/mca/
resolveRunName() {
    RUN_JARNAME=`echo $1 | sed 's/1/mca-bootstrap-1.0.1-SNAPSHOT.jar/g;s/2/mcm-bootstrap-1.0.1-SNAPSHOT.jar/g;s/5/router-bootstrap-1.0.1-SNAPSHOT.jar/g;s/8/batch/g;'`
    RUN_JARNAME="$LIB_TO_PATH/lib/$RUN_JARNAME"
}

# 服务器jar所在的目录 /home/obs/app/mca 
resolveToPath() {
    LIB_FROM_PATH=$APP_HOME"/"$APP_NAME"/upload/lib/"

    if [[ $(isWebApp) = "0" ]]; then
        LIB_TO_PATH=`echo $1 | sed 's/1/mca/g;s/2/mcm/g;s/5/router/g;s/8/batch/g;'`
        LIB_TO_PATH=$APP_HOME"/"$LIB_TO_PATH
    else
	LIB_TO_PATH=$TOMCAT_HOME"/webapps"
        LIB_FROM_PATH=$APP_HOME"/"$APP_NAME"/upload/*.war"

    fi
}

resolveLogPath() {
    LOGFILENAME="$LOG_HOME/$APP_NAME/$DATE2"
    fileName=`ls -l $LOGFILENAME | tail -1 | awk '{print $NF}'`
    LOGFILENAME="$LOGFILENAME/$fileName"
    echo "-----$LOGFILENAME"
}

backup() {
    printm "备份: $LIB_TO_PATH"
    cd $LIB_TO_PATH
    if [[ $(isWebApp) = "0" ]]; then
        if [ ! -d "$LIB_TO_PATH/lib" ]; then return 0; fi
        printInfo "备份: $LIB_TO_PATH/lib >>> $LIB_TO_PATH/lib.$DATE.tar"
        tar -cvf "lib.$DATE.tar" lib > /dev/null 2>&1
        rm -rf lib
    else
        if [ ! -f "$LIB_TO_PATH/$APP_NAME.war" ]; then return 0; fi
        printInfo "备份: $LIB_TO_PATH/$APP_NAME.war >>> $LIB_TO_PATH/bak/$APP_NAME.war$DATE"
        if [[ ! -d bak ]]; then
            mkdir bak
        fi
        mv $APP_NAME.war bak/$APP_NAME.war$DATE
        rm -rf $LIB_TO_PATH/$APP_NAME > /dev/null 2>&1
    fi
}


# 将新的包文件移动到相应的路径
copyToDeploy() {
    printInfo "复制: $LIB_FROM_PATH >>> $LIB_TO_PATH"
    cp -rf $LIB_FROM_PATH $LIB_TO_PATH
}

isWebApp() {
    if [[ "$APP_NAME" = "mca" ||  "$APP_NAME" = "mcm" ||  "$APP_NAME" = "router" ||  "$APP_NAME" = "batch" ]]; then
	    echo "0"
    else
	    echo "1"
    fi
}

# 启动JAR运行
startServer() {
    PID=`ps -ef | grep java | grep $APP_NAME-`
    if [ "$PID" != "" ]; then
        printInfo "服务已存在,将停止后重新启动" 
        stopServer
    fi
    printm "Exceting $RUN_JARNAME"
    java -Xms256M -Xmx512M -DCurrentMode=$MODE -jar $RUN_JARNAME > /dev/null 2>&1 &
}

# 停止JAR运行
stopServer() {
    PID=`ps -ef | grep java | grep $APP_NAME-`
    if [ "$PID" != "" ]; then
        printInfo "服务已存在，正在停止[$APP_NAME]" 
        PID=`echo $PID | awk '{print $2}'`
        kill -9 $PID
    fi
}
checkServerStatus() {
    resolveName $1
    resolveLogPath $1
    printInfo "请检查日志查看服务状态是否正常： $LOGFILENAME"
    if [[ $(isWebApp) = "1" ]]; then return 1; fi
    PID=`ps -ef | grep java | grep $APP_NAME`
    if [ "$PID" != "" ]; then
	printInfo "$APP_NAME 服务已启动"
    else
	printInfo "$APP_NAME 服务启动失败"
    fi
    
}

stopTomcat() {
    printm "STOP TOMCAT"
    $TOMCAT_HOME/bin/shutdown.sh
}

startTomcat() {
    printm "START TOMCAT"
    $TOMCAT_HOME/bin/startup.sh
}
checkTomcatStatus() {
    PID=`ps -ef | grep java | grep apache-tomcat`
    if [ "$PID" != "" ]; then
	printInfo "$APP_NAME Tomcat服务已启动"
    else
	printInfo "$APP_NAME Tomcat服务启动失败"
    fi
}
printInfo ()
{
echo "<INFO> $1"
}

printError ()
{
echo "<ERROR> $1"
}

println()
{
echo ------------------------------------------------------------------------
}

printm()
{
    println
    wlen=`echo $* | awk '{print length($0)}'`
    slen=$(( 36+$wlen/2 ))
    printf "%${slen}s\n" "$*"
    println
}

checkoperater ()
{
	if [[ "$USER" != "obs" ]]
	then
	    printError  "网银war包更新需使用obs用户，当前用户是 ${USER}。请切换用户后再执行此脚本!"
	    exit 1
	fi
}

main(){
    checkoperater
    config $1

    backup
    copyToDeploy

    if [[ $(isWebApp) = "0" ]]; then
	stopServer
        startServer
        checkServerStatus $1
    else
	    stopTomcat
        startTomcat
        checkTomcatStatus
    fi

}
main $1


# 1 : mca            (独立部署)
# 2 : mcm            (独立部署)
# 3 : eweb           (Tomcat部署)
# 4 : mweb           (Tomcat部署)			
# 5 : router         (独立部署)
# 6 : gateway        (Tomcat部署)
# 7 : esbaccess      (Tomcat部署)
# 8 : batch          (独立部署)
