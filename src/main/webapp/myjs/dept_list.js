var vm = new Vue({
    el:'#deptdiv',
    data: {
        deptlist: [],
        page: {},
        pageNum: 1,
        pageSize: 3,
        searchEntity: {},
        entity:{},
        postids:[],
        postlist:[],
    },
    methods: {

        paging:function(pageNum){
            this.pageNum = pageNum;
            this.getDeptList();
        },

        getDeptList:function () {
            axios.post("../dept/getDeptList.do?pageNum="+this.pageNum+"&pageSize="+this.pageSize,this.searchEntity).then(function (response) {
                this.vm.deptlist = response.data.list;
                this.vm.page = response.data;
                this.vm.pageNum = response.data.currentPage;
                this.vm.pageSize = response.data.pageSize;
            })
        },

        //回显职位（根据科室id查询一条科室数据、查询科室已经拥有的职位、查询所有职位）
        toDeptPost:function (id) {
            var _this = this;
            axios.post("../dept/toDeptPost.do?id="+id).then(function (response) {
                _this.entity = response.data;
                _this.postlist = response.data.postlist;
                _this.postids = response.data.postids;
                document.getElementById("deptpostdiv").style.display="block";
            })
        },

        //分配职位
        saveDeptPost:function () {
            var _this = this;
            axios.post("../dept/saveDeptPost.do?id="+_this.entity.id,_this.postids).then(function (response) {
                if(response.data.flag){
                    document.getElementById("deptpostdiv").style.display="none";
                    _this.getDeptListConn();
                }else{
                    alert(response.data.msg);
                }
            })
        }
    }

});
vm.getDeptList();