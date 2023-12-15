var stompClient = null;

var messageInput = document.querySelector("#message")
var btn = document.querySelector("#submitBtn");

stompClient = Stomp.client('ws://localhost:8080/ws');
stompClient.connect({}, stompConnectHandler, stompErrorHandler);

// STOMP Socket 접속 성공시 실행되는 콜백 함수
function stompConnectHandler() {
    console.log('connected!');

    // STOMP Socket 접속 성공시 '/topic/wiki' 메시지 구독을 한다.
    stompClient.subscribe('/topic/test', (data) => {
        // /topic/wiki로 메시지가 유입되면 해당 메시지를 가져와 실행되는 콜백 함수
        console.log('topic wiki subscribe data - ', JSON.parse(data.body));
        // 받은 데이터를 body에 출력한다.
        $('body').append(data.body + '<br/>');
    })
}

function stompErrorHandler(e) {
    console.error('stomp connect error - ', e);
}

//function connect() {
//    var socket = new SockJS('/ws');
//    stompClient = Stomp.over(socket);
//    stompClient.connect({}, function (frame) {
//        setConnected(true);
//        console.log('Connected: ' + frame);
//    });
//}
//
//function disconnect() {
//    if (stompClient !== null) {
//        stompClient.disconnect();
//    }
//    setConnected(false);
//    console.log("Disconnected");
//}
//
//connect()

function sendMessage() {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            msg : messageContent
        };
        stompClient.send("/app/test", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

console.log(btn)

//messageForm.addEventListener('submit', sendMessage, true)
btn.addEventListener('click', () => {
    console.log("클릭!")
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            msg : messageContent
        };
        stompClient.send("/app/test", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
})
