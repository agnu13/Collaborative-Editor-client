var mongoose = require('mongoose');

var Schema = mongoose.Schema;

var notificationSchema = new Schema({
    value: String,
    projectPath: String,
    type: String,
    projectName: String,
    userID: String
});

var notification = mongoose.model('notification', notificationSchema);

module.exports = {
    addNewNotification: function(req, res, userToShare, projectPath, projectName, userSharedWith){
        new notification({
            value: userToShare.name + " has shared this project with you.",
            projectPath: projectPath,
            type: 'COLLECTION',
            projectName: projectName,
            userID: userSharedWith
        }).save(function(err, data){
            res.end();
        })
    },
    
	getNotifications: function(req, res, userId) {
        notification.find({userID: userId}, function (err, notifications) {
            notifications.forEach(function(entry){
                    res.end(JSON.stringify(
                        [{'project': {'name' : entry.projectName, 'path' : entry.projectPath, 'type' : entry.type}, 'notificationMessage': entry.value}]
                    ));

                    //removes all seen notifications from the database
                    notification.remove({userID: userId}, function(err){
                    });
            });
        });
    },
	
    clearAll: function(req, res, userId){
        notification.remove({userID: userId}, function(err){
            res.write(userId, function(err){
                res.end();
            });
        });
    }
};