let commonPlugin = {};

commonPlugin.install = function (Vue) {
    Vue.prototype.$success = function (msg) {
        Vue.prototype.$message({
            showClose: true,
            message: msg,
            type: 'success'
        });
    };
    Vue.prototype.$fail = function (msg) {
        Vue.prototype.$message({
            showClose: true,
            message: msg,
            type: 'error',
            duration:10000
        });
    };
};

export default commonPlugin;