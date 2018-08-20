curl -d "" http://127.0.0.1:8022/shutdown
sleep 5
configpid=`ps -ef | grep micro-config-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{print $2}'`
if [ -n "$configpid" ]
then
	echo "life config program shutdown fail, now try to kill with pid:" $configpid
	kill $configpid
fi
tmpconfigpid=`ps -ef | grep micro-config-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{print $2}'`
echo "finish kill life config, find in ps pid is :" $tmpconfigpid
if [ -n "$tmpconfigpid" ]
then 
	echo "life config kill fail, now exec kill -9 with pid:" $tmpconfigpid
	kill -9 $tmpconfigpid
fi