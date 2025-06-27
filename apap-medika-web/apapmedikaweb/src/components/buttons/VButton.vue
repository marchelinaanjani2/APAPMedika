<template>
  <button class="v-button">
    <slot></slot>
  </button>
</template>

<style scoped>
  .v-button {
    @apply w-full text-lg font-semibold hover:text-white py-1 rounded-2xl;
  }
</style>
