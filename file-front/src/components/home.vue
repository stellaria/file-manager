<template>
  <div>
    <el-row class="tac">
      <el-col :span="3">
        <el-menu
          default-active="2"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
        >
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>{{user.name}}</span>
            </template>
            <el-menu-item index="1-1" @click="logout()">登出</el-menu-item>
          </el-submenu>
          <el-menu-item index="2" @click="jump2Root()">
            <i class="el-icon-menu"></i>
            <span slot="title">文件</span>
          </el-menu-item>
          <el-menu-item index="3" v-if="user.admin" @click="jump2Admin()">
            <i class="el-icon-user"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>

          <el-menu-item index="4" @click="dialogVisible=true">
            <i class="el-icon-folder"></i>
            <span slot="title">新建文件夹</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <el-breadcrumb separator="/" v-show="inFile">
              <el-breadcrumb-item v-for="(item, index) in listenPath" :key="index">{{item}}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div>
            <router-view />
          </div>
          <!-- <imageView v-if="pictureShow"></imageView> -->
        </el-card>
      </el-col>
    </el-row>
    <div>
      <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">
        <el-input v-model="folderName" placeholder="请输入内容" clearable></el-input>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleCreateFolder()" v-show="dialogVisible">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>


<script>
import filesm from "@/components/file";
import * as api from "@/api/api.js"
import imageView from "@/components/image";

export default {
  components: {
    filesm, imageView
  },
  data() {
    return {
      folderName: '',
      dialogVisible: false,
      user: {
        name: '',
        admin:false
      },
      pathList: [],
      tableData: [],
      inFile: true,
      // pictureShow: false
    };
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    handleCurrentChange() {},
    // formatVal(r, i) {
    //   if (r.type == "folder") {
    //     return '<i class="el-icon-folder" />' + r.name + "";
    //   } else {
    //     return '<i class="el-icon-document" />' + r.name + "";
    //   }
    // }
    handleCreateFolder() {
      api.createFolder({name:'/'+this.$store.state.path.join('/')+'/'+this.folderName, username: this.$store.state.username}).then((res) => {
        this.$store.commit('handleNewFolder')
      }).catch((err) => {
        
      });
      this.folderName = ''
      this.dialogVisible=false
    },
    jump2Root() {
      this.inFile = true
      this.$store.commit('handleJump')
      this.$router.push('/file')
    },
    logout() {
      localStorage.clear()
      this.$router.push('/login')
    },
    jump2Admin() {
      this.inFile = false
      this.$router.push('/admin')
    }
  },
  computed: {
    listenPath: function() {
      return this.$store.state.path
    }
  },
  watch: {
    listenPath: function(oldvar, newvar) {
    }
  },
  created() {
    var user = JSON.parse(localStorage.getItem('user'))
    this.$store.commit('setUsername', user.username)
    this.$store.commit('setUserAdmin', user.admin)
    this.user.name = this.$store.state.username
    this.user.admin = this.$store.state.admin
  },
  mounted() {
    this.user.name = this.$store.state.username
    this.user.admin = this.$store.state.admin
  }
};
</script>

