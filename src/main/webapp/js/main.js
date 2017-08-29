import Vue from "vue";
import VeeValidate from "vee-validate";
import Multiselect from "vue-multiselect";
import Toast from "vue-easy-toast";

Vue.use(VeeValidate);
Vue.use(Toast);

const url = "http://localhost:8080/sendMail";
const defaultToastSettings = {
  className: "et-warn",
  horizontalPosition: "center",
  verticalPosition: "top",
  duration: 3000,
  mode: "queue",
  transition: "slide-down"
};

new Vue({
  el: "#app",
  components: {
    Multiselect
  },
  data: {
    to: [],
    cc: [],
    bcc: [],
    from: "",
    subject: "",
    text: "",
    errorStates: {},
    mailOptions: [],
    sending: false
  },
  methods: {
    setTo(email) {
      this.addEmail(email, "to");
    },

    setCc(email) {
      this.addEmail(email, "cc");
    },

    setBcc(email) {
      this.addEmail(email, "bcc");
    },

    //Unable to have same email in to, cc and bcc
    containsSameEmails() {
      let totalEmails = this.to.concat(this.cc).concat(this.bcc);
      let emailAddresses = totalEmails.map(email => email.email);

      return emailAddresses.some(function(email, idx) {
        return emailAddresses.indexOf(email) != idx;
      });
    },

    validEmailSelects() {
      if (this.to.length < 1) {
        Vue.toast("Please add someone to mail to.", {
          ...defaultToastSettings
        });
        return false;
      } else if (this.containsSameEmails()) {
        Vue.toast("Unable to have same address in to, cc or bcc.", {
          ...defaultToastSettings
        });
        return false;
      } else {
        return true;
      }
    },

    generateSendModel() {
      return {
        to: this.to,
        cc: this.cc,
        bcc: this.bcc,
        from: this.from,
        subject: this.subject,
        text: this.text
      };
    },

    addEmail(email, component) {
      const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

      if (!re.test(email)) {
        this.errorStates[component] = true;
        Vue.toast("Please enter a valid email address.", {
          ...defaultToastSettings
        });
      } else {
        this.errorStates[component] = false;
        const emailEntry = {
          email: email,
          code: email.substring(0, 2) + Math.floor(Math.random() * 10000000)
        };
        this[component].push(emailEntry);
      }
    },
    sendMail() {
      this.sending = true;
      return fetch(url, {
        method: "post",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(this.generateSendModel())
      })
        .then(data => {
          this.sending = false;
          if (data.status === 200) {
            Vue.toast("Successfully sent mail!", {
              ...defaultToastSettings,
              className: "et-info",
              duration: 3000
            });
          } else {
            Vue.toast("Sorry we were unable to process your request", {
              ...defaultToastSettings,
              className: "et-alert"
            });
            Vue.use(Toast);
          }
        })
        .catch(err => {
          Vue.toast("Sorry we were unable to process your request", {
            ...defaultToastSettings,
            className: "et-alert"
          });
          Vue.use(Toast);
        });
    },
    validateBeforeSubmit(e) {
      this.$validator.validateAll();
      if (!this.errors.any() && this.validEmailSelects()) {
        this.sendMail();
      }
    }
  }
});
