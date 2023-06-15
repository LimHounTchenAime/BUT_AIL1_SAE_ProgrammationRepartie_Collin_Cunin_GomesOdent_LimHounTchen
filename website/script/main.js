fetch("https://transport.data.gouv.fr/gbfs/nancy/station_information.json")
    .then(response => {
        if (response.ok) {
            response.json().then(response2 => {
                // Create Leaflet frame
                const map = L.map('map').setView([response2.data.stations[0].lat, response2.data.stations[0].lon], 13);

                // Use a map service to display in the Leaflet frame
                L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                }).addTo(map);
                for (let i = 0; i < response2.data.stations.length; i++) {
                    const lat = response2.data.stations[i].lat
                    const lon = response2.data.stations[i].lon
                    //Put a marker on the map
                    const marker = L.marker([lat, lon]).addTo(map);
                }
            })
        }
    })