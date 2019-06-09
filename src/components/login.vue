<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-card class="box-card" style="margin: 0 auto;margin-top: 180px">
          <div slot="header" style="text-align: center">
            <span style="margin: 0 auto;display: inline-block;font-size: 20px;color: #4da1ff">登录</span>
          </div>
          <div class="text item" style="margin-top: 20px">
            <div style="height: 70px;width: 300px;margin: 0 auto">
              <span style="display: inline-block;margin-bottom: 10px">邮箱</span>
              <el-input placeholder="请输入邮箱" v-model="id" clearable></el-input>
            </div>
          </div>
          <div class="text item" style="margin-top: 10px">
            <div style="height: 70px;width: 300px;margin: 0 auto">
              <span style="display: inline-block;margin-bottom: 10px">密码</span>
              <el-input
                @keyup.enter.native="login"
                placeholder="请输入密码"
                v-model="password"
                show-password
              ></el-input>
            </div>
          </div>
          <div class="text item" style="text-align: center;margin-top: 30px">
            <el-button type="primary" round @click="login">← 登录</el-button>
            <router-link to="/sign"><el-button type="primary" round style="margin-left: 20px;">注册 →</el-button></router-link>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as api from "@/api/api.js"
import axios from 'axios'

export default {
  name: "Login",
  data() {
    return {
      id: "",
      password: "",
      userdata: ""
    }
  },
  methods: {
    login() {
      if (this.id == "" || this.password == "") {
        this.$message("密码不能为空")
        return;
      } else {
        axios({
          method: "post",
          url: "http://localhost/apis/login",
          data: {
            username: this.id,
            password: this.password
          }
        }).then(response => {
          if (response.data.code == 200) {
            localStorage.setItem('user', JSON.stringify(response.data.data.user))
            this.$store.commit('setUsername', response.data.data.user.username)
            this.$store.commit('setUserAdmin', response.data.data.user.admin)
            this.$router.push('/file')
          } else {
            this.$message(response.data.msg)
          }
        })
      }
    },
    checkLocal() {
      if (localStorage.getItem('user')) {
        var user = JSON.parse(localStorage.getItem('user'))
        this.$store.commit('setUsername', user.username)
        this.$store.commit('setUserAdmin', user.admin)
        this.$store.commit('setUploadable', user.upload)
        this.$store.commit('setDownloadable', user.download)
        this.$router.push('/file')
      }
    }
  },
  created() {
    this.checkLocal()
  },
}
</script>

<style scoped>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

.box-card {
  width: 600px;
  height: 400px;
}
</style>