<template>
  <div>
    <el-button icon="el-icon-arrow-left" @click="handle2last" size="mini" style="float: left;"
    :disabled="this.his.length === 1"></el-button>
    <el-upload
      style="float: right;"
      class="upload-demo"
      ref="upload"
      :multiple="false"
      action="https://localhost/apis/upload"
      :on-preview="handlePreview"
      :http-request="uploadFile"
      :auto-upload="false"
      :lazy="true"
    >
      <el-button slot="trigger" size="small" type="primary" :disabled="!uploadable">选取文件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload" :disabled="!uploadable">上传到这里</el-button>
    </el-upload>
    <el-table
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
      style="width: 100%;
      border-collapse:collapse;"
      :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
    >
      <el-table-column property="date" style="width: 100%">
        <template slot-scope="scope">
          <div @click="hanld2name(scope.row.name, scope.row.type)" style="cursor: pointer;">
            <i :class="scope.row.type==='folder'?'el-icon-folder':'el-icon-document'"/>
            {{scope.row.name}}
          </div>
        </template>
      </el-table-column>
      <el-table-column width="300">
        <template slot="header" slot-scope="scope">
          <el-input clearable v-model="search" size="mini" placeholder="输入关键字搜索"/>
        </template>
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleDownload(scope.row.name)"
            v-if="scope.row.type ==='file'"
            circle
          >
            <i class="el-icon-download"></i>
          </el-button>
          <el-button size="mini" @click="handleDelete(scope.row.name)" circle :disabled="!deletable">
            <i class="el-icon-delete"></i>
          </el-button>
          <el-button type="mini" @click="handlePreview(scope.row.name)" circle
            v-show="scope.row.type === 'file'">
            <i class="el-icon-zoom-in"></i>
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pictureView v-if="pictureShow"></pictureView>
  </div>
</template>
<script>
import * as api from "@/api/api.js";
import _ from "lodash";
import axios from "axios";
import pictureView from "@/components/image";
import preview from '@/components/preview';

export default {
  components:{
    pictureView, preview
  },
  data() {
    return {
      search: "",
      root: "",
      his: [],
      tableData: [],
      fileList: [],
      uploadable: false,
      downloadable: false,
      deletable: false
    };
  },
  created() {
    this.init();
    this.$store.state.path = ("/file/" + this.$store.state.username).split("/");
    this.uploadable = this.$store.state.upload
    this.downloadable = this.$store.state.download
    this.deletable = this.$store.state.delete
  },
  methods: {
    init() {
      api.getFileList({ current: "/file/" + this.$store.state.username })
        .then(res => {
          //console.log(res)

          // this.tableData = _.reverse(_.sortBy(res.data.list, "type"));
          this.tableData = _.orderBy(
            res.data.list,
            ["type", "name"],
            ["desc", "asc"]
          );
          this.root = "/file/" + this.$store.state.username;
          this.his.push("/file/" + this.$store.state.username);
        });
    },
    handleCurrentChange() {},
    //处理预览事件
    handlePreview(n) {
      var patt = new RegExp(".*\.(jpg|gif|bmp|bnp|png|jpeg)")
      if (n.match(/.*\.doc.?/) != null || n.match(/.*\.pdf/) !== null) {
        api.getUrl({path: this.his.join('/') + '/'+n}).then(res => {
          // console.log(res)
          this.$store.commit('setPdfUrl', res.data.url)
          this.$router.push('/preview')
        })
      } else if (patt.test(n)) { //图片
        api.getUrl({path: this.his.join('/') + '/'+n}).then(res => {
          // console.log(res)
          this.$store.commit('setImageUrl', res.data.url)
          this.$store.commit('setPictureShow', true)
        })
      }
    },
    handleRemove() {},
    //进入文件夹
    hanld2name(d,t) {
      if (t !== "folder") return
      this.his.push(d);
      api.getFileList({ current: this.his.join("/") }).then(res => {
        // console.log(res)
        this.tableData = _.orderBy(
          res.data.list,
          ["type", "name"],
          ["desc", "asc"]
        );
      });
      this.$store.commit("handleChange", d);
    },
    //返回
    handle2last() {
      // console.log(",")
      if (this.his.length > 1) {
        this.his.pop();
        this.$store.commit("handleBack");
      }
      api.getFileList({ current: this.his.join("/") }).then(res => {
        // console.log(res)
        this.tableData = _.orderBy(
          res.data.list,
          ["type", "name"],
          ["desc", "asc"]
        );
      });
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    uploadFile(param) {
      var fileObj = param.file;
      var form = new FormData();
      form.append("file", fileObj);
      form.append("username", this.$store.state.username);
      form.append("location", this.his.join("/"));
      form.append("timestamp", new Date().getTime());
      // console.log(form.get("username"));
      // console.log(form.get("location"));
      // console.log(form.get("timestamp"));
      axios({
        url: "http://localhost/apis/upload",
        method: "post",
        data: form
      }).then(res => {
        api.getFileList({ current: this.his.join("/") }).then(res => {
          // console.log(res)
          this.tableData = _.orderBy(
            res.data.list,
            ["type", "name"],
            ["desc", "asc"]
          );
        });
      });
      this.fileList = [];
    },
    handleDelete(n) {
      this.$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          var filePath = this.his.join("/") + "/" + n;
          api.deleteFile({ path: filePath, username: "lunacia" }).then(res => {
            // console.log(res);
            api.getFileList({ current: this.his.join("/") }).then(result => {
              // console.log(res)
              this.tableData = _.orderBy(
                result.data.list,
                ["type", "name"],
                ["desc", "asc"]
              );
            });
          });
          this.$message({
            type: "success",
            message: "删除成功!"
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
      // console.log(path);
    },
    handleDownload(n) {
      var filePath = this.his.join("/") + "/" + n;
      api
        .download({ file: filePath, username: this.$store.state.username })
        .then(res => {
          this.downloadRes(res, n);
        });
    },
    downloadRes(data, name) {
      if (!data) return;
      const content = data;
      const blob = new Blob([content]);
      const fileName = name;
      if ("download" in document.createElement("a")) {
        // 非IE下载
        const elink = document.createElement("a");
        elink.download = fileName;
        elink.style.display = "none";
        elink.href = URL.createObjectURL(blob);
        document.body.appendChild(elink);
        elink.click();
        URL.revokeObjectURL(elink.href); // 释放URL 对象
        document.body.removeChild(elink);
      } else {
        // IE10+下载
        navigator.msSaveBlob(blob, fileName);
      }
    }
  },
  computed: {
    hasNewFolder: function() {
      return this.$store.state.newFolder;
    },
    hasJump: function() {
      return this.$store.state.jump;
    },
    listenPath: function() {
      return this.$store.state.path;
    },
    pictureShow: function() {
      return this.$store.state.pictureShow;
    }
  },
  watch: {
    hasNewFolder: function(newvar, oldvar) {
      if (newvar === true) {
        api.getFileList({ current: this.his.join("/") }).then(res => {
          // console.log(res)
          this.tableData = _.orderBy(
            res.data.list,
            ["type", "name"],
            ["desc", "asc"]
          );
        });
        this.$store.state.newFolder = false;
      }
    },
    hasJump: function(newvar) {
      if (newvar === true) {
        api.getFileList({ current: this.root }).then(res => {
          // console.log(res)
          this.tableData = _.orderBy(
            res.data.list,
            ["type", "name"],
            ["desc", "asc"]
          );
        });
        while (this.his.length > 1) this.his.pop();
        this.$store.state.path = ("/file/" + this.$store.state.username).split(
          "/"
        );
        this.$store.state.jump = false;
      }
    }
  }
};
</script>


