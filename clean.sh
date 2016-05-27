mvn clean
STATUS=$?
if [ $STATUS -eq 0 ]; then
	echo "target Cleaned Successfully ..."
	rm -r C:/"Program Files"/"Apache Software Foundation"/"Apache Tomcat 8.0.9"/webapps/stockmarket
	STATUS=$?
	if [ $STATUS -eq 0 ]; then
		echo "stockmarket folder Cleaned Successfully ..."
	rm C:/"Program Files"/"Apache Software Foundation"/"Apache Tomcat 8.0.9"/webapps/stockmarket.war
	STATUS=$?
	if [ $STATUS -eq 0 ]; then
		echo "stockmarket.war Cleaned Successfully ..."
	rm -r out
	STATUS=$?
	if [ $STATUS -eq 0 ]; then
		echo "Out Cleaned Successfully ..."
	else
		"Error in Cleaning 'out' file ..."	
	fi
else
	echo "Error in Cleaning 'target' file ..."
fi
echo "Project Cleaned Successfully"
read text