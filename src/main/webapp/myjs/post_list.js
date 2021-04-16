var vm = new Vue({
    el:'#postdiv',
    data: {
        postlist: [],
        page: {},
        pageNum: 1,
        pageSize: 3,
        searchEntity: {},
        meunlist:[]
    },
    methods: {

        paging:function(pageNum){
            this.pageNum = pageNum;
            this.getPostList();
        },

        getPostList:function () {
            axios.post("../post/getPostList.do?pageNum="+this.pageNum+"&pageSize="+this.pageSize,this.searchEntity).then(function (response) {
                this.vm.postlist = response.data.list;
                this.vm.page = response.data;
                this.vm.pageNum = response.data.currentPage;
                this.vm.pageSize = response.data.pageSize;
            })
        },


        //分配权限
        toPostMeun:function (id) {
            var _this = this;
            axios.get("../post/getMeunListById.do?id="+id).then(function (response) {
                _this.meunlist = response.data;
                testaa(response.data,id);
                document.getElementById("nodes").style.display="block";
            })
        },

        //执行分配权限
        savePostMeun:function (postid,ids) {
            var _this = this;
            axios.post("../post/savePostMeun.do?postid="+postid,ids).then(function (reponse) {
                 if(reponse.data.flag){
                     alert(reponse.data.msg)
                     document.getElementById("nodes").style.display="none";
                 }else{
                     alert(reponse.data.msg)
                 }
            })
        }
    }

});
vm.getPostList();