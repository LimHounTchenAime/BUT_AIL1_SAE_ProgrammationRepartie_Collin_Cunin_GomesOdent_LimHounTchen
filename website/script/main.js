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

// Available Velibs
fetch("https://transport.data.gouv.fr/gbfs/nancy/station_information.json")
.then(response => {
  if (response.ok) {
        response.json().then(response2 => {
                for (let i = 0; i < response2.data.stations.length; i++) {
                    const lat = response2.data.stations[i].lat
                    const lon = response2.data.stations[i].lon
                    //Put a marker on the map
                    const marker = L.marker([lat, lon]).addTo(map);
                }
            })
        }
    })





// Traffic problems
fetch("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json")
  .then(response => {
    response.json().then(json => {
      console.log(json)

      for (ind in json.incidents)
      {
        const coordinates = json.incidents[ind].location.polyline.split(" ");
        console.log(ind)
        console.log(coordinates)
        // const lat = incident.
        // const lon = json.data.stations[i].lon
        //
        console.log(parseFloat(coordinates[0]), parseFloat(coordinates[1]))
        // const marker = L.marker(parseFloat(coordinates[0]), parseFloat(coordinates[1])).addTo(map);
      }

      // for (let i = 0; i < json.data.stations.length; i++) {
      //   const lat = json.data.stations[i].lat
      //   const lon = json.data.stations[i].lon
      //   //Put a marker on the map
      //   const marker = L.marker([lat, lon]).addTo(map);
      // }
    })
  })