function pageLoad() {

    fetch("/activities/list", {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {
        for (let i = 0; i < responseData.length; i++)
            document.getElementById("activity").innerHTML += "<option value=\"" + (i+1) + "\">" + responseData[i].description + "</option>";
    });

    document.getElementById("loginButton").addEventListener("click", add);
}

function add(event) {
    event.preventDefault();

    const form = document.getElementById("loginForm");
    const formData = new FormData(form);

    if((Date.parse(formData.get("endDate") + "T" + formData.get("endTime") + ":00") - Date.parse(formData.get("startDate") + "T" + formData.get("startTime") + ":00")) <= 0 ) {
        alert("Your activity must end after it's started!");
        return;
    }

    formData.append("userID", Cookies.get("userID"));

    fetch("/users/activities/add", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {

            window.location.href = '/client/index.html';
        }
    });
}