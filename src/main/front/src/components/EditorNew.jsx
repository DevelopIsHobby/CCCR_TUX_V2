import "./EditorNew.css";
import EmotionItem from "./EmotionItem";
import Button from "./Buttons";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { emotionList } from "../util/constants";
import { getStringedDate } from "../util/getStringedDate";
const EditorNew = ({ initData, onSubmit }) => {
  const nav = useNavigate();
  const [input, setInput] = useState({
    createdDate: new Date(),
    emotionId: 3,
    name: "",
    content: "",
    instructor: "",
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
      <section className="emotion_section">
        <h4>오늘의 감정</h4>
        <div className="emotion_list_wrapper">
          {emotionList.map((item) => (
            <EmotionItem
              onClick={() =>
                onChangeInput({
                  target: {
                    name: "emotionId",
                    value: item.emotionId,
                  },
                })
              }
              key={item.emotionId}
              {...item}
              isSelected={item.emotionId === input.emotionId}
            />
          ))}
        </div>
      </section>
        <section className="content_name">
          <h4>오늘의 강사님</h4>
          <input name="instructor" value={input.instructor} type="text" onChange={onChangeInput} placeholder="피드백하고 싶은 강사님의 이름을 적어주세요." />
        </section>

      <section className="content_section">
        <h4>오늘의 피드백</h4>
        <textarea
          name="content"
          value={input.content}
          onChange={onChangeInput}
          placeholder="오늘은 어땠나요?"
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
export default EditorNew;
