mvn package
STATUS=$?
if [ $STATUS -eq 0 ]; then
	echo "built Successfully..."
	mv ./target/stockmarket.war C:/"Program Files"/"Apache Software Foundation"/"Apache Tomcat 8.0.9"/webapps/
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