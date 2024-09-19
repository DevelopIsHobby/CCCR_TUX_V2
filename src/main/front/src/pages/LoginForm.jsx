// LoginForm.jsx
import React, { useState } from 'react';
import axios from 'axios';
import "./../components/LoginForm.css"
import { useNavigate } from 'react-router-dom';

const LoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const nav = useNavigate();


    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("Before handleSubmit");
        try {
            // Spring Security의 /login으로 POST 요청을 보냅니다
            const response = await axios.post('/login', new URLSearchParams({
                username: email,
                password: password,
            }), {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
            console.log("After handleSubmit");
            // 로그인 성공 후, 리다이렉트 또는 다른 작업을 수행합니다
            // setMessage('Login successful!');
            console.log('Login successful:', response.data);
            nav("/");  // 로그인 후 홈으로 리다이렉트 (원하는 페이지로 변경 가능)
        } catch (error) {
            // setMessage(error.response?.data || 'An error occurred');
        }
    };

    const onClickSignup = () => {
        nav("/signup");
    }

    return (
        <div className={"login-container"}>
            <h2  className={"login-h2"}>Login</h2>
            <form className="login-form" onSubmit={handleSubmit}>
                <div className="login-div-email">
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className="login-div-password">
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button className="login-btn" type="submit">Login</button>
            </form>
            {message && <p>{message}</p>}
            <a onClick={onClickSignup}>Create an account</a>
        </div>

    );
};

export default LoginForm;
