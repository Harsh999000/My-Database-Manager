// script.js — shared/global JavaScript logic for all pages

// Retrive JWT token from browser after login
function getToken() {
    return localStorage.getItem("authToken");
}


// logout
function logoutUser() {
    const token = getToken();

    fetch("/auth/logout", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            reason: "user clicked logout"
        }),
    })
        .then(res => res.json())
        .then(data => {
            localStorage.removeItem("authToken");
            window.location.href = "/html/index.html";
        })
        .catch(err => {
            console.error("Logout error:", err);
            localStorage.removeItem("authToken");
            window.location.href = "/html/index.html";
        });
}

document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("logout-btn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", logoutUser);
    }
});
