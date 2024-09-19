// SignupForm.jsx
import React, { useState } from 'react';
import axios from 'axios';
import "./../components/SignupForm.css"
import {useNavigate} from "react-router-dom";

const SignupForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [message, setMessage] = useState('');
    const nav = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('/users/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email,
                    password,
                    name,
                }),
            });

            if (!response.ok) {
                // 서버 응답이 성공적이지 않은 경우, 예외를 발생시킵니다.
                throw new Error('Network response was not ok');
            }

            console.log("User registered successfully"); // 서버의 응답 내용 확인
            setMessage("User registered successfully");
            if(response.ok) {
                alert(message);
                nav("/loginForm");
            }
        } catch (error) {
            console.error('An error occurred:', error.message);
        }
    };


    return (
        <div className={"signup-form-container"}>
            <h2 className={"signup-h2"}>Sign Up</h2>
            <form className={"signup-form"} onSubmit={handleSubmit}>
                <div className={"signup-div-email"}>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className={"signup-div-password"}>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div className={"signup-div-name"}>
                    <label>Name:</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <button className={"signup-btn"} type="submit">Sign Up</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default SignupForm;
