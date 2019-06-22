<template>
  <div class="holder">
    <span style="float: right;">
      <el-button type="info"
        icon="el-icon-circle-close"
        circle
        @click="handleClose"></el-button>
    </span>
    <img :src="url" class="image" style="zoom">
  </div>
</template>

<script>
export default {
  data() {
    return {
      scale: 0.3
    }
  },
  methods: {
    handleClose() {
      this.$store.commit('setPictureShow', false)
    }
  },
  computed: {
    url: function() {
      return this.$store.state.imageUrl
    }
  }
}
</script>

<style>
.holder{
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.7);
  height: 100%;
  width: 100%;
  z-index: 9987;
}

.image {
  /* transform: scale(0.3);
  position: absolute;
  top: -30%;
  left: -30%; */
  transform: scale(0.4);
  position: absolute;
  margin: auto;
  top: -9999px;
  right: -9999px;
  bottom: -9999px;
  left: -9999px;

  z-index: inherit;
}

span{
  z-index: 9988;
}
</style>
