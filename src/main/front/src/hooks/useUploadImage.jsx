import { useState } from 'react';
import axios from 'axios';

const useUploadImage = () => {
    const [imageUrl, setImageUrl] = useState(null);

    const uploadImage = async (file) => {
        const formData = new FormData();
        formData.append('profilePicture', file);

        try {
            const response = await axios.post('/users/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            console.log(imageUrl);

            if (response.status === 200) {
                setImageUrl(response.data); // 서버에서 반환된 이미지 URL
            }
        } catch (error) {
            console.error('Image upload failed', error);
        }
    };

    return { imageUrl, uploadImage };
};

export default useUploadImage;
