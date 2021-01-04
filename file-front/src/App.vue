<template>
  <div id="app">
    <!-- <router-view/> -->
    <input type="file" id="people-export" ref="inputer" @change="fileUpload"/>
    <input type="button" value="click" @click="onClick">
    <input type="button" value="login" @click="login">
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
import axios from "axios";
import qs from 'qs';

export default {
  data() {
    return {
      
    }
  },
  methods: {
    onClick() {
      axios({
        method: 'GET',
        url: 'http://47.97.115.41/apis/grade',
        data:{
          "major": "计算机",
          "grade": 2018,
          "term": '201801'
        },
        headers: {
            'Authorization': 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODA5MTk2NjYsInVzZXJuYW1lIjoibHVuYWNpYSJ9.AcuY4NVCWqkMcKEZfQu9rZR6-DIaySAxArlNadO4Vtc'
        }
      } ).then(res => {
        console.log(res);
      })
    },
    fileUpload(event){
      // 上传文件
      console.log(event);
      
      let file = event.target.files
      let formData = new FormData()
      formData.append('grade', 2018)
      formData.append('major', '计算机')
      formData.append('term', '201801')
      formData.append('file', file[0])
      console.log(formData);
      // 文件上传
      let url = 'http://47.97.115.41/apis/upload'
      axios({
        url: 'http://47.97.115.41/apis/upload',
        data: formData,
        headers: {
          'Authorization': 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODA5MTk2NjYsInVzZXJuYW1lIjoibHVuYWNpYSJ9.AcuY4NVCWqkMcKEZfQu9rZR6-DIaySAxArlNadO4Vtc'
        },
        method: 'POST'
      }).then((res) => {
        let data = res.data
        if(data.success) {
          console.log(data);
          
          
        } else {
          this.$message.error(data.message || '操作失败')
        }
        this.uploadLoading = false
      }).catch((e)=>{
        this.uploadLoading = false
        alert(e)
      })
      
    },
    login() {
      axios({
        url: 'http://47.97.115.41/apis/login',
        method: 'POST',
        data: qs.stringify({
          'username':'lunacia',
          'password':'lunacia'
        })
      })
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