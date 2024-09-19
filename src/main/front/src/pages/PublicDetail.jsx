import { useParams, useNavigate } from "react-router-dom";
import { getStringedDate } from "../util/getStringedDate";
import usePublic from "../hooks/usePublic";
import InnerHeader from "../components/InnerHeader";
import Button from "../components/Buttons";
import PublicViewer from "../components/PublicViewer";
import {useState} from "react";
import axios from "axios";
const PublicDetail = () => {
  const params = useParams();
  const nav = useNavigate();
  const curPublicItem = usePublic(params.id);
  if (!curPublicItem) {
    return <div>데이터 로딩중...!</div>;
  }
  const { createdDate, title, content, writer } = curPublicItem;
  const today_date = getStringedDate(new Date(createdDate));

  const checkAdmin = () => {
      console.log(writer)
      if(writer === 'admin') {
          nav(`/publicEditor/${params.id}`);
      } else {
          alert("공지사항 수정 권한이 없습니다.")
      }
  }

  return (
    <div>
      <InnerHeader
        title={`공지사항`}
        leftChild={<Button onClick={() => nav(-1)} text={"< 뒤로가기"} />}
        rightChild={
          <Button
            onClick={checkAdmin}
            text={"수정하기"}
          />
        }
      />
      <PublicViewer date={today_date} title={title} content={content} />
    </div>
  );
};
export default PublicDetail;
