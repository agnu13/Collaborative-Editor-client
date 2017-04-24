var express = require('express');
var path = require('path');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var queryString = require('querystring');
var urlParser = require('url');

var app = express();

/**
 * middle-wares
 * bodyParser.json(): parses request-body with header field "Content-type" set to "application/json" to Javascript-Object
**/
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(express.static(path.join(__dirname, 'public')));

/**
 * Path of directory where user-projects are saved
 **/
var dirName = __dirname + '\\UserProjects';

/**
 * Handlers for Requests
 *
 * @requires auth: handles authentication requests
 * @requires projectManager: handles directory and file management requests
 * @requires share: handles project sharing and viewing requests
 * @requires notification: handles view and clear notification request
 * @requires docHandler: handles document-editing request namely REGISTER, UN-REGISTER, GET and PUSH
 *
**/
var auth = require('./auth');
var projectManager = require('./projectManager');
var notification = require('./notification');
var share = require('./shareProject');
var docHandler = require('./session_manager');

/**
 * Session Manager to handle Document-Editing requests
**/
var sessionManager = new docHandler.SessionManager();

/**
 * Establish database-connection
**/
mongoose.connect('mongodb://localhost/CollabEdit');
var conn = mongoose.connection;


/**
 * Routes requests to appropriate handlers
 *
 **/

/**
 * Document-Editing Request-Handler
 **/
app.use("/register", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    sessionManager.handleRegister(req, res, parsedQuery.userId, parsedQuery.docId);
})

app.use("/unregister", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    sessionManager.handleUnregister(req, res, parsedQuery.userId, parsedQuery.docId);
})

app.use("/get_operation", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    sessionManager.handleGet(req, res, parsedQuery.userId, parsedQuery.docId);
})

app.use("/push_operation", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    sessionManager.handlePush(req, res, parsedQuery.userId, parsedQuery.docId);
})

/**
 * Project/Doc Creation and Management Request-Handler
 **/
app.use("/view", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    projectManager.view(req, res, parsedQuery.path);
})

app.use("/get_info", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    auth.getInfo(req, res);
})

app.use("/add_node", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    projectManager.addNode(req, res, parsedQuery.path);
})

/**
 * Authentication Request-Handler
 **/
app.use("/login", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    auth.logIn(req, res);
})

app.use("/signup", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    auth.signUp(req, res);
})

app.use("/get_users", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    auth.getUsers(req, res);
})

/**
 * Share Project Request-Handler
 **/
app.use("/share", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    share.share(req, res, parsedQuery.docId, parsedQuery.userId, parsedQuery.shareId);
})

app.use("/get_shared_projects", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    share.getSharedProjects(req, res, parsedQuery.userId);
})

/**
 * Notification Request-Handler
 **/
app.use("/get_notifications", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    notification.getNotifications(req, res, parsedQuery.userId);
})

app.use("/clear_notifications", function(req, res) {
    var parsedQuery = extractQuery(req.url);
    notification.clearAll(req, res, parsedQuery.userId);
})

/**
 * Utility Functions
 **/

/**
 * @return {Object} Parsed Query
 * @param url {String} url of received request
 */
function extractQuery(url) {
    var parsedURL = urlParser.parse(url);
    var query = parsedURL.query;
    var parsedQuery = queryString.parse(query);
    return parsedQuery;
}

module.exports = app;