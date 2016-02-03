#Jarvis Firefox plugin
Simple annotation collecting firefox plugin to simplify research session track.

To start adding new features to plugin first arrange your environment.

1. verify that you have jpm (Jetpack package manager) installed.
	so to get jpm you need to have npm (>=2.7.4) installed, if you haven't already. 
	npm is included in Node.js (>=v0.12.2), so to install npm, visit nodejs.org 
	and click the Install button.

	npm install jpm -g

2. jpm works only with firefox nightly build for development purposes
	to install firefox add additional ppa and proceed with installation

	sudo add-apt-repository ppa:ubuntu-mozilla-daily/ppa
	sudo apt-get update
	sudo apt-get install firefox-trunk

That's it, now go to project root directory and run you plugin
	
	jpm run -b firefox-trunk

Plugin works directly with atlassian product JIRA REST service,
to test full feature look at ./vagrant directory.
There you will find testing environment with atlassian sdk for plugin development purposes.
To run testing environment you will need to have vagrant (>=1.7.2) (https://www.vagrantup.com/) 
already installed. And execute following commands inside of ./vagrant folder:
	
	vagrant up
	vagrant ssh
	cd /vagrant
	./start-jira.sh

Now you have configured firefox development tools, and JIRA testing service.

For jpm command options refer
https://developer.mozilla.org/en-US/Add-ons/SDK/Tools/jpm#Command_reference
