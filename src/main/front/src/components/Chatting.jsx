import React, { useState } from "react";
import "./Chatting.css"; // CSS 파일을 임포트합니다
import Button from "../components/Buttons"; // Button 컴포넌트를 임포트합니다

const Chatting = () => {
  const [inputMessage, setInputMessage] = useState("");
  const [messages, setMessages] = useState([]);

  const sendMessage = () => {
    if (inputMessage.trim() === "") return; // 빈 메시지 전송 방지
    setMessages([...messages, inputMessage]);
    setInputMessage(""); // 메시지 전송 후 입력창 비우기
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      e.preventDefault(); // 기본 엔터키 동작(줄바꿈)을 방지합니다
      sendMessage();
    }
  };

  return (
    <div className="ChatBox">
      <div className="ChatMessages">
        {messages.map((msg, index) => (
          <div key={index} className="ChatMessage">
            {msg}
          </div>
        ))}
      </div>
      <div className="ChatInputSection">
        <input
          type="text"
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="메시지를 입력하세요..."
        />
        <Button text="전송" onClick={sendMessage} />
      </div>
    </div>
  );
};

export default Chatting;
