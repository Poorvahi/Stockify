<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Login Page</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/Header.js"></script>

</head>
<style>

    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
    }

    body {
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        background: lavender;
    }

    .title {
        position: absolute;
        top: 40px;
        font-size: 32px;
        color: #ffffff;
        letter-spacing: 1px;
    }

    /* ===== FORM CONTAINER ===== */
    .row {
        width: 100%;
        display: flex;
        justify-content: center;
    }

    .login-form-1 {
        background: #ffffff;
        padding: 35px 30px;
        width: 350px;
        border-radius: 10px;
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.25);
    }

    /* ===== FORM GROUP ===== */
    .form-group label {
        font-size: 14px;
        font-weight: 500;
        color: #333;
    }

    .form-control {
        width: 100%;
        padding: 10px 12px;
        margin-top: 6px;
        border-radius: 5px;
        border: 1px solid #ccc;
        outline: none;
        font-size: 14px;
        transition: border-color 0.3s;
    }

    .form-control:focus {
        border-color: #0d6efd;
    }

    .btnSubmit {
        width: 100%;
        padding: 10px;
        border-radius: 6px;
        border: none;
        cursor: pointer;
        font-size: 15px;
        font-weight: 600;
        color: #fff;
        background: #0d6efd;
        transition: background 0.3s, transform 0.2s;
    }

    .btnSubmit:hover {
        background: #0b5ed7;
        transform: translateY(-1px);
    }

    .ForgetPwd {
        display: block;
        text-align: center;
        margin-top: 12px;
        font-size: 13px;
        text-decoration: none;
        color: #6c757d;
    }

    .ForgetPwd:hover {
        color: #0d6efd;
        text-decoration: underline;
    }

    .btnRegister {
        width: 100%;
        padding: 10px;
        border-radius: 6px;
        text-align: center;
        font-size: 14px;
        font-weight: 600;
        text-decoration: none;
        color: #fff;
        background: linear-gradient(135deg, #6610f2, #0d6efd);
        transition: all 0.3s ease;
    }

    .btnRegister:hover {
        opacity: 0.9;
        transform: translateY(-2px);
    }


</style>
<body>
<h1 class="title"></h1>

<div class="row">
    <div class="col-md-6 login-form-1">
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        <form id="loginForm">
            <div class="form-group">
                <label for="">Enter Username </label><br>
                <input type="text" class="form-control" placeholder="Username" value="" name="username"
                       id="username"
                       autocomplete="username"/>
            </div>
            <br>
            <div class="form-group">
                <label for="">Enter Password </label><br>
                <input type="password" class="form-control" placeholder="Password" value="" name="password"
                       autocomplete="current-password" id="password"/>
            </div>
            <br><br>

            <div class="form-group" style="display:flex; flex-direction:column; gap:10px;">
                <input type="submit" class="btnSubmit" value="Login"/>

                <a href="/register" class="btnRegister">Create New Account</a>
            </div>

        </form>
    </div>
</div>
<script>
    document.getElementById("loginForm").addEventListener("submit", function (e) {
        e.preventDefault();

        $.ajax({
            url: "/auth/login",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                username: document.getElementById("username").value,
                password: document.getElementById("password").value
            }),
            success: function (token) {
                localStorage.setItem("jwtToken", token);

                // Use pushState to update URL then navigate
                window.history.pushState({url: "/dashboard"}, "", "/dashboard");
                navigateWithToken("/dashboard");
            },
            error: function (xhr) {
                alert("Login failed");
                console.error("Invalid credentials", xhr.status, xhr.responseText);
            }
        });
    });
</script>
</body>
</html>

