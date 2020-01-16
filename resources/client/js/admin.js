function pageLoad() {

    checkLogin();
}

function test() {
    console.log(this.parentElement);
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
                    innerHTML += '<tr><td>' +
                        responseData[i].username + '</td><td>' +
                        responseData[i].password + '</td><td>' +
                        responseData[i].DOB + '</td><td>' +
                        '<button class="editButton">Edit</button> <button class="deleteButton">Delete</button></td></tr>'
                }

                innerHTML += '</tbody>' +
                    '</table>';

                document.getElementById("tableDiv").innerHTML = innerHTML
                for (let i = 0; i < document.getElementsByClassName("deleteButton").length; i++) {
                    document.getElementsByClassName("deleteButton")[i].addEventListener('click', test)
                }
            }

        });

        logInHTML = "<a href='/client/login.html?logout'><button id='loginButton'>Logged in as: " + username + " (Log out)</button></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}
