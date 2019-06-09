import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // path: ['file', 'lunacia']
    path: [],
    newFolder: false,
    jump: false,
    username: '',
    admin: '',
    delete: true,
    upload: true,
    download: true
  },
  mutations: {
    handleChange(state, n){
      state.path.push(n)
    },
    handleBack(state) {
      state.path.pop()
    },
    handleNewFolder(state) {
      state.newFolder = true
    },
    handleJump(state) {
      this.state.jump = true
    },
    setUsername(state, n) {
      this.state.username = n
      this.state.path = ['file', n]
    },
    setUserAdmin(state, n) {
      this.state.admin = n
    },
    setDeletable(state, n) {
      this.state.delete = n
    },
    setUploadable(state, n) {
      this.state.upload = n
    },
    setDownloadable(state, n) {
      this.state.download = n
    }
  },
  actions: {

  }
})
