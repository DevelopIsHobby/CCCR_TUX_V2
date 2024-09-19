import "./DiaryItem.css";
import { getEmotionImage } from "../util/get-emtion-image";
import Button from "./Buttons";
import { useNavigate } from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
const DiaryItem = ({ id, emotionId, createdDate, name, content, instructor }) => {
  const nav = useNavigate();
    const [currentName, setCurrentName] = useState('');

    useEffect(() => {
        // 현재 사용자 정보를 가져오는 API 호출
        axios.get('/users/user')
            .then(response => {
                setCurrentName(response.data.name);
            })
            .catch(error => {
                console.error("Error fetching user data", error);
            });
    }, []);

    const handleButtonClick = () => {
        console.log("name : "+ name);
        console.log("CurrentName : " + currentName)
        if (name === currentName) { // admin 이메일 확인
            nav(`/editor/${id}`)
        } else {
            alert("수정 권한이 없습니다.");
        }
    };

  return (
    <div className="DiaryItem">
      <div
        onClick={() => nav(`/diary/${id}`)}
        className={`img_section img_section_${emotionId}`}
      >
        <img src={getEmotionImage(emotionId)} />
      </div>
        <div onClick={() => nav(`/diary/${id}`)} className="info_section">
            <div className="created_date">
                {new Date(createdDate).toLocaleDateString()}
            </div>
            <div className="instructor">{instructor}</div>
            <div className="name">{name}</div>
            <div className="diary-content">{content}</div>
        </div>
        <div onClick={handleButtonClick} className="button_section">
        <Button text={"수정하기"} />
      </div>
    </div>
  );
};

export default DiaryItem;
