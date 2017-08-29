var path = require("path");
var webpack = require("webpack");

module.exports = {
  entry: "./src/main/webapp/js/main.js",
  output: {
    path: path.resolve(__dirname, "./src/main/webapp/js/"),
    filename: "build.js"
  },
  resolve: {
    alias: {
      vue: "vue/dist/vue.js"
    }
  },
  resolveLoader: {
    modules: [path.join(__dirname, "node_modules")]
  },
  module: {
    loaders: [
      {
        test: /\.vue$/,
        loader: "vue-loader"
      },
      {
        test: /\.js$/,
        loader: "babel-loader",
        exclude: /node_modules/
      },
      {
        test: /\.(png|jpg|gif|svg)$/,
        loader: "file",
        query: {
          name: "[name].[ext]?[hash]"
        }
      }
    ]
  },
  devServer: {
    historyApiFallback: true,
    noInfo: true
  },
  devtool: "#eval-source-map"
};
