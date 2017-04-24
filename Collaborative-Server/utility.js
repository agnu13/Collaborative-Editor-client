/**
 * Utility Functions
 *
 */
 
module.exports = {
	/**
	 * Perform logging, can be redirected if needed. 
	 * @param title {String} Title of logged message.
	 * @param desc {String} Description of logged message.
	 */
	DEBUG: true,

	log: function(title, desc) {
		if (this.DEBUG) {
			console.log(title + ': ' + desc);
		}
	},

	/**
	* fix 'End of Line' issues. 
	* @param content {String} content whose EOL need to be fixed.
	* @return {String} content with '\r\n' replaced by '\n'.
	*/
	fixEOL: function (content) {
		var newContent = '';
		//replace method isn't working so doing this manually
		for (var i = 0; i < content.length; i++) {
			if ((i+1 < content.length) && (content[i]=='\r' && content[i+1]=='\n')) {
				newContent += '\n';
				i++;
			} else {
				newContent += content[i];
			}
		}
		return newContent;
	}
}