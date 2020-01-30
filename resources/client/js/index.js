function pageLoad() {
    checkLogin();
}

function editRow(row) {
    window.location.href = "/client/editActivity.html?userActivityID=" + row.parentNode.parentElement.id;
}

function removeRow(row) {
    const formData = new FormData();
    formData.append("userActivityID", row.parentNode.parentElement.id);

    fetch("/users/activities/remove", {method: 'post', body: formData}).then();
    row.parentNode.parentNode.parentNode.removeChild(row.parentNode.parentNode);
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
                    '<th id="controls">Add / Delete</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';

                    var tableData = [0, 0, 0, 0, 0];

                    for (let i = 0; i < responseData.length; i++) {
                        if (username == responseData[i].username) {
                            tableData[responseData[i].activityID - 1] += (Date.parse(responseData[i].endDate + "T" + responseData[i].endTime + ":00") -
                                Date.parse(responseData[i].startDate + "T" + responseData[i].startTime + ":00")) / 3600000

                            innerHTML += '<tr id="' + responseData[i].userActivityID + '"><td>' +
                                responseData[i].activityName + '</td><td>' +
                                responseData[i].startDate + " " + responseData[i].startTime + '</td><td>' +
                                responseData[i].endDate + " " + responseData[i].endTime + '</td><td>' +
                                '<button class="editButton" onclick="editRow(this)">Edit</button> <button class="deleteButton" onclick="removeRow(this);">Delete</button></td></tr>'
                        }
                    }

                    innerHTML += '</tbody>' +
                    '</table>';

                    document.getElementById("tableDiv").innerHTML = innerHTML;

                    var ctx = document.getElementById('myChart').getContext('2d');
                    var myChart = new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: ['Sleeping', 'Working', 'Exercise', 'Other (Productive)', 'Other (Not Productive)'],
                            datasets: [{
                                label: '# of Hours',
                                data: tableData,
                                backgroundColor: [
                                    'rgba(255, 99, 132, 0.2)',
                                    'rgba(54, 162, 235, 0.2)',
                                    'rgba(255, 206, 86, 0.2)',
                                    'rgba(75, 192, 192, 0.2)',
                                    'rgba(153, 102, 255, 0.2)',
                                    'rgba(255, 159, 64, 0.2)'
                                ],
                                borderColor: [
                                    'rgba(255, 99, 132, 1)',
                                    'rgba(54, 162, 235, 1)',
                                    'rgba(255, 206, 86, 1)',
                                    'rgba(75, 192, 192, 1)',
                                    'rgba(153, 102, 255, 1)',
                                    'rgba(255, 159, 64, 1)'
                                ],
                                borderWidth: 1
                            }]
                        },
                        options: {
                            scales: {
                                yAxes: [{
                                    ticks: {
                                        beginAtZero: true
                                    }
                                }]
                            }
                        }
                    });

            }

        });

        logInHTML = "<a href='/client/login.html?logout'><button id='loginButton'>Logged in as: " + username + " (Log out)</button></a>";
        if (username == "leos")
            logInHTML += "<br><a href='/client/admin.html'><button class='editButton'>Admin Page</button></a>"

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}
