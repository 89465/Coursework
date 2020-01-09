function pageLoad() { document.getElementById("loginButton").addEventListener("click", register);}

function register(event) {

    event.preventDefault();

    const form = document.getElementById("loginForm");
    const formData = new FormData(form);

    fetch("/users/add", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            document.getElementById("error").innerHTML = "Username already taken.";
        } else {
            alert("Account successfully created.");
            window.location.href = '/client/login.html';
        }
    });
}