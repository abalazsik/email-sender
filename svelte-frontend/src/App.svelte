<script lang="ts">
  import { contact } from "./service";
  import type { Contact } from "./service";

  let sent = false;

  let state: Contact = {senderEmail: "", senderName: "", text: "", token: ""};

  const send = function() {
    contact(state).then(function(response) {
      if (response.ok) {
        sent = true;
      }
    });
  }

  const recaptchaSubmitted = function(token) {
    grecaptcha.ready(function() {
      grecaptcha.execute('6LdMXuwoAAAAAAUGoMMHx2l8xVF32qM3qbo2KTqo', {action: 'submit'}).then(function(token) {
        state.token = token;
      });
    });
  }

</script>

<main>
  <section>
    {#if !sent}
    <div id="sender-form">
      <div>
          <label for="name">Név</label>
          <input type="text" id="name" bind:value={state.senderName} required/>
      </div>
      <div>
          <label for="email">Email</label>
          <input type="email" id="email" bind:value={state.senderEmail} required/>
      </div>
      <div>
          <textarea bind:value={state.text} required></textarea>
      </div>
      <div>
        <button class="g-recaptcha" disabled={state.token !== ""}
          on:click={recaptchaSubmitted} 
          data-action='submit'>Nem vagyok robot</button>
      </div>
      
      <div>
          <button on:click={send} disabled={state.token === ""}>Küldés</button>
      </div>
  </div>
    {:else}
      <h2>Üzenet kézbesítve!</h2>
    {/if}
  </section>
</main>

<style>
</style>
