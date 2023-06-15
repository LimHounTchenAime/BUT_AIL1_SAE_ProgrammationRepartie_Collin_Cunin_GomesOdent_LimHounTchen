let _stations=[]
let map;

// Initialize and center the map on Nancy
fetch("https://api-adresse.data.gouv.fr/search/?q=Nancy+54")
  .then(response => {
    response.json().then(json => {
      // Check the location is found
      let i = 0;
      while (json.features[i] && json.features[i].properties.context !== "54, Meurthe-et-Moselle, Grand Est")
        i ++;
      const data = json.features[i] ? json.features[i] : "Localisation introuvable";
      console.log(data);

      // Create Leaflet frame using location data
      const map = L.map('map').setView([data.geometry.coordinates[1], data.geometry.coordinates[0]], 13);

      // Use a map service to display in the Leaflet frame
      L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
      }).addTo(map);

    })
  })

//ajout des infos depuis le serveur 1
fetch("https://transport.data.gouv.fr/gbfs/nancy/station_status.json")
    .then(response=>{
        if(response.ok){
            response.json().then(response2=>{
                for (let i = 0; i < response2.data.stations.length; i++) {
                    //ajout des infos
                    _stations[i] = { ...response2.data.stations[i], ..._stations[i] };
                }
            })
        }
    })

//ajout d'infos depuis le serveur 2 et ajout stations
fetch("https://transport.data.gouv.fr/gbfs/nancy/station_information.json")
    .then(response => {
        if (response.ok) {
            response.json().then(response2 => {
                //icone des stations
                var greenIcon = L.icon({
                    iconUrl: 'https://www.smavd.org/wp-content/uploads/elementor/thumbs/Velo-pnn5fiv0mued6phjixnq24y55dm7fy4y16518hf9te.png',

                    iconSize:     [50, 30], // size of the icon
                });
                for (let i = 0; i < response2.data.stations.length; i++) {
                    //ajout des infos
                    _stations[i] = { ...response2.data.stations[i], ..._stations[i] };
                    //ajout d'une station
                    const marker = L.marker([_stations[i].lat, _stations[i].lon], {icon: greenIcon}).addTo(map);
                    marker.bindPopup(
                        "Adresse : "+_stations[i].address.toString()+
                        "<br>Vélos disponibles : "+_stations[i].num_bikes_available.toString()
                        +"<br>Docks disponibles : "+_stations[i].num_docks_available.toString());
                }
            })
        }
    })





// 2. Traffic problems
fetch("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json")
  .then(response => {
    response.json().then(data => {
      for (let i = 0; i < response2.data.stations.length; i++) {
        const lat = response2.data.stations[i].lat
        const lon = response2.data.stations[i].lon
        //Put a marker on the map
        const marker = L.marker([lat, lon]).addTo(map);
      }
    })
  })