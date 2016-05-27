1) Install HSQL (hsqldb.org)
2) Start HSQL Server
	cd lib/hsqldb
	java -cp ./hsqldb.jar org.hsqldb.server.Server
3) Start HSQL Database Manager
	java -cp hsqldb.jar org.hsqldb.util.DatabaseManagerSwing
4) Connect to the server
	Set 'type' to 'HSQL Database Engine Server'
5) put 'server.xml' in 'conf' folder of your Tomcat.
6) put 'hsqldb.jar' in 'lib' folder of your Tomcat. ( you can find hsqldb.jar in following address: ./lib/hsqldb/hsqldb.jar )
7) Build and Deploy the project
	./build&deploy.sh
8) Start Tomcat
	./startTomcat.sh
9) configure database by calling followin URL
	http://localhost:8080/stockmarket/confdb


