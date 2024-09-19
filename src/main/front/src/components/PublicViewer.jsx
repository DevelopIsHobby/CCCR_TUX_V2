import "./PublicViewer.css";
import tux from "./../assets/tux.jpg";
const PublicViewer = ({ date, title, content }) => {
  return (
    <div className="Viewer">
      <section className="img_section">
        <div className={"emotion_img_wrapper"}>
          <img src={tux} />
        </div>
      </section>
      <section className="date_section">
        <h4>오늘의 날짜</h4>
        <input readOnly name="createdDate" type="date" value={date} />
      </section>
      <section className="content_section">
        <h4 readOnly>{title}</h4>
        <div className="content_wrapper">
          <p readOnly>{content}</p>
        </div>
      </section>
    </div>
  );
};
export default PublicViewer;
