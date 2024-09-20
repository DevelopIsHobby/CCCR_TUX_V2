import "./PublicItem.css";
import Button from "./Buttons";
import { useNavigate } from "react-router-dom";

const PublicItem = ({ id, createdDate, title, content, email }) => {
  const nav = useNavigate();

  return (
    <div className="PublicItem">
      <div className="info_section" onClick={() => nav(`/publicDetail/${id}`)}>
        <div className="header">
          <span className="label">
            [ 공지사항 ] <span className="label_title">{title}</span>
          </span>
          <span className="created_date">
            {new Date(createdDate).toLocaleDateString()}
          </span>
        </div>
        <div className="content">{content}</div>
      </div>
      <div
        onClick={() => nav(`/publicDetail/${id}`, {state: {email}})}
        className="button_section"
      >
        <Button text={"자세히 보기"} />
      </div>
    </div>
  );
};

export default PublicItem;
