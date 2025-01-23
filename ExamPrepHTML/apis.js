let lat = 0.0;
let long = 0.0;
function locateMe() {
  if (navigator.geolocation) {
    const options = {
      maximumAge: 5 * 60 * 1000, // reduce need to start geoloc. hardware
      timeout: 10 * 1000, // don't keep user waiting
      enableHighAccuracy: false, // true = slower & more akku
    };
    navigator.geolocation.getCurrentPosition(success, fail, options);
  }
}

function getOsmLocationUrl() {}

function fail() {
  alert("failed");
}
function printLocation() {
  return "Latitude: " + lat + ", Longitude: " + long;
}
function success(position) {
  lat = position.coords.latitude;
  long = position.coords.longitude;
  size = 0.005;
  const url = `https://www.openstreetmap.org/export/embed.html?bbox=${
    long - size
  }%2C${lat - size}%2C${long + size}%2C${
    lat + size
  }&amp;layer=mapnik&amp;marker=${lat}%2C${long}`;
  const iframe = document.getElementById("geolocation-map");
  iframe.height = "800px";
  iframe.src = url;
  const link = document.getElementById("geolocation-link");
  link.href = `https://www.openstreetmap.org/?mlat=${lat}&amp;mlon=${long}#map=15/${lat}/${long}`;
  link.hidden = false;
  const text = document.getElementById("geolocation-text");
  text.hidden = false;
  text.textContent = printLocation();
  const linkText = document.getElementById("geolocation-link-text");
  linkText.textContent = url;
  linkText.hidden = false;
}
