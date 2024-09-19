import "./PublicNew.css";
import Button from "./Buttons";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getStringedDate } from "../util/getStringedDate";

const PublicNew = ({ initData, onSubmit }) => {
  const nav = useNavigate();
  const [input, setInput] = useState({
    createdDate: new Date(),
    title: "",
    content: "",
    writer: "admin",
  });

  useEffect(() => {
    if (initData) {
      setInput({
        ...initData,
        createdDate: new Date(Number(initData.createdDate)),
      });
    }
  }, [initData]);

  const onChangeInput = (e) => {
    let input_name = e.target.name;
    let input_value = e.target.value;

    if (input_name === "createdDate") {
      input_value = new Date(input_value);
    }

    setInput({
      ...input,
      [input_name]: input_value,
    });
  };

  const onSubmitClickbutton = () => {
    onSubmit(input);
  };

  return (
    <div className="Editor">
      <section className="date_section">
        <h4>오늘의 날짜</h4>
        <input
          name="createdDate"
          onChange={onChangeInput}
          value={getStringedDate(input.createdDate)}
          type="date"
        />
      </section>

      <section className="content_title">
        <h4>제목</h4>
        <input
          className="input_title"
          name="title"
          onChange={onChangeInput}
          type="text"
          value={input.title}
          placeholder="제목을 입력하세요."
        />
      </section>

      <section className="content_section">
        <h4>내용</h4>
        <textarea
          name="content"
          value={input.content}
          onChange={onChangeInput}
          placeholder="내용을 입력해주세요."
        />
      </section>
      <section className="button_section">
        <Button onClick={() => nav(-1)} text={"취소하기"} />
        <Button
          onClick={onSubmitClickbutton}
          text={"작성완료"}
          type={"POSITIVE"}
        />
      </section>
    </div>
  );
};

export default PublicNew;
