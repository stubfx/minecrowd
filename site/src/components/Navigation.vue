<script setup>
import en from '@/assets/locales/en.json'
defineProps({
  selected: {
    type: String,
    required: false
  }
})
</script>

<template>
  <nav class="d-flex flex-column align-items-stretch flex-shrink-0 bg-dark w-100" style="height: 100vh !important;">
    <nav class="navbar bg-black bg-opacity-50">
      <div class="container-fluid">
        <form class="d-flex" role="search">
          <input class="form-control" type="search" placeholder="Search command" aria-label="Search" v-model="search">
<!--          <button class="btn btn-outline-success" type="submit">Search</button>-->
        </form>
      </div>
    </nav>
    <div class="list-group list-group-flush" style="overflow-y: scroll" >
      <template v-for="command in commands">
        <a href="#" class="list-group-item py-3 bg-dark text-light" style="border-bottom: 1px solid #00bd7e !important;" @click="selectedCommand = command"
           :class="{active: command === selected}" v-if="!search || command.toLowerCase().includes(search.toLowerCase())">
          <div class="d-flex w-100 align-items-center justify-content-between">
            <strong class="mb-1 text-capitalize">{{command}}</strong>
            <!--          <small>Wed</small>-->
          </div>
          <div class="col mb-1 small">{{en.command[command].desc}}</div>
        </a>
      </template>
    </div>
  </nav>
</template>
<script>
export default {
  name: 'navigation',
  data: () => {
    return {
      // terrible practice.
      commands: Object.keys(en.command),
      search: '',
      selectedCommand: ""
    }
  },
  watch: {
    selectedCommand() {
      this.$emit('selectedCommand', this.selectedCommand)
    }
  }
}
</script>
