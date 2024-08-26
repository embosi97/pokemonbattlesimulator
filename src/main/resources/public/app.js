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

        const data = await response.json();

        if (data.winner) {
          //redirect to attack.html with query parameters
          window.location.href = `attack.html?player=${encodeURIComponent(this.player)}&computer=${encodeURIComponent(this.computer)}`;
        } else {
          this.error = 'Invalid data received from server';
        }
      } catch (error) {
        //handle any errors (network errors, invalid JSON, etc.)
        this.error = `Error: ${error.message}`;
      }
    }
  }
});

app.mount('#app');
