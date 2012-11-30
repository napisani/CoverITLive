var net = require('net');

//Enumeration
var RequestType = 
{
	STANDARD: 0,
	INITIAL: 1,
	GETCONNECTED:2,
	ERROR: 3
}

var nameToUserMap = {};

var server = net.createServer(function(user) {
  user.on('connect', function() {
    console.log('[connectEvent] a user connected');   
  });
  user.on('data', function(data) {
	var jsObj = JSON.parse(data);
	switch(jsObj.requestType){
		case RequestType.INITIAL:
			//handle initial connection signal
			if(nameToUserMap[jsObj.sender] != null) //dup name
			{
				console.log('[INIT] Duplicate Name Detected');
				sendError(user);
			}
			else
			{
				nameToUserMap[jsObj.sender]=user;
				console.log('[INIT] USER: '+ jsObj.sender +' has been authenticated');
				sendInitialConnectedList();
			}
			break;
		case RequestType.STANDARD:
			console.log('[STANDARD MESSAGE]');
			console.log('Sender: ' + jsObj.sender );
			console.log('Recipient: ' + jsObj.recipient );
			console.log('Message: ' + jsObj.message );
			
			//where is the message going?
			if(jsObj.recipient == 'ALL') //messages directed towards everyone
			{
				for (var item in nameToUserMap)
				{
					if (nameToUserMap.hasOwnProperty(item)) 
				    {
						try
						{
							nameToUserMap[item].write(JSON.stringify(jsObj) + '\n');
						}
						catch(ex)
						{
							console.log('ERROR SENDING MESSAGE TO: ' + item);
						}
					}
				}
			}
			else // messages to a single person
			{
				if(nameToUserMap[jsObj.recipient] != null)
				{
					try
					{
						nameToUserMap[jsObj.recipient].write(JSON.stringify(jsObj) + '\n');
					}
					catch(ex)
					{
						console.log('ERROR SENDING MESSAGE TO: ' + jsObj.recipient);
					}
				}
				else
				{
					console.log('Error: Invalid Recipient!');
				}
			}
		break;
	}
  });

  user.on('end', function() 
  {
  	console.log('User disconnected');
	var name = "";
	var connectedNames = [];
	
	//find him in the map and remove hime
	for (var item in nameToUserMap)
	{
	  if (nameToUserMap.hasOwnProperty(item)) 
	  {
	 	if(nameToUserMap[item] == user)
		{	
			name = item;	//name to remove
	    }
		else
		{
			connectedNames.push(item); //otherwise keep it
	  	}
	  }
	}
	
	if(name != "")
	{
		console.log(name + ' has been DeAuthenticated');
		delete nameToUserMap[name];
	}
	sendUserListToAll(connectedNames);
  });

  function sendInitialConnectedList()
  {
	 var connectedNames =  [];
	 for (var item in nameToUserMap)
	 {
		if (nameToUserMap.hasOwnProperty(item)) 
	    {
			connectedNames.push(item);
		}
	 }
	 sendUserListToAll(connectedNames);
  }
  
  function sendUserListToAll(names)
  {
	console.log('[sendUserListToAll] Sending a list of names to all Connected clients..');
	var oJson = {};
	oJson.sender = "";
	oJson.recipient = "";
	oJson.message = "";
	oJson.requestType = RequestType.GETCONNECTED;
	oJson.connectedUsers = names;
	console.log(JSON.stringify(oJson) + '\n');
	for (var item in nameToUserMap)
	{
		try
		{
			nameToUserMap[item].write(JSON.stringify(oJson) + '\n');
		}
		catch(ex)
		{
			console.log('ERROR SENDING MESSAGE TO: ' + item);
		}
		
  	 }
  }
  
  function sendError(user)
  {
	console.log('[sendError] Sending an error to a connected user');
	var oJson = {};
	oJson.sender = "";
	oJson.recipient = "";
	oJson.message = "That username is already signed in.";
	oJson.requestType = RequestType.ERROR;
	oJson.connectedUsers = {};
	try
	{
		user.write(JSON.stringify(oJson) + '\n');
  	}
	catch(ex)
	{
		console.log('ERROR SENDING ERROR MESSAGE');
	}
  }
});

server.listen(3000);
