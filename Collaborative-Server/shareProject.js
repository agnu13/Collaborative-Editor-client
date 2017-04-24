var mongoose = require('mongoose');

var Schema  = mongoose.Schema;

var sharedProjectsSchema = new Schema({
    userID: String,
    path: String,
    name: String,
    type: String
});

var notification = require('./notification');
var sharedProject = mongoose.model('sharedProject', sharedProjectsSchema);
var auth = require('./auth');

module.exports = {
    getSharedProjects: function(req, res, userId){
        sharedProject.find({userID: userId}, function(err, projects){
            res.write(JSON.stringify(projects), function(err){
                res.end();
            });
        });
    },

    share: function(req, res, project, userWhoShared, userSharedWith){
        //Resolve project in projectPath and projectName
        var projectName = '';
        var projectPath = '.';
        var level = 0;
        for(var i = 0; i < project.length; ++i){
            if(project.charAt(i) == '.'){
                level++;
            } else if(level == 1){
                projectPath += project.charAt(i);
            } else if(level == 2){
                projectName += project.charAt(i);
            }
        }
        //

        auth.user.find({userID: userWhoShared}, function(err, users) {
            users.forEach(function (entry) {
                new sharedProject({
                    userID: userSharedWith,
                    path: projectPath,
                    name: projectName,
                    type: 'COLLECTION'
                }).save(function (err, data) {
                    notification.addNewNotification(req, res, entry, projectPath, projectName, userSharedWith);
                });
            });
        });
    }
};

function getSharedProjects(req, res){
    sharedProject.find({userID: req.body.userId}, function(err, result){
        res.write(result, function(err){
            res.end();
        });
    });
}