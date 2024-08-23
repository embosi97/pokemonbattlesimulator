const app = Vue.createApp({
    setup() {
        let pokemonName = Vue.ref('________');
        function battle () {
            fetch("/attack?pokemonA=Bulbasaur&pokemonB=Ivysaur")
                .then(response => response.json())
                .then(data => {
                    pokemonName.value = data.winner;
                    console.log(`This pokemon has ${data.hitPoints} hit points.`);
                })
        }
        return {
            pokemonName,
            battle,
        }
    }
});
app.mount('#app');
