<script setup>
import Navigation from "@/components/Navigation.vue";</script>

<template>
  <div id="page-wrapper">
    <navigation id='navigation' @selectedCommand="onSelectedCommand"/>
    <RouterView/>
  </div>
</template>

<script>
import router from "@/router";

export default {
  data: () => {
    return {
      selectedCommand: ''
    }
  },
  methods: {
    onSelectedCommand(value) {
      this.selectedCommand = value
      router.push({path: `${value}`})
    }
  }
}
</script>

<style>
#page-wrapper {
  display: flex;
  width: 100vw !important;
  flex-direction: row;
}

#navigation {
  width: 380px !important;
}

</style>