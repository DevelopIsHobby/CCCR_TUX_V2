import Header from "../components/Header";
import Button from "../components/Buttons";
import PublicList from "../components/PublicList";
import { useState, useContext } from "react";
import { PublicStateContext } from "../App";
import { getMonthlyData } from "../util/getMonthlyData";

const Public = () => {
  const [pivotDate, setPivotDate] = useState(new Date());
  const data = useContext(PublicStateContext);
  const monthlyPublicData = getMonthlyData(pivotDate, data);

  const onIncreaseMonth = () => {
    setPivotDate(new Date(pivotDate.getFullYear(), pivotDate.getMonth() + 1));
  };
  const onDecreaseMonth = () => {
    setPivotDate(new Date(pivotDate.getFullYear(), pivotDate.getMonth() - 1));
  };
  return (
    <div>
      <Header
        title={`${pivotDate.getFullYear()}년 ${pivotDate.getMonth() + 1}월`}
        leftChild={<Button onClick={onDecreaseMonth} text={"<"} />}
        rightChild={<Button onClick={onIncreaseMonth} text={">"} />}
      />
      <PublicList data={monthlyPublicData} />
    </div>
  );
};
export default Public;
