function loginUser(){
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    var email=document.getElementById("email").value;
    var password=document.getElementById("password").value;
    console.log("Email ",email);
    var raw = JSON.stringify({
      "email":email,
      "password":password
    });
    console.log("json data : ", raw);
    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };
    
    fetch("https://maternally-health-backend.herokuapp.com/api/v1/User/login", requestOptions)
      .then(response => response.text())
      .then(result => console.log(result.toString()))
      .catch(error => console.log('error', error));
}
