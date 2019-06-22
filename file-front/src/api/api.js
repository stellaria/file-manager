import request from '@/utils/request'

export function getFileList(query) {
  return request({
    url: '/filelists',
    method: 'get',
    params: query
  })
}

export function createFolder(query) {
  return request({
    url: '/create',
    method: 'post',
    params: query
  })
}

export function deleteFile(query) {
  return request({
    url: '/delete',
    method: 'post',
    params: query
  })
}

export function login(query) {
  return request({
    url: '/login',
    method: 'post',
    params: query
  })
} 

export function code(query) {
  return request({
    url: '/code',
    method: 'get',
    params: query
  })
} 

export function download(query) {
  return request({
    url: '/download',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

export function userList() {
  return request({
    url: '/userlist',
    method: 'get',
  })
}

export function getUrl(query) {
  return request({
    url: '/image_url',
    method: 'get',
    params: query
  })
}