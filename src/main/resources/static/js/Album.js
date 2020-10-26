const params = new URLSearchParams(window.location.search);
let loggedIn = false;
let albumsToView = "";
for (const param of params) {
  console.log(param);
  loggedIn = param[1];
  let artistOrGenre = param[2];
  let artistOrGenreId = param[3];
  albumsToView = artistOrGenre+"/"+artistOrGenreId
}
getAlbums(loggedIn,albumsToView);

function getAlbums(loggedIn,albumsToView) {
  fetch("http://localhost:8082/albums/read/"+albumsToView)
    .then(function (response) {
      if (response.status !== 200) {
        console.log(
          "Looks like there was a problem. Status Code: " + response.status
        );
        return;
      }
      // Examine the text in the response
      response.json().then(function (AlbumData) {
        let table = document.querySelector("#AlbumTable");
        let data = Object.keys(AlbumData[0]);

        generateTableHead(table, data, loggedIn);
        generateTable(table, AlbumData, loggedIn);
        if(loggedIn){
            generateAddAlbumBtn(table);
        }
        
      });
    })
    .catch(function (err) {
      console.log("Fetch Error :-S", err);
    });
}

function generateTableHead(table, data, loggedIn) {
  let thead = table.createTHead();
  let row = thead.insertRow();
  for (let key of data) {
    let th = document.createElement("th");
    let text = document.createTextNode(key);
    th.appendChild(text);
    row.appendChild(th);
  }
  let th = document.createElement("th");
  let text = document.createTextNode("View Album");
  th.appendChild(text);
  row.appendChild(th);
  if (loggedIn) {
    let th2 = document.createElement("th");
    let text2 = document.createTextNode("Edit");
    th2.appendChild(text2);
    row.appendChild(th2);

    let th3 = document.createElement("th");
    let text3 = document.createTextNode("Delete");
    th3.appendChild(text3);
    row.appendChild(th3);
  }
}

function generateTable(table, AlbumData, loggedIn) {
  for (let element of AlbumData) {
    let row = table.insertRow();
    for (key in element) {
      let cell = row.insertCell();
      let text = document.createTextNode(element[key]);
      if (key === "tracks") {
        let tracksNo = 0;
        for (tracks of element[key]) {
            tracksNo = +1;
        }
        text = document.createTextNode(tracksNo);
      }
      cell.appendChild(text);
    }
    let newCell = row.insertCell();
    let myViewButton = document.createElement("button");
    myViewButton.className = "btn";
    myViewButton.id = "ViewAlbumButton";
    myViewButton.onclick = document.location='Tracks.html?loggedIn='+loggedIn+'?album='+element.id;

    let viewIcon = document.createElement("span");
    viewIcon.className = "material-icons";
    viewIcon.innerHTML="launch";
    myViewButton.appendChild(viewIcon);
    newCell.appendChild(myViewButton);

    if (loggedIn) {
        let newCell2 = row.insertCell();
        let myEditButton = document.createElement("button");
        myEditButton.className = "btn";
        myEditButton.id = "EditAlbumButton";
        myEditButton.setAttribute("data-toggle", "modal");
        myEditButton.setAttribute("data-target", "#EditAlbumModal");
    
        let editIcon = document.createElement("span");
        editIcon.className = "material-icons";
        editIcon.innerHTML="create";
        myEditButton.appendChild(editIcon);
        let ID = element.id;
        let Name=element.name;
        myEditButton.onclick = function () {
        changeAlbumModal(ID, Name);
        };
        newCell2.appendChild(myEditButton);

        let newCell3 = row.insertCell();
        let myDeleteButton = document.createElement("button");
        myDeleteButton.className = "btn";
        myDeleteButton.id = "DeleteAlbumButton"+element.name;

        let deleteIcon = document.createElement("span");
        deleteIcon.className = "material-icons";
        deleteIcon.innerHTML="delete";
        myDeleteButton.appendChild(deleteIcon);
        myDeleteButton.onclick = function () {
            deleteAlbum(element.id);
        };
        newCell3.appendChild(myDeleteButton);
    }
  }
}

function generateAddAlbumBtn(table){
    let tableFooter = document.createElement("footer");
    let myAddTaskButton = document.createElement("button");
    myAddTaskButton.className = "btn btn-outline-primary";
    myAddTaskButton.innerHTML = "Add Album";
    myAddTaskButton.id = "AddAlbumButton";
    myAddTaskButton.setAttribute("data-toggle", "modal");
    myAddTaskButton.setAttribute("data-target", "#AddAlbumModal");
    myAddTaskButton.onclick = function () {
      changeAddTaskModal(ID, Name);
    };

    tableFooter.appendChild(myAddTaskButton);
    table.appendChild(tableFooter);
}

function deleteAlbum(id) {
    fetch("http://localhost:8082/album/delete/" + id, {
      method: "delete",
      headers: {
        "Content-type": "application/json",
      },
    })
      // .then(json)
      .then(function (data) {
        console.log("Request succeeded with JSON response", data);
        location.reload();
      })
      .catch(function (error) {
        console.log("Request failed", error);
      });
  }

  let AlbumId;

  function changeEditAlbumModal(id, name) {

    let modalPH = document.getElementById("EditAlbumName");
    modalPH.setAttribute("value", name);
    AlbumId = id;
  }

  document
  .querySelector("form.EditAlbum")
  .addEventListener("submit", function (stop) {
     stop.preventDefault();

    let formElements = document.querySelector("form.EditAlbum").elements;
    console.log(formElements);

    let EditAlbumname = formElements["EditAlbumName"].value;

    let AlbumId = parseInt(AlbumId);
    console.log(AlbumId);
    editAlbum(EditAlbumname, AlbumId);
  });

  function editAlbum(name,  AlbumId) {
    fetch("http://localhost:8082/album/update/" + AlbumId, {
      method: "put",
      headers: {
        "Content-type": "application/json",
      },
      body: (json = JSON.stringify({
        "id": AlbumId,
        "name": name
      })),
    })
      .then(json)
      .then(function (data) {
        console.log("Request succeeded with JSON response", data);
        location.reload();
      })
      .catch(function (error) {
        console.log("Request failed", error);
      });
  }

  document
  .querySelector("form.Album")
  .addEventListener("submit", function (stop) {
    stop.preventDefault();

    let formElements = document.querySelector("form.Album").elements;
    console.log(formElements);
    let name = formElements["TaskListName"].value;
    addAlbum(name);
  });

  function addAlbum(name) {
    fetch("http://localhost:8082/album/create", {
      method: "post",
      headers: {
        "Content-type": "application/json",
      },
      body: (json = JSON.stringify({
        "name": name,
      })),
    })
      .then(json)
      .then(function (data) {
        console.log("Request succeeded with JSON response", data);
      })
      .catch(function (error) {
        console.log("Request failed", error);
      });
  }