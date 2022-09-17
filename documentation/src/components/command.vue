<script setup>
defineProps({
  name: {
    type: String,
    required: true
  }
})
</script>

<template>
  <p>{{ $t(`command.${name}.desc`) }}</p>
  <br/>
  <p>{{ $t(`command.${name}.usage`) }}</p>
</template>

<style scoped>

</style>