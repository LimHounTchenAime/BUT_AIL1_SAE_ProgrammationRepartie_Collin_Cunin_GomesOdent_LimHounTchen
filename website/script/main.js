let _stations = []
let map;
const NET_ERR = "Erreur réseau"

// --- Initialize and center the map on Nancy ---
fetch("https://api-adresse.data.gouv.fr/search/?q=Nancy+54")
  .then(response => {
    response.json().then(json => {
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
            //console.log(json)

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
          mapError("Impossible de récupérer les informations traffic");
        })

    })
  })
  .catch(function () {
    mapError(`Impossible de récupérer la localisation pour construire la carte`);
  })


function mapError(msg) {
  console.error(NET_ERR + " : " + msg);
  document.querySelector("#map").insertAdjacentHTML('afterend', "<span class='errorText'>" + NET_ERR + " : " + msg + "</span>");
}
