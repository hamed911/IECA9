mvn package
STATUS=$?
if [ $STATUS -eq 0 ]; then
	echo "built Successfully..."
	if [ -z "$CATALINA_HOME" ]; then
    	echo "Need to set 'CATALINA_HOME' variable in your system"
    	exit 1
	fi  
	mv ./target/stockmarket.war "$CATALINA_HOME"/webapps/
	STATUS=$?
	if [ $STATUS -eq 0 ]; then
		echo "deployed Successfully ..."
	else
		echo "deployment failed ..."
	fi
else
	echo "building failed ..."
fi
echo -n "Press any key ..."
read text