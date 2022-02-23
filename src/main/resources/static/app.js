var weatherButton = document.getElementById("weather");


weatherButton.addEventListener('click', function (){
    $("#temperature").empty();
    var city = document.getElementById("city").value;
    axios.get(city).then(res => {
        $("#temperature").append(res.data);
    }).catch(ex => {
        alert(ex);
    });
});

