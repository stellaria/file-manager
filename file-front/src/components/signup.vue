<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-card class="box-card" style="margin: 0 auto;margin-top: 180px">
          <div slot="header" style="text-align: center">
            <span style="margin: 0 auto;display: inline-block;font-size: 20px;color: #4da1ff">注册</span>
          </div>
          <div class="text item" style="margin-top: 20px">
            <div style="height: 70px;width: 300px;margin: 0 auto">
              <span style="display: inline-block;margin-bottom: 10px">用户名</span>
              <el-input placeholder="请输入用户名" v-model="username" clearable></el-input>
            </div>
          </div>
          <div class="text item" style="margin-top: 20px">
            <div style="height: 70px;width: 400px; margin: 0 auto; padding-left: 100px;">
              <span style="display: block;margin-bottom: 10px">邮箱</span>
              <el-input placeholder="请输入邮箱" v-model="email" style="width:300px;"></el-input>
              <el-input placeholder="验证码" v-model="confirmCode" style="width: 100px;"></el-input>
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
            <el-button type="primary" round @click="code()" :disabled="!canClick">{{content}}</el-button>
            <el-button type="primary" round @click="cancel">取消</el-button>
            <el-button type="primary" round @click="sign">注册</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from "axios";
import * as api from "@/api/api.js";

export default {
  data() {
    return {
      username: "",
      email: "",
      password: "",
      canClick: true,
      content: "获取验证码",
      totalTime: 60,
      confirmCode: "" //验证码
    };
  },
  methods: {
    getCode() {
      let reg = /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
      if (reg.test(this.email)) {
        api.code({ email: this.email });
        return true;
      } else {
        this.$message("邮箱输入有误");
        return false;
      }
    },
    code() {
      if (!this.canClick) return //改动的是这两行代码
      var res = this.getCode()
      if (res) {
        this.canClick = false
        this.content = this.totalTime + "s后重新发送";
        let clock = window.setInterval(() => {
          this.totalTime--;
          this.content = this.totalTime + "s后重新发送";
          if (this.totalTime < 0) {
            window.clearInterval(clock);
            this.content = "重新发送验证码";
            this.totalTime = 60;
            this.canClick = true; //这里重新开启
          }
        }, 1000);
      }
    },
    sign() {
      if (
        this.username !== "" &&
        this.password !== "" &&
        this.email !== "" &&
        this.confirmCode !== ""
      ) {
        axios({
          url: "http://39.106.73.27/apis/signUp",
          method: "post",
          data: {
            username: this.username,
            password: this.password,
            email: this.email,
            group: this.group,
            confirmCode: this.confirmCode
          }
        }).then(res => {
          if (res.data.code == 200) {
            this.$store.commit('setUsername', this.username)
            this.$store.commit('setUserAdmin', '0')
            localStorage.clear()
            var user = {
              username: this.username,
              admin: '0'
            }
            localStorage.setItem('user', JSON.stringify(user))
          }
        });
      } else {
        this.$message("全部必填");
      }
      this.$router.push('/file')
    },
    cancel() {
      this.$router.push("/login");
    }
  }
};
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
  width: 800px;
}
</style>