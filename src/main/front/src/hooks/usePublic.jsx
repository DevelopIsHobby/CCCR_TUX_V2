import { useContext, useState, useEffect } from "react";
import { PublicStateContext } from "../App";
import { useNavigate } from "react-router-dom";

const usePublic = (id) => {
  const data = useContext(PublicStateContext);
  const [curPublicItem, setCurPublicItem] = useState();
  const nav = useNavigate();

  useEffect(() => {
    const currentPublicItem = data.find(
      (item) => String(item.id) === String(id)
    );

    if (!currentPublicItem) {
      window.alert("존재하지 않는 피드백입니다.");
      nav("/public", { replace: true });
    }
    setCurPublicItem(currentPublicItem);
  }, [id, data]);

  return curPublicItem;
};

export default usePublic;
