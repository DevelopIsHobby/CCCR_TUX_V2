import "./Header.css";
import Button from "./Buttons";
import { useNavigate, useLocation } from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios";


const Header = ({ title, leftChild, rightChild }) => {
  const nav = useNavigate();
  const location = useLocation();
  const [selectedPage, setSelectedPage] = useState(location.pathname);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [profileImageUrl, setProfileImageUrl] = useState("");
  const [name, setName] = useState('');

  useEffect(() => {
    nav(selectedPage);
  }, [selectedPage, nav]);

  const handleNavigate = (path) => {
    setSelectedPage(path);
  };

  // 로그인 상태 체크
  useEffect(() => {
    axios.get("/users/check") // 인증된 사용자인지 확인하는 API
        .then(response => {
          setIsAuthenticated(response.data.authenticated); // 인증 상태 저장
        })
        .catch(() => {
          setIsAuthenticated(false);
        });
  }, []); // 빈 의존성 배열로 컴포넌트가 처음 렌더링될 때 한 번만 실행

  useEffect(() => {
    axios.get("/users/profile-picture")
        .then(response => {
          setProfileImageUrl(response.data); // 서버에서 받은 프로필 사진 URL을 상태에 저장합니다
        })
        .catch(() => {
          setProfileImageUrl(""); // 에러 발생 시 URL 초기화
        });
  }, []);

  useEffect(() => {
    // 현재 사용자 정보를 가져오는 API 호출
    axios.get('/users/user')
        .then(response => {
          setName(response.data.name);
        })
        .catch(error => {
          console.error("Error fetching user data", error);
        });
  }, []);

  const handleLogout = () => {
    axios.post("/logout") // Spring Security 로그아웃 처리
        .then(() => {
          setIsAuthenticated(false);
          nav("/loginForm"); // 로그아웃 후 로그인 페이지로 리다이렉트
        });
  };

  const onClickGoToMyPage = () => {
    nav("/myPage");
  }

  return (
    <>
      <header className="Header">
        <div className="header_left">{leftChild}</div>
        <div className="header_center">{title}</div>
        <div className="header_right">{rightChild}</div>
      </header>
      <div className="menu">
        <div className="index">
          <Button
              onClick={() => handleNavigate("/public")}
              text={"공지사항"}
              type={"INDEX"}
              isSelected={selectedPage === "/public"}
          />
          <Button
              onClick={() => handleNavigate("/chat")}
              text={"채팅방"}
              type={"INDEX"}
              isSelected={selectedPage === "/chat"}
          />
          <Button
              onClick={() => handleNavigate("/")}
              text={"피드백"}
              type={"INDEX"}
              isSelected={selectedPage === "/"}
          />
        </div>
        <div className="sensor">
          <Button text={"센서데이터"} type={"SENSOR"}/>
        </div>
        <div className="myPage">
          <ul className="myPage-menu">
            <li className={"myPage-menu-li"}>
              {isAuthenticated ? (
                  <a onClick={handleLogout}>Logout</a> // 로그아웃 처리
              ) : (
                  <a onClick={() => handleNavigate("/loginForm")}>Login</a> // 로그인 페이지로 이동
              )}
            </li>
            <li className={"myPage-menu-li"}>
              <a onClick={() => handleNavigate("/signup")}>Sign up</a>
            </li>
            <li className={"myPage-menu-li"}>
              <a onClick={onClickGoToMyPage} id="logo">
                {profileImageUrl ? (
                    <img src={`/uploadPath/${profileImageUrl}`}
                         alt="Profile" style={{width: '50px', height: '50px', borderRadius: '50%'}}/>
                ) : (
                    <span>Img Not Found</span>
                )} {name}
              </a>
            </li>
          </ul>
        </div>
      </div>
    </>
  );
};

export default Header;
