function pageLoad() {

    let now = new Date();

    let myHTML = '<div>'
        + '<h1 style="padding-top: 24px; line-height: 0;">Leo\'s Productivity Tracker</h1>'
        + '<p style="font-style: italic; font-size: 12px;">a simple time management tool</p>'
        + '<div style="font-style: italic;">'
        + 'Generated at ' + now.toLocaleTimeString()
        + '<div id="time"></div>'
        + '</div>'
        + '</div>';

    document.getElementById("index").innerHTML = myHTML;
    function checkTime(i) {
        if (i < 10) {
            i = "0" + i;
        }
        return i;
    }

    function startTime() {
        var today = new Date();
        var h = today.getHours();
        var m = today.getMinutes();
        var s = today.getSeconds();
        // add a zero in front of numbers<10
        m = checkTime(m);
        s = checkTime(s);
        document.getElementById('time').innerHTML = "Current Time: " + h + ":" + m + ":" + s;
        t = setTimeout(function() {
            startTime()
        }, 500);
    }
    startTime();
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

        logInHTML = "user: " + username + " <a href='/client/login.html?logout'><button id='loginButton'>Log Out</button></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}
