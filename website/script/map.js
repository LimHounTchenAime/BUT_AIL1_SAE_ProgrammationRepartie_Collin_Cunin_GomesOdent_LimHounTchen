/*
--- MAIN ---
 */
loadMap();

/*
--- VARIABLES ---
 */
/**
 * Error message to display when the data is not found
 * @type {string}
 */
const NET_ERR = "Erreur réseau";

/**
 * Location context to find the location data
 * @type {string}
 */
const MAP_LOC_CONTEXT = "54, Meurthe-et-Moselle, Grand Est";


/*
--- FUNCTIONS ---
 */
/**
 * Fetch data from the given url
 * @param url {string} - The url to fetch data from
 * @returns {Promise<any>}
 */
async function fetcher(url) {
  const response = await fetch(url);
  return await response.json();
}

/**
 * Check if the data is found, else throw an error
 * @param data - The data to check
 * @param message {string} - The error message to display
 * @returns {*} - The data if it is found
 */
function loadChecker(data, message)
{
  if (data.status === 'fulfilled') {
    return data.value;
  } else {
    mapError(message);
    throw new Error(message);
  }
}

/**
 * Load the map from the data sources
 * @returns {Promise<void>}
 */
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

/**
 * Process the data and display them on the map
 * @param locationData - The location data
 * @param stationStatusData - The station status data
 * @param stationInformationData - The station information data
 * @param trafficProblemsData - The traffic problems data
 */
function processMap(locationData, stationStatusData, stationInformationData, trafficProblemsData)
{
  // --- Display the map ---
  document.querySelector("#map").classList.remove("is-hidden");

  // --- Initialize and center the map on Nancy ---
  // Check the location is found
  let i = 0;
  while (locationData.features[i] && locationData.features[i].properties.context !== MAP_LOC_CONTEXT) {
    i++;
  }
  const data = locationData.features[i] ? locationData.features[i] : "Localisation introuvable";

  // Create Leaflet frame using location data
  const map = L.map('map').setView([data.geometry.coordinates[1], data.geometry.coordinates[0]], 13);

  // Use a map service to display in the Leaflet frame
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a> - Données <a href="https://www.data.gouv.fr/fr/terms/" target="_blank">DataGouv</a>, <a href="https://www.datagrandest.fr/data4citizen/visualisation/information/?id=1642070072496-1" target="_blank">DataGrandEst</a>'
  }).addTo(map);


  // --- Process station status and information data ---

  let _stations = [];
  for (let i = 0; i < stationStatusData.data.stations.length; i++) {
    _stations[i] = { ...stationStatusData.data.stations[i], ..._stations[i] };
  }

  // Process station information data and add markers to the map
  var icon = L.icon({
    iconUrl: 'assets/velo.png',
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


  // --- Process traffic problems data and add markers to the map ---

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

/**
 * Display an error message instead of the map
 * @param msg - The error message to display
 */
function mapError(msg) {
  console.error(NET_ERR + " : " + msg);
  document.querySelector("#map").insertAdjacentHTML('afterend', "<p class='error-text blink'>" + NET_ERR + " : " + msg + "</p>");
}
