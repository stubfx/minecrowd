<script setup>

</script>

<template>
  <div id="command-container" class="col-12 text-light">
    <div class="col-12" style="padding-left: 150px; padding-top: 150px">
      <main>
        <h1 class="text-capitalize">spawn</h1>
        <br>
        <p class="fs-5 col-md-8">{{$t(`command.spawn.desc`)}}</p>
        <br>
        <hr class="col-3 col-md-2 mb-5">

        <div class="col-12 g-5">
          <div class="col-12">
            <pre style="width: fit-content; min-width: 200px !important;">{{$t(`command.spawn.usage`)}}</pre>
            <p>{{$t(`command.spawn.details`)}}</p>
          </div>
        </div>
        <input class="form-control" type="search" placeholder="Entity name" aria-label="Search" v-model="search" style="width: 200px;">
        <div class="col-7 d-flex flex-wrap mt-2">
          <template v-for="entity in entities">
          <div class="bg-dark text-white p-2 rounded-1 me-1 mt-1"
               v-if="!search || entity.toLowerCase().includes(search.toLowerCase())">{{entity}}</div>
          </template>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
export default {
  data: ()=> {
    return {
      search: '',
      entities: []
    }
  },
  methods: {
    async retrieveEntities() {
      this.entities = (await import('@/assets/minecraft_items/entities.json')).entities
    }
  },
  mounted() {
    this.retrieveEntities()
  }
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