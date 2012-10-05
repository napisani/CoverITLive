var net = require('net');

//Enumeration
var RequestType = 
{
	STANDARD: 0,
	INITIAL: 1,
	GETCONNECTED:2,
	ERROR: 3
}

//var chatroom = [];
var nameToUserMap = {};

var server = net.createServer(function(user) {
  user.on('connect', function() {
    console.log('user connected');

	//chatroom.push(user);    
	//user.write('hiiii user');    
  });

  user.on('data', function(data) {
	var jsObj = JSON.parse(data);
	
	switch(jsObj.requestType)
	{
		case RequestType.INITIAL:
		
			//handle initial connection signal
			nameToUserMap[jsObj.sender]=user;
			console.log('user authenticated');
			sendInitialConnectedList();
			
		break;
		
		case RequestType.STANDARD:
		
			//where is the message going?
			if(jsObj.recipient == 'ALL') //messages directed towards everyone
			{
				for (var item in nameToUserMap)
				{
					if (nameToUserMap.hasOwnProperty(item)) 
				    {
						nameToUserMap[item].write(JSON.stringify(jsObj) + '\n');
					}
				}
				console.log(JSON.stringify(jsObj)+'\n');
			}
			else // messages to a single person
			{
				if(nameToUserMap[jsObj.recipient] != null)
				{
					//nameToUserMap[jsObj.recipient].write(jsObj.message + '\n');
					nameToUserMap[jsObj.recipient].write(JSON.stringify(jsObj) + '\n');
				}
				else
				{
					console.log('incorrect destination');
					//user.write('incorrect destination');
				}
			}
		break;
	}
  });

  user.on('end', function() 
  {
  	console.log('user disconnected');
	var name = "";
	var connectedNames = [];
	
	//find him in the map and remove hime
	for (var item in nameToUserMap)
	{
	  if (nameToUserMap.hasOwnProperty(item)) 
	  {
		console.log(item);
	  	if(nameToUserMap[item] == user)
		{	
			name = item;
	    }
		else
		{
			connectedNames.push(item);
	  	}
	  }
	}
	
	if(name != "")
	{
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
	var oJson = {};
	oJson.sender = "";
	oJson.recipient = "";
	oJson.message = "";
	oJson.requestType = RequestType.GETCONNECTED;
	oJson.connectedUsers = names;
	console.log(JSON.stringify(oJson) + '\n');
	for (var item in nameToUserMap)
	{
		if (nameToUserMap.hasOwnProperty(item)) 
	    {
			nameToUserMap[item].write(JSON.stringify(oJson) + '\n');
		}
  	 }
  }


});

server.listen(3000);
