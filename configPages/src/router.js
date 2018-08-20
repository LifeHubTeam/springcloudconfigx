import AppConfig from './views/applicationConfig.vue';
import Index from './views/index.vue';
import ProfilesConfig from './views/profilesConfig.vue';
import ProfilesList from './views/profilesList';
import UserList from './views/userList';
import Login from './views/login';
import Page404 from './views/error/404';

const routers = [
    {
        path: '/',
        name: 'home',
        redirect: 'appconfig',
        meta: {
            title: '首页'
        },
        component: Index,
        children: [
            {
                path: '/appconfig',
                name: 'appconfig',
                meta: {
                    title: '应用列表'
                },
                component: AppConfig,
            },
            {
                path: '/profiles',
                name: 'profiles',
                meta: {
                    title: '配置查询'
                },
                component: ProfilesList
            },
            {
                path: '/profileconfig',
                name: 'profileconfig',
                meta: {
                    title: '配置列表'
                },
                component: ProfilesConfig
            },
            {
                path: '/userlist',
                name: 'userlist',
                meta: {
                    title: '用户管理'
                },
                component: UserList
            }
        ]
    },
    {
        path: '/login',
        name: 'login',
        meta: {
            title: '登录'
        },
        component: Login
    },
    {
        path: '/*',
        name: 'error404',
        meta: {
            title: '404-页面不存在'
        },
        component: Page404
    }
];
export default routers;