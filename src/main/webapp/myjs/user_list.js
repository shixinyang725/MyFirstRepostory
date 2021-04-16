var vm = new Vue({
    el:'#userdiv',
    data:{
        userlist:[],
        page: {},
        pageNum: 1,
        pageSize: 3,
        searchEntity: {},
        entity:{},
        deptlist:[{postids:[]}],
        deptids:[]
    },
    methods:{

        paging:function(pageNum){
            this.pageNum = pageNum;
            this.getUserList();
        },

        getUserList:function () {
            axios.post("../user/getUserList.do?pageNum="+this.pageNum+"&pageSize="+this.pageSize,this.searchEntity).then(function (response) {
                this.vm.userlist = response.data.list;
                this.vm.page = response.data;
                this.vm.pageNum = response.data.currentPage;
                this.vm.pageSize = response.data.pageSize;
            });
        },

        //回显科室(根据id查找一条用户数据、全查科室、查询已经选中的科室)
        toUserDept:function (id) {
            var _this = this;
            axios.get("../user/toUserDept.do?id="+id).then(function (response) {
                _this.deptlist = response.data.deptBeans;
                _this.entity = response.data;
                _this.deptids = response.data.deptids;
                document.getElementById("userdeptdiv").style.display="block";
            })
        },

        //保存科室
        saveUserDept:function () {
            var _this = this;
            axios.post("../user/saveUserDept.do?id="+_this.entity.id,_this.deptids).then(function (response) {
                if(response.data.flag){
                    document.getElementById("userdeptdiv").style.display="none";
                    _this.getUserList();
                }else{
                    alert(response.data.msg);
                }
            })
        },
        
        //分配职位
        toUserPost:function (id) {
            var _this = this;
            axios.get("../user/toUserPost.do?id="+id).then(function (response) {
                _this.entity = response.data;
                _this.deptlist = response.data.deptBeans;
                document.getElementById("userpostdiv").style.display="block";
            })
        },

        //保存职位
        saveUserPost:function () {
            this.entity.deptlist = this.deptlist;
            var _this = this;
            axios.post("../user/saveUserPost.do",_this.entity).then(function (response) {
                if(response.data.flag){
                    alert(response.data.msg);
                    document.getElementById("userpostdiv").style.display="none";
                }else{
                    alert(response.data.msg);
                }
            })
        }
    }
});
vm.getUserList();