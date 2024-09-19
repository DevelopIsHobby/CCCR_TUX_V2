import {useReducer, useRef, createContext, useState, useEffect, useContext} from "react";
import "./App.css";
import Home from "./pages/Home";
import Diary from "./pages/Diary";
import New from "./pages/New";
import { Routes, Route } from "react-router-dom";
import Notfound from "./pages/Notfound";
import Editor from "./pages/Editor";
import Public from "./pages/Public";
import PublicDetail from "./pages/PublicDetail";
import PublicEditor from "./pages/PublicEditor";
import Chat from "./pages/Chat";
import ChatRoom from "./pages/ChatRoom.jsx";
import { useNavigate } from "react-router-dom";
import PublicNewPage from "./pages/PublicNewPage.jsx";
import LoginForm from "./pages/LoginForm.jsx";
import SignupForm from "./pages/SginupForm.jsx";
import myPage from "./pages/MyPage.jsx"
import MyPage from "./pages/MyPage.jsx";

export const DiaryStateContext = createContext();
export const DiaryDispatchContext = createContext();
export const PublicStateContext = createContext();
export const PublicDispatchContext = createContext();


function App() {

  const [data, setData] = useState([]);
  const nav = useNavigate();
  const [publicData, setPublicData] = useState([]);

  useEffect(() => {
    fetchFeedData(); // 컴포넌트가 마운트될 때 데이터 가져오기
    fetchPublicData();
  }, []);

  // 새로운 피드를 추가한 후, 데이터를 다시 가져와서 상태를 업데이트
  const fetchFeedData = async () => {
    try {
      const response = await fetch("/feed/list");
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();

      // 데이터를 mockData 형식으로 변환
      const transformedData = result.map(item => ({
        id: item.fno,
        createdDate: new Date(item.modDate).getTime(),
        emotionId: item.emotionId,
        name: item.name,
        instructor: item.instructor,
        content: item.content,
      }));

      setData(transformedData); // 변환된 데이터를 상태로 설정
    } catch (error) {
      console.log("Failed to fetch feed data:", error);
    }
  };

  // 새로운 피드를 추가한 후, 데이터를 다시 가져와서 상태를 업데이트
  const fetchPublicData = async () => {
    try {
      const response = await fetch("/public/list");
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();

      // 데이터를 mockData 형식으로 변환
      const transformedData = result.map(item => ({
        id: item.pno,
        createdDate: new Date(item.modDate).getTime(),
        title: item.title,
        writer: item.writer,
        content: item.content,
      }));

      setPublicData(transformedData); // 변환된 데이터를 상태로 설정
    } catch (error) {
      console.log("Failed to fetch feed data:", error);
    }
  };


  // 새로운 일기 추가
  const onCreate = async (createdDate, emotionId, name, content, instructor) => {
    try {
      const response = await fetch("/feed/register", {
        method: "POST",
        headers : {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          createdDate,
          emotionId,
          name,
          content,
          instructor
        }),
      });
      if(!response.ok) {
        throw new Error("Failed to create feed");
      }

    } catch(error) {
      console.error("Failed to crate feed: " , error);
    }
    // 데이터를 다시 가져옴
    await fetchFeedData();

    // 페이지를 홈으로 이동
    nav("/", { replace: true });
  };

  // 기존 일기 수정
  const onUpdate = async (id, createdDate, emotionId, name, content, instructor) => {
    try {
      const response = await fetch("/feed/modify", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          fno: id,  // 수정할 피드의 ID를 전달
          createdDate,
          emotionId,
          name,
          content,
          instructor,
        })
      })
    if(!response.ok) {
      throw new Error("Failed to update feed");
    }

      // 데이터를 다시 가져옴
      await fetchFeedData();

      // 페이지를 홈으로 이동
      nav("/", { replace: true });

    } catch (error) {
      console.error("Failed to update feed", error);
    }
  };
  // 기존 일기 삭제
  const onDelete = async (id) => {
    try {
      const response = await fetch("/feed/remove", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({fno: id}),
      });

      if (!response.ok) {
        throw new Error("Failed to delete feed");
      }

      // 데이터를 다시 가져옴
      await fetchFeedData();

      // 페이지를 홈으로 이동
      nav("/", { replace: true });
    } catch(error) {
      console.log("Failed to delete feed: " , error);
    }
  }
  // 공지사항 추가, 수정, 삭제
  const onCreatePublic = async (createdDate, title, content, writer) => {
    try {
      const response = await fetch("/public/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          createdDate,
          title,
          content,
          writer
        }),
      });
      if (!response.ok) {
        throw new Error("Failed to create public");
      }
    } catch (error) {
      console.log("Failed to create public : ", error);
    }
    // 데이터를 다시 가져옴
    await fetchPublicData();

    // 페이지를 홈으로 이동
    nav("/public", { replace: true });
  }


  const onUpdatePublic = async (id, createdDate, title, content, writer) => {
    try {
      const response = await fetch("/public/modify", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          pno: id,
          createdDate,
          title,
          content,
          writer,
        })
      })
      if(!response.ok) {
        throw new Error("Failed to update feed");
      }
      await fetchPublicData();
      nav("/public", {replace:true});
    } catch (error) {
      console.error("Failed to update public:" ,error);
    }
  };

  const onDeletePublic = async (id) => {
    try {
      const response = await fetch("/public/remove", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({pno: id}),
      });

      if(!response.ok) {
        throw new Error("Failed to delete public");
      }

      await fetchPublicData();
      nav("/public", {replace: true})
    } catch(error) {
      console.log("Failed to delete public: " , error);
    }
  };

  return (
    <>
      <DiaryStateContext.Provider value={data}>
        <DiaryDispatchContext.Provider value={{ onCreate, onUpdate, onDelete }}>
          <PublicStateContext.Provider value={publicData}>
            <PublicDispatchContext.Provider
              value={{ onCreatePublic, onUpdatePublic, onDeletePublic }}>
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/new" element={<New />} />
                <Route path="/diary/:id" element={<Diary />} />
                <Route path="/Editor/:id" element={<Editor />} />
                <Route path="/public" element={<Public />} />
                <Route path="/publicDetail/:id" element={<PublicDetail />} />
                <Route path="/publicEditor/:id" element={<PublicEditor />} />
                <Route path="/publicNew" element={<PublicNewPage />} />
                <Route path="/chat" element={<Chat />} />
                <Route path="/chat/room/enter/:roomId" element={<ChatRoom />} />
                <Route path="/loginForm" element={<LoginForm />} />
                <Route path="/signup" element={<SignupForm />} />
                <Route path="myPage" element={<MyPage />} />
                <Route path="*" element={<Notfound />} />
              </Routes>
            </PublicDispatchContext.Provider>
          </PublicStateContext.Provider>
        </DiaryDispatchContext.Provider>
      </DiaryStateContext.Provider>
    </>
  );
}

export default App;
