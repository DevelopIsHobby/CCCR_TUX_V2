import "./ChattingList.css";
import ChattingItem from "./ChattingItem";
const ChattingList = () => {
  return (
    <div className="PublicList">
      <div className="list_wrapper">
        <ChattingItem />
      </div>
    </div>
  );
};

export default ChattingList;
