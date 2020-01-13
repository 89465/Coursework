function pageLoad() {

    let now = new Date();

    function checkTime(i) {
        if (i < 10) {
            i = "0" + i;
        }
        return i;
    }

    checkLogin();
}

function checkLogin() {

    let username = Cookies.get("username");

    let logInHTML = '';

    if (username === undefined) {

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "hidden";
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "hidden";
        }

        document.getElementById("description").hidden = false;
        document.getElementById("timetable").hidden = true;

        logInHTML = "<a href='/client/login.html'><button id='loginButton'>Log in / Register</button></a>";
    } else {

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "visible";
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "visible";
        }

        document.getElementById("description").hidden = true;
        document.getElementById("timetable").hidden = false;

        let tableBody = document.getElementById("timetable").getElementsByTagName('tbody')[0];
        tableBody.innerHTML="";

        fetch("/users/list", {method: 'get'}
        ).then(response => response.json()
        ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                for (let i = 0; i < 4; i++) {
                    let newRow = tableBody.insertRow(i);
                    newRow.insertCell(0).appendChild(document.createTextNode(responseData[i].username));
                    newRow.insertCell(1).appendChild(document.createTextNode(responseData[i].password));
                    newRow.insertCell(2).appendChild(document.createTextNode(responseData[i].DOB));
                }
            }

        });

        logInHTML = "<a href='/client/login.html?logout'><button id='loginButton'>Logged in as: " + username + " (Log out)</button></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}
