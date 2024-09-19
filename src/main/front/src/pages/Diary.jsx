import { useParams, useNavigate } from "react-router-dom";
import InnerHeader from "../components/InnerHeader";
import Button from "../components/Buttons";
import Viewer from "../components/Viewer";
import useDiary from "../hooks/useDiary";
import { getStringedDate } from "../util/getStringedDate";
const Diary = () => {
  const params = useParams();
  const nav = useNavigate();

  const curDiaryItem = useDiary(params.id);
  if (!curDiaryItem) {
    return <div>데이터 로딩중...!</div>;
  }
  const { createdDate, emotionId, name, content, instructor } = curDiaryItem;
  console.log(createdDate)
  const title = getStringedDate(new Date(createdDate));
  console.log(title);
  return (
    <div>
      <InnerHeader
        title={`${title} 기록`}
        leftChild={<Button onClick={() => nav(-1)} text={"< 뒤로가기"} />}
        rightChild={
          <Button
            onClick={() => nav(`/editor/${params.id}`)}
            text={"수정하기"}
          />
        }
      />
      <Viewer emotionId={emotionId} instructor={instructor} content={content} />
    </div>
  );
};
export default Diary;
