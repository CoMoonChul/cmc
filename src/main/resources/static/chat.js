// test 파일 입니당
let stompClient = null;

function connect() {
    let socket = new SockJS("http://localhost:8080/ws"); // WebSocket 엔드포인트
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log("✅ WebSocket 연결됨:", frame);

        // 메시지 수신 구독
        stompClient.subscribe("/topic/messages", function (message) {
            let receivedMessage = JSON.parse(message.body);
            console.log("🔵 받은 메시지:", receivedMessage.text);
            showMessage(receivedMessage.text);
        });
    });

    // 연결 종료 시 재연결 로직
    socket.onclose = function () {
        console.log("🚨 WebSocket 연결 끊김. 재연결 시도...");
        setTimeout(connect, 3000);
    };
}

function sendMessage() {
    let messageInput = document.getElementById("message");
    let messageText = messageInput.value.trim();

    if (messageText && stompClient && stompClient.connected) {
        let chatMessage = { text: messageText };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        console.log("📤 보낸 메시지:", chatMessage.text);
        messageInput.value = "";
    } else {
        console.log("⚠️ 메시지를 보낼 수 없습니다. WebSocket이 연결되지 않았거나 메시지가 비어 있습니다.");
    }
}

// 메시지를 화면에 표시하는 함수
function showMessage(message) {
    let messageContainer = document.getElementById("messages");
    let messageElement = document.createElement("li");
    messageElement.textContent = message;
    messageContainer.appendChild(messageElement);
}

// 페이지 로딩 후 WebSocket 연결 및 이벤트 리스너 추가
document.addEventListener("DOMContentLoaded", function () {
    connect();

    document.getElementById("send").addEventListener("click", function () {
        sendMessage();
    });

    document.getElementById("message").addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            sendMessage();
        }
    });
});
