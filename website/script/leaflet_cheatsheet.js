// Create Leaflet frame
const map = L.map('map').setView([51.505, -0.09], 13);

// Use a map service to display in the Leaflet frame
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
  maxZoom: 19,
  attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);


//Put a marker on the map
const marker = L.marker([51.5, -0.09]).addTo(map);

// Draw a circle on the map
const circle = L.circle([51.508, -0.11], {
  color: 'red',
  fillColor: '#f03',
  fillOpacity: 0.5,
  radius: 500,
}).addTo(map);

// Draw a polygon on the map
const polygon = L.polygon([
  [51.509, -0.08], //vertice
  [51.503, -0.06],
  [51.51, -0.047]
]).addTo(map);


// Popup opens when clicking on an element
marker.bindPopup("<b>Hello world!</b><br>I am a popup.");
circle.bindPopup("I am a circle.")
  .openPopup(); // Popup automatically opened.
polygon.bindPopup("I'm a polygon");

//Popup as layout
const popup = L.popup()
  .setLatLng([51.513, -0.09])
  .setContent("I'm a standalone popup")
  .openOn(map); //instead of addTo - close other popups before open it
  // .addTo(map);


//Execute some stuff (function) when specified event is emitted by a component in the map
map.on("click", onMapClick)

function onMapClick(e) {
  alert("You clicked on the map at " + e.latlng);
}

// Improved example
const popup2 = L.popup();

map.on("click", onMapClick2);

function onMapClick2(e) {
  popup2
    .setLatLng(e.latlng)
    .setContent("You clicked on the map at " + e.latlng)
    .openOn(map);
}