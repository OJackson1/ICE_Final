let artistLink = document.querySelector("#ArtistLink");
artistLink.href= "Artist.html?user=" + user;
let albumLink = document.querySelector("#AlbumLink");
albumLink.href= "Album.html?user=" + user;
let genreLink = document.querySelector("#GenreLink");
genreLink.href= "Genre.html?user=" + user;
let trackLink = document.querySelector("#TrackLink");
trackLink.href= "Track.html?user=" + user;

let indexLink = document.querySelector("#IndexLink");
if(user==0){
    indexLink.innerHTML = "Log In";
}else{
    indexLink.innerHTML = "Log Out"
}