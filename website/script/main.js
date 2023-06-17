/*
--- VARIABLES ---
 */
let _stations = []
let map;
const NET_ERR = "Erreur réseau";

let loaded = 0;
const ALL_LOAD = 4;

/*
--- MAIN ---
 */
loadMap();


async function fetcher(url) {
  const response = await fetch(url);
  return await response.json();
}

async function loadMap()
{
  try {
    const [locationData, stationStatusData, stationInformationData, trafficProblemsData] = await Promise.all([
      fetcher("https://api-adresse.data.gouv.fr/search/?q=Nancy+54"),
      fetcher("https://transport.data.gouv.fr/gbfs/nancy/station_status.json"),
      fetcher("https://transport.data.gouv.fr/gbfs/nancy/station_information.json"),
      fetcher("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"),
    ]);

    // let locationData, stationStatusData, stationInformationData, trafficProblemsData;
    //
    // // for (let i in results.length)
    //   if (results[0].status === 'fulfilled') {
    //     locationData = results[0].value;
    //   } else {
    //     console.error("Error fetching location data:", results[0].reason);
    //     mapError("An error occurred while fetching location data");
    //   }
    //
    // if (results[1].status === 'fulfilled') {
    //   stationStatusData = results[1].value;
    // } else {
    //   console.error("Error fetching station status data:", results[1].reason);
    //   mapError("An error occurred while fetching station status data");
    // }
    //
    // if (results[2].status === 'fulfilled') {
    //   stationInformationData = results[2].value;
    // } else {
    //   console.error("Error fetching station information data:", results[2].reason);
    //   mapError("An error occurred while fetching station information data");
    // }
    //
    // if (results[3].status === 'fulfilled') {
    //   trafficProblemsData = results[3].value;
    // } else {
    //   console.error("Error fetching traffic problems data:", results[3].reason);
    //   mapError("An error occurred while fetching traffic problems data");
    // }

    processMap(locationData, stationStatusData, stationInformationData, trafficProblemsData);

  } catch (error) {
    console.error("Error:", error);
    mapError("An error occurred while fetching data");
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

  // Use a map service to display in the Leaflet frame
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(map);

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

/*
--- FUNCTIONS ---
 */
/*
// function loadMap()
// {
  // --- Initialize and center the map on Nancy ---
  fetch("https://api-adresse.data.gouv.fr/search/?q=Nancy+54")
    .then(response => {
      response.json().then(json => {
        // Data received
        loaded ++;

        // Display map
        document.querySelector("#map").classList.remove("is-hidden");

        // Check the location is found
        let i = 0;
        while (json.features[i] && json.features[i].properties.context !== "54, Meurthe-et-Moselle, Grand Est") {
          i++;
        }
        const data = json.features[i] ? json.features[i] : "Localisation introuvable";

        // Create Leaflet frame using location data
        const map = L.map('map').setView([data.geometry.coordinates[1], data.geometry.coordinates[0]], 13);

        // Use a map service to display in the Leaflet frame
        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
          maxZoom: 19,
          attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        }).addTo(map);

        // --- Available Velibs ---
        //ajout des infos depuis le serveur 1
        fetch("https://transport.data.gouv.fr/gbfs/nancy/station_status.json")
          .then(response => {
            if (response.ok) {
              response.json().then(response2 => {
                // Data received
                loaded ++;

                for (let i = 0; i < response2.data.stations.length; i++) {
                  //ajout des infos
                  _stations[i] = {...response2.data.stations[i], ..._stations[i]};
                }
              }).catch(error => {
                console.log('test')
              })

              //ajout d'infos depuis le serveur 2 et ajout stations
              fetch("https://transport.data.gouv.fr/gbfs/nancy/station_information.json")
                .then(response => {
                  if (response.ok) {
                    response.json().then(response2 => {
                      // Data received
                      loaded ++;

                      //icone des stations
                      var icon = L.icon({
                        iconUrl: 'https://www.smavd.org/wp-content/uploads/elementor/thumbs/Velo-pnn5fiv0mued6phjixnq24y55dm7fy4y16518hf9te.png',
                        iconSize: [50, 30], // size of the icon
                      });
                      for (let i = 0; i < response2.data.stations.length; i++) {
                        //ajout des infos
                        _stations[i] = {...response2.data.stations[i], ..._stations[i]};
                        //ajout d'une station
                        const marker = L.marker([_stations[i].lat, _stations[i].lon], {icon: icon}).addTo(map);
                        marker.bindPopup(
                          "<b>Adresse</b> : " + _stations[i].address.toString() +
                          "<br><b>Vélos disponibles</b> : " + _stations[i].num_bikes_available.toString()
                          + "<br><b>Places de parking libres</b> : " + _stations[i].num_docks_available.toString());
                      }
                    })
                  }
                }).catch(() => {
                mapError("Impossible de récupérer les informations des Vélibs");
              })
            }
          })
          .catch(() => {
            mapError("Impossible de récupérer l'état des Vélibs");
          })


        // --- Traffic problems ---
        fetch("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json")
          .then(response => {
            response.json().then(json => {
              // Data received
              loaded ++;

              //icone des stations
              const icon = L.icon({
                iconUrl: 'assets/Logo_travaux_orange.svg',

                iconSize: [50, 30], // size of the icon
              });

              for (let incident of json.incidents) {
                //console.log(incident)
                let coordinates = incident.location.polyline.split(" ");
                coordinates = coordinates.map(function (elt) {
                  return parseFloat(elt);
                });
                // console.log(coordinates);
                const marker = L.marker(coordinates, {icon: icon}).addTo(map);
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
            })
          })
          .catch(() => {
            mapError("Impossible de récupérer les informations trafic");
          })

      })
    })
    .catch(function () {
      mapError(`Impossible de récupérer la localisation pour construire la carte`);
    })
// }
 */

function mapError(msg) {
  console.error(NET_ERR + " : " + msg);
  document.querySelector("#map").insertAdjacentHTML('afterend', "<span class='errorText blink'>" + NET_ERR + " : " + msg + "</span>");
}
