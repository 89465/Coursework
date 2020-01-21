function pageLoad() {

    document.getElementById("loginButton").addEventListener("click", add);

    fetch("/users/list", {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {
        for (let i = 0; i < responseData.length; i++) {
            if (responseData[i].userID == new URLSearchParams(window.location.search).get("userID")){
                document.getElementById("editUser").innerHTML = "Edit User: " + responseData[i].username
                document.getElementById("password").value = responseData[i].password;
                console.log(responseData[i].DOB);
                document.getElementById("dob").value = responseData[i].DOB;
            }
        }
    });
}

function add(event) {
    event.preventDefault();

    const form = document.getElementById("loginForm");
    const formData = new FormData(form);

    formData.append("userID", new URLSearchParams(window.location.search).get("userID"));

    fetch("/users/update", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {

            window.location.href = '/client/admin.html';
        }
    });
}