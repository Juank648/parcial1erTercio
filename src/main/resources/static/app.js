var weatherButton = document.getElementById("weatherButton");
var respuesta = document.getElementById("temperature");

weatherButton.addEventListener('click', function (){
    respuesta.append('');
    var city = document.getElementById("name").value;
    axios.get(city).then(res => {
        console.log(res.data);
        respuesta.append(res.data);
    }).catch(ex => {
        alert(ex);
    });
});



