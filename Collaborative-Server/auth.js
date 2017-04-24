//require
var mongoose = require('mongoose');
var mkdirp = require('mkdirp');

//Schema user
var Schema = mongoose.Schema;
var userSchema = new Schema({
    name: String,
    username: String,
    userID: String,
    password: String,
    emailID: String,
});

var user = mongoose.model('users', userSchema);
var dirName = __dirname + "\\UserProjects";

module.exports = {

    user: mongoose.model('users', userSchema),

    getUsers: function(req, res) {
        user.find({}, function (err, users) {
            var resToBeSent = [];
            users.forEach(function(entry){
                resToBeSent.push({'username': entry.username, 'name': entry.name, 'userId': entry.userID, 'emailId' : entry.emailID});
            });
            res.write(JSON.stringify(resToBeSent), function (err) {
                res.end();
            });
        });
    },

    logIn: function(req, res) {
        user.find({username: req.body.username}, function (err, userToLogin) {
            if (userToLogin.length == 0) {
                res.write("{'statusCode' : '404', 'statusMessage' : 'No Such User Exists'}", function (er) {
                    res.end();
                });
            } else {
                userToLogin.forEach(function (entry) {
                    if (entry.password == req.body.password) {
                        res.write("{'statusCode' : '200', 'statusMessage' : 'Login Successful'}", function (err) {
                            res.end();
                        });
                    }
                    else {
                        res.write("{'statusCode' : '404', 'statusMessage' : 'Username and Password doesn not match'}", function (err) {
                            res.end();
                        });
                    }
                });
            }
        });
    },

    getInfo: function(req, res) {
        user.find({username: req.body.username}, function (err, userToGet) {
            userToGet.forEach(function (entry) {
                var info = {username: entry.username, name: entry.name, userId: entry.userID, emailId: entry.emailID};
                res.write(JSON.stringify(info), function (err) {
                    res.end();
                });
            })
        });
    },

    signUp: function(req, res) {
        user.find({username: req.body.username}, function (err, users) {
            if (users.length == 0) {
                new user({
                    name: req.body.name,
                    username: req.body.username,
                    userID: req.body.username,
                    password: req.body.password,
                    emailID: req.body.emailId,
                    createTime: new Date()
                }).save(function (err, newUser) {
                    if (err) {
                        res.write("{'statusCode' : '404', 'statusMessage' : 'Insertion Error.'}", function (err) {
                            res.end();
                        })
                    }
                    else {
                        var newPath = dirName + "\\" + req.body.username;
                        mkdirp(newPath, function(err) {
                            res.write("{'statusCode' : '200', 'statusMessage' : 'Registration Successful.'}", function (err) {
                                res.end();
                            });
                        });
                    }
                });
            }
            else {
                res.write("{'statusCode' : '404', 'statusMessage' : 'User Already Exists'}", function (err) {
                    res.end();
                });
            }
        });
    }
};