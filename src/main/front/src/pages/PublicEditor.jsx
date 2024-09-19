import { useParams, useNavigate } from "react-router-dom";
import { PublicDispatchContext, PublicStateContext } from "../App";
import { useContext, useEffect, useState } from "react";
import usePublic from "../hooks/usePublic";
import InnerHeader from "../components/InnerHeader";
import Button from "../components/Buttons";
import PublicNew from "../components/PublicNew";
const PublicEditor = () => {
  const params = useParams();
  const nav = useNavigate();
  const { onDeletePublic, onUpdatePublic } = useContext(PublicDispatchContext);

  const curPublicItem = usePublic(params.id);
  const onClickDelete = () => {
    if (window.confirm("공지를 정말 삭제할까요? 다시 복구되지 않아요!")) {
      onDeletePublic(params.id);
    }
  };

  const onSubmit = (input) => {
    if (window.confirm("피드백을 정말 수정할까요?")) {
      onUpdatePublic(
        params.id,
        input.createdDate.getTime(),
        input.title,
        input.content,
          input.writer
      );
    }
  };
  return (
    <div>
      <InnerHeader
        title={"공지사항 수정하기"}
        leftChild={<Button onClick={() => nav(-1)} text={"< 뒤로 가기"} />}
        rightChild={
          <Button onClick={onClickDelete} text={"삭제하기"} type={"NEGATIVE"} />
        }
      />
      <PublicNew onSubmit={onSubmit} initData={curPublicItem} />
    </div>
  );
};

export default PublicEditor;
