<script setup>
defineProps({
  selectedCommand: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div id="command-container" class="col-12 text-light">
    <div class="col-12" style="padding-left: 150px; padding-top: 150px">
      <main>
        <h1 class="text-capitalize">{{selectedCommand}}</h1>
        <br>
        <p class="fs-5 col-md-8">{{$t(`command.${selectedCommand}.desc`)}}</p>
        <br>
        <hr class="col-3 col-md-2 mb-5">

        <div class="row g-5">
          <div class="col-12">
            <pre style="width: fit-content; min-width: 200px !important;">{{$t(`command.${selectedCommand}.usage`)}}</pre>
            <p>{{$t(`command.${selectedCommand}.details`)}}</p>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style scoped>
h1 {
  font-size: xxx-large;
}

pre {
  background: var(--color-background-soft);
  padding: 0.5em;
  font-size: large;
}
</style>