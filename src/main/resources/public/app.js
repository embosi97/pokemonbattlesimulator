const app = Vue.createApp({
    data() {
        return {
            player: '',
            computer: '',
            error: null
        };
    },
    methods: {
        redirectToBattle() {
            if (!this.player || !this.computer) {
                this.error = 'Please enter both Pokemon species!';
                return;
            }

            this.error = null;

            //construct the attack url and redirect the user
            const battleUrl = `/attack?player=${encodeURIComponent(this.player)}&computer=${encodeURIComponent(this.computer)}`;
            window.location.href = battleUrl;
        }
    }
});

app.mount('#app');