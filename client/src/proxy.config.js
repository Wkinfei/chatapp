const PROXY_CONFIG = [
	{
	context: [ "/ws" ],
	target: "ws://localhost:8081",
	secure: false,
    ws: true,
    changeOrigin: true,
    pathRewrite: {'^/ws' : ''},
    logLevel: "debug"
	},
	{ 
		context: ['/api'], 
		target: 'http://localhost:8081', 
		secure: false, 
		changeOrigin: true, 
		logLevel: 'debug' 
	  } 
	]
module.exports = PROXY_CONFIG;