let _stations=[]
const map = L.map('map').setView([48.662356, 6.173442], 13);

// Use a map service to display in the Leaflet frame
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

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