let room_num;
let currentUserNum;
let lastMessageId = 0;

function initChat(roomNum, userNum) {
    room_num = roomNum;
    currentUserNum = userNum;
    
    // 주기적으로 새 메시지 확인
    setInterval(checkNewMessages, 1000);
}

// 새 메시지 확인
function checkNewMessages() {
    fetch(`/chat/messages/${room_num}?lastMessageId=${lastMessageId}`)
        .then(response => response.json())
        .then(data => {
            if (data.result === 'success' && data.messages && data.messages.length > 0) {
                data.messages.forEach(message => {
                    displayMessage(message);
                    lastMessageId = Math.max(lastMessageId, message.message_num);
                });
            }
        })
        .catch(error => console.error('메시지 조회 중 오류 발생:', error));
}

// 메시지 전송
function sendMessage() {
    const messageInput = document.getElementById('messageInput');
    const content = messageInput.value.trim();
    
    if (!content) return;
    
    const message = {
        room_num: room_num,
        content: content
    };
    
    // REST API로 메시지 저장
    fetch('/chat/messages/send', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        },
        body: JSON.stringify(message)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (data.result === 'success') {
            displayMessage(data.message);
            messageInput.value = '';
            lastMessageId = Math.max(lastMessageId, data.message.message_num);
        } else {
            alert('메시지 전송에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('메시지 전송 중 오류 발생:', error);
        alert('메시지 전송에 실패했습니다.');
    });
}

// 메시지 표시
function displayMessage(message) {
    const chatMessages = document.getElementById('chatMessages');
    const messageDiv = document.createElement('div');
    messageDiv.className = message.sender_num === currentUserNum ? 'message sent' : 'message received';
    
    const timeStr = message.sent_at ? new Date(message.sent_at).toLocaleTimeString() : new Date().toLocaleTimeString();
    
    messageDiv.innerHTML = `
        <span class="sender">${message.sender_name || '알 수 없음'}</span>
        <p class="content">${message.content}</p>
        <span class="time">${timeStr}</span>
    `;
    
    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// 엔터 키로 메시지 전송
document.getElementById('messageInput').addEventListener('keypress', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        sendMessage();
    }
}); 