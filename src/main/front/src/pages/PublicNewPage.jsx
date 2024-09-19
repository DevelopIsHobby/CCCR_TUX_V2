import InnerHeader from "../components/InnerHeader.jsx";
import Button from "../components/Buttons.jsx";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { PublicDispatchContext } from "../App";
import PublicNew from "../components/PublicNew.jsx";

const PublicNewPage = () => {
    const { onCreatePublic } = useContext(PublicDispatchContext);
    const nav = useNavigate();
    const onSubmitPublic = (input) => {
        onCreatePublic(input.createdDate.getTime(), input.title, input.content, input.writer);
    };
    return (
        <div>
            <InnerHeader
                leftChild={<Button onClick={() => nav(-1)} text={"< 뒤로가기"}/>}
                title={"새 공지사항 쓰기"}
            />
            <PublicNew onSubmit={onSubmitPublic}/>
        </div>
    )
}
export default PublicNewPage;