import React, {useState, useEffect, useRef} from 'react';
import axios from 'axios';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './../components/ChatRoom.css'; // 스타일을 위한 CSS 파일
import Button from "../components/Buttons.jsx"; // 버튼 컴포넌트
import "../components/ChatRoom.css"
import {useNavigate} from 'react-router-dom';


const ChatRoom = () => {
    const [roomId, setRoomId] = useState('');
    const [roomName, setRoomName] = useState('');
    const [message, setMessage] = useState('');
    const [messages, setMessages] = useState([]);
    const [token, setToken] = useState('');
    const [name, setName] = useState('');
    const [userCount, setUserCount] = useState(0);
    const [ws, setWs] = useState(null);
    const isEntered = useRef(false);
    const navigate = useNavigate();
    const [isVisited, setIsVisited] = useState(false);
    const sameRoomId = useRef(false);
    const [currentUser, setCurrentUser] = useState(null);

    // 현재 사용자 정보를 가져오는 함수
    useEffect(() => {
        const fetchCurrentUser = async () => {
            try {
                const response = await axios.get("/users/current-user")
                setCurrentUser(response.data);
            } catch (error) {
                console.log("Failed to fetch current user", error);
            }
        }
        fetchCurrentUser()
    }, []);

    useEffect(() => {
        // 저장된 채팅방 상태가 있으면 복원
        const savedChatRoomState = localStorage.getItem('chatRoomState');
        if (savedChatRoomState) {
            const {prevRoomId, roomName, messages} = JSON.parse(savedChatRoomState);
            setRoomId(prevRoomId);
            setRoomName(roomName);
            setMessages(messages);
            sameRoomId.current = JSON.parse(localStorage.getItem('sameRoomId'))
            console.log("sameRoomId: " + sameRoomId.current);
            if (sameRoomId) {
                setIsVisited(true);
            }
            localStorage.removeItem('chatRoomState'); // 복원 후 상태 삭제
            localStorage.removeItem('sameRoomId');
        } else {
            // 방 ID와 이름을 로컬 스토리지에서 가져오기
            setRoomId(localStorage.getItem('wschat.roomId'));
            setRoomName(localStorage.getItem('wschat.roomName'));
        }

        // WebSocket 및 Stomp 초기화
        const sock = new SockJS("/ws-stomp");
        const stompClient = Stomp.over(sock);
        setWs(stompClient);

        axios.get('/chat/user').then(response => {
            const userToken = response.data.token;
            setToken(userToken);
            setName(response.data.name);

            stompClient.connect({"token": userToken}, frame => {
                stompClient.subscribe(`/sub/chat/room/${roomId}`, message => {
                    const recv = JSON.parse(message.body);
                    recvMessage(recv);
                });
                isEntered.current = true
            }, error => {
                alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시오.");
            });
        });

        return () => {
            // 컴포넌트가 언마운트될 때 WebSocket 연결 해제
            // if (ws) {
            //     ws.disconnect();
            // }
        };
    }, [roomId]);


    const sendMessage = (type) => {
        if (ws && ws.connected) {
            const messagePayload = JSON.stringify({type, roomId, message, name})
            ws.send("/pub/chat/message", {"token": token}, messagePayload);
            setMessage('');
        }
    };

    const recvMessage = (recv) => {
        if (recv.type === 'ENTER' && !isEntered.current) {
            setUserCount(recv.userCount)

            setMessages(prevMessages => [{
                "type": recv.type,
                "sender": recv.sender,
                "message": recv.message,
                "name": recv.name,
                "profilePictureUrl": recv.profilePictureUrl
            }, ...prevMessages]);
            isEntered.current = true
        } else {
            setUserCount(recv.userCount)
            setMessages(prevMessages => [{
                "type": recv.type,
                "sender": recv.sender,
                "message": recv.message,
                "name": recv.name,
                "profilePictureUrl": recv.profilePictureUrl
            }, ...prevMessages]);

        }
    };

    const saveChatRoomState = () => {
        const chatRoomState = {
            prevRoomId: roomId,
            roomName: roomName,
            messages: messages,
        };
        localStorage.setItem('chatRoomState', JSON.stringify(chatRoomState));
    };

    const handleExitChatRoom = () => {
        if (ws) {
            // WebSocket 연결 종료
            ws.disconnect(() => {
                console.log('WebSocket disconnected');
                // WebSocket 연결이 종료된 후 페이지 이동
                navigate('/chat');
            });
        } else {
            // WebSocket이 없을 경우 바로 페이지 이동
            navigate('/chat');
        }
    };
    const GoToList = () => {
        saveChatRoomState(); // 상태 저장
        handleExitChatRoom();
        // navigate("/chat");
    }

    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6">
                    <h3>{roomName} <span className="badge badge-info badge-pill">{userCount}</span></h3>
                </div>
                <div className="chatroom-button-group">
                    <button className="chat-button-exit" onClick={handleExitChatRoom}>채팅방 나가기</button>
                    <button className="chat-button-list" onClick={GoToList}>목록</button>
                </div>
            </div>
            <div className="input-group mb-3">
                <div className="input-group-prepend">
                    <span className="input-group-text">내용</span>
                </div>
                <input
                    type="text"
                    className="form-control"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    onKeyPress={(e) => e.key === 'Enter' && sendMessage('TALK')}
                />
                <div className="input-group-append">
                    <Button text="보내기" type="INDEX" onClick={() => sendMessage('TALK')}/>
                </div>
            </div>
            <ul className="list-group">
                {messages.map((msg, index) => (
                    <li key={index}
                        className={`list-group-item message-container ${(msg.sender === currentUser ? 'message-right' : 'message-left')} ${(msg.type === 'ENTER' || msg.type === 'QUIT') ? 'message-notification' : ''}`}>
                        <div className="message-box">
                            {msg.type === 'ENTER' || msg.type === 'QUIT' ? (
                                '알림: '
                            ) : (
                                <>
                <span className="sender">
                    <img
                        src={`/uploadPath/${msg.profilePictureUrl}`}
                        alt="profile"
                        className="profile-image"
                    />
                    {msg.name}
                </span>
                                </>
                            )}
                            {msg.message}
                        </div>
                    </li>

                ))}
            </ul>
        </div>
    );
};
export default ChatRoom;
