import "./Button.css";
const Button = ({ text, type, onClick, isSelected }) => {
  return (
    <button
      onClick={onClick}
      className={
        isSelected
          ? `Button Button_${type} Button_SELECTED`
          : `Button Button_${type}`
      }
    >
      {text}
    </button>
  );
};
export default Button;
