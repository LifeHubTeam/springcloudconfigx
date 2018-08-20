let util = {

};
util.title = function (title) {
    title = title ? 'Config Center - ' + title : 'Config Center';
    window.document.title = title;
};

util.setBreadcrumbs = function (vm) {
    let name = vm.$route.name;
    let breadArr = [
        {
            title: '首页',
            path: ''
        }
    ];
    if (name === 'appconfig'){
        breadArr.push({
            title: '应用列表',
            path: ''
        });
    } else if (name === 'profiles'){
        breadArr.push({
            title: '配置查询',
            path: ''
        });
    } else if (name === 'profileconfig'){
        breadArr.push(
            {
                title: '应用列表',
                path: '/appconfig'
            },
            {
                title: '配置列表',
                path: ''
        });
    } else if (name === 'userlist'){
        breadArr.push({
            title: '用户管理',
            path: ''
        });
    }
    vm.$store.commit('setBreadcrumbs', breadArr);
};

export default util;