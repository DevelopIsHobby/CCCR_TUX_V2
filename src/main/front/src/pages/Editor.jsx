import { useParams, useNavigate } from "react-router-dom";
import InnerHeader from "../components/InnerHeader";
import Button from "../components/Buttons";
import EditorNew from "../components/EditorNew";
import { useContext, useEffect, useState } from "react";
import { DiaryDispatchContext, DiaryStateContext } from "../App";
import useDiary from "../hooks/useDiary";
const Editor = () => {
  const params = useParams();
  const nav = useNavigate();
  const { onDelete, onUpdate } = useContext(DiaryDispatchContext);

  const curDiaryItem = useDiary(params.id);
  const onClickDelete = () => {
    if (window.confirm("피드백을 정말 삭제할까요? 다시 복구되지 않아요!")) {
      onDelete(params.id);
    }
  };

  const onSubmit = (input) => {
    if (window.confirm("피드백을 정말 수정할까요?")) {
      onUpdate(
        params.id,
        input.createdDate.getTime(),
        input.emotionId,
        input.name,
        input.content,
          input.instructor
      );
    }
  };

  return (
    <div>
      <InnerHeader
        title={"피드백 수정하기"}
        leftChild={<Button onClick={() => nav(-1)} text={"< 뒤로 가기"} />}
        rightChild={
          <Button onClick={onClickDelete} text={"삭제하기"} type={"NEGATIVE"} />
        }
      />
      <EditorNew onSubmit={onSubmit} initData={curDiaryItem} />
    </div>
  );
};
export default Editor;
