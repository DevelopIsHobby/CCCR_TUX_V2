import {useState, useEffect, useContext} from 'react';
import axios from 'axios';
import "./../components/myPage.css"
import {useNavigate} from "react-router-dom";
import InnerDiaryList from "../components/InnerDiaryList.jsx";
import {getMonthlyData} from "../util/getMonthlyData.js";


const MyPage = () => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [profileImageUrl, setProfileImageUrl] = useState(null);
    const [profileImageFile, setProfileImageFile] = useState(null);
    const nav = useNavigate();
    const [data, setData] = useState([])
    const [pivotDate, setPivotDate] = useState(new Date());
    const monthlyData = getMonthlyData(pivotDate, data);

    const onIncreaseMonth = () => {
        setPivotDate(new Date(pivotDate.getFullYear(), pivotDate.getMonth() + 1));
    };
    const onDecreaseMonth = () => {
        setPivotDate(new Date(pivotDate.getFullYear(), pivotDate.getMonth() - 1));
    };

    useEffect(() => {
        fetchMyFeedData(); // 컴포넌트가 마운트될 때 데이터 가져오기
    }, []);

    // 새로운 피드를 추가한 후, 데이터를 다시 가져와서 상태를 업데이트
    const fetchMyFeedData = async () => {
        try {
            const response = await fetch("/feed/listMyPage");
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const result = await response.json();

            // 데이터를 mockData 형식으로 변환
            const transformedData = result.map(item => ({
                id: item.fno,
                createdDate: new Date(item.modDate).getTime(),
                emotionId: item.emotionId,
                name: item.name,
                instructor: item.instructor,
                content: item.content,
            }));

            setData(transformedData); // 변환된 데이터를 상태로 설정
        } catch (error) {
            console.log("Failed to fetch feed data:", error);
        }
    };


    useEffect(() => {
        // 현재 사용자 정보를 가져오는 API 호출
        axios.get('/users/user')
            .then(response => {
                setEmail(response.data.email);
                setName(response.data.name);
                setProfileImageUrl(response.data.profilePictureUrl);
            })
            .catch(error => {
                console.error("Error fetching user data", error);
            });
        console.log(profileImageUrl);
    }, []);



    const handleNameChange = (e) => {
        setName(e.target.value);
    };

    useEffect(() => {
        if (profileImageFile) {
            const formData = new FormData();
            formData.append('profilePicture', profileImageFile);

            axios.post('/users/upload-profile-picture', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
                .then(response => {
                    setProfileImageUrl(response.data.url); // 서버에서 반환된 URL을 사용
                    console.log("response.data.url: " + response.data.url);
                })
                .catch(error => {
                    console.error("Error uploading file", error);
                });
        }
    }, [profileImageFile]);

    const handleProfileImageUrlChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setProfileImageFile(file);
        }
    };

    const handleSave = () => {
        const formData = new FormData();
        formData.append('name', name);
        formData.append('email', email);
        console.log("profilePictureUrl " + profileImageUrl)
        if (profileImageFile) {
            formData.append('profilePicture', profileImageFile); // 'profilePicture' 이름으로 파일 추가
        }
        axios.post('/users/update', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(response => {
                alert('Profile updated successfully');
                nav("/");
            })
            .catch(error => {
                console.error("Error updating profile", error);
            });

    };

    const handleList = () => {
        nav("/");
    }

    return (
        <div className="myPage-total-container">
            <div className="myPage-container">
                <h1>My Page</h1>
                <div className="profile">
                    <div className="profile-picture">
                        {profileImageUrl ? (
                            <img src={`/uploadPath/${profileImageUrl}`}
                                 alt="Profile" style={{width: '150x', height: '150px', borderRadius: '50%'}}/>
                        ) : (
                            <span>Img Not Found</span>
                        )}
                        <input
                            type="file"
                            accept="image/*"
                            onChange={handleProfileImageUrlChange}
                        />
                    </div>
                    <div className="profile-info">
                        <label>
                            Name:
                            <input
                                type="text"
                                value={name}
                                onChange={handleNameChange}
                            />
                        </label>
                    </div>
                </div>
                <div className="myPage-button-group">
                    <button className="myPage-button-save" onClick={handleSave}>Save</button>
                    <button className="myPage-button-list" onClick={handleList}>List</button>
                </div>

            </div>
            <div className="myPage-container">
                <InnerDiaryList data={monthlyData}/>
            </div>
        </div>
    );
};

export default MyPage;
