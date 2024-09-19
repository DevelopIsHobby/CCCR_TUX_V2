import Header from "../components/Header";
import Button from "../components/Buttons";
import EditorNew from "../components/EditorNew";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { DiaryDispatchContext } from "../App";
import InnerHeader from "../components/InnerHeader.jsx";

const New = () => {
  const { onCreate } = useContext(DiaryDispatchContext);
  const nav = useNavigate();
  const onSubmit = (input) => {
    onCreate(input.createdDate.getTime(), input.emotionId, input.name, input.content, input.instructor);
    nav("/", { replace: true });
  };

  return (
    <div>
      <InnerHeader
        leftChild={<Button onClick={() => nav(-1)} text={"< 뒤로가기"} />}
        title={"새 피드백 쓰기"}
      />
      <EditorNew onSubmit={onSubmit} />
    </div>
  );
};
export default New;
