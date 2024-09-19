import "./Viewer.css";
import { getEmotionImage } from "../util/get-emtion-image";
import { emotionList } from "../util/constants";
import Button from "./Buttons";
const Viewer = ({ emotionId, instructor, content }) => {
  const emotionItem = emotionList.find(
    (item) => String(item.emotionId) === String(emotionId)
  );
  return (
    <div className="Viewer">
      <section className="img_section">
        <h4>오늘의 감정</h4>
        <div className={`emotion_img_wrapper emotion_img_wrapper_${emotionId}`}>
          <img src={getEmotionImage(emotionId)} />
          <div>{emotionItem.emotionName}</div>
        </div>
      </section>
      <section className="content_name">
        <h4>오늘의 강사님</h4>
        <Button text={instructor} type={"NAME"} />
      </section>
      <section className="content_section">
        <h4>오늘의 평가</h4>
        <div className="content_wrapper">
          <p>{content}</p>
        </div>
      </section>
    </div>
  );
};

export default Viewer;
