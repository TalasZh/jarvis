pushd ~
atlas="atlastutorial";
if [! -d $atlas]; then
	mkdir -p $atlas
cd $atlas
atlas-run-standalone --product jira
popd
