import Header from "../components/Header";
import Button from "../components/Buttons";
import { useState, useContext, useRef, useEffect } from "react";
import "./../components/DiaryList.css";
import ChattingList from "../components/ChattingList";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import "../components/Chat.css";


const Chat = () => {
    const [pivotDate, setPivotDate] = useState(new Date());
    const [friends, setFriends] = useState([]); // 친구 목록 상태
    const [isFriendListVisible, setIsFriendListVisible] = useState(false); // 친구 목록 표시 여부
    const [isAddingFriend, setIsAddingFriend] = useState(false); // 친구 추가 폼 표시 여부
    const [friendEmail, setFriendEmail] = useState(""); // 추가할 친구 이메일 상태
    const [error, setError] = useState(""); // 오류 메시지
    const nav = useNavigate();

    const onIncreaseMonth = () => {
        setPivotDate(new Date(pivotDate.getFullYear(), pivotDate.getMonth() + 1));
    };
    const onDecreaseMonth = () => {
        setPivotDate(new Date(pivotDate.getFullYear(), pivotDate.getMonth() - 1));
    };


    // 친구 목록을 가져오는 함수 (API 요청을 가정)
    const fetchFriends = async () => {
        try {
            const response = await axios.get("/users/friends"); // 친구 목록을 가져오는 API 요청
            setFriends(response.data);
        } catch (error) {
            console.error("친구 목록을 가져오는 중 오류 발생:", error);
        }
    };

    // 친구 목록 버튼 클릭 시 동작
    const toggleFriendsList = () => {
        if (!isFriendListVisible) {
            fetchFriends(); // 친구 목록 가져오기
        }
        setIsFriendListVisible(!isFriendListVisible); // 토글로 친구 목록 표시
    };

    // 친구 추가 버튼 클릭 시 폼 표시
    const toggleAddFriendForm = () => {
        setIsAddingFriend(!isAddingFriend);
    };

    // 친구 추가 API 호출
    const addFriend = async () => {
        try {
            await axios.post("/users/addFriend", { email: friendEmail }); // 친구 추가 API 요청
            fetchFriends(); // 친구 목록 갱신
            setIsAddingFriend(false); // 폼 닫기
            setFriendEmail(""); // 입력 필드 초기화
            setError(""); // 오류 메시지 초기화
        } catch (error) {
            setError("친구를 추가하는 중 오류가 발생했습니다.");
        }
    };

    // 친구 삭제 API 호출
    const deleteFriend = async (friendEmail, friendName) => {
        if(confirm(`${friendName}를 삭제하시겠습니까?`)) {
            try {
                console.log(friendEmail);
                await axios.post('/users/deleteFriend', { friendEmail }, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                setFriends(friends.filter(friend => friend.email !== friendEmail)); // 로컬 상태 업데이트
            } catch (error) {
                console.error("친구 삭제 중 오류 발생:", error);
            }
        }
    };

    // 일대일 대화방 생성
    const openChatRoom = async (friendEmail, friendName) => {
        if(confirm("일대일 대화방을 개설하시겠습니까?")) {
            console.log("friendEmail : " + friendEmail);
            try {
                // 채팅방 생성 요청
                const response = await axios.post("/chat/private-room", {email : friendEmail, name: friendName});
                const roomId = response.data.roomId;

                if(roomId !== null) {
                    alert(`${friendName}과의 대화방이 생성되었습니다.`)
                    window.location.reload();
                }
            } catch(error) {
                console.error("채팅방 생성 실패", error);
            }
        }
    }

    return (
        <div>
            <Header
                title={`${pivotDate.getFullYear()}년 ${
                    pivotDate.getMonth() + 1
                }월 대화방`}
                leftChild={<Button onClick={onDecreaseMonth} text={"<"} />}
                rightChild={<Button onClick={onIncreaseMonth} text={">"} />}
            />
            <div className="DiaryList">
                <div className="menu_bar">
                    <Button text={"친구추가"} onClick={toggleAddFriendForm} />
                    <Button text={"친구목록 확인"} type={"NAME"} onClick={toggleFriendsList} />
                </div>
            </div>

            {/* 친구 추가 폼 */}
            {isAddingFriend && (
                <div className="add-friend-form">
                    <h3>친구 추가</h3>
                    <input
                        type="email"
                        placeholder="친구 이메일 입력"
                        value={friendEmail}
                        onChange={(e) => setFriendEmail(e.target.value)}
                        onKeyPress={(e) => {
                            if (e.key === "Enter") {
                                addFriend(); // 엔터 키를 누르면 친구 추가 함수 호출
                            }
                        }}
                    />
                    <Button text="추가" onClick={addFriend} />
                    {error && <p className="error">{error}</p>}
                </div>
            )}

            {/* 친구 목록 표시 */}
            {isFriendListVisible && (
                <div className="friend-list">
                    <h3>친구 목록</h3>
                    {friends.length > 0 ? (
                        <ul>
                            {friends.map((friend) => (
                                <li key={friend.email} className="friend-item">
                                    <div className="friend-info">
                                        <img
                                            src={`/uploadPath/${friend.profilePictureURl}`}
                                            alt={`${friend.name}의 프로필 사진`}
                                            className="friend-profile-img"
                                        />
                                        <span
                                            className="friend-name"
                                            onClick={() => openChatRoom(friend.email,friend.name)} // 친구 이름 클릭 시 대화방 개설
                                            style={{cursor: "pointer", color: "blue"}} // 클릭 가능하게 스타일 적용
                                        >
                    {friend.name}
                </span>
                                    </div>
                                    <Button
                                        text="삭제"
                                        type={"DELETE"}
                                        onClick={() => deleteFriend(friend.email, friend.name)}
                                        className="delete-button"
                                    />
                                </li>
                            ))}
                        </ul>

                    ) : (
                        <p className={"no-friends"}>친구가 없습니다.</p>
                    )}
                </div>
            )}

            <ChattingList/>
        </div>
    );
};
export default Chat;
