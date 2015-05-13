#!/usr/bin/env bash 

pushd ~
atlas="~/atlastutorial";
if [ ! -d $atlas ]; then
	mkdir -p $atlas
fi
cd $atlas
atlas-run-standalone --product jira
popd
