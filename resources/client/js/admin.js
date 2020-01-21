function pageLoad() {
    checkLogin();
}

function editRow(row) {
    window.location.href = "/client/editUser.html?userID=" + row.parentNode.parentElement.id;
}

function removeRow(row) {
    const formData = new FormData();
    formData.append("userID", row.parentNode.parentElement.id);

    fetch("/users/remove", {method: 'post', body: formData}).then();
    row.parentNode.parentNode.parentNode.removeChild(row.parentNode.parentNode);
}

function checkLogin() {

    let username = Cookies.get("username");

    let logInHTML = '';

    if (username !== "leos") {

        window.location.href = "/client/index.html";
    } else {

        fetch("/users/list", {method: 'get'}
        ).then(response => response.json()
        ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {

                let innerHTML = '<table id="timetable">' +
                    '<thead>' +
                    '<tr>' +
                    '<th>Username</th>' +
                    '<th>Password</th>' +
                    '<th>DOB</th>' +
                    '<th id="controls">Edit / Delete</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';

                for (let i = 0; i < responseData.length; i++) {
                    innerHTML += '<tr id="' + responseData[i].userID + '"><td>' +
                        responseData[i].username + '</td><td>' +
                        responseData[i].password + '</td><td>' +
                        responseData[i].DOB + '</td><td>' +
                        '<button class="editButton" onclick="editRow(this)">Edit</button> <button class="deleteButton" onclick="removeRow(this)">Delete</button></td></tr>'
                }

                innerHTML += '</tbody>' +
                    '</table>';

                document.getElementById("tableDiv").innerHTML = innerHTML
                for (let i = 0; i < document.getElementsByClassName("deleteButton").length; i++) {
                    document.getElementsByClassName("deleteButton")[i].addEventListener('click', test)
                }
            }

        });

        logInHTML = "<a href='/client/login.html?logout'><button id='loginButton'>Logged in as: " + username + " (Log out)</button></a>" +
            "<br><a href='/client/index.html?'><button class='editButton'>Home</button></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}
