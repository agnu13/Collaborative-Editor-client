# README 

## What is this repository for? 

### Summary: 

#### RealTime Collaborative IDE :

While working on large development projects, all software developers eventually find themselves in a position where working in groups appears to be more productive. With software projects involving a multitude of developers this almost becomes a crucial part of project development. 

A real time collaborative integrated development environment can provide developers with the facility to collaborate over software projects over a network even when developers are thousand of miles away. Real-time Collaborative IDE provide developers with the ability to collaboratively write code, build and test it as well as share their projects with other developers. Chatting with other fellow developers over a project is also possible. Besides several other useful features of a complete IDE including saving snapshots, project management are also provided to ease the entire project development process.

### Developed by:
Akash Sharan , Mayur kumar Samad , sIBHI Rajan

## Version: 1.0

### How does it look like

The IDE looks like this. Its always easy to write code and share it with other developers. 
![Alt text](/./screenshots/screenshot1.jpg?raw=true "ScreenShot1")

All settings are available on hamburger menu to the left. Enable/Disable active cursors, notifications as per your need. 
![Alt text](/./screenshots/screenshot2.jpg?raw=true "ScreenShot2")

You can browse your projects like this. Shared ones too.
![Alt text](/./screenshots/screenshot3.jpg?raw=true "ScreenShot3")

You can also share your projects with fellow developers !!! Its always easy to collaborate.
![Alt text](/./screenshots/screenshot4.JPG?raw=true "ScreenShot4")

 
### Setting up the Client  

* Set up a git account and fork the repository at https://github.com/agnu13/Collaborative-Editor-client/tree/collaborative-editor/Collaborative-Client

* Clone the repository using git clone to have a local copy on your system.

* Once the cloning is complete, use NetBeans with jdk 1.8 or above to build the project. All the external libraries required for 
CollabIDE are already present in ./libs. Add them if needed.

* Modify files in ./config to change the server ip and listening port and also other desired settings for the client. By default the client tries to connect to localhost:3000.

### Setting up the Server

* Set up a git account and fork the repository at https://github.com/agnu13/Collaborative-Editor-client/tree/collaborative-editor/Collaborative-Server

* Clone the repository using git clone to have a local copy on your system. 

* Install mongoDB to your system. Create a new database named 'CollabEdit'.

* Once done, execute mongod.exe present in INSTALL_LOC\MongoDB\Server\3.0\bin\

* Once the cloning is complete, use node.js terminal to run /bin/www. The server is now up listening at port 3000 for http requests.

Introduce changes, add features, fix bugs and send a pull request to contribute. 

## Who do I talk to?

#### For any queries contact at:

Swapnil Saxena (swapnilsaxena@live.in, swapniliiit@gmail.com)
