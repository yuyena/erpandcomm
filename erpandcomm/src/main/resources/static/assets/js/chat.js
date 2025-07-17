$(function(){
    /*============================
     * 전역 변수 정의
     *============================*/
    const chat = {
        room: {
            id: null,
            name: null
        },
        user: {
            id: null,
            name: null
        },
        socket: null,
        messages: {
            displayed: new Set()
        },
        reconnectAttempts: 0,
        maxReconnectAttempts: 5,
        reconnectInterval: 3000
    };

    /*============================
     * WebSocket 연결
     *============================*/
    function connectWebSocket() {
        if (chat.socket && chat.socket.readyState === WebSocket.OPEN) {
            console.log('WebSocket이 이미 연결되어 있습니다.');
            return;
        }
        
        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const host = window.location.host;
        const wsUrl = protocol + '//' + host + '/message-ws';
        
        console.log('WebSocket 연결 시도:', wsUrl);
        
        try {
            chat.socket = new WebSocket(wsUrl);
            
            chat.socket.onopen = function() {
                console.log('WebSocket 연결 성공');
                chat.reconnectAttempts = 0;
                
                if (chat.room.id) {
                    const joinMessage = {
                        type: 'JOIN',
                        room_num: chat.room.id,
                        sender_num: chat.user.id,
                        message_id: Date.now().toString()
                    };
                    chat.socket.send(JSON.stringify(joinMessage));
                }
            };
            
            chat.socket.onmessage = function(event) {
                try {
                    console.log('메시지 수신:', event.data);
                    const message = JSON.parse(event.data);
                    
                    if (!message || !message.type) {
                        console.warn('잘못된 메시지 형식:', message);
                        return;
                    }
                    
                    switch(message.type) {
                        case 'CHAT':
                            if (message.room_num === chat.room.id && !chat.messages.displayed.has(message.message_id)) {
                    displayMessage(message);
                                chat.messages.displayed.add(message.message_id);
                            }
                            break;
                        case 'READ':
                            if (message.room_num === chat.room.id) {
                                updateUnreadCount(message.message_num, message.unread_count);
                            }
                            break;
                        case 'JOIN':
                            console.log('사용자 입장:', message.sender_name);
                            break;
                        case 'LEAVE':
                            console.log('사용자 퇴장:', message.sender_name);
                            break;
                        default:
                            console.warn('알 수 없는 메시지 타입:', message.type);
                    }
                } catch(e) {
                    console.error('메시지 처리 중 오류:', e);
            }
            };
            
            chat.socket.onclose = function(event) {
                console.log('WebSocket 연결 종료 - 코드:', event.code, '사유:', event.reason);
                chat.socket = null;
                
                if (!event.wasClean) {
                    console.warn('비정상적인 연결 종료');
                }
                
                if (chat.reconnectAttempts < chat.maxReconnectAttempts) {
                    chat.reconnectAttempts++;
                    console.log(`재연결 시도 ${chat.reconnectAttempts}/${chat.maxReconnectAttempts}`);
                    setTimeout(connectWebSocket, chat.reconnectInterval);
                } else {
                    console.log('최대 재연결 시도 횟수 초과');
                    alert('채팅 연결이 종료되었습니다. 페이지를 새로고침해주세요.');
                }
            };
            
            chat.socket.onerror = function(error) {
                console.error('WebSocket 오류:', error);
            };
            
        } catch(e) {
            console.error('WebSocket 연결 실패:', e);
            setTimeout(connectWebSocket, chat.reconnectInterval);
        }
}

    /*============================
     * 채팅방 관련 함수
     *============================*/
    function initChat(roomNum, userNum, userName) {
        console.log('채팅 초기화:', roomNum, userNum, userName);
        chat.room.id = roomNum;
        chat.user.id = userNum;
        chat.user.name = userName || '';
        chat.messages.displayed.clear();
        
        connectWebSocket();
    }

    function leaveChat() {
        if (chat.socket) {
            chat.socket.close();
            chat.socket = null;
        }
        chat.room.id = null;
        chat.user.id = null;
        chat.messages.displayed.clear();
    }

    /*============================
     * 메시지 전송/표시 함수
     *============================*/
    function sendMessage(customMessage) {
        if (customMessage) {
            if (chat.socket && chat.socket.readyState === WebSocket.OPEN) {
                chat.socket.send(JSON.stringify(customMessage));
            }
            return;
        }

        const messageInput = $('#messageInput');
        if (!messageInput.length || !messageInput.val().trim()) return;
    
    const message = {
            type: 'CHAT',
            room_num: chat.room.id,
            content: messageInput.val().trim(),
            sender_num: chat.user.id,
            message_id: Date.now().toString(),
            sender_name: chat.user.name,
            sent_at: '' // DB에서 가져온 정확한 시간으로 나중에 업데이트
    };
    
        // 1. HTTP API로 DB에 저장 (서버에서 자동으로 WebSocket 브로드캐스트)
        $.ajax({
            url: contextPath + 'chat/messages/send',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                room_num: chat.room.id,
                content: message.content,
                sender_num: chat.user.id
            }),
            beforeSend: function(xhr) {
                xhr.setRequestHeader($('meta[name="csrf-header"]').attr('content'),
                                     $('meta[name="csrf-token"]').attr('content'));
            },
            success: function(response) {
                if (response.result === 'success') {
                    console.log('메시지 DB 저장 성공');
                    // 서버에서 자동으로 WebSocket 브로드캐스트하므로 클라이언트 전송 제거
                    messageInput.val('');
                } else {
                    console.error('메시지 DB 저장 실패:', response);
                }
            },
            error: function(xhr) {
                console.error('메시지 저장 중 오류 발생:', xhr);
                messageInput.val('');
            }
        });
    }

    function displayMessage(message) {
        const chatMessages = $('#chatMessages');
        if (!chatMessages.length) return;

        // message_id가 없으면 message_num을 사용하여 생성
        if (!message.message_id && message.message_num) {
            message.message_id = 'msg_' + message.message_num;
        }

        // message_id 또는 message_num으로 중복 체크
        if (message.message_id && chat.messages.displayed.has(message.message_id)) {
            console.log('중복 메시지 무시 (message_id):', message.message_id);
            return;
        }
        
        // 이미 화면에 표시된 message_num인지 확인
        if (message.message_num && $(`[data-message-num="${message.message_num}"]`).length > 0) {
            console.log('중복 메시지 무시 (message_num):', message.message_num);
            return;
        }

        const messageDiv = $('<div>')
            .addClass(`chat-message-row ${message.sender_num == chat.user.id ? 'me' : 'other'}`)
            .attr('data-message-num', message.message_num);
        
        const messageContent = $('<div>').addClass('message-content');
        
        if (message.sender_num == chat.user.id) {
            // 내 메시지 (오른쪽)
            const messageInfo = $('<div>').addClass('message-info');
            
            // 메시지 버블
            const bubbleDiv = $('<div>')
                .addClass('chat-bubble')
                .text(message.content);
            
            // 메타 정보 컨테이너 (안 읽은 수 + 시간)
            const metaContainer = $('<div>').addClass('message-meta-container');
            
            // 안 읽은 수
            if (message.unread_count > 0) {
                const unreadDiv = $('<div>')
                    .addClass('unread-count')
                    .text(message.unread_count);
                metaContainer.append(unreadDiv);
            }
            
            // 시간
            const timeDiv = $('<div>')
                .addClass('message-time')
                .text(formatDate(message.sent_at || ''));
            metaContainer.append(timeDiv);
            
            messageInfo.append(bubbleDiv);
            messageInfo.append(metaContainer);
            messageContent.append(messageInfo);
            
        } else {
            // 상대방 메시지 (왼쪽)
            // 이름
            const nameDiv = $('<div>')
                .addClass('sender-name')
                .text(message.sender_name || '알 수 없음');
            messageContent.append(nameDiv);
            
            const messageInfo = $('<div>').addClass('message-info');
            
            // 메시지 버블
            const bubbleDiv = $('<div>')
                .addClass('chat-bubble')
                .text(message.content);
            
            // 메타 정보 컨테이너 (안 읽은 수 + 시간)
            const metaContainer = $('<div>').addClass('message-meta-container');
            
            // 안 읽은 수 (받은 메시지에도 표시)
            if (message.unread_count > 0) {
                const unreadDiv = $('<div>')
                    .addClass('unread-count')
                    .text(message.unread_count);
                metaContainer.append(unreadDiv);
            }
            
            // 시간
            const timeDiv = $('<div>')
                .addClass('message-time')
                .text(formatDate(message.sent_at || ''));
            metaContainer.append(timeDiv);
            
            messageInfo.append(bubbleDiv);
            messageInfo.append(metaContainer);
            messageContent.append(messageInfo);
        }
        
        messageDiv.append(messageContent);
        chatMessages.append(messageDiv);
        
        // message_id가 있는 경우에만 중복 방지 세트에 추가
        if (message.message_id) {
            chat.messages.displayed.add(message.message_id);
}

        // 다른 사용자의 메시지인 경우 읽음 처리
        if (message.sender_num != chat.user.id && message.message_num) {
            markMessageAsRead(message.message_num);
        }
        
        chatMessages.scrollTop(chatMessages[0].scrollHeight);
    }

    /*============================
     * 메시지 읽음 처리
     *============================*/
    function markMessageAsRead(messageNum) {
        $.ajax({
            url: contextPath + 'chat/messages/' + messageNum + '/read?room_num=' + chat.room.id,
            type: 'POST',
            beforeSend: function(xhr) {
                xhr.setRequestHeader($('meta[name="csrf-header"]').attr('content'),
                                     $('meta[name="csrf-token"]').attr('content'));
            },
            success: function(response) {
                if (response.result === 'success') {
                    console.log('메시지 읽음 처리 성공:', messageNum, '업데이트된 안 읽은 수:', response.unread_count);
                    // 서버에서 자동으로 WebSocket 브로드캐스트하므로 클라이언트 전송 제거
                }
            },
            error: function(xhr) {
                console.error('메시지 읽음 처리 실패:', xhr);
            }
        });
    }

    function updateUnreadCount(messageNum, newUnreadCount) {
        // 해당 메시지의 안 읽은 수를 정확한 값으로 업데이트
        const messageRow = $(`[data-message-num="${messageNum}"]`);
        if (messageRow.length) {  // 'me' 클래스 체크 제거
            const unreadSpan = messageRow.find('.unread-count');
            
            if (newUnreadCount > 0) {
                if (unreadSpan.length) {
                    // 기존 안 읽은 수 업데이트
                    unreadSpan.text(newUnreadCount);
                } else {
                    // 안 읽은 수 표시가 없으면 새로 생성
                    const metaContainer = messageRow.find('.message-meta-container');
                    const newUnreadSpan = $('<div>').addClass('unread-count').text(newUnreadCount);
                    metaContainer.prepend(newUnreadSpan);
                }
            } else {
                // 안 읽은 수가 0이면 표시 제거
                if (unreadSpan.length) {
                    unreadSpan.remove();
                }
            }
        }
    }

    /*============================
     * 유틸리티 함수
     *============================*/
    function formatTime(date) {
        if (!date) return '';
        
        if (typeof date === 'string') {
            // "YYYY-MM-DD HH24:MI:SS" 형식을 "YYYY-MM-DD HH:MM:SS"로 변환
            date = date.replace(/(\d{4}-\d{2}-\d{2}) (\d{2}):(\d{2}):(\d{2})/, '$1T$2:$3:$4');
            date = new Date(date);
        }
        
        if (isNaN(date.getTime())) {
            return '';
        }
        
        return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
    }

    function formatDate(date) {
        // DurationFromNow.getTimeDiffLabel()에서 이미 처리된 문자열을 그대로 반환
        return date || '';
    }

    /*============================
     * 날짜 관련 유틸리티 함수들
     *============================*/
    function getDateFromSentAt(sent_at) {
        // sent_at이 "오후 2:30" 형식이므로 현재 날짜를 사용
        const today = new Date();
        return today.toDateString();
    }

    function formatDateHeader(date) {
        const today = new Date();
        const yesterday = new Date(today);
        yesterday.setDate(yesterday.getDate() - 1);
        
        // date가 문자열이면 Date 객체로 변환
        let messageDate;
        if (typeof date === 'string') {
            messageDate = new Date(date);
        } else {
            messageDate = date;
        }
        
        // 유효한 날짜인지 확인
        if (isNaN(messageDate.getTime())) {
            return "오늘"; // 기본값
        }
        
        if (messageDate.toDateString() === today.toDateString()) {
            return "오늘";
        } else if (messageDate.toDateString() === yesterday.toDateString()) {
            return "어제";
        } else {
            return messageDate.toLocaleDateString('ko-KR', { 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric',
                weekday: 'long'
            });
        }
    }

    function groupMessagesByDate(messages) {
        const grouped = {};
        
        messages.forEach(message => {
            // sent_date 필드를 사용하여 날짜별로 그룹화
            let dateKey = message.sent_date;
            
            // sent_date가 없으면 현재 날짜 사용
            if (!dateKey) {
                dateKey = new Date().toISOString().split('T')[0]; // YYYY-MM-DD 형식
            }
            
            if (!grouped[dateKey]) {
                grouped[dateKey] = [];
            }
            grouped[dateKey].push(message);
        });
        
        return grouped;
    }

    function renderMessagesWithDateHeaders(messages, currentUserNum) {
        if (!messages || messages.length === 0) {
            return `<div class="no-message">
                <p>아직 메시지가 없습니다.</p>
                <p>첫 번째 메시지를 보내보세요!</p>
            </div>`;
        }

        const grouped = groupMessagesByDate(messages);
        let html = '';

        // 날짜 키를 정렬하여 순서대로 표시
        const sortedDateKeys = Object.keys(grouped).sort();
        
        sortedDateKeys.forEach(dateKey => {
            const dateMessages = grouped[dateKey];
            const date = new Date(dateKey);
            
            // 날짜 헤더 추가
            html += `<div class="date-header">
                <div class="date-header-line"></div>
                <div class="date-header-text">${formatDateHeader(date)}</div>
                <div class="date-header-line"></div>
            </div>`;

            // 해당 날짜의 메시지들 렌더링
            dateMessages.forEach(message => {
                if (message.sender_num == currentUserNum) {
                    // 내 메시지
                    html += `
                        <div class="chat-message-row me" data-message-num="${message.message_num}">
                            <div class="message-content">
                                <div class="message-info">
                                    <div class="chat-bubble">${message.content}</div>
                                    <div class="message-meta-container">
                                        ${message.unread_count > 0 ? 
                                            `<div class="unread-count">${message.unread_count}</div>` : 
                                            ''
                                        }
                                        <div class="message-time">${formatDate(message.sent_at)}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
    `;
                } else {
                    // 상대방 메시지
                    html += `
                        <div class="chat-message-row other" data-message-num="${message.message_num}">
                            <div class="message-content">
                                <div class="sender-name">${message.sender_name || '알 수 없음'}</div>
                                <div class="message-info">
                                    <div class="chat-bubble">${message.content}</div>
                                    <div class="message-meta-container">
                                        ${message.unread_count > 0 ? 
                                            `<div class="unread-count">${message.unread_count}</div>` : 
                                            ''
                                        }
                                        <div class="message-time">${formatDate(message.sent_at)}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                }
            });
        });

        return html;
    }

    /*============================
     * 이벤트 핸들러
     *============================*/
    $(document).on('keypress', '#messageInput', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        sendMessage();
    }
    });

    /*============================
     * 채팅방 입장/퇴장
     *============================*/
    function enterChatRoom(roomId, $btn) {
        console.log('채팅방 입장 시도:', roomId);
        
        leaveChat();
        
        $btn.prop('disabled', true).text('입장중...');
        
        $.ajax({
            url: contextPath + 'chat/enter/' + roomId,
            type: 'GET',
            beforeSend: function(xhr) {
                xhr.setRequestHeader($('meta[name="csrf-header"]').attr('content'),
                                     $('meta[name="csrf-token"]').attr('content'));
            },
            success: function(response) {
                if (response.result === 'success') {
                    console.log('채팅방 입장 성공:', response);
                    
                    // 먼저 채팅방 UI를 렌더링
                    renderChatRoom(response, $btn);
                    
                    // WebSocket 연결 초기화
                    initChat(response.room.room_num, response.currentUserNum, response.currentUserName);
                    
                    // 초기 메시지들을 화면에 표시
                    if (response.messageList && response.messageList.length > 0) {
                        console.log('초기 메시지 개수:', response.messageList.length);
                        response.messageList.forEach(msg => {
                            // 초기 메시지에는 message_id가 없으므로 message_num을 사용
                            if (!msg.message_id) {
                                msg.message_id = 'init_' + msg.message_num;
                            }
                            displayMessage(msg);
                        });
                    }
                    
                } else {
                    console.error('채팅방 입장 실패:', response);
                    alert(response.message || '채팅방 입장에 실패했습니다.');
                }
            },
            error: function(xhr) {
                try {
                    const responseJson = JSON.parse(xhr.responseText);
                    alert(responseJson.message);
                } catch(e) {
                    alert('네트워크 오류 발생');
                }
                console.error('Error:', xhr.status, xhr.responseText);
            },
            complete: function() {
                $btn.prop('disabled', false).text('입장');
            }
        });
    }

    function renderChatRoom(response, $btn) {
        const chatHtml = `
            <div class="chat-view-wrapper">
                <div class="chat-header">
                    <div class="chat-header-title">
                        <span>${response.room.room_name}</span>
                    </div>
                    <div class="chat-header-actions">
                        <button class="chat-header-btn" title="멤버 목록" onclick="toggleMemberList()">
                            <span>👥</span>
                        </button>
                    </div>
                </div>
                
                <div class="chat-member-list" id="memberList" style="display: none;">
                    <div class="member-list-header">
                        <h4>멤버 목록</h4>
                        <button onclick="toggleMemberList()" class="close-btn">×</button>
                    </div>
                    <div class="member-list-content">
                        ${response.memberList ? response.memberList.map(member => `
                            <div class="member-item">
                                <span class="member-name">${member.user_name}</span>
                                <span class="member-role">${member.role}</span>
                            </div>
                        `).join('') : ''}
                    </div>
                </div>
                
                <div class="chat-messages" id="chatMessages">
                    <!-- 메시지는 displayMessage 함수로 동적 추가 -->
                </div>
                
                <div class="chat-input-area">
                    <input type="text" class="chat-input" id="messageInput" 
                           placeholder="메시지를 입력하세요..." autocomplete="off">
                    <button type="button" class="chat-send-btn" onclick="sendMessage()">보내기</button>
                </div>
            </div>
        `;
        
        $('#chatRoomViewContainer').html(chatHtml);
        
        $('.enter-btn').removeClass('active');
        $btn.addClass('active');
        
        $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);
    }

    /*============================
     * 멤버 목록 토글
     *============================*/
    function toggleMemberList() {
        $('#memberList').toggle();
    }

    // 전역 함수로 노출
    window.enterChatRoom = enterChatRoom;
    window.toggleMemberList = toggleMemberList;
    window.sendMessage = sendMessage;
}); 