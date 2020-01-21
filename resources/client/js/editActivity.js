function pageLoad() {

    fetch("/activities/list", {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {
        for (let i = 0; i < responseData.length; i++)
            document.getElementById("activity").innerHTML += "<option value=\"" + (i+1) + "\">" + responseData[i].description + "</option>";
    });

    document.getElementById("loginButton").addEventListener("click", add);

    fetch("/users/activities/list", {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {
        for (let i = 0; i < responseData.length; i++) {
            if (responseData[i].userActivityID == new URLSearchParams(window.location.search).get("userActivityID")){
                document.getElementById("activity").value = responseData[i].activityID;
                document.getElementById("starttime").value = responseData[i].startTime;
                document.getElementById("startdate").value = responseData[i].startDate;
                document.getElementById("endtime").value = responseData[i].endTime;
                document.getElementById("enddate").value = responseData[i].endDate;
            }
        }
    });
}

function add(event) {
    event.preventDefault();

    const form = document.getElementById("loginForm");
    const formData = new FormData(form);

    formData.append("userID", Cookies.get("userID"));
    formData.append("userActivityID", new URLSearchParams(window.location.search).get("userActivityID"));

    fetch("/users/activities/update", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {

            window.location.href = '/client/index.html';
        }
    });
}