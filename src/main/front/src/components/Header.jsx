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
    const [sensorData, setSensorData] = useState(null);
    const [sensorDataVisible, setSensorDataVisible] = useState(false); // 센서 데이터 표시 여부 상태 추가


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

    const fetchSensorData = () => {
        if (sensorDataVisible) {
            // 센서 데이터가 보이는 경우, 데이터를 숨김
            setSensorData(null);
        } else {
            // 센서 데이터가 보이지 않는 경우, API 호출
            axios.get("/condition/data") // 센서 데이터 API 호출
                .then(response => {
                    setSensorData(response.data); // 받아온 데이터를 상태에 저장
                })
                .catch(error => {
                    console.error("Error fetching sensor data", error);
                });
        }
        setSensorDataVisible(!sensorDataVisible); // 상태 토글
    };

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
          <Button text={"교육장 환경 확인"} type={"SENSOR"} onClick={fetchSensorData}/>
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
        {/* 센서 데이터 표시 */}
        {sensorData && (
            <div className="sensor-data">
                <h3>현재 교육장 환경</h3>
                <p>온도: {sensorData.temp} °C</p>
                <p>습도: {sensorData.humidity} %</p>
                <p>미세먼지(10um 이하): {sensorData.miniDust} (0.1uL당 입자 수)</p>
                <p>초미세먼지(2.5um 이하): {sensorData.superMiniDust} (0.1uL당 입자 수)/</p>
                <p>CO2: {sensorData.co2} ppm</p>
                <p>시간: {sensorData.time}</p>
            </div>
        )}
    </>
  );
};

export default Header;
