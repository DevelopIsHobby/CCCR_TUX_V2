import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Button from './Buttons'; // 버튼 컴포넌트
import "./ChattingItem.css"

const ChatRoomList = () => {
    const [roomName, setRoomName] = useState('');
    const [chatrooms, setChatrooms] = useState([]);
    const [ prevRoomId, setPrevRoomId ] = useState('');
    const nav = useNavigate();

    useEffect(() => {
        findAllRoom();
    }, []);

    const findAllRoom = async () => {
        try {
            const response = await axios.get('/chat/rooms');
            setChatrooms(response.data);
        } catch (error) {
            console.error('Failed to fetch chat rooms', error);
        }
    };

    const createRoom = async () => {
        if (roomName.trim() === '') {
            alert('방 제목을 입력해 주십시요.');
            return;
        }

        try {
            const params = new URLSearchParams();
            params.append('name', roomName);

            const response = await axios.post('/chat/room', params);
            alert(`${response.data.name} 방 개설에 성공하였습니다.`);
            setRoomName('');
            findAllRoom();
        } catch (error) {
            alert('채팅방 개설에 실패하였습니다.');
        }
    };

    const deleteRoom = async (roomId, roomName) => {
        try {
            await axios.delete(`/chat/room/${roomId}`);
            alert("채팅방이 삭제되었습니다.");
            findAllRoom();
        } catch(error) {
            alert("채팅방 삭제에 실패했습니다.");
        }
    }

    const enterRoom = async (roomId, roomName) => {
        const response = await fetch(`/chat/room/${roomId}`)

        if(response.status === 403) {
            alert("이 대화방에 접근할 수 없습니다.");
            return;
        }
        const chatRoom = await response.json();

        localStorage.setItem('wschat.roomId', roomId);
        localStorage.setItem('wschat.roomName', roomName);

        const savedChatRoomState = localStorage.getItem('chatRoomState');
        if (savedChatRoomState) {
            const { prevRoomId } = JSON.parse(savedChatRoomState);
            setPrevRoomId(prevRoomId);
            if(roomId === prevRoomId) {
                localStorage.setItem("sameRoomId", true);
            }
        }
        nav(`/chat/room/enter/${roomId}`);
    };



    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6">
                    <h3>채팅방 리스트</h3>
                </div>
            </div>
            <div className="input-group mb-3">
                <div className="input-group-prepend">
                    <span className="input-group-text">방제목</span>
                </div>
                <input
                    type="text"
                    className="form-control"
                    value={roomName}
                    onChange={(e) => setRoomName(e.target.value)}
                    onKeyUp={(e) => {
                        if (e.key === 'Enter') {
                            createRoom();
                        }
                    }}
                />
                <div className="input-group-append">
                    <Button
                        className="btn btn-primary"
                        text="채팅방 개설"
                        type="POSITIVE"
                        onClick={createRoom}
                    />
                </div>
            </div>
            <ul className="list-group">
                {chatrooms.map((item) => (
                    <li
                        key={item.roomId}
                        className="list-group-item list-group-item-action"
                    >
                        <h4>
                            {item.name} <span className="badge badge-info badge-pill">{item.userCount}</span>
                        </h4>
                        <div className="btn-group">
                            <Button
                                className="btn-info"
                                text="입장"
                                type={"ENTER"}
                                onClick={() => enterRoom(item.roomId, item.name)}
                            />
                            <Button
                                className="btn-danger"
                                text="삭제"
                                type={"NEGATIVE"}
                                onClick={() => {
                                    if (window.confirm('채팅방을 삭제하시겠습니까?')) {
                                        deleteRoom(item.roomId);
                                    }
                                }}
                            />
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ChatRoomList;
