var stompClient = null;

function connect() {	
	loadAllComments();
    loadItemDescription();
	var socket = new SockJS('/live-commentary');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/commentary', function (comment) {
            showComment(JSON.parse(comment.body).comment);
        });
    });
    
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}


function sendCommentary() {
	var itemId=window.location.href.substring(window.location.href.lastIndexOf('/') + 1);
    stompClient.send("/app/live-comment", {}, JSON.stringify({'commentary': $("#commentary").val(), 'itemId': itemId}));
    $("#commentary").val('');
    $("#itemId").val('');
}

function showComment(comment) {
    $("#comment").append("<tr><td>" + comment + "</td></tr>");
}

function loadItemDescription() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", '/get-item-description?itemNumber='+$("#itemId").val(), false ); // false for synchronous request
    xmlHttp.send( null );
    var jsondat=JSON.parse(xmlHttp.response);
    var ro='<tr>';
    $.each(jsondat, function(i, d) {
          ro+='<td>'+d+'</td>';
       });
       ro+='</tr>';
       $("#item-description").append(ro);  
}

function loadAllComments() {
    console.log("loadAllComments");
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", '/all-comments?itemNumber='+$("#itemId").val(), false ); // false for synchronous request
    xmlHttp.send( null );
    var jsondata=JSON.parse(xmlHttp.response);
    $.each(jsondata, function(i, d) {
       var row='<tr>';
       $.each(d, function(j, e) {
          row+='<td>'+e+'</td>';
       });
       row+='</tr>';
       $("#dbComments").append(row);
    });
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    
    $("#publish").click(function() { sendCommentary(); });
});

