<script setup>
defineProps({
  name: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div class="command">
    <h1>{{ name }}</h1>
    <p>{{ $t(`command.${name}.desc`) }}</p>
    <pre>{{ $t(`command.${name}.usage`) }}</pre>
  </div>
</template>

<style scoped>

</style>