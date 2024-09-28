//initializes a new vue.js application
const app = Vue.createApp({
  data() {
    return {
      player: '',
      computer: '',
      error: null
    };
  },
  methods: {
    async redirectToBattle() {
      this.error = null;

      if (!this.player || !this.computer) {
        this.error = 'Both Pokemon names are required.';
        return;
      }

      try {
        //send request to backend/controller
        const response = await fetch(`/attack?player=${encodeURIComponent(this.player)}&computer=${encodeURIComponent(this.computer)}`);

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        //parses the JSON response from the server into a JS object and stores it
        const data = await response.json();

        if (data.winner) {
          //redirect to attack.html with query parameters (if winner exists)
          window.location.href = `attack.html?player=${encodeURIComponent(this.player)}&computer=${encodeURIComponent(this.computer)}`;
        } else {
          this.error = 'Invalid data received from server';
        }
      } catch (error) {
        //handle any errors (network errors, invalid JSON, etc.)
        this.error = `Error: ${error.message}`;
      }
    },
        async redirectToRandomBattle() {
               this.error = null;

               try {
                   //Redirect immediately to random.html
                   window.location.href = `random.html`;

                   const response = await fetch(`/attack/random`);

                   if (!response.ok) {
                       throw new Error('Network response was not ok');
                   }

                   //Parse the JSON response from the server
                   const data = await response.json();

                   if (!data.winner) {
                       console.error('Invalid data received from server');
                   }
               } catch (error) {
                   console.error(`Error: ${error.message}`);
               }
          }
  }
});

app.mount('#app'); //used in attack.html and random.html
