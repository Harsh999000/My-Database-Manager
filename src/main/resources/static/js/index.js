// index.js — specific to index.html (login/signup slider)

document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("container");
    const loginButton = document.getElementById("login");   // Toggle to login
    const signUpButton = document.getElementById("signUp"); // Toggle to signup

    loginButton.addEventListener("click", () => {
        container.classList.remove("active");
    });

    signUpButton.addEventListener("click", () => {
        container.classList.add("active");
    });

    const loginForm = document.getElementById("login-form");
    const signupForm = document.getElementById("signup-form");

    loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const username = loginForm.username.value.trim();
        const password = loginForm.password.value.trim();

        try {
            const response = await fetch("/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
            });

            const data = await response.json();

            if (response.ok && data.token) {
                localStorage.setItem("authToken", data.token);

                const tokenPayload = JSON.parse(atob(data.token.split('.')[1]));
                const role = tokenPayload.role;

                // Redirect based on role
                switch (role) {
                    case "root":
                        window.location.href = "/dashboard/root";
                        break;
                    case "super_admin":
                        window.location.href = "/dashboard/superadmin";
                        break;
                    case "admin":
                        window.location.href = "/dashboard/admin";
                        break;
                    case "executive":
                        window.location.href = "/dashboard/executive";
                        break;
                    default:
                        alert("Unknown role. Cannot redirect.");
                }
            } else {
                alert(data.error || "Login failed. Please check credentials.");
            }
        } catch (err) {
            console.error("Login error:", err);
            alert("Login error. Please try again.");
        }
    });

    signupForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const username = signupForm.username.value.trim();
        const password = signupForm.password.value.trim();
        const virtualDbName = signupForm.database.value.trim();

        try {
            const response = await fetch("/auth/signup", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password, virtualDbName }),
            });

            const data = await response.json();

            if (response.ok) {
                alert("Signup successful. You can now login.");
                container.classList.remove("active"); // Switch to login panel
                signupForm.reset();
            } else {
                alert(data.error || "Signup failed.");
            }
        } catch (err) {
            console.error("Signup error:", err);
            alert("Signup error. Please try again.");
        }
    });
});
