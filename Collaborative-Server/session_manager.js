/**
 * @requires session - provides operation to create and maintain user-sessions
*/
var projectManager = require('./projectManager');
var doc_session = require('./session');
var doc_utility = require('./utility');

/**
 * Path of directory where user-projects are saved
 **/
var dirName = __dirname + '\\UserProjects';

/**
 * SessionManager is a class whose objective is to create, maintain sessions. It also route user-requests to appropriate sessions    
 * @constructor
 */
function SessionManager() {
	this.sessions = {};
}

/**
 * Creates a new session for Doc#docId, if none exists. Else uses an existing session. Adds the User#userId to the session. 
 *
 * @param request {Object} User's request object
 * @param response {Object} User's response object
 * @param userId {String} User's userId who has sent REGISTER request.
 * @param docId {String} Doc's docId which user wants to edit.
 * @param docPath {String} Path of the doc on server.
 */
SessionManager.prototype.handleRegister = function(request, response, userId, docId) {
	try {
		var self = this;
		getDocPath(docId, function(docPath) {
			var state;
			try {
				if (self.sessions[docId] == undefined) {
					self.sessions[docId] = new doc_session.Session(docId, docPath);
				} else {
					var state = self.sessions[docId].state;
					state.operationsNotSaved.forEach(function(operation) {
						state.applyToRope(operation);
					});
					state.operationsNotSaved = [];
				}
				var session = self.sessions[docId];
				session.addUser(userId);
				state = session.state.getState();
				doc_utility.log('Registration Status', 'Success');
			} catch (err) {
				console.log(err.stack);
			} finally {
				response.end(state);
			}
		});
	} catch (err) {
		console.log(err.stack);
	} 
};

/**
 * Removes the User#userId to the session. Destroys it if no user is currently editing the doc.
 *
 * @param request {Object} User's request object
 * @param response {Object} User's response object
 * @param userId {String} User's userId who has sent REGISTER request.
 * @param docId {String} Doc's docId which user wants to edit.
 */
SessionManager.prototype.handleUnregister = function(request, response, userId, docId) {
	console.log('Unregister received');
	try {
		if (this.sessions[docId] != undefined) {
			var session = this.sessions[docId];
			if (session.userCursorPos[userId] != undefined) {
				doc_utility.log('Removing', 'in progress');
				session.removeUser(userId);
				doc_utility.log('Removing', 'done');
				if (session.getUserCount() == 0) {
					session.cleanup();
					delete this.sessions[docId];
				}
			} else {
				throw {
					msg: 'un-register request from non-existent user#' + userId
				}
			}
		} else {
			throw {
				msg: 'un-register request from non-existent doc#' + docId
			};
		}
	} catch (err) {
		console.log(err);
	} finally {
		response.end();
	}
};

/**
 * Checks for validity of requests. Routes requests to session Doc#docId, if one exists.
 *
 * @param request {Object} User's request object
 * @param response {Object} User's response object
 * @param userId {String} User's userId who has sent PUSH request.
 * @param docId {String} Doc's docId which user wants to edit.
 */
 SessionManager.prototype.handleGet = function(request, response, userId, docId) {
	 var self = this;
	 console.log('GET received: ' + userId);
	 try {
		if (this.sessions[docId] == undefined) {
			throw {
				msg: 'Get request for non-existent doc#' + docId
			};
		} else {
			var session = this.sessions[docId];
			if (session.userCursorPos[userId] == undefined) {
				throw {
					msg: 'Get request for non-existent user#' + userId + ' on doc#' + docId
				};
			} else {
				session.handleGet(request, response, userId);
			}
		}
	} catch(err) {
		if (self.sessions[docId] == undefined) {
			//if no session exists, create a new session
			self.handleRegister(request, response, userId, docId);
		} else if (self.sessions[docId].userCursorPos[userId] == undefined){
			//if no user exist on session, ad user to session
			self.handleRegister(request, response, userId, docId);	
		} else {
			console.log(err.stack);
		}
	} finally {
		response.end();
	}
};

SessionManager.prototype.handlePush = function(request, response, userId, docId) {
	var self = this;
	try {
		if (this.sessions[docId] == undefined) {
			throw {
				msg: 'Push request for non-existent doc#' + docId
			};
		} else {
			var session = this.sessions[docId];
			if (session.userCursorPos[userId] == undefined) {
				throw {
					msg: 'Push request for non-existent user#' + userId + ' on doc#' + docId
				};
			} else {
				session.handlePush(request, response, userId);
			}
		}
	} catch(err) {
		if (self.sessions[docId] == undefined) {
			//if no session exists, create a new session
			self.handleRegister(request, response, userId, docId);
		} else if (self.sessions[docId].userCursorPos[userId] == undefined){
			//if no user exist on session, ad user to session
			self.handleRegister(request, response, userId, docId);	
		} else {
			console.log(err.stack);
		}
	} finally {
		response.end();
	}
};			

//Lets export
module.exports.SessionManager = SessionManager;

/**
 * Utility Functions
 **/
 
/**
 * @param docId {String} Doc's docId which user wants to edit.
 * @callback called when docPath for Doc#docId is retrieved from database
 */
function getDocPath(docId, callback) {
	projectManager.file.find({fileID: docId}, function(err, data){
		data.forEach(function(entry){
			var resolvedDocPath = dirName + resolve(entry.path);
			resolvedDocPath += '\\' + entry.fileName;
			callback(resolvedDocPath);
		});
	});
}

/**
 * @param filePath {String} filePath with dot(.) as separator
 * @return {String} filePath with dot(.) replaced with slash(/) as separator
 */
function resolve(filePath) {
    var docPath = "";
    for(var i = 0; i < filePath.length; ++i) {
        if (filePath.charAt(i) == '.') {
            docPath += '\\';
        } else {
            docPath += filePath.charAt(i);
        }
    }
    return docPath;
}