#!/bin/bash
PIDFile="./maven-packaging.pid"
SPRING_OPTS="-DLOG_FILE=application.log"
function check_if_pid_file_exists {
    if [ ! -f $PIDFile ]
    then
        echo "PID file not found: $PIDFile"
        exit 1
    fi
}

function check_if_process_is_running {
    if ps -p $(print_process) > /dev/null
    then
        return 0
    else
        return 1
    fi
}

function print_process {
    echo $(<"$PIDFile")
}

function guess_jar_name {
  JAR_FILE=$(eval ls *.jar)
  echo $JAR_FILE
  echo do you confirm that the jar name is $JAR_FILE ? [y/n]
	read choice
	if [ $choice != "y" ]; then
	  echo can you please type the jar name :
	  read JAR_FILE
	fi
}

case "$1" in
    status)
        if [ -f $PIDFile ] && check_if_process_is_running
        then
            echo $(print_process)" is running"
        else
            echo "Process not running"
        fi
    ;;
    stop)
        check_if_pid_file_exists
        if ! check_if_process_is_running
        then
            echo "Process $(print_process) already stopped"
            exit 0
        fi
        kill -TERM $(print_process)
        echo -ne "Waiting for process to stop"
        NOT_KILLED=1
        for i in {1..20}; do
            if [ -f $PIDFile ] && check_if_process_is_running
            then
                echo -ne "."
                sleep 1
            else
                NOT_KILLED=0
            fi
        done
        echo
        if [ $NOT_KILLED = 1 ]
        then
            echo "Cannot kill process $(print_process)"
            exit 1
        fi
        echo "Process stopped"
    ;;
    start)
        guess_jar_name
        echo The jar file name is $JAR_FILE
        if [ -f $PIDFile ] && check_if_process_is_running
        then
            echo "Process $(print_process) already running"
            exit 1
        fi
        nohup java $SPRING_OPTS -jar $JAR_FILE &
        echo "Process started"
    ;;
    restart)
      $0 stop
      if [ $? = 1 ]
      then
        exit 1
      fi
      $0 start
    ;;
    *)
      echo "Usage: $0 {start|stop|restart|status}"
      exit 1
esac
exit 0
#source : https://dzone.com/articles/managing-spring-boot