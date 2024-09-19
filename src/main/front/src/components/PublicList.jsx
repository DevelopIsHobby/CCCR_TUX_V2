import "./PublicList.css";
import PublicItem from "./PublicItem";
import Button from "./Buttons.jsx";
import { useNavigate } from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";

const PublicList = ({ data }) => {
    const nav = useNavigate();
    const [email, setEmail] = useState('');

    useEffect(() => {
        // 현재 사용자 정보를 가져오는 API 호출
        axios.get('/users/user')
            .then(response => {
                setEmail(response.data.email);
            })
            .catch(error => {
                console.error("Error fetching user data", error);
            });
    }, []);

    const checkAdmin = () => {
        console.log(email);
        if(email === "admin@com.chattest") {
            nav("/publicNew");
        } else {
            alert("공지사항 생성 권한이 없습니다.");
        }
    }

  return (
    <div className="PublicList">
      <div className="list_wrapper">
          <div className="button">
              <Button type={"POSITIVE"} onClick={checkAdmin} text={"공지사항 생성하기"} />
          </div>
        {data.map((item) => (
          <PublicItem key={item.id} {...item} />
        ))}
      </div>
    </div>
  );
};

export default PublicList;
