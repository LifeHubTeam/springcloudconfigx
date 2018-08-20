const app = {
    state: {
        breadcrumbs: [
        ]
    },
    mutations: {
        setBreadcrumbs (state, breadArr){
            state.breadcrumbs = breadArr;
        }
    }
};

export default app;
