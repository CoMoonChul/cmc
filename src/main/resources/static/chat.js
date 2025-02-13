document.addEventListener("DOMContentLoaded", function () {

    console.log('######start>>>>>>>>>>>>>>>>>>>>>>>');
    let stompClient = null;


    document.getElementById('sendMessageBtn').addEventListener('click', function() {
        sendMessage('Hello, this is a test message!');
    });


    function connect() {
        console.log('fn connect >>> ');
        let socket = new SockJS("http://localhost:8080/ws");
        stompClient = Stomp.over(socket);

        console.log('##############');
        console.log('socket');
        console.log(socket);
        console.log('##############');
        console.log('Stomp');
        console.log(Stomp);
        console.log('##############');
        console.log('stompClient');
        console.log(stompClient);
        console.log('##############');

        stompClient.connect({}, function (frame) {
            console.log("✅ WebSocket 연결 성공:", frame);

            // 메시지 수신 핸들러
            stompClient.subscribe("/topic/messages", function (message) {
                showMessage(JSON.parse(message.body));
            });
        }, function (error) {
            console.error("🚨 WebSocket 연결 실패:", error);
        });
    }

    function sendMessage() {
        let messageInput = document.getElementById("message-input");
        let message = messageInput.value.trim();

        if (message && stompClient && stompClient.connected) {
            stompClient.send("/app/chat", {}, JSON.stringify({ text: message }));
            messageInput.value = "";
        } else {
            console.warn("❌ 메시지를 전송할 수 없습니다. WebSocket 연결 상태를 확인하세요.");
        }
    }

    function showMessage(message) {
        let chatBox = document.getElementById("chat-box");
        let messageElement = document.createElement("p");
        messageElement.textContent = message.text;
        chatBox.appendChild(messageElement);
    }

    window.onload = connect;

});
