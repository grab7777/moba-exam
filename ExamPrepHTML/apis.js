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

function startOrientation() {
  const el = document.getElementById("orientation-element");
  if (window.DeviceOrientationEvent) {
    window.addEventListener(
      "deviceorientation",
      function (e) {
        a = Math.floor(e.alpha);
        b = Math.floor(e.beta);
        c = Math.floor(e.gamma);
        el.style.transform =
          "rotateZ(" +
          a +
          "deg) rotateX(" +
          b +
          "deg)" +
          "rotateY(" +
          c +
          "deg)";
      },
      true
    );
  }
}

function startMotion() {
  if (window.DeviceMotionEvent) {
    window.addEventListener(
      "devicemotion",
      function (e) {
        const element = document.getElementById("motion-element");
        adjustWidth(Date.now(), e.acceleration.x, e.acceleration.y);
        const x = parseFloat(e.acceleration.x).toFixed(3);
        const y = parseFloat(e.acceleration.y).toFixed(3);
        xPretty = x >= 0 ? "+" + x : x;
        yPretty = y >= 0 ? "+" + y : y;
        element.textContent = `x: ${xPretty}, y: ${yPretty}`;
      },
      true
    );
  }
}

function adjustWidth(timestamp, x, y) {
  const barX = document.getElementById("motion-bar-x");
  const barY = document.getElementById("motion-bar-y");
  barX.style.width = Math.min(x * 40, 200) + "px";
  barY.style.width = Math.min(y * 40, 200) + "px";
  requestAnimationFrame(adjustWidth);
}

requestAnimationFrame(adjustWidth);
