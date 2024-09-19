import "./InnerHeader.css";
import Button from "./Buttons";

const InnerHeader = ({ title, leftChild, rightChild }) => {
  return (
    <>
      <header className="Header">
        <div className="header_left">{leftChild}</div>
        <div className="header_center">{title}</div>
        <div className="header_right">{rightChild}</div>
      </header>
      <div className="menu">
        <div className="sensor">
          <Button text={"센서데이터"} type={"SENSOR"} />
        </div>
      </div>
    </>
  );
};

export default InnerHeader;
