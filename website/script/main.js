/*
--- MAIN ---
 */
loadMap();

/*
--- VARIABLES ---
 */
let _stations = []
let map;
const NET_ERR = "Erreur réseau";


/*
--- FUNCTIONS ---
 */
async function fetcher(url) {
  const response = await fetch(url);
  return await response.json();
}

function loadChecker(data, message)
{
  if (data.status === 'fulfilled') {
    return data.value;
  } else {
    mapError(message);
    throw new Error(message);
  }
}

async function loadMap()
{
  try {
    const results = await Promise.allSettled([
      fetcher("https://api-adresse.data.gouv.fr/search/?q=Nancy+54"),
      fetcher("https://transport.data.gouv.fr/gbfs/nancy/station_status.json"),
      fetcher("https://transport.data.gouv.fr/gbfs/nancy/station_information.json"),
      fetcher("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"),
    ]);

    document.querySelector("#loadingMapMessage").classList.add("is-hidden");

    let locationData = loadChecker(results[0], `Impossible de récupérer la localisation pour construire la carte`);
    let stationStatusData = loadChecker(results[1], "Impossible de récupérer l'état des Vélibs");
    let stationInformationData = loadChecker(results[2], "Impossible de récupérer les informations des Vélibs");
    let trafficProblemsData = loadChecker(results[3], "Impossible de récupérer les informations trafic");

    processMap(locationData, stationStatusData, stationInformationData, trafficProblemsData);

  } catch (error) {
    const msg = "Erreur de récupération des données";
    console.error(msg);
  }
}

function processMap(locationData, stationStatusData, stationInformationData, trafficProblemsData)
{
  // Initialize and center the map on Nancy
  document.querySelector("#map").classList.remove("is-hidden");

  // Check the location is found
  let i = 0;
  while (locationData.features[i] && locationData.features[i].properties.context !== "54, Meurthe-et-Moselle, Grand Est") {
    i++;
  }
  const data = locationData.features[i] ? locationData.features[i] : "Localisation introuvable";

  // Create Leaflet frame using location data
  const map = L.map('map').setView([data.geometry.coordinates[1], data.geometry.coordinates[0]], 13);

  // Map copyright service to display in the Leaflet frame
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(map);

  // Data sources
  // L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
  //   maxZoom: 19,
  //   attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  // }).addTo(map);

  // Process station status and information data
  for (let i = 0; i < stationStatusData.data.stations.length; i++) {
    _stations[i] = { ...stationStatusData.data.stations[i], ..._stations[i] };
  }

  // Process station information data and add markers to the map
  var icon = L.icon({
    iconUrl: 'https://www.smavd.org/wp-content/uploads/elementor/thumbs/Velo-pnn5fiv0mued6phjixnq24y55dm7fy4y16518hf9te.png',
    iconSize: [50, 30],
  });

  for (let i = 0; i < stationInformationData.data.stations.length; i++) {
    _stations[i] = { ...stationInformationData.data.stations[i], ..._stations[i] };
    const marker = L.marker([_stations[i].lat, _stations[i].lon], { icon: icon }).addTo(map);
    marker.bindPopup(
      "<b>Adresse</b> : " + _stations[i].address.toString() +
      "<br><b>Vélos disponibles</b> : " + _stations[i].num_bikes_available.toString()
      + "<br><b>Places de parking libres</b> : " + _stations[i].num_docks_available.toString());
  }

  // Process traffic problems data and add markers to the map
  const trafficIcon = L.icon({
    iconUrl: 'assets/Logo_travaux_orange.svg',
    iconSize: [50, 30],
  });

  for (let incident of trafficProblemsData.incidents) {
    let coordinates = incident.location.polyline.split(" ");
    coordinates = coordinates.map(function (elt) {
      return parseFloat(elt);
    });
    const marker = L.marker(coordinates, { icon: trafficIcon }).addTo(map);
    marker.bindPopup(`
        <h2>${incident.short_description}</h2>
        <br><b>Type :</b> ${incident.type}
        <br><b>Localisation :</b> ${incident.location.street}
        <br><b>Description :</b> ${incident.description}
        <br><b>Depuis le :</b> ${new Date(incident.starttime).toLocaleString("fr-FR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric"
    })}
      `);
  }

}

function mapError(msg) {
  console.error(NET_ERR + " : " + msg);
  document.querySelector("#map").insertAdjacentHTML('afterend', "<p class='errorText blink'>" + NET_ERR + " : " + msg + "</p>");
}
