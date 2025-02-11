// createRouter : 라우터를 생성하는 함수
import { createRouter, createWebHashHistory } from 'vue-router';
import ItemsAll from '../view/ItemsAll.vue'
import ETLItems from "@/view/ETLItems.vue";
import DngItems from "@/view/DngItems.vue";
import DngItemsDetail from "@/view/DngItemsDetail.vue";
import NormItems from "@/view/NormItems.vue";
import NormItemsDetail from "@/view/NormItemsDetail.vue";
import ItemPost from "@/view/ItemPost.vue";
import ItemPostUpdated from "@/view/ItemPostUpdated.vue";


// path: 경로(소문자,하이픈 이용), component:연결할 컴포넌트 명
const routes = [
    {
        path: '/',
        component:ItemsAll
    },
    {
        path: '/etl-items',
        component:ETLItems
    },
    {
        path: '/dng-items',

        component:DngItems
    },
    {
        path: '/:type-items-detail/:id',
        component:DngItemsDetail
    },
    {
        path: '/norm-items',
        component:NormItems
    },
    {
        path: '/:type-items-detail/:id',
        component:NormItemsDetail
    },
    {
        path: '/item-post',
        component:ItemPost
    },{
        path: '/item-post-updated/:type/:id',
        component:ItemPostUpdated
    },

]
//라우터 생성
// createWebHashHistory: Hash로 바꿈
const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

//main.js에서 사용하기 위해 내보내기
export default router
