import Vue from 'vue';
import VueResource from 'vue-resource';
import iView from 'iview';
import VueRouter from 'vue-router';
import Routers from './router';
import Util from './libs/util';
import App from './app.vue';
import 'iview/dist/styles/iview.css';
import conf from './config/config';
import store from './store';

Vue.use(VueResource);
Vue.use(VueRouter);
Vue.use(iView);

Vue.prototype.configDef = conf;

// 路由配置
const RouterConfig = {
    //mode: 'history',
    routes: Routers
};
const router = new VueRouter(RouterConfig);

router.beforeEach((to, from, next) => {
    iView.LoadingBar.start();
    Util.title(to.meta.title);
    if (!sessionStorage.getItem('token') && to.path !== '/login') {
        next({
            path: '/login'
        })
    } else {
        next()
    }
});

router.afterEach((to, from, next) => {
    iView.LoadingBar.finish();
    window.scrollTo(0, 0);
});

//接口请求和响应拦截
Vue.http.interceptors.push((request, next) => {
    let token = sessionStorage.getItem('token');
    if (token) {
        request.headers.set('token', token);
    }
    next((response) => {
        if (response.data.code === 3425){
            router.push({
                path: '/login'
            });
        }
        return response;
    });
});

new Vue({
    el: '#app',
    store: store,
    router: router,
    render: h => h(App)
});
