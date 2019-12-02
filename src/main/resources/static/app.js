function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

var socket = null;

function url(s) {
    var l = window.location;
    return  ((l.protocol === "https:") ? "wss://" : "ws://") +
            l.hostname +
            (((l.port != 80) && (l.port != 443)) ? ":" + l.port : "")
            + l.pathname
            + s;
}

function connect() {
    socket = new WebSocket(url('/eliza'));
    socket.onopen = function(e) {
        setConnected(true);
        console.log('connection open');
    };

    socket.onmessage = function(event) {
        showGreeting(event.data);
    };

    socket.onclose = function(event) {
        setConnected(false);
        if (event.wasClean) {
            alert('[close] Connection closed cleanly, code=' + event.code + ' reason=' + event.reason);
        } else {
            // e.g. server process killed or network down
            // event.code is usually 1006 in this case
            alert('[close] Connection died');
        }
    };

    socket.onerror = function(error) {
        alert('[error]' + error.message);
    };
}

function disconnect() {
    if (socket !== null) {
        socket.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    messageToSend = $("#name").val();
    socket.send(messageToSend);
    $("#greetings").append("<tr><td><b>" + messageToSend + "<b></td></tr>");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});