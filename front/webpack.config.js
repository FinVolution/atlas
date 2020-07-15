const path = require('path');
const resolve = require('path').resolve;
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const url = require('url');
const publicPath = '';
const request = require("request");

module.exports = (options = {}) => ({
    entry: {
        vendor: './src/vendor',
        index: './src/main.js'
    },
    output: {
        path: resolve(__dirname, '../src/main/resources/static'),
        filename: options.dev ? '[name].js' : '[name].js?[chunkhash]',
        chunkFilename: '[id].js?[chunkhash]',
        publicPath: options.dev ? '/assets/' : publicPath
    },
    /*    output: {
            path: path.resolve(__dirname, 'dist'),
            filename: options.dev ? '[name].js' : '[name].js?[chunkhash]',
            chunkFilename: '[id].js?[chunkhash]',
            publicPath: options.dev ? debugPath : '/'
        },*/
    module: {
        loaders: [
            {
                test: /\.css$/,
                loader: 'style-loader!css-loader'
            }
        ],
        rules: [
            {
                test: /\.vue$/,
                use: ['vue-loader']
            },
            {
                test: /\.js$/,
                use: ['babel-loader'],
                exclude: /node_modules/
                /*                include: [
                                    resolve('srv'),
                                    resolve('test'),
                                    resolve('node_modules/_element-ui@1.4.2@element-ui/packages/src/col.js'),

                                ]*/
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader', 'postcss-loader']
            },
            {
                test: /\.(png|jpg|jpeg|gif|eot|ttf|woff|woff2|svg|svgz)(\?.+)?$/,
                use: [{
                    loader: 'url-loader',
                    options: {
                        limit: 10000
                    }
                }]
            }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin({
            names: ['vendor', 'manifest']
        }),
        new HtmlWebpackPlugin({
            title: 'My App',
            template: 'src/index.html'
        })
    ],
    resolve: {
        alias: {
            '~': resolve(__dirname, 'src'),
            'vue': 'vue/dist/vue.js',
        }
    },

    devServer: {
        host: '0.0.0.0',
        port: 8082,
        contentBase: [path.join(__dirname, "public"), path.join(__dirname, "/assets")],
        proxy: {
            '/web': {
                //target: 'http://10.1.62.54:9091',
                target: 'http://localhost:8081',
                changeOrigin: true,
            },

            '/api': {
                target: 'http://localhost:8081',
                changeOrigin: true,
            }
        },
        historyApiFallback: {
            index: url.parse(options.dev ? '/assets/' : publicPath).pathname
        }
    },
    devtool: options.dev ? '#eval-source-map' : '#source-map'
})
