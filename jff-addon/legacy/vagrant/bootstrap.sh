#!/usr/bin/env bash

sudo apt-add-repository ppa:webupd8team/java -y
sudo apt-get update

echo debconf shared/accepted-oracle-license-v1-1 select true | \
	  sudo debconf-set-selections

echo debconf shared/accepted-oracle-license-v1-1 seen true | \
	  sudo debconf-set-selections

sudo apt-get install -y oracle-java8-installer

sudo sh -c 'echo "deb http://sdkrepo.atlassian.com/debian/ stable contrib" >>/etc/apt/sources.list'
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys B07804338C015B73
sudo apt-get update
sudo apt-get install --force-yes --yes atlassian-plugin-sdk

cp start-jira.sh ~
#cd ~
#mkdir atlastutorial
#cd atlastutorial
#atlas-run-standalone --product jira
