function pageLoad() {

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
        document.getElementById("tableDiv").hidden = true;
        document.getElementById("addButton").hidden = true;

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
        document.getElementById("tableDiv").hidden = false;
        document.getElementById("addButton").hidden = false;

        fetch("/users/activities/list", {method: 'get'}
        ).then(response => response.json()
        ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {

                    let innerHTML = '<table id="timetable">' +
                    '<thead>' +
                    '<tr>' +
                    '<th>Activity</th>' +
                    '<th>Started</th>' +
                    '<th>Finished</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';

                    let usercount = 0;
                    for (let i = 0; i < responseData.length; i++) {
                        if (username == responseData[i].username)
                            usercount++;
                    }
                    for (let i = 0; i < usercount; i++) {

                        innerHTML += '<tr><td>' +
                            responseData[i].activityName + '</td><td>' +
                            responseData[i].startDate + " " + responseData[i].startTime + '</td><td>' +
                            responseData[i].endDate + " " + responseData[i].endTime + '</td></tr>'
                    }

                    innerHTML += '</tbody>' +
                    '</table>';

                    document.getElementById("tableDiv").innerHTML = innerHTML
            }

        });

        logInHTML = "<a href='/client/login.html?logout'><button id='loginButton'>Logged in as: " + username + " (Log out)</button></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}
