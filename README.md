# CMPE275-Fa16-Library
Library Project for CMPE 275 Fall 2016 using Spring


## Github setup
1. Use a github UI application. E.g. Source Tree
2. Clone this Private repository to your workstation: https://github.com/SkandaB/CMPE275-Fa16-Library
3. Follow the instructions from this document to setup the environment on your local machine.

## Google Cloud - Compute Engine Connectivity
1. You will be added to the Project on Google Cloud
2. Create a new ssh key-pair to connect to the Cloud machine. Instructions: https://cloud.google.com/compute/docs/instances/connecting-to-instance#generatesshkeypair
3. Login to your cloud console via ssh. Linux/Mac: https://cloud.google.com/compute/docs/instances/connecting-to-instance#standardssh 
Windows: https://cloud.google.com/compute/docs/instances/connecting-to-instance#putty

## Push to build with Jenkins
We will be using Jenkins for automatic builds on the server. Whenever some new code is pushed onto the master branch Jenkins will pull the commit and start the build.
Maven will be the choice of build tool.
A naked project will be created with corresponding pom.xml and pushed onto master first. Then a new brach will be created for each feature.
Users working on a branch will be taking up setting the auto-build for the corresponding branch in Jenkins. Skanda to demo how to do it by showing the example of master branch.
Each night, and there will be nightly builds of all the branches in isolation.
Branches will be merged to master after completion of a feature.
Create a pull request to merge your branch into master. If any conflicts arise in the pull request, the branch will be modified to be compatible with master branch.

## Using Jenkins
Start Jenkins: java -jar jenkins.war --httpPort=9090
URL to access Jenkins on Cloud Server: http://8.35.192.11:9090/ 
Branch check 1

