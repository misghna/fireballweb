var ws;

function connect() {
    var username = document.getElementById("username").value;
    
    var host = document.location.host;
    var pathname = document.location.pathname;
    
    ws = new WebSocket("ws://" +host  + pathname + "chat/" + username);

    ws.onmessage = function(event) {
    var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);      
        
        if(message.content.indexOf("RealizedPnL")>-1){
        	var label1 = document.getElementById("label1");
        	label1.innerHTML = message.content;
        }else if(message.content.indexOf("UnrealizedPnL")>-1){
        	var label2 = document.getElementById("label2");
        	label2.innerHTML = message.content;
        }else if(message.content.indexOf("lastUpdatedTime")>-1){
        	var label3 = document.getElementById("label3");
        	var date = new Date(Number(message.content.split("-")[1].trim()));
        	label3.innerHTML = "Updated on " + date.toString();
        }else if(message.content.indexOf("RefreshedAt")>-1){
        	var label4 = document.getElementById("label4");
        	var date = new Date(Number(message.content.split("-")[1].trim()));
        	label4.innerHTML = "Refreshed at " + date.toString();
        }else{
        	 log.innerHTML += message.from + " : " + message.content + "\n";
        }
        
    };
}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);
}