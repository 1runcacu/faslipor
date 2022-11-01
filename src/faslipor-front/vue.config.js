const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  publicPath:"./",
  lintOnSave:false,
  devServer:{
    port:8100,
    headers: {
      'Access-Control-Allow-Origin': '*'
    },
    allowedHosts:[
      '*'
    ],
    historyApiFallback: {
      index: '/'
    },
    proxy:{
      '/CONNECT':{
        target: 'http://localhost:8101',
        changeOrigin: true,
        secure:false,
        pathRewrite: {
          '/CONNECT': ''
        }
      }
    }
  }
})
