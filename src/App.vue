<template>
  <div id="app">
    <router-view />
    <!-- <router-view/> -->
  </div>
</template>

<style lang="scss">
h3 {
  margin: 0;
  padding: 0;
  line-height: 30px;
}

</style>
<script>
export default {
  data() {
    return {
      
    }
  }
}
// import filesm from "@/components/file";
// import * as api from "@/api/api.js"

// export default {
//   components: {
//     filesm
//   },
//   data() {
//     return {
//       folderName: '',
//       dialogVisible: false,
//       user: {
//         name: 'lunacia',
//         admin:false
//       },
//       pathList: [],
//       tableData: []
//     };
//   },
//   methods: {
//     handleOpen(key, keyPath) {
//       console.log(key, keyPath);
//     },
//     handleClose(key, keyPath) {
//       console.log(key, keyPath);
//     },
//     handleCurrentChange() {},
//     // formatVal(r, i) {
//     //   if (r.type == "folder") {
//     //     return '<i class="el-icon-folder" />' + r.name + "";
//     //   } else {
//     //     return '<i class="el-icon-document" />' + r.name + "";
//     //   }
//     // }
//     handleCreateFolder() {
//       api.createFolder({name:'/'+this.$store.state.path.join('/')+'/'+this.folderName}).then((res) => {
//         this.$store.commit('handleNewFolder')
//       }).catch((err) => {
        
//       });
//       this.dialogVisible=false
//     },
//     jump2Root() {
//       this.$store.commit('handleJump')
//     }
//   },
//   computed: {
//     listenPath: function() {
//       return this.$store.state.path
//     }
//   },
//   watch: {
//     listenPath: function(oldvar, newvar) {
//       console.log(oldvar+' '+newvar)
//     }
//   }
// };
</script>