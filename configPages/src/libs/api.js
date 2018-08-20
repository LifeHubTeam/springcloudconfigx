import conf from "../config/config";
let BASE_URL = conf.base_request_url;

export default {
    refresh: BASE_URL + "/api/config/refresh",

    login: BASE_URL + "/api/user/login",

    logout: BASE_URL + "/api/user/logout",

    listUser: BASE_URL + "/api/user/listUser",

    addUser: BASE_URL + "/api/user/addUser",

    updateUser: BASE_URL + "/api/user/updateUser",

    enableUser: BASE_URL + "/api/user/enableUser",

    frozenUser: BASE_URL + "/api/user/frozenUser",

    addApp: BASE_URL + "/api/config/addApp",

    delApp: BASE_URL + "/api/config/delApp",

    effectApp: BASE_URL + "/api/config/effectApp",

    modifyApp: BASE_URL + "/api/config/modifyApp",

    findAppById: BASE_URL + "/api/config/findAppById",

    findAppList: BASE_URL + "/api/config/findAppList",

    findAllLabels: BASE_URL + "/api/config/findAllLabels",

    findAllAppNames: BASE_URL + "/api/config/findAllAppNames",

    findAllProperties: BASE_URL + "/api/config/findAllProperties",

    findPropertiesList: BASE_URL + "/api/config/findPropertiesList",

    findAllAppProfileNames: BASE_URL + "/api/config/findAllAppProfileNames",

    findHistoryByProfileId: BASE_URL + "/api/config/findHistoryByProfileId",

    addAppProperties: BASE_URL + "/api/config/addAppProperties",

    deleteAppProperties: BASE_URL + "/api/config/deleteAppProperties",

    effectAppProperties: BASE_URL + "/api/config/effectAppProperties",

    modifyAppProperties: BASE_URL + "/api/config/modifyAppProperties",

    disableAppProperties: BASE_URL + "/api/config/disableAppProperties"
};